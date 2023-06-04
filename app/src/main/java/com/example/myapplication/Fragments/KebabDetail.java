package com.example.myapplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerCity.CityList;
import com.example.myapplication.RecyclerFood.FoodAdapter;
import com.example.myapplication.RecyclerFood.FoodList;
import com.example.myapplication.RecyclerKebab.KebabAdapter;
import com.example.myapplication.RecyclerKebab.KebabList;
import com.example.myapplication.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KebabDetail extends AppCompatActivity {
    private TextView nombre;
    private TextView lugar;
    private FoodList foodList;
    private RestClient client;
    private Context context;
    private TextView nota;
    private Button puntuar;
    Spinner spinner;
    private RecyclerView recyclerViewPlato;

    public void setFoodList(FoodList foodList){
        this.foodList = foodList;
        recyclerViewPlato = findViewById(R.id.recyclerFood);
        FoodAdapter foodAdapter = new FoodAdapter(foodList);
        recyclerViewPlato.setAdapter(foodAdapter);
        recyclerViewPlato.setLayoutManager(new LinearLayoutManager(context));

    }
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kebab_detail);

        Intent intent = getIntent();

        nombre = findViewById(R.id.tituloKebabDetail);
        lugar = findViewById(R.id.LugarKebabDetail);
        nota = findViewById(R.id.PuntuacionKebabDetail);
        context = this;

        nombre.setText(intent.getStringExtra("NOMBRE_KEBAB"));

        lugar.setText(intent.getStringExtra("LUGAR_KEBAB"));

        nota.setText(intent.getStringExtra("NOTA_KEBAB"));



        client.getFood(  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response.length() != 0) {
                    FoodList foodList = null;
                    try {
                        foodList = new FoodList(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    setFoodList(foodList);
                }

            }
        },                 new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    Toast.makeText(context, "No se pudo establecer la conexión", Toast.LENGTH_SHORT).show();
                } else {
                    int serverCode = error.networkResponse.statusCode;
                    switch (serverCode) {
                        case 401:
                            Toast.makeText(context,"Peticion no autorizada.", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(context, "Estado de respuesta: "+serverCode, Toast.LENGTH_SHORT).show();
                    }                }
            }
        });


        spinner = findViewById(R.id.score_spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.score_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);




        int userNota = spinner.getSelectedItemPosition();

        puntuar = findViewById(R.id.botonOpinion);

        puntuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.postOpinion(userNota,intent.getStringExtra("ID_KEBAB") ,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(context, "Puntuacion enviada!", Toast.LENGTH_SHORT).show();
                            }
                        },                 new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse == null) {
                                    Toast.makeText(context, "No se pudo establecer la conexión", Toast.LENGTH_SHORT).show();
                                } else {
                                    int serverCode = error.networkResponse.statusCode;
                                    switch (serverCode) {
                                        case 401:
                                            Toast.makeText(context,"Peticion no autorizada.", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(context, "Estado de respuesta: "+serverCode, Toast.LENGTH_SHORT).show();
                                    }                }
                            }
                        });
            }
        });





















    }
}

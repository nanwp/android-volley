package com.example.p12_memanggil_api;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etKota;
    TextView tvInfo;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "ad1b9daf7b80abf7c3e0093d81a22d13";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etKota = findViewById(R.id.etCity);
        tvInfo = findViewById(R.id.tvResult);

        Button btnGet = findViewById(R.id.btnGet);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aksesAPI(v);
            }
        });

    }

    public void aksesAPI(View view){
        String tmpURL = "";
        String kotanya = etKota.getText().toString().trim();

        if(kotanya.equals("")){
            tvInfo.setText("City field can not be empty!");
        }
        else{
            tmpURL = url+"?q="+kotanya+"&appid="+appid;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tmpURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Hasil response", response);
                        String output ="";
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                            String descriptions = jsonObjectWeather.getString("description");

                            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                            double temp = jsonObjectMain.getDouble("temp")-273;
                            double feels_like = jsonObjectMain.getDouble("feels_like")-273;
                            int humidity = jsonObjectMain.getInt("humidity");
                            int pressure = jsonObjectMain.getInt("pressure");


                            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                            double speed = jsonObjectWind.getDouble("speed");

                            JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                            int clouds = jsonObjectClouds.getInt("all");

                            JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");

                            String cityName = jsonResponse.getString("name");

                            output += "Current weather of "+cityName + "("+country+")" +
                                    "\nTemp = "+ df.format(temp) + " C"+
                                    "\nFeels like = " + df.format(feels_like) + " C"+
                                    "\nHumidity = " + humidity+" %"+
                                    "\nPressure = "+ pressure+" hPa"+
                                    "\nDescriptions = "+ descriptions+
                                    "\nWind speed = "+speed+"m/s (meter/second)"+
                                    "\nCloudiness = "+clouds;
                            tvInfo.setText(output);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }
}
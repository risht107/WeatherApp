package com.example.openweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText zip;
    Button go;
    URL myURL, myURL2, myURL3;
    Double latt, lonn,tempC, tempL, tempH, convertedTemp;
    String s,num,s2, s3;
    TextView high, low, temp, conditions, quote, lat, lon, city, day1, h1l, h2l, h3l, h4l, h1h, h2h, h3h, h4h, h1, h2, h3, h4, h1c, h2c, h3c, h4c, date;
    ImageView currentImage, h1i, h2i, h3i, h4i;
    AsyncThread2 getCurrentData;
    AsyncThread3 future;
    JSONObject current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zip=findViewById(R.id.zip);
        date=findViewById(R.id.date);
        quote=findViewById(R.id.quote);
        go=findViewById(R.id.go);
        high=findViewById(R.id.high);
        low=findViewById(R.id.low);
        temp=findViewById(R.id.temp);
        conditions=findViewById(R.id.conditions);
        lat=findViewById(R.id.lat);
        lon=findViewById(R.id.lon);
        city=findViewById(R.id.city);
        day1=findViewById(R.id.h1);
        h1l=findViewById(R.id.h1l);
        h2l=findViewById(R.id.h2l);
        h3l=findViewById(R.id.h3l);
        h4l=findViewById(R.id.h4l);
        h1c=findViewById(R.id.h1c);
        h2c=findViewById(R.id.h2c);
        h3c=findViewById(R.id.h3c);
        h4c=findViewById(R.id.h4c);
        h1h=findViewById(R.id.h1h);
        h2h=findViewById(R.id.h2h);
        h3h=findViewById(R.id.h3h);
        h4h=findViewById(R.id.h4h);
        h1i=findViewById(R.id.h1i);
        h2i=findViewById(R.id.h2i);
        h3i=findViewById(R.id.h3i);
        h4i=findViewById(R.id.h4i);
        h1=findViewById(R.id.h1);
        h2=findViewById(R.id.h2);
        h3=findViewById(R.id.h3);
        h4=findViewById(R.id.h4);
        currentImage=findViewById(R.id.currentImage);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num=zip.getText().toString();
                s="https://api.openweathermap.org/geo/1.0/zip?zip="+num+"&appid=348577966b541c56afff524c2e796163";
                AsyncThread convert = new AsyncThread();
                convert.execute(s);
            }
        });
    }

    public class AsyncThread extends AsyncTask<String, Void, Void> { //I passed the string in here!!!
        JSONObject obs;
        @Override
        protected Void doInBackground(String... ds) {
            try{
                myURL = new URL(ds[0]);
                URLConnection urlConnection = myURL.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    Log.d("test",""+inputLine);
                    obs = new JSONObject(inputLine);
                    latt=obs.getDouble("lat");
                    lonn=obs.getDouble("lon");
                }
                in.close();

            } catch(MalformedURLException e){
                e.fillInStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            lat.setText("Lat: "+ String.valueOf(latt));
            lon.setText("Lon: "+String.valueOf(lonn));
            s2 = "https://api.openweathermap.org/data/2.5/weather?lat=" + latt + "&lon=" + lonn + "&appid=348577966b541c56afff524c2e796163";
            getCurrentData = new AsyncThread2();
            getCurrentData.execute(s2);
            long timestamp = System.currentTimeMillis();
            Date dater = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            String dateTime = sdf.format(dater);
            date.setText(dateTime.substring(0,10)+"   "+dateTime.substring(10));

        }
    }

    public class AsyncThread2 extends AsyncTask<String, Void, Void> {
        JSONObject current2;
        JSONObject current3;

        @Override
        protected Void doInBackground(String... dss) {
            try {
                myURL2 = new URL(dss[0]);
                URLConnection urlConnection2 = myURL2.openConnection();
                BufferedReader in2 = new BufferedReader(new InputStreamReader(urlConnection2.getInputStream()));
                String inputLine2;
                while ((inputLine2 = in2.readLine()) != null) {
                    current = new JSONObject((inputLine2));
                    Log.d("test2", "" + current);

                    current2 = new JSONObject(((current.getString("weather").substring(1, current.getString("weather").length() - 1))));
                    Log.d("test3", "" + current2);

                    current3 = new JSONObject(((current.getString("main"))));
                    Log.d("test4", "" + current3);
                }

                in2.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            String pic = null;
            try {
                city.setText(current.getString("name"));
                conditions.setText(current2.getString("description"));
                tempC = (current3.getDouble("temp"));
                convertTemp(tempC);
                temp.setText(convertedTemp.toString() + "°F");

                tempL = (current3.getDouble("temp_min"));
                convertTemp(tempL);
                low.setText("L: "+convertedTemp.toString() + "°F");

                tempH = (current3.getDouble("temp_max"));
                convertTemp(tempH);
                high.setText("H: "+convertedTemp.toString() + "°F");
                pic = current2.getString("icon");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            addPicture(pic, currentImage);
            addQuote(pic);
            s3 = "https://api.openweathermap.org/data/2.5/forecast?lat="+latt+"&lon="+lonn+"&appid=348577966b541c56afff524c2e796163";
            future = new AsyncThread3();
            future.execute(s3);
        }
    }


    public class AsyncThread3 extends AsyncTask<String, Void, Void> {
        JSONObject future2, future3,future4,future5,future6,future7,future8,future9,future10,future11,future12,future13,future14;

        @Override
        protected Void doInBackground(String... dsss) {
            try{
                myURL3 = new URL(dsss[0]);
                URLConnection urlConnection3 = myURL3.openConnection();
                BufferedReader in3 = new BufferedReader(new InputStreamReader(urlConnection3.getInputStream()));
                String inputLine3;
                while ((inputLine3 = in3.readLine()) != null) {
                    future2 = new JSONObject((inputLine3));
                    future3 = new JSONObject(future2.getJSONArray("list").getJSONObject(0).toString());
                    future4 = new JSONObject(future3.getString("main"));
                    future5 = new JSONObject(((future3.getString("weather").substring(1,future3.getString("weather").length()-1))));
                    Log.d("future3", future3.toString());
                    Log.d("future4", future4.toString());
                    Log.d("future5", future5.toString());

                    future6 = new JSONObject(future2.getJSONArray("list").getJSONObject(1).toString());
                    future7 = new JSONObject(future6.getString("main"));
                    future8 = new JSONObject(((future6.getString("weather").substring(1,future6.getString("weather").length()-1))));
                    Log.d("future6", future6.toString());
                    Log.d("future7", future7.toString());
                    Log.d("future8", future8.toString());

                    future9 = new JSONObject(future2.getJSONArray("list").getJSONObject(2).toString());
                    future10 = new JSONObject(future9.getString("main"));
                    future11 = new JSONObject(((future9.getString("weather").substring(1,future9.getString("weather").length()-1))));
                    Log.d("future10", future10.toString());
                    Log.d("future11", future11.toString());

                    future12 = new JSONObject(future2.getJSONArray("list").getJSONObject(3).toString());
                    future13 = new JSONObject(future12.getString("main"));
                    future14 = new JSONObject(((future12.getString("weather").substring(1,future12.getString("weather").length()-1))));
                    Log.d("future13", future13.toString());
                    Log.d("future14", future14.toString());
                }
                in3.close();

            } catch(MalformedURLException e){
                e.fillInStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            try {
                long t = (Long.parseLong(future3.getString("dt")));
                Date d = new Date(t*1000);
                SimpleDateFormat sdf = new SimpleDateFormat("h a");
                String n = sdf.format(d);
                h1.setText(n);

                long t2 = (Long.parseLong(future6.getString("dt")));
                Date d2 = new Date(t2*1000);
                SimpleDateFormat sdf2 = new SimpleDateFormat("h a");
                String n2 = sdf2.format(d2);
                h2.setText(n2);

                long t3 = (Long.parseLong(future9.getString("dt")));
                Date d3 = new Date(t3*1000);
                SimpleDateFormat sdf3 = new SimpleDateFormat("h a");
                String n3 = sdf3.format(d3);
                h3.setText(n3);

                long t4 = (Long.parseLong(future12.getString("dt")));
                Date d4 = new Date(t4*1000);
                SimpleDateFormat sdf4 = new SimpleDateFormat("h a");
                String n4 = sdf4.format(d4);
                h4.setText(n4);

                tempL = (future4.getDouble("temp_min"));
                convertTemp(tempL);
                h1l.setText("L: "+convertedTemp.toString()+"°F");
                tempH = (future4.getDouble("temp_max"));
                convertTemp(tempH);
                h1h.setText("  H: "+convertedTemp.toString()+"°F");
                h1c.setText(future5.getString("description"));
                addPicture(future5.getString("icon"), h1i);

                tempL = (future7.getDouble("temp_min"));
                convertTemp(tempL);
                h2l.setText("L: "+convertedTemp.toString()+"°F");
                tempH = (future7.getDouble("temp_max"));
                convertTemp(tempH);
                h2h.setText("H: "+convertedTemp.toString()+"°F");
                h2c.setText(future8.getString("description"));
                addPicture(future8.getString("icon"), h2i);

                tempL = (future10.getDouble("temp_min"));
                convertTemp(tempL);
                h3l.setText("L: "+convertedTemp.toString()+"°F");
                tempH = (future10.getDouble("temp_max"));
                convertTemp(tempH);
                h3h.setText("H: "+convertedTemp.toString()+"°F");
                h3c.setText(future11.getString("description"));
                addPicture(future11.getString("icon"), h3i);

                tempL = (future13.getDouble("temp_min"));
                convertTemp(tempL);
                h4l.setText("L: "+convertedTemp.toString()+"°F");
                tempH = (future13.getDouble("temp_max"));
                convertTemp(tempH);
                h4h.setText("H: "+convertedTemp.toString()+"°F");
                h4c.setText(future14.getString("description"));
                addPicture(future14.getString("icon"), h4i);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    private void addQuote(String pic){
        if(pic.equals("01d")) //day clear
            quote.setText("\"You walk in the room and there's a beautiful sunshine ray\"");
        if(pic.equals("01d")) //night clear
            quote.setText("\"It was the night things changed\"");
        if(pic.equals("02d") || pic.equals("02n")) //few c
           quote.setText("\"You'll brighten up the whole sky\"");
        if(pic.equals("03d") || pic.equals("03n")) //scattered
            quote.setText("\"And the sky turned black like a perfect storm\"");
        if(pic.equals("04n") || pic.equals("04d")) //broken
            quote.setText("\"I think you fell off of your cloud today\"");
        if(pic.equals("09d") || pic.equals("09n")) //shower
            quote.setText("\"Just know I'm right here hopin' that you'll come in with the rain.\"");
        if(pic.equals("10d") || pic.equals("10n")) //rain
            quote.setText("\"There’s something 'bout the say the street looks when it’s just rained\"");
        if(pic.equals("11d") || pic.equals("11n")) //thunder
            quote.setText("\"You'll brighten up the sky\"");
        if(pic.equals("13d") || pic.equals("13d")) //snow
            quote.setText("\"Sidewalk chalk covered in snow\"");
        if(pic.equals("50d") || pic.equals("50n")) //mist
            quote.setText("\"Into the mist, into the clouds\"");
    }


    private void addPicture(String pic, ImageView v) {
        if(pic.equals("01d"))
            v.setImageResource(R.drawable.dayclear);
        if(pic.equals("01n"))
            v.setImageResource(R.drawable.nightclear);
        if(pic.equals("02d"))
            v.setImageResource(R.drawable.dayfewc);
        if(pic.equals("02n"))
            v.setImageResource(R.drawable.nightfewc);
        if(pic.equals("03d") || pic.equals("03n"))
            v.setImageResource(R.drawable.scattered);
        if(pic.equals("04n") || pic.equals("04d"))
            v.setImageResource(R.drawable.broken);
        if(pic.equals("09d") || pic.equals("09n"))
            v.setImageResource(R.drawable.shower);
        if(pic.equals("10d"))
            v.setImageResource(R.drawable.dayrain);
        if(pic.equals("10n"))
            v.setImageResource(R.drawable.nightrain);
        if(pic.equals("11d") || pic.equals("11n"))
            v.setImageResource(R.drawable.thunder);
        if(pic.equals("13d") || pic.equals("13d"))
            v.setImageResource(R.drawable.snow);
        if(pic.equals("50d") || pic.equals("50n"))
            v.setImageResource(R.drawable.mist);
    }

    private void convertTemp(Double t) {
        double tp = (t-273.15)*9/5+32;
        convertedTemp = Math.round(tp*100.0)/100.0;
    }



}
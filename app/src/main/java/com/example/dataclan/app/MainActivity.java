package com.example.dataclan.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RequestQueue rq;


    String URL = "http://40.121.193.68/api/Person/01";
    String URL_Payment="http://40.121.193.68/api/Payment";

    TextView balancetext;
    EditText amounttext;
    //String amounttext2="3000";
   String recipient_text="02";
    String sender_text="01";
    Button execute;

    String balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amounttext=(EditText)findViewById( R.id.editText);
        execute=(Button) findViewById(R.id.button);

        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertSV();
             }
        });


        rq = Volley.newRequestQueue(this);

        balancetext = (TextView) findViewById(R.id.balance);

        sendjsonrequest();

    }
    public void sendjsonrequest(){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    balance = response.getString("balance");

                    balancetext.setText(balance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            public void onErrorResponse(VolleyError error) {

            }
        });

        rq.add(jsonObjectRequest);
    }
    /*
        public void postjsonrequest(){
            JSONObject object= new JSONObject();
            try {
                object.put("amount","500");

            }catch (JSONException e){
                e.printStackTrace();
            }
            JSONArray array= new JSONArray();



            rq.add(object);
        }*/
   public void InsertSV(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Payment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse (VolleyError error){
                //Toast.makeText(MainActivity.this, error+"", Toast.LENGTH_SHORT.show());
            }
        }){
            @Override
            protected Map<String, String> getParams () throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
               String amount= amounttext.getText().toString();
               String recipient=recipient_text.toString();
               String sender=sender_text.toString();
               params.put("amount",amount);
               params.put("recipient",recipient);
               params.put ("sender",sender);

                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(stringRequest);
    }

}


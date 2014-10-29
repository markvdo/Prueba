package com.example.markvan.prueba;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView nombre = (TextView) findViewById(R.id.nombre);


        Button boton = (Button) findViewById(R.id.GetData);

        boton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ws = "http://www.mocky.io/v2/54511ff8584e0f38186d00c3";

                if (this.hayConexion()) {
                    new HttpAsyncTask().execute(ws);
                } else {
                    final String noConex = "No hay conexion";
                    nombre.setText(noConex);
                }
            }
        });
    }

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();

            //si la linea no es vacia, la convierto a string y devuelvo
            if(inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "Did not work!";
            }
        } catch (Exception e) {

        }

        return result;
    }

    //leo la linea (append)
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    // chequeo si hay conexion a internet
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false; }
    }
}

private class HttpAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {

        return GET(urls[0]);
    }
    // onPostExecute muestro resultados
    @Override
    protected void onPostExecute(String result) {

        JSONObject json = new JSONObject(result);
        nombre.setText(json.getString("Nombre"));
    }
}
}
package es.truscoandrisco.discotecasmadrid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class MyJSONEventos {

    public ArrayList<Evento> arrayEvento = null;

    public Evento evento = null;

    public String doGet(String url) {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String str = convertStreamToString(entity.getContent());
            return str;
        } catch (IOException e) {
            return null;
        }
    }

    public ArrayList<Evento> parse(String str) {
        try {
            arrayEvento = new ArrayList<Evento>();
            JSONArray jsonArray = new JSONArray(str);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                JSONObject jsonChild = json.getJSONObject("fields");
                String ID = json.getString("pk");
                String disco = jsonChild.getString("discoteca").toString();
                String descripcion = jsonChild.getString("descripcion").toString();
                String fecha = jsonChild.getString("fecha").toString();
                evento = new Evento(ID, disco, descripcion, fecha, null, null);
                arrayEvento.add(evento);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Dentro", String.valueOf(arrayEvento.size()));
        return arrayEvento;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8 * 1024);
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) sb.append(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

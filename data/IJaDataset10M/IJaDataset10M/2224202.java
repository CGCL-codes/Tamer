package ve.usb.buscame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AmigosOffline extends ListActivity implements Runnable {

    static String seleccion;

    static String[] amigosOffline;

    static String amigoABorrar = "";

    private ProgressDialog dialog;

    static final String mostrarOfflinePhp = "http://" + Login.ipServidor + "/mostrarOnOffline.php";

    static final String eliminarAmigoPhp = "http://" + Login.ipServidor + "/eliminarAmigo.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = ProgressDialog.show(AmigosOffline.this, "", "Cargando, por favor espere.", true);
        Thread thread = new Thread(this);
        thread.start();
    }

    protected boolean onLongListItemClick(View v, int pos, long id) {
        Log.i("AmigosOffline", "onLongListItemClick id=" + id);
        amigoABorrar = amigosOffline[pos];
        Log.i("AmigosOffline", "Amigo a borrar: " + amigoABorrar);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Desea eliminar a " + amigoABorrar + "?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                getServerData(eliminarAmigoPhp);
                AmigosOffline.this.finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return true;
    }

    public void onListItemClick(View v, int position, long id) {
        Log.i("AmigosOffline", "onListItemClick id=" + id);
        Toast.makeText(this, "Seleccionaste a: " + amigosOffline[position], Toast.LENGTH_SHORT).show();
        seleccion = amigosOffline[position];
        Cliente.userConv = seleccion.trim();
        Log.i("AmigosOffline", "seleccion = " + Cliente.userConv + ": " + Cliente.userConv.length());
        startActivity(new Intent(this, Conversacion.class));
    }

    private String getServerData(String archivoPhp) {
        InputStream is = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("usuario", Login.usuario.trim()));
        nameValuePairs.add(new BasicNameValuePair("amigoABorrar", amigoABorrar.trim()));
        Log.d("AmigosOffline", "getServerData:" + Login.usuario + ":" + amigoABorrar);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(archivoPhp);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("AmigosOffline", "Error en la conexion http " + e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            String line = reader.readLine();
            is.close();
            result = line.trim().toString();
            Log.d("AmigosOffline", "Longitud line: " + line.trim().length());
        } catch (Exception e) {
            Log.e("AmigosOffline", "Error convirtiendo el resultado " + e.toString());
        }
        Log.d("AmigosOffline", "Funciono Json" + result);
        return result;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int indiceListaAmigos = 0;
        switch(keyCode) {
            case KeyEvent.KEYCODE_DEL:
                Log.i("AmigosOffline", "Delete key hit: " + keyCode);
                indiceListaAmigos = getSelectedItemPosition();
                amigoABorrar = amigosOffline[indiceListaAmigos];
                Log.i("AmigosOffline", "Amigo a borrar: " + amigoABorrar);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Desea eliminar a " + amigoABorrar + "?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        getServerData(eliminarAmigoPhp);
                        AmigosOffline.this.finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            setListAdapter(new ArrayAdapter<String>(AmigosOffline.this, R.layout.list_item, amigosOffline));
            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                    onListItemClick(v, pos, id);
                }
            });
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                    return onLongListItemClick(v, pos, id);
                }
            });
            dialog.dismiss();
        }
    };

    @Override
    public void run() {
        String stringUsuarios = getServerData(mostrarOfflinePhp).replace("[", "").replace("]", "").replace("\"", "");
        amigosOffline = stringUsuarios.split(",");
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(Cliente.socket.getOutputStream())), true);
            out.println("AmigosOffline|x");
            List<String> usuarioFinalSerializado = Arrays.asList(amigosOffline);
            out.println(usuarioFinalSerializado.toString());
            Log.d("AmigosOffline", "Enviando para offline: " + usuarioFinalSerializado.toString());
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.sendEmptyMessage(0);
    }
}

package br.nom.camargo.wanderson.presenter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import br.nom.camargo.wanderson.presenter.DeviceElement.Type;

/**
 * Lista de Dispositivos
 * 
 * Exibe uma lista dos dispositivos disponíveis para utilização no aplicativo.
 * Efetua uma conexão ao banco de dados procurando os dispositivos cadastrados,
 * exibindo-os em linhas especializadas.
 * 
 * @author Wanderson Henrique Camargo Rosa
 */
public class DeviceListActivity extends ListActivity {

    /**
     * Atualiza a Lista de Dispositivos
     * @return Próprio Objeto para Encadeamento
     */
    public DeviceListActivity update() {
        DeviceArrayAdapter adapter = (DeviceArrayAdapter) getListAdapter();
        adapter.clear();
        ArrayList<DeviceElement> list = ((PresenterApplication) getApplication()).getDatabase().synchronize().retrieve();
        for (DeviceElement device : list) {
            adapter.add(device);
        }
        return this;
    }

    /**
     * Cria uma Conexão ao Dispositivo Informado
     * Enviando uma Intensão para Atividade do Apresentador
     */
    public DeviceListActivity connect(DeviceElement device) {
        Intent intent = new Intent(PresenterActivity.class.getName());
        intent.putExtra("name", device.getName());
        intent.putExtra("type", device.getType().toString());
        startActivity(intent);
        device.setUpdate(System.currentTimeMillis());
        ((PresenterApplication) getApplication()).getDatabase().update(device, device.getName(), device.getType());
        return this;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceArrayAdapter adapter = new DeviceArrayAdapter(this, R.layout.devicerow);
        setListAdapter(adapter);
        registerForContextMenu(getListView());
    }

    public void onResume() {
        super.onResume();
        update();
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo minfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) minfo;
        DeviceElement element = (DeviceElement) getListAdapter().getItem(info.position);
        if (element.getType() == Type.Bluetooth) {
            menu.setHeaderTitle(R.string.app_bluetooth);
            getMenuInflater().inflate(R.menu.bluetooth, menu);
        } else {
            menu.setHeaderTitle(R.string.app_ethernet);
            getMenuInflater().inflate(R.menu.ethernet, menu);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final DeviceElement element = (DeviceElement) getListAdapter().getItem(info.position);
        switch(item.getItemId()) {
            case R.id.menu_ethernet_connect:
            case R.id.menu_bluetooth_connect:
                connect(element);
                break;
            case R.id.menu_ethernet_remove:
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.ethernet_remove_confirm);
                    builder.setTitle(R.string.ethernet_remove);
                    builder.setPositiveButton(R.string.app_yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            ((PresenterApplication) getApplication()).getDatabase().remove(element);
                            update();
                        }
                    });
                    builder.setNegativeButton(R.string.app_no, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.menu_ethernet_update:
                {
                    Intent intent = new Intent(EthernetActivity.class.getName());
                    intent.putExtra("name", element.getName());
                    startActivity(intent);
                }
                break;
        }
        return true;
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        DeviceElement device = (DeviceElement) getListAdapter().getItem(position);
        connect(device);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        if (result) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.devicelist, menu);
        }
        return result;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        switch(item.getItemId()) {
            case R.id.menu_device_add:
                {
                    Intent intent = new Intent(EthernetActivity.class.getName());
                    startActivity(intent);
                    result = true;
                    break;
                }
            case R.id.menu_config:
                {
                    Intent intent = new Intent(ConfigActivity.class.getName());
                    startActivity(intent);
                    result = true;
                    break;
                }
            case R.id.menu_about:
                {
                    Intent intent = new Intent(AboutActivity.class.getName());
                    startActivity(intent);
                    result = true;
                    break;
                }
            default:
                result = super.onOptionsItemSelected(item);
        }
        return result;
    }

    /**
     * Lista de Dispositivos
     * 
     * Utilizada pela atividade de listagem de dispositivos para renderização
     * das linhas e gerenciamento do conteúdo.
     * 
     * @author Wanderson Henrique Camargo Rosa
     */
    private class DeviceArrayAdapter extends ArrayAdapter<DeviceElement> {

        /**
         * Construtor
         * @param context Atividade
         * @param view Identificador da Visualização
         */
        public DeviceArrayAdapter(Context context, int view) {
            super(context, view);
        }

        public View getView(int position, View convert, ViewGroup parent) {
            LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convert = li.inflate(R.layout.devicerow, null);
            TextView name = (TextView) convert.findViewById(R.id.devicerow_name);
            TextView summary = (TextView) convert.findViewById(R.id.devicerow_summary);
            ImageView image = (ImageView) convert.findViewById(R.id.devicerow_image);
            ContentValues content = getItem(position).getContentValues();
            name.setText(content.getAsString("name"));
            long timestamp = content.getAsLong("updated");
            String update = "Never";
            if (timestamp > 0) {
                Date d = new Date(timestamp);
                update = DateFormat.getDateInstance().format(d);
            }
            summary.setText(content.getAsString("type") + " @ " + update);
            if (getItem(position).getType() == Type.Bluetooth) {
                image.setImageResource(R.drawable.bluetooth);
            } else {
                image.setImageResource(R.drawable.wifi);
            }
            return convert;
        }
    }
}

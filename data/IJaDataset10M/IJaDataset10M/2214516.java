package com.android.plagas;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.android.plagas.dao.Campana;
import com.android.plagas.dao.CampanaDao;
import com.android.plagas.dao.Lote;
import com.android.plagas.dao._dao;

public class listCampanasActivity extends Activity {

    private CampanaDao campanaDao;

    private Campana campana;

    private Lote lote;

    private _dao dao;

    List<Campana> listOfCampanas = new ArrayList<Campana>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = com.android.plagas.dao._dao.getInstance(this);
        campanaDao = dao.getSession().getCampanaDao();
        Bundle bundle = getIntent().getExtras();
        lote = dao.getSession().getLoteDao().load(bundle.getLong("loteId"));
        setContentView(R.layout.mainlist);
        TextView titulo = (TextView) findViewById(R.id.tvTitle);
        titulo.setText(R.string.Campaigns);
        fillListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dao.getSession().clear();
        fillListView();
    }

    private void fillListView() {
        ListView list = (ListView) findViewById(R.id.lvMain);
        list.setClickable(true);
        lote.resetCampanas();
        listOfCampanas = lote.getCampanas();
        adapCampana adapter = new adapCampana(this, listOfCampanas);
        list.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
                campana = listOfCampanas.get(position);
                goToListMonitoreos();
            }
        });
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
        registerForContextMenu(list);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert;
        builder.setMessage("Esta seguro que desea borrar la campa�a? Todos los registros asociados seran eliminados.").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                campana.deleteAndCascade();
                fillListView();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alert = builder.create();
        alert.show();
        return dialog;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvMain) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            ListView lv = (ListView) findViewById(R.id.lvMain);
            campana = (Campana) lv.getAdapter().getItem(info.position);
            menu.setHeaderTitle("Opciones");
            String[] menuItems = getResources().getStringArray(R.array.botonera);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int menuItemIndex = item.getItemId();
        switch(menuItemIndex) {
            case 0:
                edit();
                break;
            case 1:
                delete();
                break;
        }
        return true;
    }

    public void onAddClick(View Button) {
        Intent intent = new Intent();
        intent.putExtra("MODO", 0);
        intent.putExtra("loteId", lote.getId());
        intent.setClass(this, abmCampanasActivity2.class);
        startActivity(intent);
    }

    public void delete() {
        showDialog(0);
    }

    public void edit() {
        Intent intent = new Intent();
        intent.putExtra("MODO", 1);
        intent.putExtra("campanaId", campana.getId());
        intent.putExtra("loteId", lote.getId());
        intent.setClass(this, abmCampanasActivity2.class);
        startActivity(intent);
    }

    private void goToListMonitoreos() {
        Intent intent = new Intent();
        intent.putExtra("campanaId", campana.getId());
        intent.setClass(this, listMonitoreosActivity.class);
        startActivity(intent);
    }
}

package com.etracks.gui;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.client.ClientProtocolException;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.etracks.domini.CtrlServerRoutes;

public class RemoteRouteListActivity extends ListActivity {

    private CtrlServerRoutes remoteRouteListCtrl;

    private ArrayList<String> routes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            remoteRouteListCtrl = new CtrlServerRoutes(getApplicationContext());
            routes = remoteRouteListCtrl.getRoutes();
            setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, routes));
        } catch (ClientProtocolException e) {
            returnErrorMsg(e.getMessage());
        } catch (IOException e) {
            returnErrorMsg(e.getMessage());
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        downloadTrack(position);
    }

    protected void downloadTrack(int position) {
        boolean newRoute = false;
        ;
        try {
            newRoute = remoteRouteListCtrl.downloadRoute(position);
            Intent mIntent = new Intent();
            mIntent.putExtra("route_id", remoteRouteListCtrl.getId(position));
            if (newRoute) mIntent.putExtra("msg", "downloaded track \"" + routes.get(position) + "\" from server"); else mIntent.putExtra("msg", "la ruta ja estava baixada");
            setResult(RESULT_OK, mIntent);
            finish();
        } catch (ClientProtocolException e) {
            returnErrorMsg(e.getMessage());
        } catch (IOException e) {
            returnErrorMsg(e.getMessage());
        }
    }

    protected void returnErrorMsg(String msg) {
        Intent intent = new Intent();
        intent.putExtra("msg", "ERROR: " + msg);
        setResult(RESULT_OK, intent);
        finish();
    }
}

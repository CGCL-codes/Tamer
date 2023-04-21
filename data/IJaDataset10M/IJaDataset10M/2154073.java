package ddss.geographical.ui;

import java.util.ArrayList;
import ddss.android.R;
import ddss.geographical.webservice.GeographicEntitySerializable;
import ddss.geographical.webservice.GetRootGeographicEntityRequest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GeographicExplorerMain extends ListActivity implements Runnable, OnItemClickListener {

    private ProgressDialog m_LoadProgress;

    private Handler m_MsgHandler;

    private ArrayList<GeographicEntitySerializable> m_EntitiesList;

    public GeographicExplorerMain() {
        m_MsgHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                messageHandler(msg);
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(this);
        getRootGeographicEntities();
    }

    private void getRootGeographicEntities() {
        m_LoadProgress = ProgressDialog.show(this, "", getString(R.string.loading), true);
        new Thread(this).start();
    }

    public void run() {
        GetRootGeographicEntityRequest request = new GetRootGeographicEntityRequest();
        Message msg = new Message();
        try {
            request.Send();
            msg.what = 0;
            msg.obj = request.getResponse();
        } catch (Exception e) {
            msg.what = -1;
            msg.obj = e;
        }
        m_MsgHandler.sendMessage(msg);
    }

    protected void messageHandler(Message msg) {
        m_LoadProgress.dismiss();
        if (!(msg.obj instanceof GeographicEntitySerializable)) {
            showFailMessage(msg.obj);
            return;
        }
        displayEntitiesList(msg);
    }

    private void displayEntitiesList(Message msg) {
        GeographicEntitySerializable entity = (GeographicEntitySerializable) msg.obj;
        ArrayList<String> entitiesList = new ArrayList<String>();
        entitiesList.add(entity.Name);
        m_EntitiesList = new ArrayList<GeographicEntitySerializable>();
        m_EntitiesList.add(entity);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.geographical_geox_list_item, entitiesList));
    }

    private void showFailMessage(Object failure) {
        String failMessage;
        if (failure instanceof Exception) failMessage = ((Exception) failure).getMessage(); else failMessage = failure.toString();
        showFailMessage(getString(R.string.cannotLoadData) + failMessage);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(failMessage).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onItemClick(AdapterView<?> parent, View vw, int position, long id) {
        GeographicEntitySerializable entity = m_EntitiesList.get(position);
        Intent childrenActivity = new Intent();
        childrenActivity.putExtra("ddss.geographical.entities.id", entity.ID);
        childrenActivity.putExtra("ddss.geographical.entities.name", entity.Name);
        childrenActivity.setClass(this, ddss.geographical.ui.GeographicExplorerEntitiesList.class);
        startActivity(childrenActivity);
    }
}

package de.tudresden.inf.rn.mobilis.android;

import java.util.List;
import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import de.tudresden.inf.rn.mobilis.android.services.AbstractAuthService;
import de.tudresden.inf.rn.mobilis.android.services.SessionService;
import de.tudresden.inf.rn.mobilis.android.services.SocialNetworkManagementService;
import de.tudresden.inf.rn.mobilis.android.util.DBHelper;
import de.tudresden.inf.rn.mobilis.xmpp.beans.Credential;

/**
 * Activity to show all logged-in networks.
 * @author Dirk
 */
public class ConnectionsActivity extends ListActivity {

    private static final String TAG = "ConnectionsActivity";

    private SocialNetworkManagementService snms;

    private DBHelper db;

    private static int listItemId = R.id.cons_act_lst_item;

    private static int listItemLayout = R.layout.connections_act_item;

    private TextView markedView;

    private AbstractAuthService markedNetwork;

    private TextView selectedView;

    private ListView conList;

    private Button disconnectAllButton;

    private Button disconnectSelButton;

    private Button refreshSelButton;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snms = SessionService.getInstance().getSocialNetworkManagementService();
        db = DBHelper.getDB();
        createGUIContents();
    }

    private void createGUIContents() {
        setContentView(R.layout.connections_activity);
        conList = getListView();
        conList.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> listView, View view, int position, long rowId) {
                deselectCurrentListEntry();
                selectListEntry(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        conList.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!conList.hasFocus()) {
                    deselectCurrentListEntry();
                }
            }
        });
        disconnectAllButton = (Button) findViewById(R.id.cons_act_btn_disall);
        disconnectAllButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                enableSelectionDependentButtons(false);
                disconnectAllNetworks();
                updateConnectionList();
            }
        });
        disconnectSelButton = (Button) findViewById(R.id.cons_act_btn_dissel);
        disconnectSelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                enableSelectionDependentButtons(false);
                disconnectFromSelectedNetwork();
                updateConnectionList();
            }
        });
        refreshSelButton = (Button) findViewById(R.id.cons_act_btn_refresh);
        refreshSelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                enableSelectionDependentButtons(false);
                refreshSelectedNetwork();
                updateConnectionList();
            }
        });
        backButton = (Button) findViewById(R.id.cons_act_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
        enableSelectionDependentButtons(false);
        updateConnectionList();
    }

    private void updateConnectionList() {
        List<AbstractAuthService> connectedNetworks = snms.getAuthenticatedServices();
        ArrayAdapter<AbstractAuthService> a = new ArrayAdapter<AbstractAuthService>(this, listItemLayout, connectedNetworks);
        setListAdapter(a);
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long rowId) {
        super.onListItemClick(listView, view, position, rowId);
        unmarkCurrentListEntry();
        markedNetwork = (AbstractAuthService) listView.getAdapter().getItem(position);
        Log.v(TAG, "Selected Network: " + markedNetwork.toString());
        markListEntry(view);
        enableSelectionDependentButtons(true);
    }

    private void deselectCurrentListEntry() {
        if ((selectedView != null) && (selectedView != markedView)) {
            selectedView.setBackgroundColor(Color.rgb(68, 68, 68));
            selectedView.setTextColor(Color.rgb(190, 190, 190));
        }
    }

    private void selectListEntry(View view) {
        selectedView = (TextView) view;
        if (selectedView != markedView) {
            selectedView = (TextView) view;
            view.setBackgroundColor(Color.rgb(255, 125, 0));
            TextView listEntry = (TextView) view.findViewById(listItemId);
            listEntry.setTextColor(Color.rgb(0, 0, 0));
        }
    }

    private void unmarkCurrentListEntry() {
        if (markedView != null) {
            markedView.setBackgroundColor(Color.rgb(68, 68, 68));
            markedView.setTextColor(Color.rgb(190, 190, 190));
        }
    }

    private void markListEntry(View view) {
        markedView = (TextView) view;
        view.setBackgroundColor(Color.rgb(234, 171, 0));
        TextView listEntry = (TextView) view.findViewById(listItemId);
        listEntry.setTextColor(Color.rgb(0, 0, 0));
    }

    protected String disconnectFromSelectedNetwork() {
        String lastLogin = null;
        if (markedNetwork != null) {
            lastLogin = markedNetwork.getConnectedUserId();
            markedNetwork.logout(null);
            updateConnectionList();
        }
        return lastLogin;
    }

    protected void disconnectAllNetworks() {
        List<AbstractAuthService> connectedNetworks = snms.getAuthenticatedServices();
        for (AbstractAuthService network : connectedNetworks) {
            network.logout(null);
        }
        updateConnectionList();
    }

    /**
     * Just reconnects to the selected network.
     */
    private void refreshSelectedNetwork() {
        if (markedNetwork != null) {
            String lastLogin = disconnectFromSelectedNetwork();
            if (lastLogin != null) {
                String network = markedNetwork.getNetworkName();
                Credential lastCredential = db.getCredential(lastLogin, network);
                if (lastCredential != null) {
                    SessionService.getInstance().getLoginLoop().requestSingleLogin(lastCredential);
                }
            }
        }
    }

    private void discardListSelection() {
        markedView = null;
        markedNetwork = null;
        enableSelectionDependentButtons(false);
    }

    private void enableSelectionDependentButtons(boolean enabled) {
        disconnectSelButton.setEnabled(enabled);
        refreshSelButton.setEnabled(enabled);
    }

    @Override
    protected void onResume() {
        super.onResume();
        discardListSelection();
    }
}

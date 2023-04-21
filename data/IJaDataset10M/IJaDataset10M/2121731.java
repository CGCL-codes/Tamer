package org.aladdinframework.ui;

import java.util.ArrayList;
import java.util.List;
import org.aladdinframework.core.AladdinApplication;
import org.aladdinframework.core.AladdinService;
import org.aladdinframework.core.R;
import org.aladdinframework.data.SettingsManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * User interface for reviewing applications that still require authorization before they are able to use Aladdin
 * Framework services (these are known as pending applications). This Activity is automatically focused if the user 
 * clicks on an Aladdin Pending Application Notification in the Android notification tray (handled by AladdinActivity).
 * 
 * @author Darren Carlson
 *
 */
public class PendingApplicationActivity extends Activity {

    public final String TAG = this.getClass().getSimpleName();

    private static final int ACTIVITY_EDIT = 1;

    private List<AladdinApplication> pendingApps = null;

    private AladdinApplicationAdapter adapter;

    private ListView pendingAppList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_app_tab);
        pendingAppList = (ListView) findViewById(R.id.android_pendingAppList);
        pendingAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                editApplication((AladdinApplication) pendingAppList.getItemAtPosition(position));
            }
        });
        registerForContextMenu(pendingAppList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pendingApps = new ArrayList<AladdinApplication>(SettingsManager.getPendingApplications());
        this.adapter = new AladdinApplicationAdapter(this, R.layout.icon_row, new ArrayList<AladdinApplication>(pendingApps), true);
        pendingAppList.setAdapter(this.adapter);
    }

    ;

    private void editApplication(AladdinApplication app) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("app", app);
        bundle.putBoolean("pending", true);
        Intent i = new Intent(this, ApplicationSettingsActivity.class);
        i.putExtras(bundle);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            Bundle extras = intent.getExtras();
            switch(requestCode) {
                case ACTIVITY_EDIT:
                    AladdinApplication app = (AladdinApplication) extras.getSerializable("app");
                    if (AladdinService.authorizeApplication(app)) {
                        adapter.remove(app);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Application Approval!", Toast.LENGTH_SHORT).show();
                        if (adapter.isEmpty()) {
                            AladdinActivity.activateTab(0);
                        }
                    }
                    break;
            }
        }
    }
}

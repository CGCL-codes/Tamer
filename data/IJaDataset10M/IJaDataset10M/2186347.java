package org.aladdinframework.ui;

import java.util.List;
import org.aladdinframework.contextplugin.api.FidelityLevel;
import org.aladdinframework.core.AladdinApplication;
import org.aladdinframework.core.AladdinService;
import org.aladdinframework.core.R;
import org.aladdinframework.security.PluginPrivacySettings;
import org.aladdinframework.security.PrivacyPolicy;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * User interface for adjusting Aladdin Application settings.
 * 
 * @author Darren Carlson
 *
 */
public class ApplicationSettingsActivity extends Activity {

    public final String TAG = this.getClass().getSimpleName();

    private AladdinApplication app = null;

    private TextView nameText;

    private TextView appStatus;

    private TextView appDescription;

    private ListView plugList;

    private ImageView icon;

    private Spinner policySpinner;

    private boolean pending = false;

    private PluginSettingsAdapter pluginSettingsAdapter;

    private ArrayAdapter<PrivacyPolicy> privacyPolicyAdapter;

    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_app);
        Button btnFinishedEdit = (Button) findViewById(R.id.btn_finished_app_edit);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            app = (AladdinApplication) extras.getSerializable("app");
            nameText = (TextView) findViewById(R.id.app_edit_name);
            appDescription = (TextView) findViewById(R.id.app_edit_description);
            nameText.setText(app.getName());
            plugList = (ListView) findViewById(R.id.plug_settings_list);
            appStatus = (TextView) findViewById(R.id.app_edit_status);
            appDescription.setText(app.getDescription());
            icon = (ImageView) findViewById(R.id.icon);
            pending = extras.getBoolean("pending");
            if (pending) {
                btnFinishedEdit.setText(R.string.btn_finished_app_pending);
                appStatus.setText(app.getStatusString());
                icon.setImageResource(R.drawable.alert);
            } else {
                btnFinishedEdit.setText(R.string.btn_finished_app_edit);
                appStatus.setText(app.getStatusString());
                if (app.isEnabled()) {
                    if (AladdinService.checkConnected(app)) {
                        icon.setImageResource(R.drawable.app_connected);
                    } else {
                        icon.setImageResource(R.drawable.app_disconnected);
                    }
                } else {
                    icon.setImageResource(R.drawable.app_blocked);
                }
            }
            policySpinner = (Spinner) findViewById(R.id.privacy_policy_spinner);
            ImageView privacy_icon = (ImageView) findViewById(R.id.privacy_icon);
            privacy_icon.setImageResource(R.drawable.profile);
            privacyPolicyAdapter = new ArrayAdapter<PrivacyPolicy>(this, android.R.layout.simple_spinner_item, AladdinService.getPrivacyPolicies());
            privacyPolicyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            policySpinner.setAdapter(privacyPolicyAdapter);
            int position = privacyPolicyAdapter.getPosition(app.getPrivacyPolicy());
            policySpinner.setSelection(position, false);
            policySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                    handlePrivacyPolicyChange((PrivacyPolicy) policySpinner.getSelectedItem());
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
        btnFinishedEdit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("app", app);
                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }
        });
        registerForContextMenu(plugList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pluginSettingsAdapter = new PluginSettingsAdapter(this, R.layout.icon_row, app.getPluginPrivacySettings());
        plugList.setAdapter(this.pluginSettingsAdapter);
    }

    ;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.app_settings_contextmenu_fidelity);
        menu.add(0, -1, 0, "Default");
        for (FidelityLevel l : FidelityLevel.getAllFidelityLevels()) {
            menu.add(0, l.getID(), l.getID(), l.toString());
        }
    }

    /**
     * Called when a potentially custom FidelityLevel ContextItem is selected from the Plugin Settings List context menu. 
     * This means that a new PrivacyPolicy has been chosen and the app should be updated.
     */
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        PluginPrivacySettings policy = (PluginPrivacySettings) pluginSettingsAdapter.getItem(info.position);
        FidelityLevel level = FidelityLevel.getLevelForID(item.getItemId());
        if (level != null) {
            policy.overrideMaxFidelityLevel(level);
        } else {
            policy.setDefaultMaxFidelityLevel();
        }
        pluginSettingsAdapter.notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }

    /**
     * Handle PrivacyPolicy changes.
     * @param newPolicy
     */
    private void handlePrivacyPolicyChange(final PrivacyPolicy newPolicy) {
        boolean alertUser = false;
        for (PluginPrivacySettings settings : app.getPluginPrivacySettings()) {
            if (settings.isCustom()) {
                alertUser = true;
                break;
            }
        }
        if (alertUser) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Override Custom Settings?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    app.setPrivacyPolicy(newPolicy, true);
                    pluginSettingsAdapter.notifyDataSetChanged();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {
                    app.setPrivacyPolicy(newPolicy, false);
                    pluginSettingsAdapter.notifyDataSetChanged();
                    dialog.cancel();
                }
            });
            builder.create().show();
        } else {
            app.setPrivacyPolicy(newPolicy, true);
            pluginSettingsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Local class used as a datasource for PluginPrivacySettings. This class extends a typed Generic ArrayAdapter
     * and overrides getView in order to update the UI state.
     * 
     * @author Darren Carlson
     *
     */
    private class PluginSettingsAdapter extends ArrayAdapter<PluginPrivacySettings> {

        private List<PluginPrivacySettings> settings;

        public PluginSettingsAdapter(Context context, int textViewResourceId, List<PluginPrivacySettings> settings) {
            super(context, textViewResourceId, settings);
            this.settings = settings;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.icon_row, null);
            }
            PluginPrivacySettings setting = settings.get(position);
            if (setting != null) {
                TextView tt = (TextView) v.findViewById(R.id.toptext);
                TextView bt = (TextView) v.findViewById(R.id.bottomtext);
                ImageView icon = (ImageView) v.findViewById(R.id.icon);
                if (tt != null) {
                    tt.setText(setting.getPlugin().getName());
                }
                if (bt != null) {
                    if (setting.isCustom()) {
                        bt.setText(setting.getMaxFidelityLevel().toString() + " (Custom)");
                    } else {
                        bt.setText(setting.getMaxFidelityLevel().toString() + " (Auto)");
                    }
                }
                if (setting.getPlugin().isEnabled()) {
                    icon.setImageResource(R.drawable.plugin_enabled);
                } else {
                    icon.setImageResource(R.drawable.plugin_disabled);
                }
            }
            return v;
        }
    }
}

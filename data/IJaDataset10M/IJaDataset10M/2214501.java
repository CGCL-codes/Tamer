package de.janbusch.jhashpassword.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import de.janbusch.jhashpassword.R;
import de.janbusch.jhashpassword.xml.simple.data.HashPassword;
import de.janbusch.jhashpassword.xml.simple.data.Host;
import de.janbusch.jhashpassword.xml.simple.data.LoginName;

public class HPSettings extends Activity {

    private HashPassword hashPassword;

    private Spinner sprHostname;

    private Spinner sprLoginname;

    private Button btnRemHost;

    private Button btnEditHost;

    private Button btnAddLogin;

    private Button btnRemLogin;

    private ArrayAdapter<CharSequence> sprHostnameAdapter;

    private Button btnEditLogin;

    private boolean hasChanged;

    private int iCurrentSelectionHost;

    private int iCurrentSelectionLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        this.hashPassword = (HashPassword) getIntent().getSerializableExtra(getString(R.string.hp));
        sprHostname = (Spinner) findViewById(R.id.sprHostname);
        sprLoginname = (Spinner) findViewById(R.id.sprLoginname);
        btnRemHost = (Button) findViewById(R.id.btnRemHost);
        btnEditHost = (Button) findViewById(R.id.btnEditHost);
        btnEditLogin = (Button) findViewById(R.id.btnEditLogin);
        btnAddLogin = (Button) findViewById(R.id.btnAddLogin);
        btnRemLogin = (Button) findViewById(R.id.btnRemLogin);
        sprHostname.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int i, long id) {
                loadLoginNames();
                if (iCurrentSelectionHost != i) {
                    hasChanged = true;
                }
                iCurrentSelectionHost = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        sprLoginname.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int i, long id) {
                if (iCurrentSelectionLogin != i) {
                    hasChanged = true;
                }
                iCurrentSelectionLogin = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        loadHostNames();
        loadLoginNames();
        iCurrentSelectionHost = sprHostname.getSelectedItemPosition();
        iCurrentSelectionLogin = sprLoginname.getSelectedItemPosition();
    }

    private void loadHostNames() {
        int lastHostPos;
        String lastHostname = hashPassword.getLastHost();
        sprHostnameAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        sprHostnameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (Host host : hashPassword.getHosts().getHost()) {
            sprHostnameAdapter.add(host.getName());
        }
        lastHostPos = sprHostnameAdapter.getPosition(lastHostname);
        sprHostname.setAdapter(sprHostnameAdapter);
        if (lastHostPos == -1) {
            if (sprHostname.getCount() > 0) {
                sprHostname.setSelection(0);
            }
        } else {
            sprHostname.setSelection(lastHostPos);
        }
        if (sprHostname.getCount() > 0) {
            btnRemHost.setEnabled(true);
            btnEditHost.setEnabled(true);
            btnAddLogin.setEnabled(true);
        } else {
            btnRemHost.setEnabled(false);
            btnEditHost.setEnabled(false);
            btnAddLogin.setEnabled(false);
        }
    }

    private void loadLoginNames() {
        int lastLoginPos;
        if (sprHostname.getSelectedItem() != null) {
            String hostName = sprHostname.getSelectedItem().toString();
            Host currentHost = hashPassword.getHosts().getHostByName(hostName);
            if (currentHost != null) {
                ArrayAdapter<CharSequence> sprLoginAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
                sprLoginAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                for (LoginName login : currentHost.getLoginNames().getLoginName()) {
                    sprLoginAdapter.add(login.getName());
                }
                sprLoginname.setAdapter(sprLoginAdapter);
                lastLoginPos = sprLoginAdapter.getPosition(currentHost.getLastLogin());
                if (lastLoginPos == -1) {
                    if (sprLoginname.getCount() > 0) {
                        sprLoginname.setSelection(0);
                    }
                } else {
                    sprLoginname.setSelection(lastLoginPos);
                }
            }
        } else {
            ArrayAdapter<CharSequence> sprLoginAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
            sprLoginAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sprLoginname.setAdapter(sprLoginAdapter);
        }
        if (sprLoginname.getCount() > 0) {
            btnRemLogin.setEnabled(true);
            btnEditLogin.setEnabled(true);
        } else {
            btnRemLogin.setEnabled(false);
            btnEditLogin.setEnabled(false);
        }
    }

    /**
	 * This method specifies what to do if a button was clicked.
	 * 
	 * @param btn
	 *            Button that has been clicked as {@link View}.
	 * @return True if the button is known otherwise false.
	 */
    public boolean onButtonClicked(View btn) {
        final Host currentHost;
        final EditText etInput;
        LoginName currentLogin;
        AlertDialog.Builder inputDialog;
        switch(btn.getId()) {
            case R.id.btnAddHost:
                etInput = new EditText(this);
                inputDialog = new AlertDialog.Builder(this);
                inputDialog.setTitle(R.string.titleAddHost);
                inputDialog.setMessage(R.string.msgAddHost);
                inputDialog.setView(etInput);
                inputDialog.setPositiveButton(getString(R.string.OK), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newHostName = etInput.getText().toString().trim();
                        if (hashPassword.getHosts().getHostByName(newHostName) == null && newHostName.length() > 0) {
                            Host newHost = new Host();
                            newHost.setName(newHostName);
                            newHost.setCharset(hashPassword.getDefaultCharset());
                            newHost.setPasswordLength(hashPassword.getDefaultPasswordLength());
                            newHost.setHashType(hashPassword.getDefaultHashType());
                            hashPassword.getHosts().getHost().add(newHost);
                            hashPassword.setLastHost(newHostName);
                            loadHostNames();
                            loadLoginNames();
                            hasChanged = true;
                        }
                    }
                });
                inputDialog.setNegativeButton(getString(R.string.Cancel), null);
                inputDialog.show();
                return true;
            case R.id.btnRemHost:
                if (sprHostname.getSelectedItem() != null) {
                    currentHost = hashPassword.getHosts().getHostByName(sprHostname.getSelectedItem().toString());
                    if (currentHost != null) {
                        hashPassword.getHosts().getHost().remove(currentHost);
                        hasChanged = true;
                    }
                    loadHostNames();
                    loadLoginNames();
                }
                return true;
            case R.id.btnEditHost:
                if (sprHostname.getSelectedItem() != null) {
                    currentHost = hashPassword.getHosts().getHostByName(sprHostname.getSelectedItem().toString());
                    if (currentHost != null) {
                        Intent editHostIntent = new Intent(getBaseContext(), HPSettingsHost.class);
                        editHostIntent.putExtra(getString(R.string.host), currentHost);
                        editHostIntent.putExtra(getString(R.string.hp), hashPassword);
                        startActivityForResult(editHostIntent, 0);
                    }
                }
                return true;
            case R.id.btnEditLogin:
                if (sprHostname.getSelectedItem() != null) {
                    currentHost = hashPassword.getHosts().getHostByName(sprHostname.getSelectedItem().toString());
                    if (currentHost != null) {
                        if (sprLoginname.getSelectedItem() != null) {
                            currentLogin = currentHost.getLoginNames().getLoginNameByName(sprLoginname.getSelectedItem().toString());
                            if (currentLogin != null) {
                                Intent editLoginIntent = new Intent(getBaseContext(), HPSettingsHost.class);
                                editLoginIntent.putExtra(getString(R.string.host), currentHost);
                                editLoginIntent.putExtra(getString(R.string.login), currentLogin);
                                editLoginIntent.putExtra(getString(R.string.hp), hashPassword);
                                startActivityForResult(editLoginIntent, 0);
                            }
                        }
                    }
                }
                return true;
            case R.id.btnAddLogin:
                if (sprHostname.getSelectedItem() != null) {
                    currentHost = hashPassword.getHosts().getHostByName(sprHostname.getSelectedItem().toString());
                    etInput = new EditText(this);
                    inputDialog = new AlertDialog.Builder(this);
                    inputDialog.setTitle(R.string.titleAddLogin);
                    inputDialog.setMessage(R.string.msgAddLogin);
                    inputDialog.setView(etInput);
                    inputDialog.setPositiveButton(getString(R.string.OK), new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String newLoginName = etInput.getText().toString().trim();
                            if (currentHost != null && newLoginName.length() > 0) {
                                if (currentHost.getLoginNames().getLoginNameByName(newLoginName) == null) {
                                    LoginName newLogin = new LoginName();
                                    newLogin.setName(newLoginName);
                                    currentHost.getLoginNames().getLoginName().add(newLogin);
                                    currentHost.setLastLogin(newLoginName);
                                    hasChanged = true;
                                }
                            }
                            loadLoginNames();
                        }
                    });
                    inputDialog.setNegativeButton(getString(R.string.Cancel), null);
                    inputDialog.show();
                }
                return true;
            case R.id.btnRemLogin:
                if (sprHostname.getSelectedItem() != null) {
                    currentHost = hashPassword.getHosts().getHostByName(sprHostname.getSelectedItem().toString());
                    if (currentHost != null && sprLoginname.getSelectedItem() != null) {
                        currentLogin = currentHost.getLoginNames().getLoginNameByName(sprLoginname.getSelectedItem().toString());
                        if (currentLogin != null) {
                            currentHost.getLoginNames().getLoginName().remove(currentLogin);
                            hasChanged = true;
                        }
                    }
                    loadLoginNames();
                }
                return true;
            default:
                Log.d(this.toString(), "Clicked button has no case.");
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(getString(R.string.hp), hashPassword);
        resultIntent.putExtra(getString(R.string.settingsChanged), hasChanged);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode) {
            case Activity.RESULT_OK:
                Log.d(this.toString(), "Result okay, reloading!");
                hashPassword = (HashPassword) data.getSerializableExtra("hashPassword");
                hasChanged = data.getBooleanExtra(getString(R.string.settingsChanged), false);
                break;
            case Activity.RESULT_CANCELED:
                Log.d(this.toString(), "Result canceled!");
                break;
            default:
                break;
        }
    }
}

package com.william.lifetraxer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends BasicActivity {

    private final String TAG = "LoginActivity";

    public final int ON_EXIT_DIALOG = 1;

    public final int ABOUT_US_DIALOG = 2;

    public final int LOGIN_MODE_DIALOG = 3;

    private final int REGISTER_SUCCESS = 3;

    private final String REMEMBER_PASSWORD = "remeber_password";

    private final String AUTO_LOGIN = "auto_login";

    private final String MUTE_LOGIN = "mute_login";

    private String loginMode = "full";

    private Button loginButton = null;

    private Button loginModeButton = null;

    private TextView loginModeText = null;

    private EditText usernameEdit = null;

    private EditText passwordEdit = null;

    private TextView usernameText = null;

    private ImageView headPortraitImage = null;

    private CheckBox rememberPasswordCheck = null;

    private CheckBox autoLoginCheck = null;

    private CheckBox muteLoginCheck = null;

    public void initViews() {
        loginButton = (Button) this.findViewById(R.id.login_button_login);
        loginButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, DuringLogin.class);
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("password", password);
                bundle.putString("loginMode", loginMode);
                intent.putExtra("fromLogin", bundle);
                if (!"".equals(username) && username != null && !"".equals(password) && password != null) {
                    Login.this.startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "�˻������벻��Ϊ�գ�", 3000).show();
                }
            }
        });
        loginModeButton = (Button) Login.this.findViewById(R.id.login_button_log_in_mode);
        loginModeButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                Login.this.getDialog(LOGIN_MODE_DIALOG).show();
            }
        });
        loginModeText = (TextView) this.findViewById(R.id.login_textview_current_log_in_mode);
        usernameEdit = (EditText) this.findViewById(R.id.login_edittext_username);
        passwordEdit = (EditText) this.findViewById(R.id.login_edittext_password);
        headPortraitImage = (ImageView) this.findViewById(R.id.login_imageview_header_portrait);
        usernameText = (TextView) this.findViewById(R.id.login_textview_username);
        rememberPasswordCheck = (CheckBox) this.findViewById(R.id.login_checkbox_remember_password);
        autoLoginCheck = (CheckBox) this.findViewById(R.id.login_checkbox_auto_login);
        muteLoginCheck = (CheckBox) this.findViewById(R.id.login_checkbox_mute_login);
    }

    public Dialog getDialog(int dialogType) {
        Dialog dialog = null;
        switch(dialogType) {
            case ON_EXIT_DIALOG:
                dialog = getOnExitDialog();
                break;
            case ABOUT_US_DIALOG:
                dialog = getAboutUsDialog();
                break;
            case LOGIN_MODE_DIALOG:
                dialog = getLoginModeDialog();
                break;
            default:
                break;
        }
        return dialog;
    }

    private Dialog getOnExitDialog() {
        CharSequence titleMsg = this.getResources().getString(R.string.login_textview_alertdialog_on_exit_title_text);
        CharSequence mainMsg = this.getResources().getString(R.string.login_textview_alertdialog_on_exit_content_text);
        CharSequence posMsg = this.getResources().getString(R.string.login_button_alertdialog_on_exit_pos_text);
        CharSequence nagMsg = this.getResources().getString(R.string.login_button_alertdialog_on_exit_nag_text);
        DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Login.this.finish();
            }
        };
        DialogInterface.OnClickListener nagListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        return createSimpleDialog(R.drawable.dialog_icon, titleMsg, mainMsg, posMsg, posListener, nagMsg, nagListener);
    }

    private Dialog getLoginModeDialog() {
        return new AlertDialog.Builder(this).setTitle(R.string.login_dialog_login_mode_select_title_text).setItems(R.array.login_mode_selector_array, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        loginModeText.setText(Login.this.getResources().getString(R.string.login_textview_log_in_complete_mode_text));
                        loginMode = "full";
                        break;
                    case 1:
                        loginModeText.setText(Login.this.getResources().getString(R.string.login_textview_log_in_gps_mode_text));
                        loginMode = "gps";
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        }).create();
    }

    private Dialog getAboutUsDialog() {
        CharSequence title = this.getResources().getString(R.string.login_textview_alertdialog_about_us_title_text);
        CharSequence mainMsg = this.getResources().getString(R.string.login_textview_alertdialog_about_us_content_text);
        CharSequence buttonMsg = this.getResources().getString(R.string.login_button_alertdialog_about_us_ok_text);
        DialogInterface.OnClickListener buttonListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        };
        return createSimpleDialog(R.drawable.login_menu_about_icon, title, mainMsg, buttonMsg, buttonListener, null, null);
    }

    public void setHeadPortrait() {
        Drawable d = this.getResources().getDrawable(R.drawable.default_head_portrait);
        headPortraitImage.setBackgroundDrawable(d);
        usernameText.setText("default");
    }

    public void getPreferences() {
        SharedPreferences loginSettings = this.getSharedPreferences("Login_Settings", 0);
        boolean doRememberPassword = loginSettings.getBoolean(REMEMBER_PASSWORD, false);
        boolean doAutoLogin = loginSettings.getBoolean(AUTO_LOGIN, false);
        boolean doMuteLogin = loginSettings.getBoolean(MUTE_LOGIN, false);
        rememberPasswordCheck.setChecked(doRememberPassword);
        autoLoginCheck.setChecked(doAutoLogin);
        muteLoginCheck.setChecked(doMuteLogin);
    }

    public void setPreferences() {
        SharedPreferences loginSettings = this.getSharedPreferences("Login_Settings", 0);
        loginSettings.edit().putBoolean(REMEMBER_PASSWORD, rememberPasswordCheck.isChecked()).putBoolean(AUTO_LOGIN, autoLoginCheck.isChecked()).putBoolean(MUTE_LOGIN, muteLoginCheck.isChecked()).commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        this.initViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        setPreferences();
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown(");
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            getDialog(ON_EXIT_DIALOG).show();
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu()");
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected()");
        switch(item.getItemId()) {
            case R.id.login_menu_delete_user:
                startActivity(new Intent(this, DeleteUser.class));
                break;
            case R.id.login_menu_register:
                startActivityForResult(new Intent(this, Register.class), 1);
                break;
            case R.id.login_menu_net_config:
                startActivity(new Intent("android.settings.WIFI_SETTINGS"));
                break;
            case R.id.login_menu_about:
                getDialog(ABOUT_US_DIALOG).show();
                break;
            case R.id.login_menu_exit:
                getDialog(ON_EXIT_DIALOG).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == REGISTER_SUCCESS && requestCode == 1) {
            Bundle b = data.getBundleExtra("fromRegister");
            this.usernameEdit.setText(b.getString("username"));
            this.passwordEdit.setText(b.getString("password"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

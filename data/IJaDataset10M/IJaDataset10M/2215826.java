package com.mekya;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mekya.interfaces.IAppManager;
import com.mekya.services.IMService;

public class Login extends Activity {

    protected static final int NOT_CONNECTED_TO_SERVICE = 0;

    protected static final int FILL_BOTH_USERNAME_AND_PASSWORD = 1;

    public static final String AUTHENTICATION_FAILED = "0";

    public static final String FRIEND_LIST = "FRIEND_LIST";

    protected static final int MAKE_SURE_USERNAME_AND_PASSWORD_CORRECT = 2;

    protected static final int NOT_CONNECTED_TO_NETWORK = 3;

    private EditText usernameText;

    private EditText passwordText;

    private Button cancelButton;

    private IAppManager imService;

    public static final int SIGN_UP_ID = Menu.FIRST;

    public static final int EXIT_APP_ID = Menu.FIRST + 1;

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            imService = ((IMService.IMBinder) service).getService();
            if (imService.isUserAuthenticated() == true) {
                Intent i = new Intent(Login.this, FriendList.class);
                startActivity(i);
                Login.this.finish();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            imService = null;
            Toast.makeText(Login.this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(Login.this, IMService.class));
        setContentView(R.layout.login_screen);
        setTitle("Login");
        Button loginButton = (Button) findViewById(R.id.login);
        cancelButton = (Button) findViewById(R.id.cancel_login);
        usernameText = (EditText) findViewById(R.id.userName);
        passwordText = (EditText) findViewById(R.id.password);
        loginButton.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                if (imService == null) {
                    showDialog(NOT_CONNECTED_TO_SERVICE);
                    return;
                } else if (imService.isNetworkConnected() == false) {
                    showDialog(NOT_CONNECTED_TO_NETWORK);
                } else if (usernameText.length() > 0 && passwordText.length() > 0) {
                    Thread loginThread = new Thread() {

                        private Handler handler = new Handler();

                        @Override
                        public void run() {
                            String result = imService.authenticateUser(usernameText.getText().toString(), passwordText.getText().toString());
                            if (result == null || result.equals(AUTHENTICATION_FAILED)) {
                                handler.post(new Runnable() {

                                    public void run() {
                                        showDialog(MAKE_SURE_USERNAME_AND_PASSWORD_CORRECT);
                                    }
                                });
                            } else {
                                handler.post(new Runnable() {

                                    public void run() {
                                        Intent i = new Intent(Login.this, FriendList.class);
                                        startActivity(i);
                                        Login.this.finish();
                                    }
                                });
                            }
                        }
                    };
                    loginThread.start();
                } else {
                    showDialog(FILL_BOTH_USERNAME_AND_PASSWORD);
                }
            }
        });
        cancelButton.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                imService.exit();
                finish();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        int message = -1;
        switch(id) {
            case NOT_CONNECTED_TO_SERVICE:
                message = R.string.not_connected_to_service;
                break;
            case FILL_BOTH_USERNAME_AND_PASSWORD:
                message = R.string.fill_both_username_and_password;
                break;
            case MAKE_SURE_USERNAME_AND_PASSWORD_CORRECT:
                message = R.string.make_sure_username_and_password_correct;
                break;
            case NOT_CONNECTED_TO_NETWORK:
                message = R.string.not_connected_to_network;
                break;
            default:
                break;
        }
        if (message == -1) {
            return null;
        } else {
            return new AlertDialog.Builder(Login.this).setMessage(message).setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                }
            }).create();
        }
    }

    @Override
    protected void onPause() {
        unbindService(mConnection);
        super.onPause();
    }

    @Override
    protected void onResume() {
        bindService(new Intent(Login.this, IMService.class), mConnection, Context.BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, SIGN_UP_ID, 0, R.string.sign_up);
        menu.add(0, EXIT_APP_ID, 0, R.string.exit_application);
        return result;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case SIGN_UP_ID:
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
                return true;
            case EXIT_APP_ID:
                cancelButton.performClick();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}

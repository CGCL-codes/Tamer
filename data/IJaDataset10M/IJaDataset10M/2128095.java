package team2.billpayreminder.gui;

import team2.billpayreminder.controller.ApplicationController;
import team2.billpayreminder.utilities.CommandConstants;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginScreen extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomescreen);
        Button loginButton = (Button) findViewById(R.id.buttonlogin);
        final EditText userNameEditText = (EditText) findViewById(R.id.editTextUsername);
        final EditText passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        Button createAccountButton = (Button) findViewById(R.id.buttonCreateAccount);
        Button buttonForgotPassword = (Button) findViewById(R.id.buttonForgotPassword);
        loginButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String[] loginCredentials = new String[2];
                loginCredentials[0] = userNameEditText.getText().toString();
                loginCredentials[1] = passwordEditText.getText().toString();
                if ((ApplicationController.getApplicationController().passCommand(CommandConstants.loginUser, loginCredentials))) {
                    try {
                        startService(new Intent(LoginScreen.this, ServiceRunner.class));
                        Class menuScreen = Class.forName("team2.billpayreminder.gui." + "MenuScreen");
                        Intent myIntent = new Intent(LoginScreen.this, menuScreen);
                        startActivity(myIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String error = ApplicationController.getApplicationController().passNonFunctionalCommand(CommandConstants.getAccountErrorMessage);
                    try {
                        Dialog dialogOK = new Dialog(LoginScreen.this);
                        dialogOK.setContentView(R.layout.failurepopupbox);
                        dialogOK.setTitle("Bill Pay Reminder");
                        TextView dialogText = (TextView) dialogOK.findViewById(R.id.textViewDialogMessage);
                        dialogText.setText(error);
                        Button button = (Button) dialogOK.findViewById(R.id.buttonDialogOK);
                        button.setOnClickListener(new OnClickListener() {

                            public void onClick(View v) {
                                try {
                                    Class menuScreen = Class.forName("team2.billpayreminder.gui." + "LoginScreen");
                                    Intent myIntent = new Intent(LoginScreen.this, menuScreen);
                                    startActivity(myIntent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        dialogOK.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        createAccountButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    Class createAccountScreen = Class.forName("team2.billpayreminder.gui." + "CreateNewAccount");
                    Intent myIntent = new Intent(LoginScreen.this, createAccountScreen);
                    startActivity(myIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        buttonForgotPassword.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    Class forgotPassword = Class.forName("team2.billpayreminder.gui." + "ForgotPassword");
                    Intent myIntent = new Intent(LoginScreen.this, forgotPassword);
                    startActivity(myIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

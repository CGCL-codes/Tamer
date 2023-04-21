package org.imogene.android.app;

import org.imogene.android.Constants.Intents;
import org.imogene.android.W;
import org.imogene.android.app.setup.AccountCreationIntroduction;
import org.imogene.android.app.setup.AccountSetupBasics;
import org.imogene.android.preference.PreferenceHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener, TextWatcher {

    @SuppressWarnings("unused")
    private static final String TAG = MainActivity.class.getName();

    private static final int ERROR_DIALOG_ID = 1;

    private String mShortpw;

    private EditText mShortpwView;

    private Button mStartButton;

    private Button mChangeUserView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShortpw = PreferenceHelper.getShortPassword(this);
        if (mShortpw != null) {
            setContentView(W.layout.account_check_shortpw);
            mShortpwView = (EditText) findViewById(W.id.check_shortpw);
            mStartButton = (Button) findViewById(W.id.start);
            mChangeUserView = (Button) findViewById(W.id.change_user);
            mShortpwView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mStartButton.setOnClickListener(this);
            mChangeUserView.setOnClickListener(this);
            mShortpwView.addTextChangedListener(this);
        } else {
            AccountCreationIntroduction.accountCreationIntroduction(this);
            finish();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == ERROR_DIALOG_ID) {
            return new AlertDialog.Builder(this).setTitle(android.R.string.dialog_alert_title).setIcon(android.R.drawable.ic_dialog_alert).setMessage(W.string.account_setup_shortpw_error).setCancelable(false).setPositiveButton(android.R.string.ok, null).create();
        } else {
            return super.onCreateDialog(id);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        validateFields();
    }

    public void afterTextChanged(Editable s) {
        validateFields();
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    private void validateFields() {
        boolean valid = mShortpwView.getText() != null && mShortpwView.getText().length() >= 4;
        mStartButton.setEnabled(valid);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case W.id.start:
                if (mShortpw != null && mShortpw.equals(mShortpwView.getText().toString())) {
                    Intent intent = new Intent(Intents.ACTION_LIST_ENTITIES);
                    startActivity(intent);
                    finish();
                } else {
                    showDialog(ERROR_DIALOG_ID);
                }
                break;
            case W.id.change_user:
                AccountSetupBasics.actionNewAccount(this);
                finish();
        }
    }
}

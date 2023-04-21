package org.imogene.android.app.setup;

import org.imogene.android.W;
import org.imogene.android.app.AuthenticationSmsActivity;
import org.imogene.android.app.OffsetActivity;
import org.imogene.android.preference.PreferenceHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AccountSetupSmsBasics extends Activity implements OnClickListener, TextWatcher {

    private static final int ACTIVITY_OFFSET_ID = 1;

    private static final int ACTIVITY_AUTHENTICATION_ID = 2;

    private static final String EXTRA_CHANGE_ACCOUNT = "change-account";

    private EditText mLoginView;

    private EditText mServerPhoneView;

    private EditText mServerCodeView;

    private Button mNextButton;

    public static final void actionNewAccount(Activity fromActivity) {
        Intent i = new Intent(fromActivity, AccountSetupSmsBasics.class);
        fromActivity.startActivity(i);
    }

    public static final void actionModifyAccount(Activity fromActivity) {
        Intent i = new Intent(fromActivity, AccountSetupSmsBasics.class);
        i.putExtra(EXTRA_CHANGE_ACCOUNT, true);
        fromActivity.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(W.layout.account_setup_sms_basics);
        mLoginView = (EditText) findViewById(W.id.account_login);
        mServerPhoneView = (EditText) findViewById(W.id.account_server_phone);
        mServerCodeView = (EditText) findViewById(W.id.account_server_code);
        mNextButton = (Button) findViewById(W.id.next);
        mServerCodeView.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mNextButton.setOnClickListener(this);
        mLoginView.addTextChangedListener(this);
        mServerPhoneView.addTextChangedListener(this);
        mServerCodeView.addTextChangedListener(this);
        if (getIntent().hasExtra(EXTRA_CHANGE_ACCOUNT)) {
            mLoginView.setText(PreferenceHelper.getSmsLogin(this));
            mServerPhoneView.setText(PreferenceHelper.getSmsServerPhone(this));
            validateFields();
            if (mNextButton.isEnabled()) {
                onNext();
            }
        } else {
            mLoginView.setText(null);
            mServerCodeView.setText(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_OFFSET_ID) {
            AccountSetupShortPassword.actionNewShortPassword(this);
            finish();
        } else if (resultCode == RESULT_OK && requestCode == ACTIVITY_AUTHENTICATION_ID) {
            startActivityForResult(new Intent(this, OffsetActivity.class), ACTIVITY_OFFSET_ID);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
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
        boolean valid = required(mLoginView) && required(mServerPhoneView) && required(mServerCodeView);
        mNextButton.setEnabled(valid);
    }

    private void onNext() {
        String login = mLoginView.getText().toString();
        String serverPhone = mServerPhoneView.getText().toString();
        String serverCode = mServerCodeView.getText().toString();
        startActivityForResult(AuthenticationSmsActivity.getAuthenticationIntent(this, login, serverPhone, serverCode), ACTIVITY_AUTHENTICATION_ID);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case W.id.next:
                onNext();
                break;
        }
    }

    private static final boolean required(EditText editText) {
        return editText.getText() != null && editText.getText().length() != 0;
    }
}

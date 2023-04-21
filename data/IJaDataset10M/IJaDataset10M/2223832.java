package com.busfm.activity;

import com.busfm.R;
import com.busfm.listener.NetResponseListener;
import com.busfm.model.ResultEntity;
import com.busfm.net.NetWorkManager;
import com.busfm.util.Constants;
import com.busfm.util.LogUtil;
import com.busfm.util.PatternUtil;
import com.busfm.util.Utilities;
import com.mobclick.android.MobclickAgent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements OnClickListener {

    EditText etEmail;

    EditText etNickname;

    EditText etPwd;

    EditText etRepeatPwd;

    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        setupView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    private void initView() {
        etEmail = (EditText) findViewById(R.id.activity_register_et_email);
        etNickname = (EditText) findViewById(R.id.activity_register_et_nickname);
        etPwd = (EditText) findViewById(R.id.activity_register_et_pwd);
        etRepeatPwd = (EditText) findViewById(R.id.activity_register_et_repeatpwd);
        btnRegister = (Button) findViewById(R.id.activity_register_btn_register);
    }

    private void setupView() {
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.activity_register_btn_register:
                if (isIlleageRegisterData()) {
                    register();
                }
                break;
            default:
                break;
        }
    }

    /**
     * check whether the input data is illeage
     * @return
     */
    private boolean isIlleageRegisterData() {
        boolean result = true;
        if (Utilities.isEmpty(etEmail.getText().toString())) {
            etEmail.setError(getString(R.string.activity_register_email_empty_error));
            result = false;
        } else if (!PatternUtil.EMAIL_ADDRESS_PATTERN.matcher(etEmail.getText().toString()).find()) {
            etEmail.setError(getString(R.string.activity_register_email_format_error));
            result = false;
        }
        if (Utilities.isEmpty(etNickname.getText().toString())) {
            etNickname.setError(getString(R.string.activity_register_nickname_empty_error));
        }
        if (Utilities.isEmpty(etPwd.getText().toString())) {
            etPwd.setError(getString(R.string.activity_register_password_empty_error));
            result = false;
        } else if (!PatternUtil.PWD_PATTERN.matcher(etPwd.getText().toString()).find()) {
            etPwd.setError(getString(R.string.activity_register_password_format_error));
            result = false;
        } else if (Utilities.isEmpty(etRepeatPwd.getText().toString())) {
            etPwd.setError(getString(R.string.activity_register_password_empty_error));
            result = false;
        } else if (!PatternUtil.PWD_PATTERN.matcher(etRepeatPwd.getText().toString()).find()) {
            etPwd.setError(getString(R.string.activity_register_password_format_error));
            result = false;
        } else if (!etPwd.getText().toString().equals(etRepeatPwd.getText().toString())) {
            etPwd.setError(getString(R.string.activity_register_password_not_same_error));
            etRepeatPwd.setError(getString(R.string.activity_register_password_not_same_error));
        }
        return result;
    }

    private void register() {
        showDialog(DIALOG_WAIT);
        NetWorkManager.register(this, etEmail.getText().toString(), etPwd.getText().toString(), etNickname.getText().toString());
    }

    private void removeWaitingDialog() {
        try {
            removeDialog(DIALOG_WAIT);
        } catch (Exception e) {
        }
    }

    @Override
    public void clientDidRegister(NetResponseListener mClientListener, ResultEntity resultEntity) {
        removeWaitingDialog();
        Toast.makeText(this, resultEntity.getErrorMsg(), Toast.LENGTH_SHORT).show();
        try {
            if (resultEntity.getResult() == 1) {
                MobclickAgent.onEvent(this, Constants.REGISTER_TIMES);
                finish();
            }
        } catch (Exception e) {
            LogUtil.i(Constants.TAG, e.getMessage());
        }
        super.clientDidRegister(mClientListener, resultEntity);
    }

    @Override
    public void clientDidFailWithError(NetResponseListener mClientListener, int mOp, int scUnknown, String localizedMessage) {
        removeWaitingDialog();
        Toast.makeText(this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
        super.clientDidFailWithError(mClientListener, mOp, scUnknown, localizedMessage);
    }
}

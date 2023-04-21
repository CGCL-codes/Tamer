package cn.com.androidforfun.finance.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.androidforfun.commons.util.GUIs;
import cn.com.androidforfun.finance.R;
import cn.com.androidforfun.finance.context.ContextsActivity;

public class PassConfigActivity extends ContextsActivity implements OnClickListener {

    private Button okBtn;

    private Button cancelBtn;

    private TextView presentPassView;

    private EditText presentPass;

    private EditText pass;

    private EditText confirmPass;

    private boolean firstCofig = true;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.alert_dialog_passconfig);
        setTitle(R.string.password_config_title);
        initialEditor();
    }

    private void initialEditor() {
        presentPassView = (TextView) findViewById(R.id.present_password_view);
        presentPass = (EditText) findViewById(R.id.present_password);
        pass = (EditText) findViewById(R.id.password);
        confirmPass = (EditText) findViewById(R.id.confirm_password);
        String pass = getContexts().getPrefPassword();
        if (pass != null && !"".equals(pass)) {
            firstCofig = false;
            presentPassView.setVisibility(TextView.VISIBLE);
            presentPass.setVisibility(EditText.VISIBLE);
        }
        okBtn = (Button) findViewById(R.id.acceditor_ok);
        cancelBtn = (Button) findViewById(R.id.acceditor_cancel);
        okBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.acceditor_ok:
                setPassword();
                break;
            case R.id.acceditor_cancel:
                finish();
        }
    }

    private void setPassword() {
        String present = presentPass.getText().toString().trim();
        if (!firstCofig) {
            if ("".equals(present)) {
                presentPass.requestFocus();
                GUIs.alert(this, i18n.string(R.string.cmsg_field_empty, i18n.string(R.string.alert_dialog_present_password)));
                return;
            }
        }
        String newPass = pass.getText().toString().trim();
        String confirm = confirmPass.getText().toString().trim();
        if (!firstCofig) {
            if (!present.equals(getContexts().getPrefPassword())) {
                presentPass.requestFocus();
                GUIs.alert(this, i18n.string(R.string.msg_field_notsame, i18n.string(R.string.alert_dialog_present_password), "�����õ�����"));
                return;
            }
        }
        if (!confirm.equals(newPass)) {
            pass.requestFocus();
            GUIs.alert(this, i18n.string(R.string.msg_field_notsame, i18n.string(R.string.alert_dialog_password), i18n.string(R.string.alert_dialog_confirm_password)));
            return;
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.PREFS_PASSWORD, newPass);
        editor.commit();
        getContexts().setPreferenceDirty();
        finish();
    }
}

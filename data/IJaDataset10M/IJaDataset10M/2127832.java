package com.guanri.android.insurance.activity.dialog;

import com.guanri.android.insurance.R;
import com.guanri.android.insurance.activity.SystemConfigActivity;
import com.guanri.android.insurance.service.SystemConfigService;
import com.guanri.android.lib.utils.StringUtils;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * 服务器设置对话框
 * @author wuxiang
 *
 */
public class SystemSettingDialog extends Dialog implements android.view.View.OnClickListener {

    private Button mOkBtn = null;

    private Button mCancelBtn = null;

    private EditText mServerIpEdt1 = null;

    private EditText mServerIpEdt2 = null;

    private EditText mServerIpEdt3 = null;

    private EditText mServerIpEdt4 = null;

    private EditText mServerPortEdt = null;

    private EditText mTimeOutEdt = null;

    private SystemConfigService systemConfigDAO;

    private Context context;

    private String serverip = "";

    public SystemSettingDialog(Context context, SystemConfigService systemConfigDAO) {
        super(context);
        this.context = context;
        this.systemConfigDAO = systemConfigDAO;
    }

    public void displayDlg() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sys_server_setting_dialog);
        mOkBtn = (Button) findViewById(R.id.setting_in);
        mCancelBtn = (Button) findViewById(R.id.setting_out);
        mServerIpEdt1 = (EditText) findViewById(R.id.edt_serverip1);
        mServerIpEdt2 = (EditText) findViewById(R.id.edt_serverip2);
        mServerIpEdt3 = (EditText) findViewById(R.id.edt_serverip3);
        mServerIpEdt4 = (EditText) findViewById(R.id.edt_serverip4);
        mServerPortEdt = (EditText) findViewById(R.id.edt_serverport);
        mTimeOutEdt = (EditText) findViewById(R.id.edt_timeout);
        serverip = systemConfigDAO.getServerInfoIp();
        int i = serverip.indexOf(".");
        String tmpstr = serverip.substring(0, i);
        mServerIpEdt1.setText(tmpstr);
        serverip = serverip.substring(i + 1);
        i = serverip.indexOf(".");
        tmpstr = serverip.substring(0, i);
        mServerIpEdt2.setText(tmpstr);
        serverip = serverip.substring(i + 1);
        i = serverip.indexOf(".");
        tmpstr = serverip.substring(0, i);
        mServerIpEdt3.setText(tmpstr);
        serverip = serverip.substring(i + 1);
        mServerIpEdt4.setText(serverip);
        mServerPortEdt.setText(systemConfigDAO.getServerInfoPort());
        mTimeOutEdt.setText(systemConfigDAO.getServerTimtOut());
        mOkBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setting_in) {
            String strServerIp = mServerIpEdt1.getText().toString() + "." + mServerIpEdt2.getText().toString() + "." + mServerIpEdt3.getText().toString() + "." + mServerIpEdt4.getText().toString();
            String strServerPort = mServerPortEdt.getText().toString();
            String strSimcode = mTimeOutEdt.getText().toString();
            if (systemConfigDAO.serverInfoValuesCheck(strServerIp, strServerPort, strSimcode)) {
                systemConfigDAO.saveServerInfoValues(strServerIp, strServerPort, strSimcode);
                Msgdialog msgdialog = new Msgdialog(context);
                msgdialog.setTitle(StringUtils.getStringFromValue(R.string.apsai_common_advise));
                msgdialog.setMsgstr(StringUtils.getStringFromValue(R.string.apsai_insu_manager_data_success));
                msgdialog.setImageid(R.drawable.dialog_success);
                msgdialog.displayDlg();
                dismiss();
            }
        } else if (v.getId() == R.id.setting_out) {
            dismiss();
        }
    }
}

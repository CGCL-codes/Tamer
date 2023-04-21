package it.coctal.datereminder;

import it.coctal.datereminder.helper.Utilities;
import it.coctal.datereminder.model.Contact;
import it.coctal.datereminder.util.SmsUtil;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SmsView extends Activity implements OnClickListener {

    private EditText txtPhoneNo;

    private EditText txtSms;

    private Button btnSendSms;

    private Button btnSendAndSaveSms;

    private boolean isValidForm = false;

    private SQLiteDatabase _db;

    private Contact _c;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addevent);
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtSms = (EditText) findViewById(R.id.txtMessage);
        btnSendSms = (Button) findViewById(R.id.btnSendSms);
        btnSendAndSaveSms = (Button) findViewById(R.id.btnSendAndSaveSms);
        Bundle bundle = this.getIntent().getExtras();
        _c = new Contact();
        if (bundle != null) _c = (Contact) bundle.getSerializable("contact");
        txtPhoneNo.setText(_c.getPhone());
        txtSms.setText(_c.getTxtmessage());
        btnSendSms.setOnClickListener(this);
        btnSendAndSaveSms.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ValidateForm();
        if (v == btnSendSms) {
            if (isValidForm) {
                SmsUtil.sendSMS(txtPhoneNo.getText().toString(), txtSms.getText().toString(), this);
            } else {
                Toast toast = Toast.makeText(this, R.string.msgNotValidData, Toast.LENGTH_LONG);
                toast.show();
            }
        } else if (v == btnSendAndSaveSms) {
            ContentValues cv = new ContentValues();
            cv.put("txtmessage", txtSms.getText().toString());
            _db.update("contacts", cv, "txtmessage=?", new String[] { txtSms.getText().toString() });
            if (isValidForm) {
                SmsUtil.sendSMS(txtPhoneNo.getText().toString(), txtSms.getText().toString(), this);
            } else {
                Toast toast = Toast.makeText(this, R.string.msgNotValidData, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private void ValidateForm() {
        String txtPhoneNo_value = txtPhoneNo.getText().toString();
        String txtSms_value = txtSms.getText().toString();
        if (((txtPhoneNo_value.length() == 0) || (txtSms_value.length() == 0) || (!Utilities.isAValidPhoneNumber(txtPhoneNo.getText().toString())))) isValidForm = false; else isValidForm = true;
    }
}

package pl.szpadel.android.gadu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ContactInfoActivity extends Activity {

    public static final String EXTRA_UIN = "uin";

    private static final int DIALOG_INVALID_UIN = 0;

    private static final int DIALOG_INVALID_DISPLAY_NAME = 1;

    private long mUin = 0;

    public ContactInfoActivity() {
        super();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info_window);
        Button saveButton = (Button) findViewById(R.id.ContactInfoSave);
        Button cancelButton = (Button) findViewById(R.id.ContactInfoCancel);
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_UIN)) {
            long uin = intent.getLongExtra(EXTRA_UIN, 0);
            loadContact(uin);
        } else {
            preareNewContact();
        }
    }

    private void loadContact(long uin) {
        ContactInfo info = App.getInstance().getContactInfoManager().getByUin(uin);
        if (info == null) {
            finish();
        }
        mUin = uin;
        EditText uinEdit = (EditText) findViewById(R.id.ContactInfoUin);
        uinEdit.setText(Long.toString(uin));
        uinEdit.setEnabled(false);
        EditText displayNameEdit = (EditText) findViewById(R.id.ContactInfoDisplay);
        displayNameEdit.setText(info.displayName);
        EditText firstNameEdit = (EditText) findViewById(R.id.ContactInfoFirstName);
        firstNameEdit.setText(info.firstName);
        EditText lastNameEdit = (EditText) findViewById(R.id.ContactInfoLastName);
        lastNameEdit.setText(info.lastName);
        EditText mobileEdit = (EditText) findViewById(R.id.ContactInfoMobileNumber);
        mobileEdit.setText(info.mobilePhone);
        EditText homeEdit = (EditText) findViewById(R.id.ContactInfoHomeNumber);
        homeEdit.setText(info.homePhone);
        EditText emailEdit = (EditText) findViewById(R.id.ContactInfoEmail);
        emailEdit.setText(info.email);
        EditText wwwEdit = (EditText) findViewById(R.id.ContactInfoWww);
        wwwEdit.setText(info.wwwAdrress);
        setTitle(R.string.title_edit_contact);
    }

    private void preareNewContact() {
        mUin = 0;
        EditText uinEdit = (EditText) findViewById(R.id.ContactInfoUin);
        uinEdit.setText("");
        uinEdit.setEnabled(true);
        EditText displayNameEdit = (EditText) findViewById(R.id.ContactInfoDisplay);
        displayNameEdit.setText("");
        EditText firstNameEdit = (EditText) findViewById(R.id.ContactInfoFirstName);
        firstNameEdit.setText("");
        EditText lastNameEdit = (EditText) findViewById(R.id.ContactInfoLastName);
        lastNameEdit.setText("");
        EditText mobileEdit = (EditText) findViewById(R.id.ContactInfoMobileNumber);
        mobileEdit.setText("");
        EditText homeEdit = (EditText) findViewById(R.id.ContactInfoHomeNumber);
        homeEdit.setText("");
        EditText emailEdit = (EditText) findViewById(R.id.ContactInfoEmail);
        emailEdit.setText("");
        EditText wwwEdit = (EditText) findViewById(R.id.ContactInfoWww);
        wwwEdit.setText("");
        setTitle(R.string.title_add_contact);
    }

    private void onSaveClicked() {
        EditText uinEdit = (EditText) findViewById(R.id.ContactInfoUin);
        long uin = mUin;
        ContactInfo info = null;
        if (uin == 0) {
            try {
                uin = Integer.parseInt(uinEdit.getText().toString());
            } catch (NumberFormatException e) {
                uin = 0;
            }
        } else {
            info = App.getInstance().getContactInfoManager().getByUin(uin);
        }
        if (uin == 0) {
            showDialog(DIALOG_INVALID_UIN);
            return;
        }
        EditText displayNameEdit = (EditText) findViewById(R.id.ContactInfoDisplay);
        String displayName = displayNameEdit.getText().toString();
        if (displayName.length() == 0) {
            showDialog(DIALOG_INVALID_DISPLAY_NAME);
            return;
        }
        if (info == null) {
            info = new ContactInfo(displayName, uin);
        }
        EditText firstNameEdit = (EditText) findViewById(R.id.ContactInfoFirstName);
        info.firstName = firstNameEdit.getText().toString();
        EditText lastNameEdit = (EditText) findViewById(R.id.ContactInfoLastName);
        info.lastName = lastNameEdit.getText().toString();
        EditText mobileEdit = (EditText) findViewById(R.id.ContactInfoMobileNumber);
        info.mobilePhone = mobileEdit.getText().toString();
        EditText homeEdit = (EditText) findViewById(R.id.ContactInfoHomeNumber);
        info.homePhone = homeEdit.getText().toString();
        EditText emailEdit = (EditText) findViewById(R.id.ContactInfoEmail);
        info.email = emailEdit.getText().toString();
        EditText wwwEdit = (EditText) findViewById(R.id.ContactInfoWww);
        info.wwwAdrress = wwwEdit.getText().toString();
        App.getInstance().getContactInfoManager().addContact(info);
        finish();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DIALOG_INVALID_UIN:
                return new AlertDialog.Builder(this).setMessage(R.string.dialog_invalid_uin).create();
            case DIALOG_INVALID_DISPLAY_NAME:
                return new AlertDialog.Builder(this).setMessage(R.string.dialog_invalid_display_name).create();
            default:
                return null;
        }
    }
}

package com.foamsnet.way2droid;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

class AboutDialogBuilder {

    public static AlertDialog create(Context context) throws NameNotFoundException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        String versionInfo = pInfo.versionName;
        String aboutTitle = String.format("About %s", context.getString(R.string.app_name));
        String versionString = String.format("Version: %s", versionInfo);
        String aboutText = "Free SMS via Way2SMS from your Android!\n\nBy FoamsNet!\nhttp://www.foamsnet.com/way2droid\nhttp://code.google.com/p/way2droid";
        final TextView message = new TextView(context);
        final SpannableString s = new SpannableString(aboutText);
        message.setPadding(5, 5, 5, 5);
        message.setText(versionString + "\n\n" + s);
        Linkify.addLinks(message, Linkify.ALL);
        return new AlertDialog.Builder(context).setTitle(aboutTitle).setCancelable(true).setIcon(R.drawable.icon).setPositiveButton("Okay!", null).setView(message).create();
    }
}

class MessageSender extends AsyncTask<String, Void, String> {

    private String username;

    private String password;

    private String to;

    private String msg;

    private ContentResolver cr;

    private ProgressDialog dialog;

    private Context context;

    private String output;

    private boolean logsent;

    private boolean bgsend;

    public MessageSender(String a, String b, String c, String d, ContentResolver e, ProgressDialog f, Context g) {
        username = a;
        password = b;
        to = c;
        msg = d;
        cr = e;
        dialog = f;
        context = g;
        SharedPreferences settings = context.getSharedPreferences("Way2Droid", Context.MODE_PRIVATE);
        logsent = settings.getBoolean("logsent", true);
        bgsend = settings.getBoolean("bgsend", false);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (!bgsend) dialog.dismiss();
        show(output);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (bgsend) {
            show("Sending message!");
        } else {
            dialog.setMessage("Sending SMS. Please wait!");
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.setTitle("");
            dialog.show();
        }
    }

    private void show(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("http://www.foamsnet.com/smsapi/send.php?username=" + username + "&password=" + password + "&to=" + to + "&msg=" + URLEncoder.encode(msg));
            URLConnection urlc = url.openConnection();
            BufferedReader sin = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String inputLine = sin.readLine();
            inputLine = inputLine == null ? "null" : inputLine;
            sin.close();
            output = inputLine;
            if (logsent) {
                ContentResolver contentResolver = cr;
                ContentValues values = new ContentValues();
                values.put("address", "+91" + inputLine.split(" ")[3]);
                values.put("body", msg);
                contentResolver.insert(Uri.parse("content://sms/sent"), values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

public class Way2SMS extends Activity implements OnClickListener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((Button) findViewById(R.id.btnPick)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnSend)).setOnClickListener(this);
    }

    private void showSettings() {
        Intent intent = new Intent(Way2SMS.this, Settings.class);
        startActivity(intent);
    }

    private void showSpeedDial() {
        Intent intent = new Intent(Way2SMS.this, SpeedDial.class);
        startActivity(intent);
    }

    private void showAbout() {
        try {
            AlertDialog aboutdialog = AboutDialogBuilder.create(this);
            aboutdialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) return true;
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings:
                showSettings();
                return true;
            case R.id.speeddial:
                showSpeedDial();
                return true;
            case R.id.about:
                showAbout();
                return true;
        }
        return true;
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        String phoneNumber = "";
        String name = "";
        switch(reqCode) {
            case (0):
                if (resultCode == Activity.RESULT_OK) {
                    Cursor cursor = managedQuery(data.getData(), null, null, null, null);
                    while (cursor.moveToNext()) {
                        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (hasPhone.equalsIgnoreCase("1")) hasPhone = "true"; else hasPhone = "false";
                        if (Boolean.parseBoolean(hasPhone)) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (phones.moveToNext()) {
                                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            phones.close();
                        }
                        EditText txtdest = (EditText) findViewById(R.id.txtdest);
                        txtdest.setText(name + ":" + phoneNumber);
                    }
                    break;
                }
            case (34):
                if (resultCode == Activity.RESULT_OK) {
                    EditText txtdest = (EditText) findViewById(R.id.txtdest);
                    txtdest.setText(data.getStringExtra("name") + ":" + data.getStringExtra("number"));
                }
                break;
        }
    }

    private void show(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public void onClick(View v) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("Way2Droid", Context.MODE_PRIVATE);
        boolean simcontactschk = settings.getBoolean("simcontactschk", false);
        if (v.equals(findViewById(R.id.btnPick))) {
            if (simcontactschk) {
                Intent intent = new Intent(this, ContactActivity.class);
                startActivityForResult(intent, 34);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        } else if (v.equals(findViewById(R.id.btnSend))) {
            if (!isOnline()) {
                show("You cannot send messages without Internet access! Please enable mobile data or wifi and try again!");
                return;
            }
            String username = settings.getString("username", "-1");
            String password = settings.getString("password", "-1");
            if (username.equalsIgnoreCase("-1") || password.equalsIgnoreCase("-1")) {
                show("Please provide username and password in settings before sending messages!");
                return;
            }
            String dest = ((EditText) findViewById(R.id.txtdest)).getText().toString().trim();
            String message = ((EditText) findViewById(R.id.txtmsg)).getText().toString().trim();
            if (dest.equalsIgnoreCase("") || message.equalsIgnoreCase("")) {
                show("Please enter valid to address and message!");
                return;
            }
            if (dest.indexOf(":") > -1) dest = dest.split(":")[1];
            dest = settings.getString("d" + dest, dest);
            try {
                ProgressDialog dialog = new ProgressDialog(this);
                MessageSender m = new MessageSender(username, password, dest, message, getContentResolver(), dialog, getApplicationContext());
                m.execute((String[]) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

package com.fflandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.fflandroid.R;
import com.model.KUserInfo;
import com.web_services.ToServer;

/**
 * This Activity is used to modify the user's data.
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public class ModifyUser extends Activity implements OnClickListener {

    private Button bSave;

    private Button bCancel;

    private EditText et_name, et_surname, et_address, et_email, et_country, et_password, et_phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_user);
        bSave = (Button) findViewById(R.id.modifyuser_bSave);
        bSave.setOnClickListener(this);
        bCancel = (Button) findViewById(R.id.modifyuser_bCancel);
        bCancel.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.modifyuser_tName);
        et_surname = (EditText) findViewById(R.id.modifyuser_tSurname);
        et_address = (EditText) findViewById(R.id.modifyuser_tAddress);
        et_country = (EditText) findViewById(R.id.modifyuser_tCountry);
        et_email = (EditText) findViewById(R.id.modifyuser_tEmail);
        et_password = (EditText) findViewById(R.id.modifyuser_tPassword);
        et_phone = (EditText) findViewById(R.id.modifyuser_tPhone);
        KUserInfo user;
        try {
            user = ToServer.myUser();
            et_name.setText(user.getName());
            et_surname.setText(user.getSurname());
            et_address.setText(user.getAddress());
            et_country.setText(user.getCountry());
            et_email.setText(user.getEmail());
            et_phone.setText(String.valueOf(user.getPhone()));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed to try to load user's information.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        Button sourceButton = (Button) v;
        if (sourceButton == this.bSave) {
            String name, surname, address, email, country, password, phone;
            name = et_name.getText().toString();
            surname = et_surname.getText().toString();
            address = et_address.getText().toString();
            country = et_country.getText().toString();
            email = et_email.getText().toString();
            password = et_password.getText().toString();
            phone = et_phone.getText().toString();
            if (name.length() == 0) Toast.makeText(getApplicationContext(), "You must enter the name.", Toast.LENGTH_LONG).show(); else if (surname.length() == 0) Toast.makeText(getApplicationContext(), "You must enter the surname .", Toast.LENGTH_LONG).show(); else if (address.length() == 0) Toast.makeText(getApplicationContext(), "You must enter the address.", Toast.LENGTH_LONG).show(); else if (country.length() == 0) Toast.makeText(getApplicationContext(), "You must enter the country .", Toast.LENGTH_LONG).show(); else if (email.length() == 0) Toast.makeText(getApplicationContext(), "You must enter the email.", Toast.LENGTH_LONG).show(); else if (password.length() == 0 || password.length() < 4) Toast.makeText(getApplicationContext(), "You must enter a password greater than 4 characters.", Toast.LENGTH_LONG).show(); else if (phone.length() == 0) Toast.makeText(getApplicationContext(), "You must enter the phone number.", Toast.LENGTH_LONG).show(); else if (phone.length() > 9) Toast.makeText(getApplicationContext(), "The phone number is incorrect.", Toast.LENGTH_LONG).show(); else {
                try {
                    ToServer.myUser().setName(name);
                    ToServer.myUser().setSurname(surname);
                    ToServer.myUser().setEmail(email);
                    ToServer.myUser().setPhone(Integer.parseInt(phone));
                    ToServer.myUser().setCountry(country);
                    ToServer.myUser().setAddress(address);
                    boolean succesfully = ToServer.changeUser(ToServer.myUser(), password);
                    if (succesfully) {
                        Toast.makeText(getApplicationContext(), "The user has been updated.", Toast.LENGTH_LONG).show();
                        this.finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error, has not changed user information.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed while trying to save the user." + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        } else if (sourceButton == this.bCancel) {
            this.finish();
        }
    }
}

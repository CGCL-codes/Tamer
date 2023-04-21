package Projet675.Sobjal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class add_mydeskfriend extends Activity {

    private EditText nomMydeskfriend;

    private EditText serialNumber;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_mydeskfriend);
        nomMydeskfriend = (EditText) findViewById(R.id.edittext_mydeskhelpername);
        serialNumber = (EditText) findViewById(R.id.edittext_mydeskhelperSN);
        Button button = (Button) findViewById(R.id.button_createMyDeskFriend);
        button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                if (serialNumber.length() == 0) {
                    Toast.makeText(add_mydeskfriend.this, "The length of  the Srerial number of mydeskfriend  don't allow empty ", Toast.LENGTH_LONG).show();
                }
                if (nomMydeskfriend.length() < 6) {
                    Toast.makeText(add_mydeskfriend.this, "The length of  the Name of mydeskfriend  is minimum 6 ", Toast.LENGTH_LONG).show();
                } else {
                    gotoNextPage();
                }
            }
        });
    }

    public void gotoNextPage() {
        Intent intent = new Intent();
        intent.setClass(add_mydeskfriend.this, configuration.class);
        startActivity(intent);
        add_mydeskfriend.this.finish();
    }
}

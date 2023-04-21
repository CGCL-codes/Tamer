package org.ramadda.client.android;

import android.app.Activity;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.Button;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class RamaddaActivity extends Activity implements View.OnClickListener {

    public void showMainScreen() {
        Intent i = new Intent(this, RamaddaClient.class);
        startActivity(i);
    }

    public void onClick(View v) {
    }

    public void addClickListener(int[] ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }

    public void setText(int id, String text) {
        TextView textView = (TextView) findViewById(id);
        textView.setText(text);
    }

    public String getTextFromWidget(int id) {
        TextView textView = (TextView) findViewById(id);
        return textView.getText().toString();
    }
}

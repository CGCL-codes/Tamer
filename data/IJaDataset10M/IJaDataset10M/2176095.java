package com.etracks.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.etracks.R;
import com.etracks.domini.CtrlPlacemark;

public class PlacemarkActivity extends Activity {

    private CtrlPlacemark placemarkCtrl;

    private long routeId;

    private long placemarkId;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routeId = this.getIntent().getLongExtra("route_id", -1);
        placemarkId = this.getIntent().getLongExtra("placemark_id", -1);
        if (placemarkId == -1) placemarkCtrl = new CtrlPlacemark(routeId, this); else placemarkCtrl = new CtrlPlacemark(routeId, placemarkId, this);
        setContentView(R.layout.placemark);
        final EditText markerNameEdit = (EditText) findViewById(R.id.placemark_name);
        markerNameEdit.setText(placemarkCtrl.getName());
        final EditText markerDescEdit = (EditText) findViewById(R.id.placemark_desc);
        markerDescEdit.setText(placemarkCtrl.getDesc());
        Button saveButton = (Button) this.findViewById(R.id.save_placemark);
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                placemarkCtrl.save(markerNameEdit.getText().toString(), markerDescEdit.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("msg", getString(R.string.placemark_saved));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        Button deleteButton = (Button) this.findViewById(R.id.delete_placemark);
        deleteButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                placemarkCtrl.delete();
                Intent intent = new Intent();
                intent.putExtra("msg", getString(R.string.placemark_deleted));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}

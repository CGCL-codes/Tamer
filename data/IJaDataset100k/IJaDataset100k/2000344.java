package uk.gov.snh.android.biorecorder.activity;

import uk.gov.snh.android.biorecorder.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class WelcomeActivity extends Activity implements OnClickListener {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        View btnNewRecord = findViewById(R.id.btn_new_record);
        btnNewRecord.setOnClickListener(this);
        View btnViewRecords = findViewById(R.id.btn_view_records);
        btnViewRecords.setOnClickListener(this);
        View btnViewMap = findViewById(R.id.btn_view_map);
        btnViewMap.setOnClickListener(this);
        View btnCamera = findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_new_record:
                {
                    Intent intent = new Intent(this, NewRecordActivity.class);
                    startActivity(intent);
                    break;
                }
            case R.id.btn_view_records:
                {
                    Intent intent = new Intent(this, ViewRecordsActivity.class);
                    startActivity(intent);
                    break;
                }
            case R.id.btn_view_map:
                {
                    break;
                }
            case R.id.btn_camera:
                {
                    break;
                }
        }
    }
}

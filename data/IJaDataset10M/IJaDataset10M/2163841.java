package com.mb.bvgIstZeiten;

import java.io.UnsupportedEncodingException;
import com.mb.bvgIstZeiten.data.DataLoader;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void btnSearchClicked(View v) throws UnsupportedEncodingException {
        final EditText tbSearch = (EditText) findViewById(R.string.main_id_tb_station);
        if (tbSearch.getText().toString().trim().length() == 0) {
            return;
        }
        String requestText = tbSearch.getText().toString().trim();
        String[] options = DataLoader.GetOptions(requestText);
        if (options.length == 0) {
            return;
        }
        if (options.length == 1) {
            Intent intent = new Intent(v.getContext(), ViewStationInfoActivity.class);
            intent.putExtra(ViewStationInfoActivity.EXTRA_STATION_NAME, options[0]);
            startActivity(intent);
            return;
        }
        Intent intent = new Intent(v.getContext(), SelectStationActivity.class);
        intent.putExtra(SelectStationActivity.EXTRA_OPTIONS_NAME, options);
        startActivity(intent);
    }

    public void btnFovouritsClicked(View v) {
    }
}

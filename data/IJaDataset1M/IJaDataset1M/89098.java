package de.tuberlin.julia.SFD_Numbers;

import java.io.File;
import de.tlabs.ahmad.trigger_server.*;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import com.google.code.microlog4android.Logger;
import com.google.code.microlog4android.LoggerFactory;
import com.google.code.microlog4android.config.PropertyConfigurator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SFD_Numbers extends Activity {

    private static Logger logger = LoggerFactory.getLogger();

    EditText vpnr;

    EditText block;

    EditText run;

    Button start;

    Thread ts = new Thread(new TriggerServer());

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File sdcard = Environment.getExternalStorageDirectory();
        File log = new File(sdcard, "microlog.txt");
        if (log.exists()) {
            TimeZone tz = TimeZone.getTimeZone("Europe/Berlin");
            Locale locale = Locale.GERMANY;
            final Calendar c = Calendar.getInstance(tz, locale);
            String date = Integer.toString(c.get(Calendar.DATE)) + "_" + Integer.toString(c.get(Calendar.MONTH)) + "_" + Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + "_" + Integer.toString(c.get(Calendar.MINUTE));
            File to = new File(sdcard, date + ".log");
            log.renameTo(to);
            Environment.getExternalStorageDirectory();
            new File(sdcard, "microlog.txt");
            ts.start();
        }
        PropertyConfigurator.getConfigurator(this).configure();
        setContentView(R.layout.startview);
        vpnr = (EditText) findViewById(R.id.EditText01);
        block = (EditText) findViewById(R.id.EditText02);
        run = (EditText) findViewById(R.id.EditText03);
        start = (Button) findViewById(R.id.Button01);
        start.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), NumberView.class);
                logger.info("********** HEADER **********");
                logger.info("* " + "VP " + vpnr.getText().toString());
                myIntent.putExtra("vpStr", vpnr.getText().toString());
                myIntent.putExtra("block", block.getText().toString());
                myIntent.putExtra("run", run.getText().toString());
                startActivityForResult(myIntent, 0);
                finish();
            }
        });
    }

    public void onRestart() {
        super.onRestart();
        vpnr = (EditText) findViewById(R.id.EditText01);
        vpnr.setText("");
    }
}

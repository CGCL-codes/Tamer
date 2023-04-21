package com.rgm.gasbook;

import java.util.Calendar;
import java.util.Date;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

public class ReFuelEdit extends GasBookActivity {

    static final int DATE_DIALOG_ID = 0;

    private Date mDate;

    private TextView mDateDisplay;

    private ImageButton mPickDate;

    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refuel_edit);
        mDateDisplay = (TextView) findViewById(R.id.reFuelDate);
        mPickDate = (ImageButton) findViewById(R.id.pickDate);
        mPickDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        confirmButton = (Button) findViewById(R.id.confirm);
        final Calendar c = Calendar.getInstance();
        updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_DIALOG_ID:
                final Calendar c = Calendar.getInstance();
                return new DatePickerDialog(this, mDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            updateDate(year, monthOfYear, dayOfMonth);
        }
    };

    private void updateDate(int year, int monthOfYear, int dayOfMonth) {
        mDate = new Date(year, monthOfYear, dayOfMonth);
        mDateDisplay.setText(new StringBuilder().append(dayOfMonth).append("-").append(monthOfYear + 1).append("-").append(year).append(" "));
    }
}

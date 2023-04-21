package com.example.android.apis.view;

import com.example.android.apis.R;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

public class TableLayout8 extends Activity {

    private boolean mStretch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_layout_8);
        final TableLayout table = (TableLayout) findViewById(R.id.menu);
        Button button = (Button) findViewById(R.id.toggle);
        button.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                mStretch = !mStretch;
                table.setColumnStretchable(1, mStretch);
            }
        });
        mStretch = table.isColumnStretchable(1);
        appendRow(table);
    }

    private void appendRow(TableLayout table) {
        TableRow row = new TableRow(this);
        TextView label = new TextView(this);
        label.setText(R.string.table_layout_8_quit);
        label.setPadding(3, 3, 3, 3);
        TextView shortcut = new TextView(this);
        shortcut.setText(R.string.table_layout_8_ctrlq);
        shortcut.setPadding(3, 3, 3, 3);
        shortcut.setGravity(Gravity.RIGHT | Gravity.TOP);
        row.addView(label, new TableRow.LayoutParams(1));
        row.addView(shortcut, new TableRow.LayoutParams());
        table.addView(row, new TableLayout.LayoutParams());
    }
}

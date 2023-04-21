package org.jazzteam.studenthelper.activity;

import org.jazzteam.studenthelper.android.R;
import android.os.Bundle;
import android.widget.TextView;

public class OneMessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onemessage);
        Bundle b = getIntent().getExtras();
        TextView tv_message_date = (TextView) findViewById(R.id.text_view_message_date);
        TextView tv_message_header = (TextView) findViewById(R.id.text_view_message_header);
        TextView tv_message_text = (TextView) findViewById(R.id.text_view_message_text);
        tv_message_date.setText((CharSequence) b.get("date"));
        tv_message_text.setText((CharSequence) b.get("text"));
        tv_message_header.setText((CharSequence) b.get("header"));
    }
}

package fire.eagle.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class AboutActivity extends AbstractActivity implements OnClickListener {

    public static final String ACTION_ABOUT = "jfireeagle.android.action.ABOUT";

    public static final Intent INTENT = new Intent(ACTION_ABOUT);

    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        ScrollView scroll = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(layout);
        Button close = new Button(this);
        close.setText("Close");
        close.setOnClickListener(this);
        close.setGravity(Gravity.CENTER);
        TextView text = new TextView(this);
        text.setText("http://code.google.com/p/jfireeagle/wiki/Android");
        text.setAutoLinkMask(Linkify.ALL);
        text.setTextSize((text.getTextSize() - 2.0f));
        text.setGravity(Gravity.CENTER);
        layout.addView(text);
        layout.addView(close);
        setContentView(scroll);
    }

    public void onClick(View v) {
        finish();
    }
}

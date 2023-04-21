package com.android.development;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;
import android.os.Bundle;
import android.server.data.CrashData;
import android.server.data.ThrowableData;
import android.server.data.StackTraceElementData;
import android.graphics.Typeface;

/**
 * Views a single stack trace.
 */
public class StacktraceViewer extends Activity {

    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.log_viewer);
        TextView text = (TextView) findViewById(R.id.text);
        text.setTextSize(10);
        text.setHorizontallyScrolling(true);
        text.setTypeface(Typeface.MONOSPACE);
        text.setMovementMethod(ScrollingMovementMethod.getInstance());
        String stacktrace = getIntent().getExtras().getString(CrashData.class.getName());
        text.setText(stacktrace);
    }
}

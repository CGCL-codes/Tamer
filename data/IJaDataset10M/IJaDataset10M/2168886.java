package com.example.android.debuggermenu;

import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

public class DebuggerHelp extends DebuggerActivity {

    private static final String TAG = DebuggerHelp.class.getSimpleName();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
        InputStream iFile = getResources().openRawResource(R.raw.help);
        try {
            TextView helpText = (TextView) findViewById(R.id.TextView_HelpText);
            helpText.setMaxLines(15);
            helpText.setMovementMethod(new ScrollingMovementMethod());
            String strFile = inputStreamToString(iFile);
            helpText.setText(strFile);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onCreate()" + e.toString());
        }
    }

    /**
     * Converts an input stream to a string
     *
     * @param is
     *            The {@code InputStream} object to read from
     * @return A {@code String} object representing the string for of the input
     * @throws IOException
     *             Thrown on read failure from the input
     */
    public String inputStreamToString(InputStream is) throws IOException {
        StringBuffer sBuffer = new StringBuffer();
        DataInputStream dataIO = new DataInputStream(is);
        String strLine = null;
        while ((strLine = dataIO.readLine()) != null) {
            sBuffer.append(strLine + "\n");
        }
        dataIO.close();
        is.close();
        return sBuffer.toString();
    }
}

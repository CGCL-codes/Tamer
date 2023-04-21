package net.sf.andpdf.icepdfviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * U:\Android\android-sdk-windows-1.5_r1\tools\adb push u:\Android\simple_T.pdf /data/test.pdf
 * @author ferenc.hechler
 */
public class IcePdfFileSelectActivity extends Activity {

    private static final String TAG = "PDFVIEWER";

    public static final String PREFS_NAME = "PDFViewerPrefs";

    public static final String PREFS_PDFFILENAME = "pdffilename";

    public static final String PREFS_USEFILLSTYLE = "usefillstyle";

    public static final String DEFAULTPDFFILENAME = "/sdcard/download/example.pdf";

    public static final boolean DEFAULTUSEFILLSTYLE = false;

    public static final String EXTRA_PDFFILENAME = "net.sf.andpdf.extra.PDFFILENAME";

    public static final String EXTRA_USEFILLSTYLE = "net.sf.andpdf.extra.USEFILLSTYLE";

    private EditText mFilename;

    private EditText mOutput;

    private CheckBox mUseFillStyle;

    private Button mShow;

    private Button mExit;

    private SimplePersistence persist;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_file_select);
        mFilename = (EditText) findViewById(R.id.filename);
        mOutput = (EditText) findViewById(R.id.output);
        mUseFillStyle = (CheckBox) findViewById(R.id.cbUseFillStyle);
        mShow = (Button) findViewById(R.id.btShow);
        mExit = (Button) findViewById(R.id.btExit);
        mShow.setOnClickListener(ShowPdfListener);
        mExit.setOnClickListener(ExitListener);
        persist = new SimplePersistence(this, PREFS_NAME);
        String pdffilename = persist.getString(PREFS_PDFFILENAME, DEFAULTPDFFILENAME);
        boolean useFillStyle = persist.getBoolean(PREFS_USEFILLSTYLE, DEFAULTUSEFILLSTYLE);
        mFilename.setText(pdffilename);
        mUseFillStyle.setChecked(useFillStyle);
    }

    @Override
    protected void onStop() {
        super.onStop();
        persistValues();
    }

    private void persistValues() {
        String pdffilename = mFilename.getText().toString();
        boolean useFillStyle = mUseFillStyle.isChecked();
        persist.putString(PREFS_PDFFILENAME, pdffilename);
        persist.putBoolean(PREFS_USEFILLSTYLE, useFillStyle);
        persist.commit();
    }

    private void showText(String text) {
        Log.i(TAG, text);
        mOutput.setTag(text);
    }

    OnClickListener ExitListener = new OnClickListener() {

        public void onClick(View v) {
            finish();
        }
    };

    OnClickListener ShowPdfListener = new OnClickListener() {

        public void onClick(View v) {
            persistValues();
            String pdffilename = mFilename.getText().toString();
            boolean useFillStyle = mUseFillStyle.isChecked();
            Intent intent = new Intent(IcePdfFileSelectActivity.this, IcePdfViewerActivity.class).putExtra(EXTRA_PDFFILENAME, pdffilename).putExtra(EXTRA_USEFILLSTYLE, useFillStyle);
            startActivity(intent);
        }
    };
}

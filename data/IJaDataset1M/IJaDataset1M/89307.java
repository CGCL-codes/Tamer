package pl.blip.divide.getpills;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class PillConfirmer extends Activity implements OnClickListener {

    private int skipped = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.confirm_pill);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        window.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_dialog_pill);
        LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0f;
        window.setAttributes(lp);
        ((Button) findViewById(R.id.ok)).setOnClickListener(this);
        ((Button) findViewById(R.id.later)).setOnClickListener(this);
        Uri uri = getIntent().getData();
        if (uri == null) {
            Log.e("PillConfirmer", "called without an URI");
            this.finish();
        }
        Cursor c = managedQuery(uri, null, null, null, null);
        if (c.moveToNext()) {
            ((TextView) findViewById(R.id.name)).setText(c.getString(c.getColumnIndex(Pills.NAME)));
            skipped = Pills.getSkipped(c);
            if (skipped > 0) {
                ((TextView) findViewById(R.id.skipped_pills)).setText(getString(R.string.n_pills_skipped, skipped));
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        setResult(RESULT_CANCELED);
        if (v.getId() == R.id.ok) {
            Uri uri = getIntent().getData();
            int id = (int) ContentUris.parseId(uri);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(id);
            final ContentResolver r = getContentResolver();
            final String projection[] = { Pills.NEXT_TAKE, Pills.INTERVAL };
            final Cursor c = r.query(uri, projection, null, null, null);
            c.moveToNext();
            final ContentValues cv = new ContentValues();
            long next_take = c.getLong(0);
            long interval = c.getInt(1) * 60 * 60 * 1000;
            next_take += (1 + skipped) * interval;
            cv.put(Pills.NEXT_TAKE, next_take);
            r.update(uri, cv, null, null);
            PillNotifier.scheduleAlarm(this, uri);
            setResult(RESULT_OK);
        }
        finish();
    }
}

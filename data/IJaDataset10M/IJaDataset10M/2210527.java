package il.co.gadiworks.tutorial;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class GFX extends Activity {

    GadiWorks ourView;

    WakeLock wL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PowerManager pM = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wL = pM.newWakeLock(PowerManager.FULL_WAKE_LOCK, "whatever");
        super.onCreate(savedInstanceState);
        ourView = new GadiWorks(this);
        setContentView(ourView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wL.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wL.acquire();
    }
}

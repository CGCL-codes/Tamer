package uoft.smp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;

public class Smp2011Activity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        new CountDownTimer(1000, 1000) {

            public void onFinish() {
                final Intent intent = new Intent().setClass(Smp2011Activity.this, Menu.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onTick(long arg0) {
            }
        }.start();
    }
}

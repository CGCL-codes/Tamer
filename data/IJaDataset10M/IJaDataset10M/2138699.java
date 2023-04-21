package skylight1.marketapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import skylight1.marketapp.view.CandleView;
import skylight1.marketapp.view.CandleDrawView;

public class CandleSticksActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        CandleDrawView customView = new CandleDrawView(this);
        setContentView(customView);
        customView.requestFocus();
    }
}

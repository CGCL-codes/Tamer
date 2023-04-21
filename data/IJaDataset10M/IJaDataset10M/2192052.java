package android.widget.cts;

import com.android.cts.stub.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * A minimal application for TextView test.
 */
public class DigitalClockStubActivity extends Activity {

    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digitalclock_simplelayout);
    }
}

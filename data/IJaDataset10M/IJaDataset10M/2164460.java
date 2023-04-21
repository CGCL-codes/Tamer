package android.widget.cts;

import com.android.cts.stub.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * A minimal application for {@link GridView} test.
 */
public class GridViewStubActivity extends Activity {

    /**
     * Called with the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_layout);
    }
}

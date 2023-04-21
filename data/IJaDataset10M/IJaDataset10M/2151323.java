package android.widget.gridview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.android.frameworks.coretests.R;

/**
 * Exercises a grid in a horizontal linear layout
 */
public class GridInHorizontal extends Activity {

    Handler mHandler = new Handler();

    TextView mText;

    GridView mGridView;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.grid_in_horizontal);
        String values[] = new String[1000];
        int i = 0;
        for (i = 0; i < 1000; i++) {
            values[i] = ((Integer) i).toString();
        }
        mText = (TextView) findViewById(R.id.text);
        mGridView = (GridView) findViewById(R.id.grid);
        mGridView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values));
    }

    public GridView getGridView() {
        return mGridView;
    }
}

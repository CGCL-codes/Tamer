package my.app;

import my.app.common.Globals;
import android.app.Activity;
import android.os.Bundle;

public class massesActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Globals.setMainActivity(this);
        Globals.setCnotext(this);
        MassesListView list = new MassesListView(this);
        list.init();
        setContentView(list);
    }
}

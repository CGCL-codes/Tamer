package skylight1.mymaps;

import skylight1.mymaps.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Welcome extends Activity {

    private static final String TAG = "mymaps";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button map0 = (Button) findViewById(R.id.Button01);
        final String goMap = "http://maps.google.com/maps/ms?ie=UTF8&hl=en&msa=0&msid=101509637019979626548.00048f4bd84c82a876629&ll=40.736292,-73.993446&spn=0.068027,0.169086&z=13";
        final Uri url = Uri.parse(goMap);
        map0.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setComponent(ComponentName.unflattenFromString("com.google.android.apps.maps/com.google.android.maps.MapsActivity"));
                intent.addCategory("android.intent.category.LAUNCHER");
                intent.setData(url);
                startActivity(intent);
            }
        });
    }
}

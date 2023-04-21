package skylight1.nycrecycle;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Welcome extends Activity implements OnClickListener {

    /** Called when the activity is first created. */
    static final String TAG = "NYC Recycle";

    static final int ABOUT = 0;

    static final int GREET_DIALOG = 1;

    static final int ITS_OVER_DIALOG = 2;

    static final String INTENT_CATAGORY_TYPE = "android.intent.category.LAUNCHER";

    static final String MAP_COMPONENT_NAME = "com.google.android.apps.maps/com.google.android.maps.MapsActivity";

    static final String MANHATTAN = " http://maps.google.com/maps/ms?hl=en&ie=UTF8&msa=0&msid=214217748623619348483.00049a9eee29811a27bb3&ll=40.712915,-74.022446&spn=0.329977,0.668106&z=11  ";

    static final String BROOKLYN = " http://maps.google.com/maps/ms?hl=en&ie=UTF8&msa=0&msid=214217748623619348483.00049a9ef1f30f8a74ffa&ll=40.712915,-74.022446&spn=0.329977,0.668106&z=11 ";

    static final String BRONX = "http://maps.google.com/maps/ms?hl=en&ie=UTF8&msa=0&msid=214217748623619348483.00049a9ef5fd32776dc18&ll=40.712915,-74.022446&spn=0.329977,0.668106&z=11  ";

    static final String QUEENS = "  http://maps.google.com/maps/ms?hl=en&ie=UTF8&msa=0&msid=214217748623619348483.00049a9ef8674f8dbd9c1&ll=40.712915,-74.022446&spn=0.329977,0.668106&z=11";

    static final String STATEN_ISLAND = "  http://maps.google.com/maps/ms?hl=en&ie=UTF8&msa=0&msid=214217748623619348483.00049a9efa3bb7ad2d989&ll=40.712915,-74.022446&spn=0.329977,0.668106&z=11";

    static final String DONATE = "http://www.nycharities.org/donate/c_donate.asp?CharityCode=1114";

    Uri uriManhattan;

    Uri uriBrooklyn;

    Uri uriBronx;

    Uri uriQueens;

    Uri uriStatenIsland;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        uriManhattan = Uri.parse(MANHATTAN);
        uriBrooklyn = Uri.parse(BROOKLYN);
        uriBronx = Uri.parse(BRONX);
        uriQueens = Uri.parse(QUEENS);
        uriStatenIsland = Uri.parse(STATEN_ISLAND);
        View m1 = findViewById(R.id.Button01);
        m1.setOnClickListener(this);
        View m2 = findViewById(R.id.Button02);
        m2.setOnClickListener(this);
        View m3 = findViewById(R.id.Button03);
        m3.setOnClickListener(this);
        View m4 = findViewById(R.id.Button04);
        m4.setOnClickListener(this);
        View m5 = findViewById(R.id.Button05);
        m5.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setComponent(ComponentName.unflattenFromString("com.google.android.apps.maps/com.google.android.maps.MapsActivity"));
        intent.addCategory(INTENT_CATAGORY_TYPE);
        switch(v.getId()) {
            case R.id.Button01:
                intent.setData(uriBronx);
                break;
            case R.id.Button02:
                intent.setData(uriQueens);
            case R.id.Button03:
                intent.setData(uriManhattan);
                break;
            case R.id.Button04:
                intent.setData(uriBrooklyn);
                break;
            case R.id.Button05:
                intent.setData(uriStatenIsland);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

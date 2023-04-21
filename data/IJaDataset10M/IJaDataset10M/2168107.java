package ru.elifantiev.cityrouter;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.elifantiev.cityrouter.data.JSONHadler;
import ru.elifantiev.cityrouter.infrastructure.adapters.JSONRoutesAdapter;

public class RoutesDisplayActivity extends ListActivity {

    private int current = 0;

    private int max = 0;

    private Button prev, next;

    private JSONArray routes = null;

    private static final String urlFormat = "http://city-router.appspot.com/routes/?from_lat=%s&from_lon=%s&to_lat=%s&to_lon=%s";

    private void recalcAvail() {
        prev.setVisibility(current > 0 ? View.VISIBLE : View.INVISIBLE);
        next.setVisibility(current < max - 1 ? View.VISIBLE : View.INVISIBLE);
        invalidateListData();
    }

    private String getUrl(Intent call) {
        Bundle extras = call.getExtras();
        return String.format(urlFormat, extras.getString("from_lat"), extras.getString("from_lon"), extras.getString("to_lat"), extras.getString("to_lon"));
    }

    private void invalidateListData() {
        if (routes != null) {
            try {
                setListAdapter(new JSONRoutesAdapter(this, R.layout.route_item, routes.getJSONObject(current).getJSONArray("items")));
            } catch (JSONException e) {
                Log.d("RoutesDisplayActivity", e.getMessage());
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_result);
        prev = (Button) findViewById(R.id.btnPrev);
        next = (Button) findViewById(R.id.btnNext);
        prev.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (current > 0) current--;
                recalcAvail();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (current < max - 1) current++;
                recalcAvail();
            }
        });
        new AsyncLoadRoutes().execute(getUrl(getIntent()));
    }

    class AsyncLoadRoutes extends AsyncTask<String, Void, JSONObject> {

        ProgressDialog progress;

        private String TAG = "AsyncLoadRoutes";

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(RoutesDisplayActivity.this, "", getString(R.string.loading), true, false);
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                return JSONHadler.getJSONData(strings[0]);
            } catch (JSONException e) {
                Log.d(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progress.dismiss();
            if (jsonObject != null) {
                try {
                    routes = jsonObject.getJSONArray("routes");
                } catch (JSONException e) {
                    Log.d(TAG, e.getMessage());
                }
                max = routes.length();
                recalcAvail();
            }
        }
    }
}

package com.gobynote.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.goby.util.widget.ActionBar;
import com.goby.util.widget.ActionBar.IntentAction;
import com.gobynote.android.Dashboard;
import com.gobynote.android.R;
import com.gobynote.android.models.WeatherIcon;
import com.gobynote.android.models.WeatherIcons;

public class WeatherIconSelection extends Activity {

    String TAG = this.getClass().getSimpleName();

    String action;

    Button cancelBtn;

    Button okBtn;

    TextView t_title;

    ImageView i_weather_icon;

    GridView gridview;

    WeatherIcon selectedIcon;

    String iconName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iconName = getIntent().getStringExtra("icon_name");
        setContentView(R.layout.weather_icon_select);
        final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(getString(R.string.sel_weather));
        actionBar.setHomeAction(new IntentAction(this, Dashboard.createIntent(this), R.drawable.goto_home));
        actionBar.setDisplayHomeAsUpEnabled(true);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        okBtn = (Button) findViewById(R.id.okBtn);
        i_weather_icon = (ImageView) findViewById(R.id.i_mood_icon);
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        loadData();
        createListenser();
    }

    private void loadData() {
        selectedIcon = WeatherIcons.getFromName(iconName);
        i_weather_icon.setImageDrawable(WeatherIcons.getIconFromName(this.getResources(), iconName));
    }

    private void createListenser() {
        gridview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                selectedIcon = WeatherIcons.getFromPos(position);
                i_weather_icon.setImageResource(selectedIcon.getDrawableId());
            }
        });
        cancelBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                finish();
            }
        });
        okBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Intent intent = WeatherIconSelection.this.getIntent();
                Log.d(TAG, "Weather ID : " + selectedIcon.getId());
                intent.putExtra("_result", selectedIcon.getId());
                WeatherIconSelection.this.setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    class ImageAdapter extends BaseAdapter {

        private Context mContext;

        private Integer[] mThumbIds = WeatherIcons.getAllIconIds();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(65, 65));
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }
    }
}

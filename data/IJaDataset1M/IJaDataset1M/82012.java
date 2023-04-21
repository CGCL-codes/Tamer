package com.ray.project.splash;

import com.ray.project.oceanicwaveinformation.OceanicBuoyMapViewActivity;
import com.ray.project.oceanicwaveinformation.R;
import com.ray.project.rssreader.rssreader;
import com.ray.project.seismograph.Seismograph;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ListSplashView extends Activity implements OnItemClickListener {

    private static String[] menuOptions;

    private static final Integer[] menuImages = { R.drawable.map, R.drawable.rss, R.drawable.seismograph, R.drawable.web_browser, R.drawable.settings };

    private static class CustomListSpalashAdapter extends BaseAdapter {

        private LayoutInflater viewInflater;

        public CustomListSpalashAdapter(Context context) {
            viewInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return menuOptions.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewStructure structure;
            if (convertView == null) {
                convertView = viewInflater.inflate(R.layout.list_splash_view, null);
                structure = new ViewStructure();
                structure.menuImage = (ImageView) convertView.findViewById(R.id.menuImage);
                structure.menuText = (TextView) convertView.findViewById(R.id.menuText);
                convertView.setTag(structure);
            } else {
                structure = (ViewStructure) convertView.getTag();
            }
            structure.menuImage.setImageResource(menuImages[position]);
            structure.menuText.setText(menuOptions[position]);
            return convertView;
        }

        static class ViewStructure {

            ImageView menuImage;

            TextView menuText;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_splash_main);
        menuOptions = getResources().getStringArray(R.array.menuOptions);
        ListView menuList = (ListView) findViewById(R.id.menulist);
        menuList.setAdapter(new CustomListSpalashAdapter(this));
        menuList.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch(position) {
            case 0:
                startActivity(new Intent(ListSplashView.this, OceanicBuoyMapViewActivity.class));
                break;
            case 1:
                startActivity(new Intent(ListSplashView.this, rssreader.class));
                break;
            case 2:
                startActivity(new Intent(ListSplashView.this, Seismograph.class));
                break;
            case 3:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ndbc.noaa.gov/mobile/")));
                break;
            case 4:
                startActivity(new Intent(ListSplashView.this, com.ray.project.settings.Settings.class));
                break;
            default:
                break;
        }
    }

    private void SendIntent(View v, String fruit) {
    }
}

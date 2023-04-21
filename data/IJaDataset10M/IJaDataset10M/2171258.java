package mobilesearch.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import mobilesearch.ui.SearchResultListActivity;
import android.content.Intent;
import android.app.Application;
import android.widget.ImageButton;

public class MobileSearchActivity extends Activity implements OnClickListener, OnItemClickListener {

    /** Called when the activity is first created. */
    private static final String TAG = "MobileSearchActivity";

    private EditText searchBox;

    private ImageButton searchButton;

    private ListView mainList;

    private SimpleAdapter listAdapter;

    private MobileSearchEngine engine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        searchBox = (EditText) findViewById(R.id.search_box);
        searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setImageDrawable(this.getResources().getDrawable(android.R.drawable.ic_menu_search));
        searchButton.setOnClickListener(this);
        mainList = (ListView) findViewById(R.id.main_list);
        listAdapter = new SimpleAdapter(this, getData(), R.layout.search_type_list_item, new String[] { "type_img", "text", "hint_img" }, new int[] { R.id.type_img, R.id.text, R.id.hint_img });
        mainList.setAdapter(this.listAdapter);
        mainList.setOnItemClickListener(this);
        engine = new MobileSearchEngine(this);
        MobileSearchApplication application = (MobileSearchApplication) this.getApplication();
        application.setEngine(engine);
    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type_img", android.R.drawable.btn_star_big_off);
        map.put("text", "Alarm");
        map.put("hint_img", android.R.drawable.arrow_down_float);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("type_img", android.R.drawable.btn_star_big_off);
        map.put("text", "Application");
        map.put("hint_img", android.R.drawable.arrow_down_float);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("type_img", android.R.drawable.btn_star_big_off);
        map.put("text", "Audio");
        map.put("hint_img", android.R.drawable.arrow_down_float);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("type_img", android.R.drawable.btn_star_big_off);
        map.put("text", "Bookmark");
        map.put("hint_img", android.R.drawable.arrow_down_float);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("type_img", android.R.drawable.btn_star_big_off);
        map.put("text", "CallLog");
        map.put("hint_img", android.R.drawable.arrow_down_float);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("type_img", android.R.drawable.btn_star_big_off);
        map.put("text", "Contacts");
        map.put("hint_img", android.R.drawable.arrow_down_float);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("type_img", android.R.drawable.btn_star_big_off);
        map.put("text", "Email");
        map.put("hint_img", android.R.drawable.arrow_down_float);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("type_img", android.R.drawable.btn_star_big_off);
        map.put("text", "File");
        map.put("hint_img", android.R.drawable.arrow_down_float);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("type_img", android.R.drawable.btn_star_big_off);
        map.put("text", "Image");
        map.put("hint_img", android.R.drawable.arrow_down_float);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("type_img", android.R.drawable.btn_star_big_off);
        map.put("text", "Sms");
        map.put("hint_img", android.R.drawable.arrow_down_float);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("type_img", android.R.drawable.btn_star_big_off);
        map.put("text", "Video");
        map.put("hint_img", android.R.drawable.arrow_down_float);
        list.add(map);
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick");
        TextView textview = (TextView) view.findViewById(R.id.text);
        String sourText = textview.getText().toString();
        ArrayList<String> list = new ArrayList<String>();
        String destText = this.getString(R.string.alarm);
        list.add(destText);
        destText = this.getString(R.string.application);
        list.add(destText);
        destText = this.getString(R.string.audio);
        list.add(destText);
        destText = this.getString(R.string.bookmark);
        list.add(destText);
        destText = this.getString(R.string.calllog);
        list.add(destText);
        destText = this.getString(R.string.contacts);
        list.add(destText);
        destText = this.getString(R.string.email);
        list.add(destText);
        destText = this.getString(R.string.file);
        list.add(destText);
        destText = this.getString(R.string.image);
        list.add(destText);
        destText = this.getString(R.string.sms);
        list.add(destText);
        destText = this.getString(R.string.video);
        list.add(destText);
        Log.i(TAG, String.valueOf(list.size()));
        for (int i = 0; i < list.size(); i++) {
            if (sourText.compareTo(list.get(i)) == 0) {
                Log.i(TAG, sourText);
                Intent intent = new Intent(MobileSearchActivity.this, SearchResultListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(sourText);
                this.startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick");
        engine.startSearch("search");
    }
}

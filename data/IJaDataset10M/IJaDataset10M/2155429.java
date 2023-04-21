package mars.mp3player.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mars.mp3player.R;
import mars.mp3player.entity.Mp3Bean;
import mars.mp3player.utils.FileUtils;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class LocalMP3Activity extends ListActivity {

    private List<Mp3Bean> mp3List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_mp3);
    }

    @Override
    protected void onResume() {
        getLocalMp3List();
        super.onResume();
    }

    public void getLocalMp3List() {
        List<Mp3Bean> mp3Beans = new ArrayList<Mp3Bean>();
        FileUtils fileUtils = new FileUtils();
        File[] files = null;
        try {
            files = fileUtils.getFileList("mp3", ".mp3");
        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        }
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                Mp3Bean bean = new Mp3Bean();
                bean.setMp3Name(files[i].getName());
                bean.setMp3Size(files[i].length() + "");
                mp3Beans.add(bean);
            }
        }
        mp3List = mp3Beans;
        LocalMP3Activity.this.setListAdapter(buildListAdapter(mp3Beans));
    }

    public SimpleAdapter buildListAdapter(List<Mp3Bean> beans) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (Mp3Bean bean : beans) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("mp3Name", bean.getMp3Name());
            map.put("mp3Size", bean.getMp3Size() + "byte");
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(LocalMP3Activity.this, list, R.layout.mp3, new String[] { "mp3Name", "mp3Size" }, new int[] { R.id.mp3Name, R.id.mp3Size });
        return adapter;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Mp3Bean mp3Bean = mp3List.get(position);
        Intent intent = new Intent();
        intent.putExtra("mp3Bean", mp3Bean);
        intent.setClass(LocalMP3Activity.this, PlayerActivity.class);
        LocalMP3Activity.this.startActivity(intent);
        super.onListItemClick(l, v, position, id);
    }
}

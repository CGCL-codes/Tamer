package com.mp3player;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.download.DownLoader;
import com.mode.Mp3Info;
import com.mp3player.service.DownloadService;
import com.xml.Mp3ListContentHandler;

public class Mp3ListActivity extends ListActivity {

    private static final int UPDATE = 1;

    private static final int ABOUT = 2;

    private List<Mp3Info> mp3Infos = null;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_mp3_list);
        updateListView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, R.string.mp3list_update);
        menu.add(0, 2, 2, R.string.mp3list_about);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == UPDATE) {
            updateListView();
        } else if (item.getItemId() == ABOUT) {
        }
        return super.onOptionsItemSelected(item);
    }

    private SimpleAdapter buildAdapter(List<Mp3Info> mp3Infos) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext(); ) {
            Mp3Info mp3Info = (Mp3Info) iterator.next();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("mp3_name", mp3Info.getMp3Name());
            map.put("mp3_size", mp3Info.getMp3Size());
            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.mp3info_item, new String[] { "mp3_name", "mp3_size" }, new int[] { R.id.mp3_name, R.id.mp3_size });
        return simpleAdapter;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Mp3Info mp3Info = mp3Infos.get(position);
        Intent intent = new Intent();
        intent.putExtra("mp3Info", mp3Info);
        intent.setClass(this, DownloadService.class);
        startService(intent);
        super.onListItemClick(l, v, position, id);
    }

    private void updateListView() {
        String xmlPath = AppConstant.URL.BASE_URL + File.separator + "resources.xml";
        String xml = downloadXML(xmlPath);
        System.out.println(xml);
        List<Mp3Info> mp3Infos = parse(xml);
        SimpleAdapter simpleAdapter = buildAdapter(mp3Infos);
        setListAdapter(simpleAdapter);
        if (mp3Infos == null || mp3Infos.size() == 0) {
            Toast.makeText(this, "无法访问" + xmlPath + "资源", Toast.LENGTH_LONG).show();
        }
    }

    private String downloadXML(String urlStr) {
        DownLoader downLoader = new DownLoader();
        String result = downLoader.download(urlStr);
        return result;
    }

    private List<Mp3Info> parse(String xmlStr) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
            mp3Infos = new ArrayList<Mp3Info>();
            Mp3ListContentHandler mp3ListContentHandler = new Mp3ListContentHandler(mp3Infos);
            xmlReader.setContentHandler(mp3ListContentHandler);
            xmlReader.parse(new InputSource(new StringReader(xmlStr)));
            for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext(); ) {
                Mp3Info mp3Info = (Mp3Info) iterator.next();
                System.out.println(mp3Info);
            }
        } catch (Exception e) {
        }
        return mp3Infos;
    }
}

package mars.mp3player.core;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import mars.mp3player.R;
import mars.mp3player.download.HttpDownloader;
import mars.mp3player.entity.Mp3Bean;
import mars.mp3player.service.AppConstant;
import mars.mp3player.service.DownloadService;
import mars.mp3player.xml.Mp3ListContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RemoteMP3Activity extends ListActivity {

    private static final int UPDATELIST = 1;

    private static final int ABOUT = 2;

    private static final String mp3XML_URL = AppConstant.URL.XML_URL;

    private List<Mp3Bean> mp3Beans = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remote_mp3);
        updateMp3List();
    }

    public void updateMp3List() {
        String xml = downloadXML(mp3XML_URL);
        Log.i("info", xml);
        mp3Beans = parseXml(xml);
        RemoteMP3Activity.this.setListAdapter(buildListAdapter(mp3Beans));
    }

    public SimpleAdapter buildListAdapter(List<Mp3Bean> beans) {
        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (Mp3Bean bean : beans) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("mp3Name", bean.getMp3Name());
            map.put("mp3Size", bean.getMp3Size() + "byte");
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(RemoteMP3Activity.this, list, R.layout.mp3, new String[] { "mp3Name", "mp3Size" }, new int[] { R.id.mp3Name, R.id.mp3Size });
        return adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, UPDATELIST, 1, R.string.updateList);
        menu.add(0, ABOUT, 2, R.string.about);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == UPDATELIST) {
        } else if (item.getItemId() == ABOUT) {
        }
        return super.onOptionsItemSelected(item);
    }

    public String downloadXML(String url) {
        HttpDownloader downloader = new HttpDownloader();
        return downloader.download(url);
    }

    private List<Mp3Bean> parseXml(String xmlStr) {
        List<Mp3Bean> mp3List = new ArrayList<Mp3Bean>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            Mp3ListContentHandler handler = new Mp3ListContentHandler(mp3List);
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(new StringReader(xmlStr)));
        } catch (SAXException e) {
            Log.e("exception", e.getMessage());
        } catch (ParserConfigurationException e) {
            Log.e("exception", e.getMessage());
        } catch (IOException e) {
            Log.e("exception", e.getMessage());
        }
        return mp3List;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Mp3Bean mp3Bean = mp3Beans.get(position);
        Intent intent = new Intent();
        intent.putExtra("fileName", mp3Bean.getMp3Name());
        intent.setClass(RemoteMP3Activity.this, DownloadService.class);
        RemoteMP3Activity.this.startService(intent);
        super.onListItemClick(l, v, position, id);
    }
}

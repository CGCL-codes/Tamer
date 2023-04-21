package eks.youtube;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import dk.nordfalk.android.elementer.R;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Hjælpeklasse til at holde info om et klip
 * @author Jacob Nordfalk
 */
class Klip {

    String id;

    HashMap<String, String> egenskaber = new HashMap<String, String>();

    String videourl;

    String thumburl;

    String titel;

    Bitmap thumb;

    String link;

    public Klip() {
    }

    public String toString() {
        return titel + egenskaber.toString();
    }
}

/**
 * Parser youtubes RSS-feed v.hj.a. XMLPull
 * Se også http://code.google.com/intl/da/apis/youtube/2.0/developers_guide_protocol_api_query_parameters.html#keysp
 *
 * Inspiration til eksemplet er fundet på
 * http://www.ibm.com/developerworks/opensource/library/x-android/index.html
 * http://code.google.com/p/feedgoal/
 * http://stackoverflow.com/questions/5162088/video-view-not-playing-youtube-video
 *
 * @author Jacob Nordfalk
 */
public class YoutubeRssParsning extends Activity implements OnItemClickListener {

    /** Listen over videoklip - en klassevariabel der kun indlæses én gang */
    static ArrayList<Klip> videoklip = new ArrayList<Klip>();

    ParseKlipAsyncTask klipAsyncTask = new ParseKlipAsyncTask();

    ListView listView;

    KlipAdapter klipadapter = new KlipAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FilCache.init(getCacheDir());
        if (videoklip.isEmpty()) {
            klipAsyncTask.execute();
        }
        listView = new ListView(this);
        listView.setAdapter(klipadapter);
        listView.setOnItemClickListener(this);
        listView.setId(117);
        setContentView(listView);
    }

    public class ParseKlipAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object... arg0) {
            try {
                InputStream is;
                is = new URL("http://gdata.youtube.com/feeds/api/users/Esperantoestas/uploads").openStream();
                is = new FileInputStream(FilCache.hentFil("http://gdata.youtube.com/feeds/api/users/Esperantoestas/uploads", false));
                is = getResources().openRawResource(R.raw.youtubefeed_eksempel);
                ArrayList<Klip> klip = parseRss(is);
                is.close();
                videoklip.addAll(klip);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            publishProgress();
            for (Klip k : videoklip) {
                try {
                    System.out.println(k.titel + " " + k.videourl);
                    System.out.println("k.thumburl = " + k.thumburl);
                    if (k.thumburl != null) {
                        k.thumb = BitmapFactory.decodeFile(FilCache.hentFil(k.thumburl, true));
                    }
                    publishProgress();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return "ok";
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            klipadapter.notifyDataSetChanged();
        }
    }

    /** Parser et youtube RSS feed og returnerer det som en liste at Klip-objekter */
    private static ArrayList<Klip> parseRss(InputStream is) throws Exception {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser p = factory.newPullParser();
        p.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        p.setInput(is, null);
        HashSet egenskaber = new HashSet(Arrays.asList("published", "updated", "content"));
        ArrayList<Klip> liste = new ArrayList<Klip>();
        Klip k = new Klip();
        while (true) {
            int eventType = p.next();
            if (eventType == XmlPullParser.END_DOCUMENT) {
                break;
            }
            if (eventType != XmlPullParser.START_TAG) {
                continue;
            }
            String ns = p.getPrefix();
            String tag = p.getName();
            if (ns == null) {
                if ("entry".equals(tag)) {
                    k = new Klip();
                    liste.add(k);
                } else if ("id".equals(tag)) {
                    k.id = p.nextText();
                } else if ("title".equals(tag)) {
                    k.titel = p.nextText();
                    System.out.println("=============== " + k.titel + " ================");
                } else if ("link".equals(tag)) {
                    if ("text/html".equals(p.getAttributeValue(null, "type"))) {
                        if (k.link == null) {
                            k.link = p.getAttributeValue(null, "href");
                        }
                    }
                } else if (egenskaber.contains(tag)) {
                    k.egenskaber.put(tag, p.nextText());
                }
            } else if ("media".equals(ns)) {
                if ("content".equals(tag)) {
                    for (int i = 0; i < p.getAttributeCount(); i++) {
                        System.out.print(p.getAttributeName(i) + ":" + p.getAttributePrefix(i) + "=" + p.getAttributeValue(i) + " ");
                    }
                    System.out.println();
                    String type = p.getAttributeValue(null, "type");
                    if ("application/x-shockwave-flash".equals(type)) {
                        continue;
                    }
                    if (k.videourl == null) {
                        k.videourl = p.getAttributeValue(null, "url");
                    }
                } else if ("thumbnail".equals(tag)) {
                    k.thumburl = p.getAttributeValue(null, "url");
                }
            }
        }
        return liste;
    }

    public class KlipAdapter extends BaseAdapter {

        public int getCount() {
            return videoklip.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.listeelement, null);
            }
            TextView listeelem_overskrift = (TextView) view.findViewById(R.id.listeelem_overskrift);
            TextView listeelem_beskrivelse = (TextView) view.findViewById(R.id.listeelem_beskrivelse);
            ImageView listeelem_billede = (ImageView) view.findViewById(R.id.listeelem_billede);
            Klip k = videoklip.get(position);
            listeelem_overskrift.setText(k.titel);
            listeelem_beskrivelse.setText(k.egenskaber.get("content"));
            listeelem_billede.setImageBitmap(k.thumb);
            return view;
        }
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        Klip k = videoklip.get(position);
        Intent i = new Intent(this, BenytVideoView.class);
        i.putExtra("titel", k.titel);
        i.putExtra("beskrivelse", k.egenskaber.get("content"));
        i.putExtra("videourl", k.videourl);
        i.putExtra("link", k.link);
        startActivity(i);
    }
}

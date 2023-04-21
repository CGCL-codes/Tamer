package com.groovytagger.database.lyricsfly.engine;

import com.groovytagger.database.lyricsfly.bean.LyricsFlyBean;
import com.groovytagger.interfaces.CdDataBeanInterface;
import com.groovytagger.interfaces.CdDataEngineInterface;
import com.groovytagger.utils.LogManager;
import com.groovytagger.utils.StaticObj;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Properties;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class LyricsFly implements CdDataEngineInterface {

    private static String flyApi = "55544119684-magic-tagger.com";

    private static String flyUrl = "http://lyricsfly.com/api/api.php";

    private String engineName = "";

    public LyricsFly(String name) {
        this.engineName = name;
    }

    public String formatQuery(String s) {
        try {
            s = URLEncoder.encode(s.trim(), "UTF-8");
        } catch (Exception e) {
            LogManager.getInstance().getLogger().error(e);
            if (StaticObj.DEBUG) e.printStackTrace();
        }
        return s;
    }

    private static String parseLyric(Document doc) {
        Element root;
        root = doc.getRootElement();
        Iterator sg = root.getChildren().iterator();
        while (sg.hasNext()) {
            Element nodes = (Element) sg.next();
            Iterator nodeIter = nodes.getChildren().iterator();
            while (nodeIter.hasNext()) {
                Element node = (Element) nodeIter.next();
                if (node.getName().equalsIgnoreCase("tx")) {
                    String lyricContent = node.getValue();
                    if (lyricContent != null && !lyricContent.equalsIgnoreCase("")) {
                        String patternReg = "\\[br\\]*";
                        lyricContent = lyricContent.replaceAll(patternReg, "\n");
                        return lyricContent;
                    }
                }
            }
        }
        return null;
    }

    private static Document getXml(String urlString) {
        Document doc = null;
        HttpURLConnection uc = null;
        try {
            URL url = new URL(urlString);
            uc = (HttpURLConnection) url.openConnection();
            if (StaticObj.PROXY_ENABLED) {
                Properties systemSettings = System.getProperties();
                systemSettings.put("http.proxyHost", StaticObj.PROXY_URL);
                systemSettings.put("http.proxyPort", StaticObj.PROXY_PORT);
                System.setProperties(systemSettings);
                sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
                String encoded = new String(encoder.encode(new String(StaticObj.PROXY_USERNAME + ":" + StaticObj.PROXY_PASSWORD).getBytes()));
                uc.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
            }
            BufferedReader ir = null;
            if (uc.getInputStream() != null) {
                ir = new BufferedReader(new InputStreamReader(uc.getInputStream(), "ISO8859_1"));
                SAXBuilder builder = new SAXBuilder();
                doc = builder.build(ir);
            }
        } catch (IOException io) {
            LogManager.getInstance().getLogger().error(io);
            if (StaticObj.DEBUG) io.printStackTrace();
        } catch (Exception e) {
            LogManager.getInstance().getLogger().error(e);
            if (StaticObj.DEBUG) e.printStackTrace();
            System.out.println("No Data found!");
        } finally {
            try {
                uc.disconnect();
            } catch (Exception ex) {
            }
            return doc;
        }
    }

    public static void main(String[] args) {
        LyricsFly lf = new LyricsFly("dd");
        try {
            CdDataBeanInterface d = lf.searchByArtist("Rammstein", "", "Du Riechst So Gut");
            System.out.println(d.getTags("", "").getLyric());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CdDataBeanInterface searchByArtist(String artist, String album, String song) {
        CdDataBeanInterface bean = null;
        try {
            String urlino = "";
            urlino = flyUrl + "?i=" + flyApi;
            urlino = urlino + "&a=" + formatQuery(artist);
            urlino = urlino + "&t=" + formatQuery(song);
            Document doc = getXml(urlino);
            bean = new LyricsFlyBean(parseLyric(doc));
        } catch (Exception e) {
            LogManager.getInstance().getLogger().error(e);
            if (StaticObj.DEBUG) e.printStackTrace();
        }
        return bean;
    }

    public void setEngineName(String name) {
        this.engineName = name;
    }

    public String getEngineName() {
        return engineName;
    }
}

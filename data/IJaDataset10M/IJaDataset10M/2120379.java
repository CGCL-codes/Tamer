package com.saret.crawler.search;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.saret.crawler.type.GUrl;
import com.saret.crawler.type.GUrlImpl;
import com.saret.utils.FileLocator;
import com.saret.utils.UtfFileHandle;
import org.json.JSONException;
import org.json.JSONObject;
import sun.awt.windows.ThemeReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * @author biniam.gebremichael
 * @since 1/18/12
 */
@Singleton
public class SmartWebSearch implements WebTranslate {

    WebTranslate webSearch;

    private static final int maxAllowedByBing = 50;

    private static File urlArchive = new File(FileLocator.getConfigFile("crawler"), "urlArchive.json");

    private static final Logger logger = Logger.getLogger(SmartWebSearch.class.getName());

    ConcurrentMap<String, List<GUrl>> cache = new ConcurrentHashMap<String, List<GUrl>>();

    @Inject
    public SmartWebSearch(WebTranslate webSearch) {
        this.webSearch = webSearch;
        initCache();
    }

    private void initCache() {
        for (String s : UtfFileHandle.readFileToListOfLines(urlArchive)) {
            try {
                GUrl g = new GUrlImpl(new JSONObject(s));
                cache.putIfAbsent(g.getKeyWord(), new ArrayList<GUrl>());
                cache.get(g.getKeyWord()).add(g);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<GUrl> search(String searchKey) {
        List<GUrl> urls = cache.get(searchKey);
        if (urls == null) {
            urls = webSearch.search(searchKey);
            cache.put(searchKey, urls);
            List<String> line = new ArrayList<String>();
            for (List<GUrl> gUrlList : cache.values()) {
                for (GUrl gUrl : gUrlList) {
                    line.add(gUrl.toJson());
                }
            }
            UtfFileHandle.write(urlArchive, line.toArray());
        }
        return urls;
    }

    @Override
    public String translate(String from, String to, String text) {
        return webSearch.translate(from, to, text);
    }

    @Override
    public List<String> translate(List<String> text) {
        List<String> r = new ArrayList<String>();
        for (int i = 0; i < text.size(); i = i + maxAllowedByBing) {
            int max = i + maxAllowedByBing - 1 > text.size() ? text.size() - 1 : i + maxAllowedByBing - 1;
            logger.info("requesting for the next [" + i + "," + max + "] batch");
            try {
                r.addAll(webSearch.translate(text.subList(i, max)));
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                return r;
            }
        }
        return r;
    }
}

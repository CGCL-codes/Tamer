package net.sipvip.SevCommon;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.json.JSONArray;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.StrictErrorHandler;
import com.sun.syndication.feed.synd.SyndEntry;

public class RssReader {

    private static final Logger log = Logger.getLogger(RssReader.class.getName());

    public static JSONArray RssReader(String rssurlvar) {
        MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
        ms.setErrorHandler(new StrictErrorHandler());
        JSONArray outlist = new org.json.JSONArray();
        try {
            ms.get("key");
            List<SyndEntry> entries = (List<SyndEntry>) ms.get(rssurlvar);
            if (entries != null) {
                for (int i = 0; i < entries.size(); i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("title", entries.get(i).getTitle());
                    map.put("description", entries.get(i).getDescription().getValue());
                    outlist.put(map);
                }
            } else {
                log.severe("memcache EMPTY!!");
            }
        } catch (com.google.appengine.api.memcache.MemcacheServiceException e) {
            log.severe("memcache don't work!!! " + e.getMessage());
        }
        return outlist;
    }
}

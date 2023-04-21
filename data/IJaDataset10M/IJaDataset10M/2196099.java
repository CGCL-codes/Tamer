package biz.taoconsulting;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.collections.map.HashedMap;

class AssetCache {

    LRUMap cacheMap;

    HashedMap accessKeyMap;

    int maxCacheSize = 500;

    public AssetCache(int newCacheSize) {
        this.maxCacheSize = newCacheSize;
        this.flushCache();
    }

    public AssetCache() {
        this.flushCache();
    }

    /**
	 * Adds a file to the cache, uses multipe keys
	 * 
	 * @author stw
	 * @param cacheKeys
	 * @param fileContent
	 * @param mimeType
	 * @param fileLocation
	 */
    public synchronized void addToCache(HashedMap cacheKeys, byte[] fileContent, String mimeType, String fileLocation) {
        AssetItem asset = new AssetItem(fileLocation, mimeType, fileContent);
        MapIterator it = cacheKeys.mapIterator();
        while (it.hasNext()) {
            this.accessKeyMap.put(it.next().toString(), fileLocation);
        }
        this.cacheMap.put(fileLocation, asset);
    }

    public byte[] getFromCache(String cacheKey) {
        if (this.cacheMap.containsKey(cacheKey)) {
            AssetItem asset = (AssetItem) this.cacheMap.get(cacheKey);
            return (asset.getFileContent());
        } else return null;
    }

    public boolean isInCache(String cacheKey) {
        String trueKey = (String) this.accessKeyMap.get(cacheKey);
        if (trueKey == null) return false;
        if (this.cacheMap.containsKey(trueKey)) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized void flushCache() {
        this.cacheMap = new LRUMap(this.maxCacheSize);
        this.accessKeyMap = new HashedMap();
    }

    private void showHTMLContent(ServletOutputStream out) throws IOException {
        MapIterator it = this.cacheMap.mapIterator();
        out.println("<table width=\"100%\" style=\"font-family : Verdana, Arial, sans-serif\">");
        out.println("<tr><th>File</th><th>Size</th><th>last request keys</th></tr>");
        while (it.hasNext()) {
            String key = it.next().toString();
            AssetItem ae = (AssetItem) it.getValue();
            out.println("<tr><td colspan=\"3\" class=\"filepath\">");
            MapIterator mit = this.accessKeyMap.mapIterator();
            boolean first = true;
            while (mit.hasNext()) {
                String curKey = (String) mit.next();
                String curVal = (String) mit.getValue();
                if (ae.getKey().equals(curVal)) {
                    if (first) {
                        first = false;
                    } else {
                        out.println("<br />");
                    }
                    out.println(curKey);
                }
            }
            out.println("</td></tr>");
            out.println("<tr><td>");
            out.println(ae.getKey());
            out.println("</td><td>");
            out.println(ae.getFileContent(false).length);
            out.println("</td><td>");
            out.print(ae.getLastRestquested().toString());
            out.println("</td></tr>");
        }
        out.println("</table>");
    }

    /**
	 * @param key
	 * @return
	 */
    public String getContentType(String key) {
        if (this.cacheMap.containsKey(key)) {
            AssetItem ai = (AssetItem) this.cacheMap.get(key);
            return ai.getMimeType();
        } else {
            return "unknown";
        }
    }

    /**
	 * @param out
	 * @throws IOException
	 */
    public void showConfigurationInfo(ServletOutputStream out) throws IOException {
        out.println("<li>");
        out.println("Max Objects in cache : ");
        out.println(this.maxCacheSize);
        out.println("</li>");
        out.println("<li>");
        out.println("Current Objects in cache : ");
        out.println(this.cacheMap.size());
        out.println("</li>");
        out.println("</ul>");
        this.showHTMLContent(out);
    }
}

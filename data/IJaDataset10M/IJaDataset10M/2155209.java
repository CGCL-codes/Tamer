package web.sopo.asset;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import web.sopo.util.FileUtils;
import web.sopo.util.Log;
import web.sopo.util.WebUtils;

public class AssetHandle {

    private Map<String, byte[]> cache;

    public void destroy() {
        cache.clear();
        cache = null;
    }

    private Map<String, byte[]> getCache() {
        if (null == cache) {
            cache = new HashMap<String, byte[]>();
        }
        return cache;
    }

    public boolean handle(HttpServletRequest request, HttpServletResponse response) {
        String url = WebUtils.getServletPath(request);
        if (!url.startsWith("/" + Asset.ASSET)) {
            return false;
        }
        if (url.endsWith(".class") || url.endsWith(".java") || url.endsWith(".properties") || url.endsWith(".xml")) {
            Log.warn("an attack?request for resource :" + url);
            return false;
        }
        Map<String, byte[]> cache = getCache();
        url = url.substring(Asset.ASSET.length() + 1);
        try {
            byte[] bs = null;
            if (cache.containsKey(url)) {
                bs = cache.get(url);
            } else {
                bs = FileUtils.readPackageByteResource(url);
                cache.put(url, bs);
            }
            ServletOutputStream out = response.getOutputStream();
            out.write(bs);
            out.close();
        } catch (Exception e) {
            Throwable exs = e.getCause();
            if (exs instanceof SocketException) {
                Log.info("ignore:handle resource socket exception.url = %s,ex = %s", url, exs.getMessage());
                return true;
            }
            Log.error(e, "handle resource error.url = %s", url);
        }
        return true;
    }
}

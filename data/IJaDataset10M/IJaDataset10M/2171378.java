package net.livebookstore.web.filter;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Define pages to be cached. Using following syntax:<br/>
 * <code>url=param1, param2 defaultValue, param3, ...</code><br/>
 * For example, for url /listBooks.jsp?id=xxx&page=xxx, rule is:<br/>
 * <code>/listBooks.jsp=id,page 1</code><br/>
 * Each line for one rule.
 * 
 * @author Xuefeng
 */
public class CacheDefinition {

    private CacheEntry[] entries;

    public CacheDefinition(CacheEntry[] entries) {
        this.entries = entries;
    }

    public String getKey(HttpServletRequest request) {
        for (CacheEntry entry : entries) {
            String s = entry.buildKey(request);
            if (s != null) return s;
        }
        return null;
    }
}

class CacheEntry {

    private String url;

    private Map<String, String> parameters = new HashMap<String, String>();

    private int parametersCount;

    private Log log = LogFactory.getLog(getClass());

    /**
     * Config string, e.g.:
     * /user/test.jspx=id,name,page "1"
     * @param config
     */
    public CacheEntry(String config) {
        log.info("Parse cache config: " + config);
        String[] ss = config.trim().split("\\=");
        if (ss.length != 2) throw new IllegalArgumentException("Illegal config: " + config);
        url = ss[0].trim();
        if (url.equals("")) throw new IllegalArgumentException("Illegal config: " + config);
        String[] ps = ss[1].split("\\,");
        for (String p : ps) addParameter(p);
        parametersCount = parameters.size();
    }

    private void addParameter(String p) {
        p = p.trim();
        String[] ss = p.split("[ ]+", 2);
        String param = ss[0];
        String value = null;
        if (ss.length == 2) {
            value = ss[1].trim();
            if (value.matches("\\\".*\\\"")) value = value.substring(1, value.length() - 1);
        }
        parameters.put(param, value);
    }

    public String buildKey(HttpServletRequest request) {
        if (!url.equals(request.getRequestURI())) return null;
        if (parametersCount == 0) return url;
        StringBuffer sb = new StringBuffer(256);
        sb.append(this.url).append('?');
        for (String p : parameters.keySet()) {
            String value = request.getParameter(p);
            if (value != null) sb.append(p).append('=').append(value).append('&'); else {
                String dv = parameters.get(p);
                if (dv == null) return null;
                sb.append(p).append('=').append(dv).append('&');
            }
        }
        return sb.toString();
    }
}

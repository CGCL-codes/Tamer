package ru.adv.mozart.servlet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author vic
 * взято из apache tomcat
 */
public class HttpDateFormat {

    /** 
     * HTTP date format. 
     */
    protected static final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /** 
     * Formatter cache. 
     */
    protected static final HashMap formatCache = new HashMap();

    private HttpDateFormat() {
    }

    /** 
     * Get the HTTP format of the specified date. 
     */
    public static final String formatDate(long value) {
        String cachedDate = null;
        Long longValue = new Long(value);
        try {
            cachedDate = (String) formatCache.get(longValue);
        } catch (Exception e) {
        }
        if (cachedDate != null) return cachedDate;
        String newDate = null;
        Date dateValue = new Date(value);
        synchronized (formatCache) {
            newDate = format.format(dateValue);
            updateCache(formatCache, longValue, newDate);
        }
        return newDate;
    }

    /** 
     * Update cache. 
     */
    private static final void updateCache(HashMap cache, Object key, Object value) {
        if (value == null) {
            return;
        }
        if (cache.size() > 1000) {
            cache.clear();
        }
        cache.put(key, value);
    }
}

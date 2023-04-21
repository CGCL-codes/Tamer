package net.jwpa.dao.cache;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import net.jwpa.config.LogUtil;
import net.jwpa.dao.Cacheable;
import net.jwpa.dao.IntegerProvider;
import net.jwpa.dao.StringArrayProvider;
import net.jwpa.dao.StringProvider;
import net.jwpa.model.PropertyHolder;

public abstract class CachedPropertyHolder implements Cacheable, PropertyHolder {

    private static final Logger logger = LogUtil.getLogger();

    public static final long SECOND = 1000l;

    public static final long MINUTE = 60000l;

    public static final long HOUR = 3600000l;

    public static final long DAY = 86400000l;

    public static final long MONTH = DAY * 30;

    public static final long YEAR = DAY * 365;

    public final Object LOCK = new Object();

    public abstract long getLastModified();

    public abstract String getName();

    private Map<String, Object> cached_data = new HashMap<String, Object>();

    public void propertyChanged() {
        try {
            CacheUtils.setModified(this);
        } catch (IOException e) {
            LogUtil.logError(logger, e);
        }
    }

    public void gettingOld(long birth) {
        if (getLastModified() > birth) {
            synchronized (LOCK) {
                resetCachedData();
            }
        }
    }

    protected void resetCachedData() {
        cached_data.clear();
    }

    private Object getCachedData(String key) {
        return cached_data.get(key);
    }

    public void resetCachedValues() throws IOException {
        synchronized (LOCK) {
            resetCachedData();
            getProperties().setProperty("cachedRefresh", String.valueOf(System.currentTimeMillis()));
        }
    }

    public int getCachedIntProperty(String name, long life, final String method) throws IOException {
        synchronized (LOCK) {
            final CachedPropertyHolder cl = this;
            Integer res = (Integer) getCachedData(name);
            if (res != null) {
                LogUtil.logDebug(logger, "FROM CACHE IN MEMORY " + getName());
                if (PropertyHolderUtils.DEBUG) System.out.print(".");
                return res;
            }
            res = PropertyHolderUtils.getCachedIntValue(this, name, new IntegerProvider() {

                public int get() throws IOException {
                    try {
                        LogUtil.logDebug(logger, "COMPUTE " + cl.getName());
                        if (PropertyHolderUtils.DEBUG) System.out.print("#");
                        return (Integer) cl.getClass().getMethod(method, new Class[0]).invoke(cl, new Object[0]);
                    } catch (Exception e) {
                        LogUtil.logError(logger, e);
                    }
                    return -1;
                }
            }, life);
            cached_data.put(name, res);
            return res;
        }
    }

    public String getCachedStringProperty(String name, long life, final String method) throws IOException {
        synchronized (LOCK) {
            final CachedPropertyHolder cl = this;
            String res = (String) getCachedData(name);
            if (res != null) {
                LogUtil.logDebug(logger, "FROM CACHE IN MEMORY " + getName());
                if (PropertyHolderUtils.DEBUG) System.out.print(".");
                return res;
            }
            res = PropertyHolderUtils.getCachedStringValue(this, name, new StringProvider() {

                public String get() throws IOException {
                    try {
                        LogUtil.logDebug(logger, "COMPUTE " + cl.getName());
                        if (PropertyHolderUtils.DEBUG) System.out.print("#");
                        return (String) cl.getClass().getMethod(method, new Class[0]).invoke(cl, new Object[0]);
                    } catch (Exception e) {
                        LogUtil.logError(logger, e);
                    }
                    return "";
                }
            }, life);
            cached_data.put(name, res);
            return res;
        }
    }

    public String[] getCachedSAProperty(String name, long life, final String method) throws IOException {
        synchronized (LOCK) {
            final CachedPropertyHolder cl = this;
            String[] res = (String[]) getCachedData(name);
            if (res != null) {
                LogUtil.logDebug(logger, "FROM CACHE IN MEMORY " + getName());
                if (PropertyHolderUtils.DEBUG) System.out.print(".");
                return res;
            }
            res = PropertyHolderUtils.getCachedSAValue(this, name, new StringArrayProvider() {

                public String[] get() throws IOException {
                    try {
                        LogUtil.logDebug(logger, "COMPUTE " + cl.getName());
                        if (PropertyHolderUtils.DEBUG) System.out.print("#");
                        return (String[]) cl.getClass().getMethod(method, new Class[0]).invoke(cl, new Object[0]);
                    } catch (Exception e) {
                        LogUtil.logError(logger, e);
                    }
                    return new String[0];
                }
            }, life);
            cached_data.put(name, res);
            return res;
        }
    }

    public Object getMemoryCachedObjectProperty(String name, long life, final String method) throws IOException {
        synchronized (LOCK) {
            CachePropertyEntry res = (CachePropertyEntry) getCachedData(name);
            if (res != null && !res.expired()) {
                LogUtil.logDebug(logger, "FROM CACHE IN MEMORY " + name + "." + getName());
                if (PropertyHolderUtils.DEBUG) System.out.print(".");
                Object r = res.getValue();
                if (r != null) return r;
            }
            try {
                LogUtil.logDebug(logger, "COMPUTE " + name + "." + getName());
                if (PropertyHolderUtils.DEBUG) System.out.print("/");
                Object r = this.getClass().getMethod(method, new Class[0]).invoke(this, new Object[0]);
                res = new CachePropertyEntry(r, life);
                cached_data.put(name, res);
                return r;
            } catch (Exception e) {
                LogUtil.logError(logger, e);
                return null;
            }
        }
    }
}

class CachePropertyEntry {

    public long expires;

    public long birth;

    private WeakReference<Object> value;

    public CachePropertyEntry(Object v, long life) {
        value = new WeakReference<Object>(v);
        birth = System.currentTimeMillis();
        expires = birth + life;
    }

    public boolean expired() {
        return System.currentTimeMillis() > expires;
    }

    public Object getValue() {
        return value.get();
    }
}

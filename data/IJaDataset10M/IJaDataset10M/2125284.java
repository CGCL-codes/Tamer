package com.road.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 * A RAM verison cache implentation
 * @author huan
 *
 */
public class RamCache implements Cache {

    static Logger LOGGER = Logger.getLogger(RamCache.class);

    Map<String, Object> dateMap = null;

    SortedMap<Date, String> expiredMap = null;

    boolean exit = false;

    static long SleepInterval = 1000;

    public RamCache() {
        dateMap = new HashMap<String, Object>();
        expiredMap = new TreeMap<Date, String>();
        new Thread(new Runnable() {

            public void run() {
                while (!exit) {
                    synchronized (expiredMap) {
                        Date now = new Date();
                        SortedMap<Date, String> expiredItems = expiredMap.headMap(now);
                        boolean bchanged = false;
                        for (Map.Entry<Date, String> e : expiredItems.entrySet()) {
                            dateMap.remove(e.getValue());
                            LOGGER.info("timout - " + e.getKey());
                            bchanged = true;
                        }
                        if (bchanged) expiredMap = expiredMap.tailMap(now);
                    }
                    try {
                        Thread.sleep(SleepInterval);
                    } catch (InterruptedException e) {
                        LOGGER.error(e);
                    }
                }
            }
        }).start();
    }

    public synchronized Object get(String key) throws CacheExeption {
        return dateMap.get(key);
    }

    public synchronized void set(String key, Object value) throws CacheExeption {
        dateMap.put(key, value);
    }

    public synchronized void set(String key, Object val, Date expiredtime) throws CacheExeption {
        dateMap.put(key, val);
        expiredMap.put(expiredtime, key);
    }

    public void close() {
        exit = true;
    }
}

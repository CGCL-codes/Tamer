package com.sts.webmeet.common;

import java.util.*;

public class RandomHashCache {

    private Hashtable hashItems;

    private int iMaxItems;

    private Random random = new Random();

    public RandomHashCache(int iMaxItems) {
        this.iMaxItems = iMaxItems;
        hashItems = new Hashtable(iMaxItems + 1);
    }

    public RandomHashCache() {
        this(20);
    }

    public Object getObject(Object objKey) {
        return hashItems.get(objKey);
    }

    public void putObject(Object objKey, Object obj) {
        synchronized (hashItems) {
            if (hashItems.size() >= iMaxItems) {
                Enumeration enumer = hashItems.keys();
                int iVictim = (Math.abs(random.nextInt())) % hashItems.size();
                for (int i = 0; i < iVictim; i++) {
                    enumer.nextElement();
                }
                Object objVictim = enumer.nextElement();
                System.out.println(getClass().getName() + " removing " + objVictim + " [" + iVictim + "]");
                hashItems.remove(objVictim);
            }
            hashItems.put(objKey, obj);
        }
    }
}

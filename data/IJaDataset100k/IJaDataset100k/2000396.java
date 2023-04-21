package com.ynhenc.comm.registry;

import java.io.*;
import java.util.*;
import java.util.prefs.*;
import com.ynhenc.comm.*;

public class WindowRegistryUtil extends GisComLib {

    public final void setWriteWindowRegistry(String key, Object value) {
        this.getPrefrences().put(key, "" + value);
    }

    public final void writeWindowRegistry(String nodeKey, Object... valueList) throws Exception {
        Preferences prefs = this.getPrefrences();
        Preferences prefsNode = prefs.node(nodeKey);
        long time_MiliSec = System.currentTimeMillis();
        for (Object value : valueList) {
            prefsNode.putLong("" + value, time_MiliSec);
        }
        prefsNode.flush();
    }

    public final ArrayListRegiItem getWindowRegistryValues(String nodeKey) {
        Preferences prefs = this.getPrefrences();
        Preferences prefsNode = prefs.node(nodeKey);
        ArrayListRegiItem regiItems = new ArrayListRegiItem();
        try {
            String[] vals = prefsNode.keys();
            RegiItem regiItem;
            for (String val : vals) {
                regiItem = new RegiItem();
                regiItem.setValue(val);
                regiItems.add(regiItem);
            }
        } catch (Exception e) {
            this.debug(e);
        }
        long def = System.currentTimeMillis();
        for (RegiItem regiItem : regiItems) {
            long key = prefsNode.getLong(regiItem.getValue(), def);
            regiItem.setKey(key);
        }
        Collections.sort(regiItems);
        return regiItems;
    }

    private Preferences getPrefrences() {
        if (this.prefs == null) {
            this.prefs = Preferences.userNodeForPackage(this.getClass());
        }
        return this.prefs;
    }

    private Preferences prefs;

    public static WindowRegistryUtil getWindowRegistryUtil() {
        if (windowRegistryUtil == null) {
            windowRegistryUtil = new WindowRegistryUtil();
        }
        return windowRegistryUtil;
    }

    public void main(String[] args) throws Exception {
        WindowRegistryUtil windowRegistryUtil = getWindowRegistryUtil();
        Object objList[] = { "def", "ghi", "jkl" };
        windowRegistryUtil.writeWindowRegistry("abcd", objList);
        ArrayListRegiItem regiItems = windowRegistryUtil.getWindowRegistryValues("abcd");
        for (RegiItem regiItem : regiItems) {
            System.out.println(regiItem.getValue());
        }
        System.out.println("Good bye!");
    }

    public static WindowRegistryUtil windowRegistryUtil;
}

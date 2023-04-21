package org.apache.ws.security;

import java.util.Hashtable;

public class WSDocInfoStore {

    static Hashtable storage = new Hashtable(10);

    public static WSDocInfo lookup(int hash) {
        Integer intObj = new Integer(hash);
        return (WSDocInfo) storage.get(intObj);
    }

    public static void store(WSDocInfo info) {
        Integer intObj = new Integer(info.getHash());
        if (storage.containsKey(intObj)) {
            return;
        }
        storage.put(intObj, info);
    }

    public static void delete(int hash) {
        Integer intObj = new Integer(hash);
        WSDocInfo wsInfo = (WSDocInfo) storage.get(intObj);
        if (wsInfo != null) {
            storage.remove(intObj);
        }
    }

    public static void delete(WSDocInfo info) {
        delete(info.getHash());
    }
}

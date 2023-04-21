package org.gnomus.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class link extends object {

    public link(Entity e) {
        setSourceKind((String) e.getProperty("source_kind"));
        setSourceKey((Key) e.getProperty("source_key"));
        setTargetKind((String) e.getProperty("target_kind"));
        setTargetKey((Key) e.getProperty("target_key"));
    }

    public link(String source_kind, Key source_key, String target_kind, Key target_key) {
        super("link");
        setSourceKind(source_kind);
        setSourceKey(source_key);
        setTargetKind(target_kind);
        setTargetKey(target_key);
    }

    public static List<link> fetch(String source_kind, Key source_key, String target_kind, Key target_key) {
        Map<String, Object> filters = new HashMap<String, Object>();
        if (source_kind == null || target_kind == null) {
            throw new Error("source_kind and target_kind must be specified.");
        }
        filters.put("source_kind", source_kind);
        filters.put("target_kind", target_kind);
        if (source_key != null) {
            filters.put("source_key", source_key);
        }
        if (target_key != null) {
            filters.put("target_key", target_key);
        }
        List<link> links = new ArrayList<link>();
        for (Entity e : store.get("link", filters)) {
            links.add(new link(e));
        }
        return links;
    }

    public static void delete(Key key) {
        String[] sa = { "source_key", "target_key" };
        Transaction txn = store.beginTransaction();
        try {
            for (String s : sa) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put(s, key);
                for (Entity e : store.get("link", m)) {
                    store.delete(e);
                }
            }
            txn.commit();
        } finally {
            if (txn.isActive()) {
                txn.rollback();
            }
        }
    }

    public Entity getSource() {
        return store.get((Key) get("source_key"));
    }

    public Entity getTarget() {
        return store.get((Key) get("target_key"));
    }

    public void setSourceKind(String source_kind) {
        put("source_kind", source_kind);
    }

    public String getSourceKind() {
        return (String) get("source_kind");
    }

    public void setSourceKey(Key source_key) {
        put("source_key", source_key);
    }

    public Key getSourceKey() {
        return (Key) get("source_key");
    }

    public void setTargetKind(String target_kind) {
        put("target_kind", target_kind);
    }

    public String getTargetKind() {
        return (String) get("target_kind");
    }

    public void setTargetKey(Key target_key) {
        put("target_key", target_key);
    }

    public Key getTargetKey() {
        return (Key) get("target_key");
    }
}

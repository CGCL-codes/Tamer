package jws.services.common;

import java.io.Serializable;

public final class PersisterRecord {

    private Serializable _obj;

    private String _lock_key = null;

    private long _timestamp;

    public PersisterRecord(Serializable obj) {
        _obj = obj;
        _timestamp = System.currentTimeMillis();
    }

    public PersisterRecord(Serializable obj, long ts) {
        _obj = obj;
        _timestamp = ts;
    }

    public Serializable getObject() {
        return _obj;
    }

    public boolean isLocked() {
        return _lock_key != null;
    }

    public String getLockKey() {
        return _lock_key;
    }

    public void lock(String key) {
        _lock_key = key;
        _timestamp = System.currentTimeMillis();
    }

    public void unlock() {
        _lock_key = null;
        _timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return _timestamp;
    }
}

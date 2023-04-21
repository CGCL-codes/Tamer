package com.incendiaryblue.storage.test;

import com.incendiaryblue.storage.BusinessObject;
import com.incendiaryblue.storage.StorageImpl;
import java.util.*;

/**
 * An implementation of StorageImpl used to test the StorageManager class.
 */
public class DummyStorageImpl extends StorageImpl {

    /** Serial number user for allocating primary keys. */
    int serial = 0;

    /** Used to store the objects. */
    Map<Object, String> objects = new HashMap<Object, String>();

    public BusinessObject getObject(Object primaryKey) {
        Integer pk = (Integer) primaryKey;
        String data = objects.get(pk);
        if (data == null) return null;
        return new DummyBusinessObject(pk, data);
    }

    public Object getPrimaryKey(String keyName, Object keyValue) {
        throw new IllegalArgumentException();
    }

    public List getPrimaryKeyList(String keyName, Object keyValue) {
        throw new IllegalArgumentException();
    }

    public void store(BusinessObject o) {
        DummyBusinessObject bo = (DummyBusinessObject) o;
        if (bo.getPrimaryKey() == null) {
            Integer pk = ++serial;
            bo.setPrimaryKey(pk);
        }
        objects.put(bo.getPrimaryKey(), bo.getData());
    }

    public void delete(BusinessObject o) {
        objects.remove(o.getPrimaryKey());
    }
}

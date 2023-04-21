package edu.uiuc.ncsa.security.storage.sql.internals;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Helper class with a bunch of built in casts. It contains key value pairs,
 * where the keys are SQL column names and the values are values. This
 * practically contains the information for a single row in a database.
 * <p/>
 * <p>Note that the keys are all column names AND that
 * the keys are automatically converted to/from lower case. This is because this object may have its
 * values given from a result set metadata object and most vendors allow only case-insensitive column names,
 * so they may or may not elect to convert them to lower or upper case as they see fit.
 * </>
 * <p>Created by Jeff Gaynor<br>
 * on 8/31/11 at  4:05 PM
 */
public class ColumnMap extends HashMap<String, Object> {

    @Override
    public Object put(String key, Object value) {
        return super.put(key.toLowerCase(), value);
    }

    @Override
    public Object get(Object key) {
        return super.get(key.toString().toLowerCase());
    }

    public String getString(String key) {
        return (String) get(key);
    }

    public long getLong(String key) {
        return (Long) get(key);
    }

    public Boolean getBoolean(String key) {
        return (Boolean) get(key);
    }

    public byte[] getBytes(String key) {
        Object o = get(key.toLowerCase());
        if (o instanceof String) {
            if (o == null || ((String) o).length() == 0) {
                return new byte[] {};
            }
        }
        return (byte[]) o;
    }

    public Timestamp getTimestamp(String key) {
        return (Timestamp) get(key);
    }
}

package org.goobs.internet;

import java.util.HashMap;
import java.util.Map;
import org.goobs.database.DatabaseRow;

public class Cookie implements DatabaseRow {

    private String name, value, host, path;

    private long expiry, lastAccessed;

    private boolean isSecure, isHttpOnly;

    public Cookie(String host, String name, String value) {
        this.host = host;
        this.name = name;
        this.value = value;
    }

    public Cookie() {
    }

    @Override
    public Map<String, Object> export() {
        Map<String, Object> rtn = new HashMap<String, Object>();
        rtn.put("name", name);
        rtn.put("value", value);
        rtn.put("host", host);
        rtn.put("path", path);
        rtn.put("expiry", expiry);
        rtn.put("lastAccessed", lastAccessed);
        rtn.put("isSecure", isSecure);
        rtn.put("isHttpOnly", isHttpOnly);
        return rtn;
    }

    @Override
    public void load(Map<String, Object> row) {
        name = row.get("name").toString();
        value = row.get("value").toString();
        host = row.get("host").toString();
        path = row.get("path").toString();
        expiry = Long.parseLong(row.get("expiry").toString());
        lastAccessed = Long.parseLong(row.get("lastAccessed").toString());
        isSecure = Boolean.parseBoolean(row.get("isSecure").toString());
        isHttpOnly = Boolean.parseBoolean(row.get("isHttpOnly").toString());
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getHost() {
        return host;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return name + "=" + value;
    }

    public boolean equals(Object o) {
        if (o instanceof Cookie) {
            Cookie other = (Cookie) o;
            return name.equalsIgnoreCase(other.name) && value.equals(other.value) && host.equals(other.host);
        } else {
            return false;
        }
    }
}

package org.silicolife.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class SLDatabaseConnectionRepository {

    private HashMap<String, SLDatabaseConnection> databaseConnectionMap = new HashMap<String, SLDatabaseConnection>();

    public void put(SLDatabaseConnection databaseConnection) {
        databaseConnectionMap.put(databaseConnection.getKey(), databaseConnection);
    }

    public SLDatabaseConnection get(String key) {
        SLDatabaseConnection connection = databaseConnectionMap.get(key);
        if (connection == null) {
        }
        return connection;
    }

    public void del(String key) {
        databaseConnectionMap.remove(key);
    }

    public int size() {
        return databaseConnectionMap.size();
    }

    public Set<String> keySet() {
        return databaseConnectionMap.keySet();
    }

    public Collection<SLDatabaseConnection> values() {
        return databaseConnectionMap.values();
    }
}

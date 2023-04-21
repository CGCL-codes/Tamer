package org.dbwiki.data.index;

import java.util.Vector;

public class VectorDatabaseListing implements DatabaseContent {

    private Vector<DatabaseEntry> _entries;

    public VectorDatabaseListing() {
        _entries = new Vector<DatabaseEntry>();
    }

    public void add(DatabaseEntry entry) {
        _entries.add(entry);
    }

    public DatabaseEntry get(int index) {
        return _entries.get(index);
    }

    public DatabaseEntry get(String key) {
        for (DatabaseEntry entry : _entries) {
            if (entry.label().equals(key)) {
                return entry;
            }
        }
        return null;
    }

    public int size() {
        return _entries.size();
    }
}

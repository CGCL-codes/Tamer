package com.dbxml.db.core.adapter;

import com.dbxml.db.core.Collection;
import com.dbxml.db.core.DBException;
import com.dbxml.db.core.adapter.Adapter;
import com.dbxml.db.core.data.Key;
import com.dbxml.db.core.query.ResultSet;
import com.dbxml.db.core.transaction.Transaction;
import com.dbxml.xml.NamespaceMap;

/**
 * SimpleAdapter is a base class for implementing custom Adapters.
 */
public class SimpleAdapter implements Adapter {

    protected Collection col;

    public SimpleAdapter(Collection col) {
        this.col = col;
    }

    public Collection getCollection() {
        return col;
    }

    public String getName() {
        return col.getName();
    }

    public String getCanonicalName() {
        return col.getCanonicalName();
    }

    public long getKeyCount(Transaction tx) throws DBException {
        return col.getKeyCount(tx);
    }

    public void remove(Transaction tx, Object key) throws DBException {
        col.remove(tx, key);
    }

    public Key[] listKeys(Transaction tx) throws DBException {
        return col.listKeys(tx);
    }

    public ResultSet queryCollection(Transaction tx, String style, String query, NamespaceMap nsMap) throws DBException {
        return col.queryCollection(tx, style, query, nsMap);
    }

    public ResultSet queryDocument(Transaction tx, String style, String query, NamespaceMap nsMap, Object key) throws DBException {
        return col.queryDocument(tx, style, query, nsMap, key);
    }
}

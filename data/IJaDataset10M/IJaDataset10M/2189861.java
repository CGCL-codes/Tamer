package org.dbwiki.data.query.handler;

import java.util.Hashtable;
import java.util.Iterator;
import org.dbwiki.data.database.DatabaseElementNode;

public class QueryNodeSet {

    private Hashtable<String, DatabaseElementNode> _nodes;

    public QueryNodeSet() {
        _nodes = new Hashtable<String, DatabaseElementNode>();
    }

    public QueryNodeSet(String name, DatabaseElementNode node) {
        this();
        this.add(name, node);
    }

    public void add(String name, DatabaseElementNode node) {
        _nodes.put(name, node);
    }

    public DatabaseElementNode get(String name) {
        return _nodes.get(name);
    }

    public Iterator<DatabaseElementNode> iterator() {
        return _nodes.values().iterator();
    }

    public int size() {
        return _nodes.size();
    }
}

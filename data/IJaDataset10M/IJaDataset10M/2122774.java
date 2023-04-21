package org.neodatis.btree.impl;

import org.neodatis.btree.IBTree;
import org.neodatis.btree.IBTreeNode;
import org.neodatis.btree.IBTreePersister;
import org.neodatis.odb.OID;

/**
 * TODO check if this class must exist
 * @author osmadja
 *
 */
public class InMemoryPersister implements IBTreePersister {

    public IBTreeNode loadNodeById(Object id) {
        return null;
    }

    public Object saveNode(IBTreeNode node) {
        return null;
    }

    public void close() throws Exception {
    }

    public Object deleteNode(IBTreeNode parent) {
        return null;
    }

    public IBTree loadBTree(Object id) {
        return null;
    }

    public OID saveBTree(IBTree tree) {
        return null;
    }

    public void setBTree(IBTree tree) {
    }

    public void clear() {
    }

    public void flush() {
    }
}

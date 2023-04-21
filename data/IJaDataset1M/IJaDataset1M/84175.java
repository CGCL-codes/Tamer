package org.ozoneDB.collections;

import java.util.Iterator;

/**
 * Do not use this class directly.
 * This should be an inner class; unfortunately ozone does not support those yet.
 */
public class _AbstractOzoneMap_values extends AbstractOzoneSet implements OzoneSet {

    static final long serialVersionUID = 7339104015492809976L;

    private OzoneMap owner;

    /** Creates a new instance of AbstractDbMap_ValuesImpl */
    public _AbstractOzoneMap_values(OzoneMap owner) {
        this.owner = owner;
    }

    public int size() {
        return owner.size();
    }

    public boolean contains(Object value) {
        return owner.containsValue(value);
    }

    public Iterator iterator() {
        return new _AbstractOzoneMap_values_iterator(owner.entrySet().iterator());
    }

    public Iterator _org_ozoneDB_internalIterator() {
        return new _AbstractOzoneMap_values_iterator(((OzoneSet) owner.entrySet())._org_ozoneDB_internalIterator());
    }
}

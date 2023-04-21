package org.nakedobjects.object;

import org.nakedobjects.object.persistence.Oid;

public class DummyOid implements Oid {

    private int id;

    public DummyOid(int id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof DummyOid) {
            return ((DummyOid) obj).id == id;
        }
        return false;
    }

    public int hashCode() {
        return 37 * 17 + (int) (id ^ (id >>> 32));
    }

    public String toString() {
        return "DummyOid#" + id;
    }

    public boolean hasPrevious() {
        return false;
    }

    public Oid getPrevious() {
        return null;
    }

    public void copyFrom(Oid oid) {
    }
}

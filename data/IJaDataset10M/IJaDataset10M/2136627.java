package org.nakedobjects.nos.store.sql;

public class IntegerPrimaryKey implements PrimaryKey {

    private static final long serialVersionUID = 1L;

    private final int primaryKey;

    public IntegerPrimaryKey(final int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof IntegerPrimaryKey) {
            IntegerPrimaryKey o = ((IntegerPrimaryKey) obj);
            return primaryKey == o.primaryKey;
        }
        return false;
    }

    public String stringValue() {
        return "" + primaryKey;
    }

    public int hashCode() {
        int hash = 17;
        hash = 37 * hash + primaryKey;
        return hash;
    }

    public String toString() {
        return "" + primaryKey;
    }
}

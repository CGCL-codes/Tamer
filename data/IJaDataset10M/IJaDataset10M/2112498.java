package se.marianna.simpleDB;

import java.util.Hashtable;

public class HashIndex implements Index {

    private int hashCode = 0;

    public HashIndex(String[] columnNames) {
        this.columnNames = columnNames;
        for (int i = 0; i < columnNames.length; i++) {
            hashCode += columnNames.hashCode();
        }
    }

    Hashtable values = new Hashtable();

    String[] columnNames;

    public boolean indexFor(String[] columnNames) {
        if (this.columnNames.length != columnNames.length) {
            return false;
        }
        for (int i = 0; i < this.columnNames.length; i++) {
            boolean found = false;
            for (int i2 = 0; i2 < columnNames.length; i2++) {
                if (this.columnNames[i].equals(columnNames[i2])) {
                    found = true;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (obj instanceof HashIndex) {
            HashIndex other = (HashIndex) obj;
            if (other.columnNames.length != columnNames.length) {
                return false;
            }
            for (int i = 0; i < columnNames.length; i++) {
                boolean found = false;
                for (int i2 = 0; i2 < other.columnNames.length; i2++) {
                    if (columnNames[i].equals(columnNames[i2])) {
                        found = true;
                    }
                }
                if (!found) {
                    return false;
                }
            }
            return this.values.equals(other.values);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return hashCode;
    }

    public void add(Tupel value) throws MissingColumnName {
        add(value.sub(columnNames), value);
    }

    public void remove(Tupel value) throws MissingColumnName {
        remove(value.sub(columnNames), value);
    }

    private synchronized void add(Tupel key, Tupel value) {
        MutableSet set = (MutableSet) values.get(key);
        if (set == null) {
            set = new MutableSet();
            values.put(key, set);
        }
        set.add(value);
    }

    private synchronized void remove(Tupel key, Tupel value) {
        MutableSet set = (MutableSet) values.get(key);
        if (set != null) {
            set.remove(value);
            if (set.size() == 0) {
                values.remove(key);
            }
        }
    }

    public synchronized MutableSet get(Tupel key) {
        MutableSet set = (MutableSet) values.get(key);
        if (set == null) {
            return new MutableSet();
        }
        return new MutableSet(set);
    }
}

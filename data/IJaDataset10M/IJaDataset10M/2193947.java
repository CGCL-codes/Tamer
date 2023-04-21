package org.liris.schemerger.utils;

/**
 * A class that stores two integers that is meant to be used as a key type for a
 * map.
 * 
 * @author Damien Cram
 * 
 */
public class IndexEntry implements Comparable<IndexEntry>, Cloneable {

    private int index1;

    private int index2;

    public IndexEntry(int index1, int index2) {
        super();
        this.index1 = index1;
        this.index2 = index2;
    }

    public IndexEntry(IndexEntry indexEntry) {
        this.index1 = indexEntry.index1;
        this.index2 = indexEntry.index2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IndexEntry) {
            IndexEntry indexEntry = (IndexEntry) obj;
            return (indexEntry.index1 == this.index1) && (indexEntry.index2 == this.index2);
        }
        return false;
    }

    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
    }

    @Override
    public int hashCode() {
        return index1 + 65536 * index2;
    }

    @Override
    public String toString() {
        return "(" + index1 + "," + index2 + ")";
    }

    public int compareTo(IndexEntry o) {
        return Comparators.indexEntryComparator.compare(this, o);
    }

    public IndexEntry clone() {
        return new IndexEntry(index1, index2);
    }

    public void setIndex1(int index1) {
        this.index1 = index1;
    }

    public void setIndex2(int index2) {
        this.index2 = index2;
    }
}

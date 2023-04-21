package com.webobjects.foundation;

public class NSIndexPath {

    private int[] _indexes;

    public NSIndexPath(int index) {
        this(new int[] { index });
    }

    public NSIndexPath(int[] indexes) {
        super();
        if (indexes != null) {
            _indexes = indexes;
        } else {
            _indexes = new int[] {};
        }
    }

    public int compare(NSIndexPath otherPath) {
        return 0;
    }

    public int[] getIndexes() {
        return _indexes;
    }

    public int indexAtPosition(int position) {
        return _indexes[position];
    }

    public NSIndexPath indexPathByAddingIndex(int index) {
        int[] currentIndexes = getIndexes();
        int[] extendedIndexes = new int[currentIndexes.length + 1];
        System.arraycopy(currentIndexes, 0, extendedIndexes, 0, currentIndexes.length);
        extendedIndexes[currentIndexes.length] = index;
        return new NSIndexPath(extendedIndexes);
    }

    public NSIndexPath indexPathByRemovingLastIndex() {
        int[] currentIndexes = getIndexes();
        int[] truncatedIndexes = new int[currentIndexes.length - 1];
        System.arraycopy(currentIndexes, 0, truncatedIndexes, 0, truncatedIndexes.length);
        return new NSIndexPath(truncatedIndexes);
    }

    public int length() {
        return _indexes.length;
    }

    public String toString() {
        int[] indexes = getIndexes();
        int i, length = indexes.length;
        StringBuffer sb = new StringBuffer(length * 3);
        for (i = 0; i < length; i++) {
            sb.append(indexes[i]);
            if (i < length - 1) {
                sb.append('.');
            }
        }
        return sb.toString();
    }
}

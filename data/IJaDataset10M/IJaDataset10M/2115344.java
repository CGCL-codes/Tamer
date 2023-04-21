package com.rbnb.api;

final class SortedStrings {

    /**
     * vector containing the sorted strings.
     * <p>
     *
     * @author Ian Brown
     *
     * @since V2.0
     * @version 08/30/2002
     */
    private java.util.Vector strings = new java.util.Vector();

    SortedStrings() {
        super();
    }

    public final void add(String stringI) {
        int length = strings.size();
        if (length == 0) {
            strings.addElement(stringI);
        } else {
            int insertAt = indexOf(stringI);
            if (insertAt < 0) {
                strings.insertElementAt(stringI, (-insertAt) - 1);
            }
        }
    }

    public final boolean contains(String stringI) {
        return (indexOf(stringI) >= 0);
    }

    public final String elementAt(int indexI) {
        return ((String) strings.elementAt(indexI));
    }

    public final String[] elements() {
        String[] stringsR = new String[strings.size()];
        for (int idx = 0; idx < strings.size(); ++idx) {
            stringsR[idx] = (String) strings.elementAt(idx);
        }
        return (stringsR);
    }

    private final int indexOf(String stringI) {
        int lo = 0, hi = strings.size() - 1, idx;
        String entry;
        for (idx = (hi + lo) / 2; lo <= hi; idx = (hi + lo) / 2) {
            entry = (String) strings.elementAt(idx);
            int cmpr = entry.compareTo(stringI);
            if (cmpr == 0) {
                return (idx);
            } else if (cmpr < 0) {
                lo = idx + 1;
            } else {
                hi = idx - 1;
            }
        }
        return (-(lo + 1));
    }

    public final void remove(String stringI) {
        int idx = indexOf(stringI);
        if (idx >= 0) {
            strings.removeElementAt(idx);
        }
    }

    public final int size() {
        return (strings.size());
    }
}

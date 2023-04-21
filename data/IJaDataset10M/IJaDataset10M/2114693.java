package com.goodcodeisbeautiful.archtea.io;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author hata
 *
 */
public class EntryComparator implements Comparator<Entry>, Serializable {

    /**
     * <code>serialVersionUID</code> comment.
     */
    private static final long serialVersionUID = 4188214137900795106L;

    /**
     * 
     */
    public EntryComparator() {
        super();
    }

    public int compare(Entry arg0, Entry arg1) {
        return (arg0).getName().compareTo((arg1).getName());
    }
}

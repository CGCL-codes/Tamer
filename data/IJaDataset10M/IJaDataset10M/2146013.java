package com.loribel.commons.io.comparator;

import java.io.File;
import java.util.Comparator;

/**
 * Comparator to sort files by their date (use method getLastModified()).
 */
public class GB_FileComparatorByDate implements Comparator {

    /**
     * Attribute flagAscending. <br />.
     */
    private boolean flagAscending;

    /**
     * Constructor of GB_FileComparatorByDate without parameter.
     */
    public GB_FileComparatorByDate() {
        this(true);
    }

    /**
     * Constructor of GB_FileComparatorByDate with parameter(s).
     *
     * @param a_flagAscending boolean - true to sort ascending, false to sort descending
     */
    public GB_FileComparatorByDate(boolean a_flagAscending) {
        super();
        flagAscending = a_flagAscending;
    }

    /**
     * Compare two files by value of last modified.
     *
     * @param a_file1 Object -
     * @param a_file2 Object -
     *
     * @return int
     */
    public int compare(Object a_file1, Object a_file2) {
        File l_file1 = (File) a_file1;
        File l_file2 = (File) a_file2;
        long l_delta = (l_file1.lastModified() - l_file2.lastModified());
        int retour = 0;
        if (l_delta == 0) {
            return 0;
        } else if (l_delta > 0) {
            retour = 1;
        } else {
            retour = -1;
        }
        if (!flagAscending) {
            retour = -retour;
        }
        return retour;
    }

    /**
     * M�thode isAscending.
     * <p>
     *
     * @return boolean - <tt>flagAscending</tt>
     */
    public boolean isAscending() {
        return flagAscending;
    }

    /**
     * M�thode setAscending.
     * <p>
     *
     * @param a_flagAscending boolean - <tt>flagAscending</tt>
     */
    public void setAscending(boolean a_flagAscending) {
        flagAscending = a_flagAscending;
    }
}

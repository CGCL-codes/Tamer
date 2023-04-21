package com.jiexplorer.rcp.util;

import java.io.File;
import java.util.Comparator;

public class FileSortbyDate<T> implements Comparator<File> {

    private static boolean desend = true;

    public void toggel() {
        desend = !desend;
    }

    public int compare(final File a, final File b) {
        final Long lnga = a.lastModified();
        final Long lngb = b.lastModified();
        return (desend) ? lngb.compareTo(lnga) : lnga.compareTo(lngb);
    }
}

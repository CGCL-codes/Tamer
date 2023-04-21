package org.jmove.core;

public class NoFilter implements LinkFilter {

    private static NoFilter theFilter = null;

    public static NoFilter filter() {
        if (theFilter == null) {
            theFilter = new NoFilter();
        }
        return theFilter;
    }

    public boolean accept(Linkable linkable, Link link) {
        return true;
    }
}

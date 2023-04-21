package com.cromoteca.meshcms.server.storage;

public class AcceptAllItemFilter implements ItemFilter {

    private static AcceptAllItemFilter instance = new AcceptAllItemFilter();

    private AcceptAllItemFilter() {
    }

    public boolean accept(File item) {
        return item != null;
    }

    public static AcceptAllItemFilter get() {
        return instance;
    }
}

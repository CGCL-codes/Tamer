package com.tensegrity.palowebviewer.server.paloaccessor;

import org.palo.api.PaloObject;

public class IdFinder {

    private PaloObject[] items;

    private IdFinder(PaloObject[] items) {
        this.items = items;
    }

    private PaloObject find(String id) {
        PaloObject r = null;
        for (int i = 0; (i < items.length) && (r == null); i++) {
            if (items[i].getId().equals(id)) {
                r = items[i];
            }
        }
        return r;
    }

    public static PaloObject find(PaloObject[] items, String id) {
        IdFinder finder = new IdFinder(items);
        return finder.find(id);
    }
}

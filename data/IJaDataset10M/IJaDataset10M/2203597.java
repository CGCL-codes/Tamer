package org.moltools.lib.utils;

import org.apache.commons.collections.Transformer;
import org.moltools.lib.Identifiable;

public class IDExtractor implements Transformer {

    protected static IDExtractor instance;

    private IDExtractor() {
    }

    public static IDExtractor getInstance() {
        if (instance == null) {
            instance = new IDExtractor();
        }
        return instance;
    }

    public Object transform(Object arg0) {
        return ((Identifiable) arg0).getID();
    }
}

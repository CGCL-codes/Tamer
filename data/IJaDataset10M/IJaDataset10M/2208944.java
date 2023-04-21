package org.gdbi.util.fsdb;

import org.gdbi.api.*;
import org.gdbi.util.parse.UParseStatic;

/**
 * FileProperties is a subclass of Properties that is saved in
 * UMemdbRecord.properties for FSDB record files.
 */
class FileProperties extends UFsdbTypedProperties {

    private static final String KEY_CREATED = "created";

    private static final String KEY_CURRENT = "current";

    private static final String KEY_ORIGINAL = "original";

    public FileProperties() {
        super();
        setProperty(KEY_CREATED, "todo...");
    }

    public long getTime() {
        return getLongProperty(KEY_CREATED);
    }

    String removeCurrent() {
        final String line = getProperty(KEY_CURRENT);
        setProperty(KEY_CURRENT, "");
        return line;
    }

    void saveCurrent(GdbiRecord rec) {
        final String lines = UParseStatic.toGedcom(rec);
        setProperty(KEY_CURRENT, lines);
    }

    void setTime(long time) {
        setProperty(KEY_CREATED, "" + time);
    }
}

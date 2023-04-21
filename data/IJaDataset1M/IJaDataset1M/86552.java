package com.cell.util;

import java.io.InputStream;
import com.cell.reflect.Parser;

public class Properties extends Property<String> {

    public Properties() {
    }

    public Properties(InputStream is) {
        super.load(is);
    }

    public Properties(java.util.Properties cfg) {
        super.fromJavaProperties(cfg);
    }

    public Properties(MarkedHashtable map) {
        super(map);
    }

    public Properties(String text, String separator) {
        loadText(text, separator);
    }

    protected boolean putText(String k, String v) {
        return put(k, v);
    }

    public boolean putObject(String k, Object v) {
        return put(k, Parser.objectToString(v));
    }
}

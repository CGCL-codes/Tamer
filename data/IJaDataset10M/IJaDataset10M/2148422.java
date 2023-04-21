package com.neidetcher.toolkit.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Io {

    private static Log log = LogFactory.getLog(Io.class);

    public static java.io.InputStream getInputStreamFromString(String xml) {
        if (xml == null) {
            throw new IllegalArgumentException("string can't be null");
        }
        xml = xml.trim();
        java.io.InputStream in = null;
        try {
            in = new java.io.ByteArrayInputStream(xml.getBytes("UTF-8"));
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return in;
    }
}

package com.totalchange.wtframework.basichttpserver;

import java.io.IOException;
import java.util.Properties;

final class MimeLookup {

    private static final String MAP_RESOURCE = "/META-INF/wtf-basic-http-server-mime-mapping.properties";

    private static MimeLookup instance = new MimeLookup();

    private Properties map;

    private MimeLookup() {
        map = new Properties();
        try {
            map.load(MimeLookup.class.getResourceAsStream(MAP_RESOURCE));
        } catch (IOException ioEx) {
            throw new RuntimeException("Error loading properties file " + MAP_RESOURCE, ioEx);
        }
    }

    String getMimeType(String filename) {
        int extensionStart = filename.lastIndexOf('.');
        String extension = null;
        if (extensionStart > 0) {
            extension = filename.substring(extensionStart + 1);
        }
        if (extension != null) {
            return map.getProperty(extension);
        } else {
            return "unknown/unknown";
        }
    }

    static MimeLookup getInstance() {
        return instance;
    }
}

package com.volantis.xml.pipeline.sax.drivers.web;

import java.util.List;
import java.util.ArrayList;

/**
 * A type safe enumeration representing an identifier of http version.
 */
public class HTTPVersion {

    /**
     * HTTP Version 1.0.
     */
    public static final HTTPVersion HTTP_1_0 = new HTTPVersion("1.0");

    /**
     * HTTPVersion 1.1.
     */
    public static final HTTPVersion HTTP_1_1 = new HTTPVersion("1.1");

    private static final String VERSION_PREFIX = "HTTP/";

    /**
     * The name of this instance of HTTPVersion.
     */
    private String name;

    /**
     * A container for all the available HTTPVersions.
     */
    private static List versions;

    static {
        versions = new ArrayList();
        versions.add(HTTP_1_0);
        versions.add(HTTP_1_1);
    }

    /**
     * The private constructor.
     */
    private HTTPVersion(String name) {
        this.name = name;
    }

    /**
     * Get the name of this version.
     * @return The name of this version e.g. 1.0, 1.1 etc.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name with the "HTTP/" prefix.
     *
     * @return the name with the "HTTP/" prefix
     */
    public String getFullName() {
        return VERSION_PREFIX + name;
    }

    /**
     * Provide the HTTPVersion for a specified version name.
     * @param name The name of the required HTTPVersion. Must not be null.
     * can either be of the form "1.0" or of the form "HTTP/1.0"
     * @return The HTTPVersion with the same name as that provided.
     * @throws IllegalArgumentException if the <code>name</code> paramater is
     * not a recognised HTTP version.
     */
    public static HTTPVersion httpVersion(String name) {
        HTTPVersion version = null;
        int length = 0;
        if (name.startsWith(VERSION_PREFIX)) {
            length = VERSION_PREFIX.length();
        }
        for (int i = 0; i < versions.size() && version == null; i++) {
            HTTPVersion currentVersion = (HTTPVersion) versions.get(i);
            String currentName = currentVersion.getName();
            if (name.length() == (currentName.length() + length) && name.startsWith(currentName, length)) {
                version = currentVersion;
            }
        }
        if (version == null) {
            throw new IllegalArgumentException("HTTP version " + name + " is not recognised");
        }
        return version;
    }
}

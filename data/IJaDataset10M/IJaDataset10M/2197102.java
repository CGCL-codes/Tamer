package com.jcompressor.faces.config;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Map;

public interface Configurator {

    void parseBuild();

    void parseResources();

    Map<String, String> getProperties();

    Build getBuild();

    Resources getResources();

    boolean compress(final String contentType, final LinkedList<InputStream> streams);
}

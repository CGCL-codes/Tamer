package com.googlecode.pinthura.traverser.collection.old;

import java.util.Map;
import java.util.HashMap;

public final class LineFileReaderImpl implements LineFileReader {

    private final Map<String, String> fileMap = new HashMap<String, String>();

    public LineFileReaderImpl() {
        fileMap.put("build.txt", "# Describes how to build the project");
        fileMap.put("readme.txt", "##Update## Important information about use of this project");
    }

    public String readLine(final String file) {
        return fileMap.get(file);
    }
}

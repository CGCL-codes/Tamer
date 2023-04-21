package com.nexirius.framework.htmlview;

import com.nexirius.util.XFile;

public class HTMLStreamMapEntry {

    private String name;

    private String stringStream;

    private XFile fileStream;

    public HTMLStreamMapEntry(String name, String stringStream) {
        this.name = name;
        this.stringStream = stringStream;
    }

    public HTMLStreamMapEntry(String name, XFile fileStream) {
        this.name = name;
        this.fileStream = fileStream;
    }

    public String getName() {
        return name;
    }

    public String getStringStream() {
        return stringStream;
    }

    public XFile getFileStream() {
        return fileStream;
    }
}

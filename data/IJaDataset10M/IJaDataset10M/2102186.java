package com.orientechnologies.odbms.enterprise.interfaces.misc;

public class IContent {

    public IContent() {
    }

    public IContent(String iContent) {
        content = iContent;
    }

    public String getContent() {
        return content;
    }

    private String content;
}

package com.lehphyro.gamemcasa.scrapper.httpclient.parsing;

import java.util.*;

public abstract class HtmlParsingException extends Exception {

    private static final long serialVersionUID = -494082770609926370L;

    private String id;

    private String name;

    private Map<String, String> attributes;

    private String xPath;

    protected HtmlParsingException(String message, Object... args) {
        super(String.format(message, args));
    }

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    protected void setAttributes(Map<String, String> attributes) {
        this.attributes = Collections.unmodifiableMap(attributes);
    }

    public String getXPath() {
        return xPath;
    }

    protected void setXPath(String xPath) {
        this.xPath = xPath;
    }
}

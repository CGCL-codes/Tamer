package com.googlecode.technorati4j.entity;

import com.googlecode.technorati4j.enums.ResponseFormat;

/**
 * Request entity for toptags API.
 * 
 * @author Otávio Scherer Garcia
 * @version $Revision$
 */
public final class TopTagsRequest implements Request {

    /** The Technorati API key. */
    private String key;

    /**
     * This allows you to request an output format, which by default is set to xml. At the moment only the XML (xml) and
     * RSS (rss) formats are supported. We plan to support the Atom Syndication Format as well as XOXO in the near
     * future.
     */
    private ResponseFormat format;

    /**
     * Set this to a number larger than 0 and less than 100 for the top (limit) tags in the Technorati database. By
     * default this value is 20.
     */
    private int limit;

    /** Set this to a number larger than 0 and you'll get the top tags from (start) to (start)+(limit). */
    private int start;

    /**
     * The default constructor.
     */
    public TopTagsRequest() {
    }

    /**
     * Constructor with mandatory attributes.
     * 
     * @param key The key parameter.
     */
    public TopTagsRequest(String key) {
        setKey(key);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ResponseFormat getFormat() {
        return format;
    }

    public void setFormat(ResponseFormat format) {
        this.format = format;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getParameters() {
        final StringBuilder str = new StringBuilder();
        str.append("key=");
        str.append(key);
        if (format != null) {
            str.append("&format=");
            str.append(format);
        }
        if (limit > 0 && limit <= 100) {
            str.append("&limit=");
            str.append(limit);
        }
        if (start > 0) {
            str.append("&start=");
            str.append(start);
        }
        return str.toString();
    }
}

package com.hotbot;

public class HBFilter {

    String wfr;

    String wfw;

    String w;

    public HBFilter() {
    }

    public String getPattern() {
        return wfr;
    }

    public void setPattern(String pattern) {
        wfr = pattern;
    }

    public String getQuery() {
        return w;
    }

    public void setQuery(String query) {
        w = query;
    }

    public String getWhere() {
        return wfw;
    }

    public void setWhere(String where) {
        wfw = where;
    }
}

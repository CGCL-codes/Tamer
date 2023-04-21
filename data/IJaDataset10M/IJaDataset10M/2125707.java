package com.googlecode.mycontainer.web;

import java.util.ArrayList;
import java.util.List;

public class ContextWebServer {

    private String context;

    private String resources;

    private final List<ServletDesc> servlets = new ArrayList<ServletDesc>();

    private final List<FilterDesc> filters = new ArrayList<FilterDesc>();

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public List<ServletDesc> getServlets() {
        return servlets;
    }

    public List<FilterDesc> getFilters() {
        return filters;
    }
}

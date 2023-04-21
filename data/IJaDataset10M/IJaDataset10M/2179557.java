package com.sokolov.portal.core.dto;

import java.util.List;
import java.util.ArrayList;

public class Portlet extends AbstractCommonHeader {

    private String version;

    private String defaultContainer;

    private String defaultLocale;

    private List<String> supportedLocales = new ArrayList<String>();

    private String keywords;

    private Long height;

    private String url;

    private String javascriptUrl;

    private List<String> views = new ArrayList<String>();

    private List<Parameter> parameters = new ArrayList<Parameter>();

    private List<Event> events = new ArrayList<Event>();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDefaultContainer() {
        return defaultContainer;
    }

    public void setDefaultContainer(String defaultContainer) {
        this.defaultContainer = defaultContainer;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public List<String> getSupportedLocales() {
        return supportedLocales;
    }

    public void setSupportedLocales(List<String> supportedLocales) {
        this.supportedLocales = supportedLocales;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJavascriptUrl() {
        return javascriptUrl;
    }

    public void setJavascriptUrl(String javascriptUrl) {
        this.javascriptUrl = javascriptUrl;
    }

    public List<String> getViews() {
        return views;
    }

    public void setViews(List<String> views) {
        this.views = views;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}

package com.sokolov.portal.core.dto;

public class Service extends AbstractCommon {

    private String type;

    private String javascriptUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJavascriptUrl() {
        return javascriptUrl;
    }

    public void setJavascriptUrl(String javascriptUrl) {
        this.javascriptUrl = javascriptUrl;
    }
}

package org.restfaces.adapter;

public class ServletRequestUrlInfo implements RequestUrlInfo {

    private String info;

    public ServletRequestUrlInfo(String info) {
        this.info = info;
    }

    public String getMapping() {
        throw new UnsupportedOperationException("this method shouldn't be called.");
    }

    public String getRelevantPart() {
        return info;
    }
}

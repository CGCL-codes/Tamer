package com.bradmcevoy.http;

class Dest {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Dest.class);

    public final String host;

    public final String url;

    public final String name;

    public Dest(String sourceHost, String sDest) {
        log.debug("sDest: " + sDest);
        String sUrl;
        if (sDest.endsWith("/")) sDest = sDest.substring(0, sDest.length() - 1);
        log.debug("sDest2: " + sDest);
        if (sDest.contains("http://")) {
            String s = sDest.replace("http://", "");
            int pos = s.indexOf(":");
            if (pos > 0) {
                host = s.substring(0, pos);
                pos = s.indexOf("/");
            } else {
                pos = s.indexOf("/");
                host = s.substring(0, pos);
            }
            sUrl = s.substring(pos);
        } else {
            host = sourceHost;
            sUrl = sDest;
        }
        int pos = sUrl.lastIndexOf("/");
        if (pos <= 0) {
            url = "/";
        } else {
            url = sUrl.substring(0, pos);
        }
        name = sUrl.substring(pos + 1);
        log.debug("Dest: host: " + host);
        log.debug("Dest: url: " + url);
        log.debug("Dest: name: " + name);
    }
}

package com.saret.dictionary;

/**
 * @author biniam.gebremichael
 * @since 12/27/11
 */
public class SourceSite {

    String domain;

    String replaceFrom;

    String replaceTo;

    public SourceSite(String domain, String replaceFrom, String replaceTo) {
        this.domain = domain;
        this.replaceFrom = replaceFrom;
        this.replaceTo = replaceTo;
    }

    public boolean validForward(String url) {
        return url.toLowerCase().contains(replaceFrom.toLowerCase());
    }

    String translate(String url) {
        return url.replaceAll(replaceFrom, replaceTo).replaceAll(replaceFrom.toLowerCase(), replaceTo.toLowerCase());
    }

    String searchKey(String key) {
        return key + " site:" + domain;
    }
}

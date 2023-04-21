package org.jazzteam.modelBrowserDnsServer;

public class Browser {

    private String url;

    public void goUrl(String newUrl) {
        this.url = newUrl;
    }

    public String getUrl() {
        return url;
    }
}

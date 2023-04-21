package org.kotemaru.browser;

public class RedirectNavigator extends Navigator {

    public RedirectNavigator(String url) {
        super(url);
    }

    public String getUrl() {
        return getResource();
    }
}

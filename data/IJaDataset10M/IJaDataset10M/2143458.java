package org.gudy.azureus2.plugins.utils.search;

public class SearchException extends Exception {

    public SearchException(String str) {
        super(str);
    }

    public SearchException(String str, Throwable e) {
        super(str, e);
    }
}

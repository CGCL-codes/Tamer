package com.google.code.stubit.match;

import java.net.URL;
import java.util.regex.Pattern;

public class UrlMatch implements Match<URL> {

    private Pattern pattern;

    public UrlMatch(Pattern pattern) {
        this.pattern = pattern;
    }

    public boolean matches(URL target) {
        return pattern.matcher(target.toExternalForm()).matches();
    }
}

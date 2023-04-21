package radeox.filter;

import radeox.filter.regex.LocaleRegexReplaceFilter;

public class BoldFilter extends LocaleRegexReplaceFilter implements CacheFilter {

    @Override
    protected String getLocaleKey() {
        return "filter.bold";
    }
}

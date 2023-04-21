package radeox.filter;

import radeox.filter.regex.LocaleRegexReplaceFilter;

/**
 * Filter for special words ...
 * Currently if replaces the word neotis with a link to the old
 * neotis homepage. Neotis was the company from funzel and leo.
 * Can be used for other words.
 *
 * @author leo
 * @team other
 * @version $Id: MarkFilter.java,v 1.4 2003/08/13 12:37:05 stephan Exp $
 */
public class MarkFilter extends LocaleRegexReplaceFilter implements CacheFilter {

    @Override
    protected String getLocaleKey() {
        return "filter.mark";
    }
}

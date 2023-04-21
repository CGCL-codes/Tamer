package org.bionote.radeox.filter;

import java.text.MessageFormat;
import org.radeox.filter.CacheFilter;
import org.radeox.filter.regex.LocaleRegexReplaceFilter;

/**
 * @author mbreese
 *
 */
public class BioNoteHeaderFilter extends LocaleRegexReplaceFilter implements CacheFilter {

    private MessageFormat formatter;

    protected String getLocaleKey() {
        return "filter.heading";
    }
}

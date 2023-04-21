package org.radeox.macro.code;

import org.radeox.filter.regex.RegexReplaceFilter;

public class DefaultRegexCodeFormatter extends RegexReplaceFilter {

    public DefaultRegexCodeFormatter(String regex, String substitute) {
        super(regex, substitute);
    }

    public int getPriority() {
        return 0;
    }
}

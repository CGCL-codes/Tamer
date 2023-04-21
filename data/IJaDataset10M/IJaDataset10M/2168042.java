package org.illico.common.text.formatter;

import org.illico.common.lang.StringUtils;

public abstract class AbstractFormatter<T> implements Formatter<T> {

    protected String getProtectedPattern(String pattern) {
        if (!StringUtils.isEmpty(pattern)) {
            return pattern;
        }
        return getDefaultPattern();
    }
}

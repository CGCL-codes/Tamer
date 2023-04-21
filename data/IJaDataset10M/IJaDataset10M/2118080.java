package org.easymock.internal.matchers;

import org.easymock.IArgumentMatcher;

public class LessOrEqual implements IArgumentMatcher {

    private final Number expected;

    public LessOrEqual(Number value) {
        this.expected = value;
    }

    public boolean matches(Object actual) {
        return (actual instanceof Number) && ((Number) actual).longValue() <= expected.longValue();
    }

    public void appendTo(StringBuffer buffer) {
        buffer.append("leq(" + expected + ")");
    }
}

package org.uasd.jalgor.model;

/**
 *
 * @author Edwin Bratini <edwin.bratini@gmail.com>
 */
public class KeywordToken extends Token {

    private String value;

    public KeywordToken() {
    }

    public KeywordToken(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Statement.cppReps.get(Statement.keywordMatcher.get(value));
    }
}

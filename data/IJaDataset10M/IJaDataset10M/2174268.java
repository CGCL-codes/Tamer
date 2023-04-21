package org.easymock.internal.matchers;

public class CompareEqual<T extends Comparable<T>> extends CompareTo<T> {

    public CompareEqual(Comparable<T> value) {
        super(value);
    }

    @Override
    protected String getName() {
        return "cmpEq";
    }

    @Override
    protected boolean matchResult(int result) {
        return result == 0;
    }
}

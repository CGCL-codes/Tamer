package reducedmodel;

import java.util.LinkedList;

public class UnmatchedBreaker extends SingleBreaker {

    private int _matched;

    public static final int UNMATCHED = 0;

    public static final int START = 1;

    public static final int END = 2;

    public UnmatchedBreaker(LinkedList<PartialBreaker> partials, SingleFactory factory) {
        super(partials, factory);
        _matched = UNMATCHED;
    }

    public Object execute(BreakerVisitor visitor, Object... args) {
        return visitor.unmatchedCase(this, args);
    }

    public int matchStatus() {
        return _matched;
    }
}

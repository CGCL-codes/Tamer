package jlibs.util.logging;

/**
 * @author Santhosh Kumar T
 */
public class PrecedingCondition implements Condition {

    public final Condition condition;

    public final boolean includeSelf;

    public PrecedingCondition(Condition condition, boolean includeSelf) {
        this.condition = condition;
        this.includeSelf = includeSelf;
    }

    private boolean matched;

    @Override
    public boolean matches(LogRecord record) {
        if (matched) return false;
        if (condition.matches(record)) {
            matched = true;
            return includeSelf;
        } else return true;
    }
}

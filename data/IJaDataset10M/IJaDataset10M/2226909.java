package eu.pisolutions.predicate;

/**
 * {@link eu.pisolutions.predicate.Predicate} that evaluates to <code>true</code> if the input is less than or equal to another value.
 *
 * @author Laurent Pireyn
 */
public final class LessThanOrEqualToPredicate<T extends Comparable<T>> extends ComparisonPredicate<T> {

    public LessThanOrEqualToPredicate(T other) {
        super(other);
    }

    @Override
    protected boolean evaluate(int result) {
        return result <= 0;
    }
}

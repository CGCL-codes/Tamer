package net.jadoth.lang;

/**
 *
 * @author Thomas M�nz
 *
 * @param <T>
 */
public class SimilatorSequence<T> implements Similator<T> {

    private final Similator<? super T>[] similators;

    public SimilatorSequence(final Similator<? super T>... comparators) {
        super();
        this.similators = comparators;
    }

    @Override
    public double evaluate(final T o1, final T o2) {
        double result = 0.0;
        for (int i = 0; i < this.similators.length; i++) {
            result += this.similators[i].evaluate(o1, o2);
        }
        return result / this.similators.length;
    }
}

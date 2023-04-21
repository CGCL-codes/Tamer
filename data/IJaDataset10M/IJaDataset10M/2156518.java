package eu.pisolutions.predicate;

import eu.pisolutions.lang.Validations;

/**
 * {@link eu.pisolutions.predicate.Predicate} wrapper.
 *
 * @author Laurent Pireyn
 */
public class PredicateWrapper<T> extends Object implements Predicate<T> {

    protected final Predicate<? super T> predicate;

    public PredicateWrapper(Predicate<? super T> predicate) {
        super();
        Validations.notNull(predicate, "predicate");
        this.predicate = predicate;
    }

    public boolean evaluate(T input) {
        return this.predicate.evaluate(input);
    }
}

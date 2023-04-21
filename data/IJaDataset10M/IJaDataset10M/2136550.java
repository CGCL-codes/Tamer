package test.fp;

/**
 * Utility-Methoden zu Predicates.
 */
public class PredicateToolkit {

    public static Predicate bind(UnaryPredicate predicate, Object argument) {
        return new BindingArgumentPredicate(predicate, argument);
    }
}

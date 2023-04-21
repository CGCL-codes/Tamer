package org.middleheaven.util.classification;

/**
 * Filter that returns the opposite result  of a underlying filter.
 *
 */
public class NegationClassifier<T> implements Classifier<Boolean, T> {

    private Classifier<Boolean, T> original;

    public NegationClassifier() {
    }

    ;

    public NegationClassifier(Classifier<Boolean, T> original) {
        this.original = original;
    }

    public void setFilter(BooleanClassifier<T> filter) {
        this.original = filter;
    }

    public Classifier<Boolean, T> getFilter() {
        return original;
    }

    @Override
    public Boolean classify(T obj) {
        return !original.classify(obj);
    }
}

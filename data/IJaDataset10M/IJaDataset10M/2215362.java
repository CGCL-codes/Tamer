package net.sourceforge.ondex.predicate;

/**
 * Logical negation.
 *
 * @author Matthew Pocock
 */
public final class Not<PV extends PredicateVisitor<T>, T> implements Predicate<PV, T> {

    private final Predicate<PV, ? super T> predicate;

    Not(Predicate<PV, ? super T> predicate) {
        this.predicate = predicate;
    }

    public Predicate<PV, ? super T> getPredicate() {
        return predicate;
    }

    public boolean accepts(T t) {
        return !predicate.accepts(t);
    }

    public void entertain(PV pv) {
        pv.visit(pv, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Not not = (Not) o;
        if (!predicate.equals(not.predicate)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return predicate.hashCode();
    }

    @Override
    public String toString() {
        return "Not{" + "predicate=" + predicate + '}';
    }
}

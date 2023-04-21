package net.sf.doolin.util.collection;

import java.util.List;

public class AndPredicate<E> extends CombinedPredicate<E> {

    public AndPredicate() {
        super();
    }

    public AndPredicate(List<Predicate<E>> list) {
        super(list);
    }

    public AndPredicate(Predicate<E> p1, Predicate<E> p2) {
        super(p1, p2);
    }

    @Override
    protected Boolean combine(Boolean result, boolean pr) {
        if (result == null) {
            return pr;
        } else {
            return result && pr;
        }
    }
}

package com.infomancers.collections.predicates;

import com.infomancers.collections.Predicate;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author aviadbd
 */
public class Or<T> implements Predicate<T> {

    private Collection<Predicate<T>> predicates;

    public Or(Predicate<T>... predicates) {
        this(Arrays.asList(predicates));
    }

    public Or(Collection<Predicate<T>> predicates) {
        this.predicates = predicates;
    }

    public boolean isTrue(T item) {
        for (Predicate p : predicates) {
            if (p.isTrue(item)) {
                return true;
            }
        }
        return false;
    }
}

package fr.lelouet.ga.combinators;

import fr.lelouet.ga.Combinator;
import java.util.Collections;
import java.util.Set;

/**
 * @author guigolum
 */
public class LazyCombinator<T> implements Combinator<T> {

    @SuppressWarnings("unchecked")
    @Override
    public Set<T> combination(final Set<T> set) {
        return Collections.EMPTY_SET;
    }
}

package ch.ethz.globis.javaom.collection.imp;

import ch.ethz.globis.javaom.collection.Collection;
import ch.ethz.globis.javaom.collection.ReduceParameter;

/**
 * @author aldespin
 * @version 1.0
 */
public class UnionParameter implements ReduceParameter {

    /**
    * {@inheritDoc}
    */
    public Object isEmpty() {
        return Collection.empty();
    }

    /**
    * {@inheritDoc}
    */
    public Object isSingle(final Object member) {
        return Collection.single(member);
    }

    /**
    * {@inheritDoc}
    */
    public Object combine(Object lhs, Object rhs) {
        return Collection.combine((Collection) lhs, (Collection) rhs);
    }
}

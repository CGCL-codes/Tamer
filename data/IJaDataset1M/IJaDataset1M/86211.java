package ch.unibe.jexample.internal.graph;

import java.util.Collections;

/** Incoming dependencies of an example.
 *<P> 
 * @author Adrian Kuhn
 *
 */
public class Producers<E> extends Consumers<E> {

    @Override
    protected E value(Dependency<E> edge) {
        return edge.producer.value;
    }

    public Iterable<E> transitiveClosure() {
        if (this.isEmpty()) return Collections.<E>emptySet();
        return edges().get(0).consumer.transitiveClosure();
    }
}

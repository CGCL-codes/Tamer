package org.demis.troll;

/**
 * @version 1.0
 * @author <a href="mailto:demis27@demis27.net">Stéphane kermabon</a>
 */
public class MonoLink<E> extends GraphLink<E> {

    public MonoLink(GraphNode<E> source, GraphNode<E> target) {
        super(source, target);
    }

    @Override
    public boolean mayCrossLink(GraphNode<E> from) {
        return (from != null && from.equals(getSource()));
    }

    @Override
    public GraphNode<E> crossLink(GraphNode<E> from) {
        if (from == null) {
            throw new IllegalArgumentException("from can't be null");
        }
        if (from.equals(getSource())) {
            return getTarget();
        }
        throw new IllegalArgumentException("from isn't source of this link");
    }

    @Override
    public GraphNode<E> backcrossLink(GraphNode<E> to) {
        if (to == null) {
            throw new IllegalArgumentException("to can't be null");
        }
        if (to.equals(getTarget())) {
            return getSource();
        }
        throw new IllegalArgumentException("to isn't target of this link");
    }

    @Override
    public boolean mayBackcrossLink(GraphNode<E> to) {
        return (to != null && to.equals(getTarget()));
    }
}

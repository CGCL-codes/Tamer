package jaxlib.col.concurrent;

import java.util.Collection;
import jaxlib.col.queue.AbstractXQueue;
import jaxlib.lang.Objects;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: AbstractBlockingXQueue.java 2791 2010-03-29 08:45:11Z joerg_wassmer $
 */
public abstract class AbstractBlockingXQueue<E> extends AbstractXQueue<E> implements BlockingXQueue<E> {

    protected AbstractBlockingXQueue() {
        super();
    }

    @Override
    public abstract boolean addIfAbsent(E e);

    @Override
    public abstract int drainTo(Collection<? super E> dest, int maxElements);

    @Override
    public abstract E internalize(E e);

    @Override
    public int drainTo(final Collection<? super E> dest) {
        return drainTo(dest, Integer.MAX_VALUE);
    }

    @Override
    public final int freeCapacity() {
        return remainingCapacity();
    }

    /**
   * Returns {@code false} always.
   *
   * @return
   *  {@code false}.
   *
   * @since JaXLib 1.0
   */
    @Override
    public final boolean isSizeStable() {
        return false;
    }

    @Override
    public Object[] toArray() {
        return toArray(Objects.EMPTY_ARRAY);
    }
}

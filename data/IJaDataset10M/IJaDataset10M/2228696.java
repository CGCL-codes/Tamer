package net.sf.jdsc.traversal;

import static net.sf.jdsc.asserts.NotNull.checkNotNull;
import net.sf.jdsc.AbstractEnumerator;
import net.sf.jdsc.IPosition;
import net.sf.jdsc.NoSuchObjectException;

/**
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
public class TraversalEnumerator<E, T extends IPosition<E>> extends AbstractEnumerator<T> {

    private final Traversal<E, T> traversal;

    private T current;

    public TraversalEnumerator(Traversal<E, T> traversal) {
        super();
        checkNotNull(traversal, Traversal.class);
        this.traversal = traversal;
        this.current = null;
    }

    @Override
    public T getCurrent() throws NoSuchObjectException {
        checkNotNull(current, IPosition.class, NoSuchObjectException.class);
        return current;
    }

    @Override
    public boolean moveNext() {
        if (current == null) traversal.traverseFirst(); else traversal.traverseForward();
        current = traversal.getCurrent();
        return current != null;
    }

    @Override
    public boolean movePrevious() {
        if (current == null) traversal.traverseLast(); else traversal.traverseBackward();
        current = traversal.getCurrent();
        return current != null;
    }

    @Override
    public void reset() {
        traversal.reset();
        current = null;
    }
}

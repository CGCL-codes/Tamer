package org.apache.commons.collections.primitives.adapters;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.collections.primitives.LongList;

/**
 * Adapts an {@link LongList LongList} to the {@link List List} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link LongList LongList} implementation in the "obvious" way.
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1.4.3 $ $Date: 2008/04/26 11:00:55 $
 * @author Rodney Waldhoff
 */
public final class LongListList extends AbstractLongListList implements Serializable {

    /**
	 * Create a {@link List List} wrapping the specified
	 * {@link LongList LongList}. When the given <i>list</i> is
	 * <code>null</code>, returns <code>null</code>.
	 * 
	 * @param list
	 *            the (possibly <code>null</code>) {@link LongList LongList}
	 *            to wrap
	 * @return a {@link List List} wrapping the given <i>list</i>, or
	 *         <code>null</code> when <i>list</i> is <code>null</code>.
	 */
    public static List wrap(final LongList list) {
        if (null == list) {
            return null;
        } else if (list instanceof Serializable) {
            return new LongListList(list);
        } else {
            return new NonSerializableLongListList(list);
        }
    }

    /**
	 * Creates a {@link List List} wrapping the specified
	 * {@link LongList LongList}.
	 * 
	 * @see #wrap
	 */
    public LongListList(final LongList list) {
        _list = list;
    }

    @Override
    protected LongList getLongList() {
        return _list;
    }

    private LongList _list = null;
}

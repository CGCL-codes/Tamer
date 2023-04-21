package org.apache.commons.collections.primitives.adapters;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.collections.primitives.ByteList;

/**
 * Adapts an {@link ByteList ByteList} to the {@link List List} interface.
 * <p />
 * This implementation delegates most methods to the provided
 * {@link ByteList ByteList} implementation in the "obvious" way.
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1.4.3 $ $Date: 2008/04/26 11:00:55 $
 * @author Rodney Waldhoff
 */
public final class ByteListList extends AbstractByteListList implements Serializable {

    /**
	 * Create a {@link List List} wrapping the specified
	 * {@link ByteList ByteList}. When the given <i>list</i> is
	 * <code>null</code>, returns <code>null</code>.
	 * 
	 * @param list
	 *            the (possibly <code>null</code>) {@link ByteList ByteList}
	 *            to wrap
	 * @return a {@link List List} wrapping the given <i>list</i>, or
	 *         <code>null</code> when <i>list</i> is <code>null</code>.
	 */
    public static List wrap(final ByteList list) {
        if (null == list) {
            return null;
        } else if (list instanceof Serializable) {
            return new ByteListList(list);
        } else {
            return new NonSerializableByteListList(list);
        }
    }

    /**
	 * Creates a {@link List List} wrapping the specified
	 * {@link ByteList ByteList}.
	 * 
	 * @see #wrap
	 */
    public ByteListList(final ByteList list) {
        _list = list;
    }

    @Override
    protected ByteList getByteList() {
        return _list;
    }

    private ByteList _list = null;
}

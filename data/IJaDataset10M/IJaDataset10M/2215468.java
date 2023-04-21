package org.apache.commons.collections.primitives.adapters;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.collections.primitives.DoubleList;

/**
 * Adapts a {@link Number}-valued {@link List List} to the
 * {@link DoubleList DoubleList} interface.
 * <p />
 * This implementation delegates most methods to the provided {@link List List}
 * implementation in the "obvious" way.
 * 
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1.4.3 $ $Date: 2008/04/26 11:00:55 $
 * @author Rodney Waldhoff
 */
public class ListDoubleList extends AbstractListDoubleList implements Serializable {

    /**
	 * Create an {@link DoubleList DoubleList} wrapping the specified
	 * {@link List List}. When the given <i>list</i> is <code>null</code>,
	 * returns <code>null</code>.
	 * 
	 * @param list
	 *            the (possibly <code>null</code>) {@link List List} to wrap
	 * @return a {@link DoubleList DoubleList} wrapping the given <i>list</i>,
	 *         or <code>null</code> when <i>list</i> is <code>null</code>.
	 */
    public static DoubleList wrap(final List list) {
        if (null == list) {
            return null;
        } else if (list instanceof Serializable) {
            return new ListDoubleList(list);
        } else {
            return new NonSerializableListDoubleList(list);
        }
    }

    /**
	 * Creates an {@link DoubleList DoubleList} wrapping the specified
	 * {@link List List}.
	 * 
	 * @see #wrap
	 */
    public ListDoubleList(final List list) {
        _list = list;
    }

    @Override
    protected List getList() {
        return _list;
    }

    private List _list = null;
}

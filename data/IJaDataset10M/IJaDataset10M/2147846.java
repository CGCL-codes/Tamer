package net.sf.joafip.java.util.support.tree;

import java.io.Serializable;
import net.sf.joafip.StorableClass;
import net.sf.joafip.StoreNotUseStandardSerialization;
import net.sf.joafip.java.util.support.AbstractDescendingIterator;
import net.sf.joafip.store.service.proxy.IInstanceFactory;

@StoreNotUseStandardSerialization
@StorableClass
public class TreeSupportSubDescendingIterator<E> extends AbstractDescendingIterator<E> implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7934788543701526053L;

    /**
	 * 
	 * @param tree
	 *            tree this iterate in
	 * @param fromElement
	 *            starting element for iteration
	 * @param fromInclusive
	 *            true if starting element is included in iteration
	 * @param toElement
	 *            ending element for iteration
	 * @param toInclusive
	 *            true if ending element is included in iteration
	 */
    protected TreeSupportSubDescendingIterator(final TreeSupport<E> tree, final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        this(null, tree, fromElement, fromInclusive, toElement, toInclusive);
    }

    @SuppressWarnings("unchecked")
    protected TreeSupportSubDescendingIterator(final IInstanceFactory instanceFactory, final TreeSupport<E> tree, final E fromElement, final boolean fromInclusive, final E toElement, final boolean toInclusive) {
        super();
        iterator = TreeSupportSubIterator.newInstance(instanceFactory, tree, fromElement, fromInclusive, toElement, toInclusive, true);
        if (iterator == null) {
            throw new IllegalStateException("iterator can not be null");
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static TreeSupportSubDescendingIterator newInstance(final IInstanceFactory instanceFactory, final TreeSupport tree, final Object fromElement, final boolean fromInclusive, final Object toElement, final boolean toInclusive) {
        TreeSupportSubDescendingIterator newInstance;
        if (instanceFactory == null) {
            newInstance = new TreeSupportSubDescendingIterator(tree, fromElement, fromInclusive, toElement, toInclusive);
        } else {
            newInstance = (TreeSupportSubDescendingIterator) instanceFactory.newInstance(TreeSupportSubDescendingIterator.class, new Class[] { IInstanceFactory.class, TreeSupport.class, Object.class, boolean.class, Object.class, boolean.class }, new Object[] { instanceFactory, tree, fromElement, fromInclusive, toElement, toInclusive });
        }
        return newInstance;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected int size() {
        return ((TreeSupportSubIterator) iterator).size();
    }
}

package net.sf.joafip.java.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.sf.joafip.AssertNotNull;
import net.sf.joafip.Fortest;
import net.sf.joafip.StorableClass;
import net.sf.joafip.StoreNotUseStandardSerialization;
import net.sf.joafip.store.service.proxy.IInstanceFactory;

/**
 * iterator on set of map entries values
 * 
 * @author luc peuvrier
 * 
 */
@StoreNotUseStandardSerialization
@StorableClass
public class SetOfMapEntryValueIterator<K, V> implements Iterator<V>, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7581493085904979388L;

    /** iterator on the set of entries accessible by key */
    @AssertNotNull
    private final Iterator<Map.Entry<K, V>> iterator;

    public SetOfMapEntryValueIterator(final Set<Entry<K, V>> set) {
        super();
        iterator = set.iterator();
        if (iterator == null) {
            throw new IllegalStateException("iterator can not be null");
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static SetOfMapEntryValueIterator newInstance(final IInstanceFactory instanceFactory, final Set set) {
        SetOfMapEntryValueIterator newInstance;
        if (instanceFactory == null) {
            newInstance = new SetOfMapEntryValueIterator(set);
        } else {
            newInstance = (SetOfMapEntryValueIterator) instanceFactory.newInstance(SetOfMapEntryValueIterator.class, new Class[] { Set.class }, new Object[] { set });
        }
        return newInstance;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public V next() {
        return iterator.next().getValue();
    }

    public void remove() {
        iterator.remove();
    }

    @Fortest
    public Iterator<Map.Entry<K, V>> getIterator() {
        return iterator;
    }
}

package net.sf.jdsc;

/**
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
public abstract class AbstractSortedMap<K, V> extends AbstractMap<K, V> implements ISortedMap<K, V> {

    @Override
    public K smallestKey() throws EmptyDataStructureException {
        IEntry<K, V> entry = smallest();
        if (entry == null) throw new EmptyDataStructureException();
        return entry.getKey();
    }

    @Override
    public K greatestKey() throws EmptyDataStructureException {
        IEntry<K, V> entry = greatest();
        if (entry == null) throw new EmptyDataStructureException();
        return entry.getKey();
    }

    @Override
    public ISortedMap<K, V> headMap(K greatest) throws EmptyDataStructureException {
        return subMap(smallestKey(), greatest);
    }

    @Override
    public ISortedMap<K, V> tailMap(K smallest) throws EmptyDataStructureException {
        return subMap(smallest, greatestKey());
    }

    @Override
    public ISortedMap<K, V> clone() {
        return clone(false);
    }

    @Override
    public ISortedMap<K, V> clone(boolean deepclone) {
        return (ISortedMap<K, V>) super.clone(deepclone);
    }
}

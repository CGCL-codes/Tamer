package net.sf.jdsc;

/**
 * An <b>IAllocation</b> contains an amount of entries. An {@link IEntry} is a
 * key-value pair.
 * 
 * @author <a href="mailto:twyss@users.sourceforge.net">twyss</a>
 * @version 1.0
 */
public interface IAllocation<K, V> extends IDataStructure, IEnumerable<IEntry<K, V>> {

    @Override
    public IAllocation<K, V> create();

    /**
     * Inserts a new key-value pair into this {@link IAllocation}.
     * 
     * @param key
     *            The key of the new {@link IEntry}.
     * @param value
     *            The value of the new {@link IEntry}.
     * @return the new {@link IEntry}.
     * @throws PositionAlreadyExistsException
     *             if an other identical {@link IEntry} already exists.
     * @throws FullDataStructureException
     *             if this {@link IAllocation} is full.
     */
    public IEntry<K, V> insert(K key, V value) throws PositionAlreadyExistsException, FullDataStructureException;

    /**
     * Enumerates the keys of this {@link IAllocation}.
     * 
     * @return an {@link IEnumerator} over the keys.
     */
    public IEnumerator<K> keys();

    /**
     * Enumerates the values of this {@link IAllocation}.
     * 
     * @return an {@link IEnumerator} over the values.
     */
    public IEnumerator<V> values();

    /**
     * Enumerates all entries of this {@link IAllocation}.
     * 
     * @return an {@link IEnumerator} over all entries.
     */
    public IEnumerator<? extends IEntry<K, V>> entries();

    /**
     * Reports <code>true</code> if this {@link IAllocation} contains an
     * {@link IEntry} with the specified <i>key</i>.
     * 
     * @param key
     *            The key to check.
     * @return <code>true</code> if the specified <i>key</i> exists, otherwise
     *         <code>false</code>.
     */
    public boolean containsKey(K key);

    /**
     * Reports <code>true</code> if for the specified <i>key</i> exists a
     * matching value.
     * 
     * @param key
     *            The key to check.
     * @param value
     *            The value to check.
     * @return <code>true</code> if the specified key-value pair exists,
     *         otherwise <code>false</code>.
     */
    public boolean contains(K key, V value);

    /**
     * Reports <code>true</code> if the specified <i>entry</i> exists in this
     * {@link IAllocation}.
     * 
     * @param entry
     *            The entry to check.
     * @return <code>true</code> if the <i>entry</i> exists, otherwise
     *         <code>false</code>.
     */
    public boolean containsEntry(IEntry<K, V> entry);

    /**
     * Removes the specified <i>entry</i> from this {@link IAllocation}.
     * 
     * @param entry
     *            The entry to remove.
     * @return <code>false</code> if the entry is not contained in this
     *         {@link IAllocation}; <code>true</code> if the <i>entry</i> was
     *         removed.
     * @throws PositionNotFoundException
     *             if the <i>entry</i> does not exists.
     */
    public boolean remove(IEntry<K, V> entry) throws PositionNotFoundException;

    @Override
    public IAllocation<K, V> clone();

    @Override
    public IAllocation<K, V> clone(boolean deepclone);
}

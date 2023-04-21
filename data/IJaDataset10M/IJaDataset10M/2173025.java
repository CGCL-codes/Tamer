package org.jcvi.common.core.assembly.util.trim;

import java.io.IOException;
import org.jcvi.common.core.Range;
import org.jcvi.common.core.datastore.DataStore;
import org.jcvi.common.core.datastore.DataStoreException;
import org.jcvi.common.core.util.iter.CloseableIterator;

/**
 * {@code TrimDataStoreAdatper} wraps
 * a {@link DataStore} of {@link Range}s around a   
 * {@link TrimDataStore}.
 * wrapper
 * @author dkatzel
 *
 *
 */
public final class TrimDataStoreAdatper implements TrimDataStore {

    private final DataStore<Range> delegate;

    /**
     * Adapt the given {@link DataStore} of {@link Range}s
     * into a {@link TrimDataStore}.
     * @param toBeAdapted the datastore to be adapted into a TrimDataStore.
     * @return a new {@link TrimDataStore} which wraps the given
     * {@link DataStore}
     * @throws NullPointerException if the given datastore is null.
     */
    public static TrimDataStoreAdatper adapt(DataStore<Range> toBeAdapted) {
        return new TrimDataStoreAdatper(toBeAdapted);
    }

    /**
     * Adapt the given {@link DataStore} of {@link Range}s
     * into a {@link TrimDataStore}.
     * @param delegate the datastore to be adapted into a TrimDataStore.
     * @return a new {@link TrimDataStore} which wraps the given
     * {@link DataStore}
     * @throws NullPointerException if the given datastore is null.
     */
    private TrimDataStoreAdatper(DataStore<Range> delegate) {
        if (delegate == null) {
            throw new NullPointerException("delegate can not be null");
        }
        this.delegate = delegate;
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public boolean contains(String id) throws DataStoreException {
        return delegate.contains(id);
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public Range get(String id) throws DataStoreException {
        return delegate.get(id);
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public CloseableIterator<String> idIterator() throws DataStoreException {
        return delegate.idIterator();
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public long getNumberOfRecords() throws DataStoreException {
        return delegate.getNumberOfRecords();
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public void close() throws IOException {
        delegate.close();
    }

    /**
    * {@inheritDoc}
     * @throws DataStoreException 
    */
    @Override
    public CloseableIterator<Range> iterator() throws DataStoreException {
        return delegate.iterator();
    }

    /**
    * {@inheritDoc}
    */
    @Override
    public boolean isClosed() throws DataStoreException {
        return delegate.isClosed();
    }
}

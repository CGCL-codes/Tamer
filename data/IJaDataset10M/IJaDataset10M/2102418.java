package org.jcvi.glk.adapter;

import java.io.IOException;
import java.util.Iterator;
import org.jcvi.common.core.datastore.DataStore;
import org.jcvi.common.core.datastore.DataStoreException;
import org.jcvi.common.core.datastore.DataStoreIterator;
import org.jcvi.common.core.util.iter.CloseableIterator;
import org.jcvi.common.core.util.iter.CloseableIteratorAdapter;
import org.jcvi.glk.Basecalls;
import org.jcvi.glk.GlkSequence;
import org.jcvi.glk.helpers.HelperUtil;
import org.jcvi.glk.helpers.HibernateGLKHelper;

public abstract class AbstractGLKTraceDataStore<T> implements DataStore<T> {

    private final HibernateGLKHelper helper;

    private final Integer version;

    /**
     * conveinence constructor, the same as calling
     * {@link #AbstractGLKAssemblyDataStore(HibernateGLKHelper,Integer)
     * new AbstractGLKTraceDataStore(helper,null)}
     * @param helper
     * @see #AbstractGLKAssemblyDataStore(HibernateGLKHelper,Integer)
     */
    public AbstractGLKTraceDataStore(HibernateGLKHelper helper) {
        this(helper, null);
    }

    /**
     * Adapt GLK into a DataStore.
     * @param helper the instance of {@link HibernateGLKHelper} to use.
     * @param version the basecall version to use, if null, then use current basecalls.
     */
    public AbstractGLKTraceDataStore(HibernateGLKHelper helper, Integer version) {
        this.helper = helper;
        this.version = version;
    }

    @Override
    public boolean contains(String id) throws DataStoreException {
        return helper.getSequence(id) != null;
    }

    protected abstract T convertBasecalls(Basecalls basecalls);

    @Override
    public T get(String id) throws DataStoreException {
        final GlkSequence sequence = getHelper().getSequence(id);
        if (sequence == null) {
            return null;
        }
        Basecalls basecalls = null;
        if (version == null) {
            basecalls = sequence.getCurrentBasecalls();
        } else {
            basecalls = sequence.getBasesByVersion().get(version);
        }
        return convertBasecalls(basecalls);
    }

    protected HibernateGLKHelper getHelper() {
        return helper;
    }

    @Override
    public CloseableIterator<String> idIterator() throws DataStoreException {
        final Iterator<String> iterator = HelperUtil.convertIteratorIntoGenericIterator(helper.getSession().createSQLQuery("select seq_name from SequenceRead").iterate());
        return CloseableIteratorAdapter.adapt(iterator);
    }

    @Override
    public long getNumberOfRecords() throws DataStoreException {
        return Integer.parseInt((String) helper.getSession().createSQLQuery("select count(seq_name) from SequenceRead").list().get(0));
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public CloseableIterator<T> iterator() {
        return new DataStoreIterator<T>(this);
    }

    @Override
    public boolean isClosed() throws DataStoreException {
        return false;
    }
}

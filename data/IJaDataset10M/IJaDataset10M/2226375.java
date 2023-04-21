package org.objectwiz.metadata.dataset;

import java.util.Collection;
import java.util.Iterator;
import org.objectwiz.metadata.Criteria;
import org.objectwiz.util.AbstractBufferedCollection;

/**
 * An abstract {@link DataSet} implementation that adds support for buffered
 * loading.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public abstract class BufferedDataSet extends DataSet {

    public static final int DEFAULT_BUFFER_SIZE = 50;

    private int bufferSize;

    protected BufferedDataSet(String fullUnitName) {
        this(fullUnitName, DEFAULT_BUFFER_SIZE);
    }

    protected BufferedDataSet(String fullUnitName, int bufferSize) {
        super(fullUnitName);
        this.bufferSize = bufferSize;
    }

    @Override
    public Iterator<DatasetRow> getRowIterator(final Criteria criteria) {
        return new AbstractBufferedCollection(bufferSize) {

            @Override
            protected Collection getElements(int firstOffset, int maxResults) {
                return getDataBlock(firstOffset, maxResults, criteria);
            }

            @Override
            public int size() {
                return (int) getRowCount(criteria);
            }
        }.iterator();
    }

    /**
     * Shall return the block of rows starting at <code>firstOffset</code> (and
     * limited to <code>maxRows</code>).
     * If less than <code>maxRows</code> are returned, this class will assume that
     * the block was the last one.
     */
    public abstract Collection<DatasetRow> getDataBlock(int firstOffset, int maxRows, Criteria criteria);
}

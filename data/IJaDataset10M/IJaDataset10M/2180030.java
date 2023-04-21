package org.objectwiz.core.representation;

import java.util.Iterator;
import java.util.List;
import org.objectwiz.core.Application;
import org.objectwiz.core.criteria.Criteria;
import org.objectwiz.core.dataset.DataSet;
import org.objectwiz.core.dataset.DataSetStructure;
import org.objectwiz.core.dataset.DatasetRow;

/**
 * Wraps a {@link DataSet} to handle type conversion.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public class DataSetRepresentationWrapper extends DataSet {

    private DataSet dataset;

    private Application application;

    private Representation representation;

    public DataSetRepresentationWrapper(DataSet dataset, Representation representation) {
        super(dataset.getApplication());
        this.dataset = dataset;
        this.representation = representation;
    }

    @Override
    public DataSetStructure getStructure() {
        return this.dataset.getStructure();
    }

    @Override
    public long getRowCount(Criteria criteria) {
        return this.dataset.getRowCount(criteria);
    }

    @Override
    public Iterator<DatasetRow> getRowIterator(Criteria criteria) {
        final Iterator<DatasetRow> it = this.dataset.getRowIterator(criteria);
        final ObjectProxy proxy = getProxy();
        return new Iterator<DatasetRow>() {

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public DatasetRow next() {
                DatasetRow row = it.next();
                return new DatasetRow(row.getId(), proxy.resolvePojos(row.getColumns()));
            }

            @Override
            public void remove() {
                it.remove();
            }
        };
    }

    @Override
    public String getHumanReadableDescription() {
        return this.dataset.getHumanReadableDescription();
    }

    @Override
    public DataSet createRepresentationWrapper(Representation representation) {
        return this;
    }

    @Override
    public List<Object[]> getUntamperedData(List<Object[]> unidentifiedData) {
        return this.dataset.getUntamperedData(getProxy().resolvePojos(unidentifiedData));
    }

    @Override
    public Object[] getColumns(Object rowId) {
        return getProxy().resolvePojos(this.dataset.getColumns(rowId));
    }

    private ObjectProxy getProxy() {
        return RepresentationBasedObjectProxy.instance(application, representation);
    }
}

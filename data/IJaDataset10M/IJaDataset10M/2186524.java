package org.datanucleus.store.mapped.scostore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ListIterator;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.store.ExecutionContext;
import org.datanucleus.store.ObjectProvider;
import org.datanucleus.store.mapped.DatastoreContainerObject;
import org.datanucleus.store.mapped.exceptions.MappedDatastoreException;
import org.datanucleus.store.mapped.mapping.EmbeddedElementPCMapping;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.mapped.mapping.ReferenceMapping;
import org.datanucleus.store.mapped.mapping.SerialisedPCMapping;
import org.datanucleus.store.mapped.mapping.SerialisedReferenceMapping;
import org.datanucleus.store.query.ResultObjectFactory;

/**
 * Abstract iterator for presenting the results for a list store.
 */
public abstract class ListStoreIterator implements ListIterator {

    private final ObjectProvider sm;

    private final ListIterator delegate;

    private Object lastElement = null;

    private int currentIndex = -1;

    private final AbstractListStore abstractListStore;

    /**
     * Constructor
     * @param sm the StateManager
     * @param resultSet the ResultSet
     * @param rof the Query.ResultObjectFactory
     * @throws SQLException
     */
    public ListStoreIterator(ObjectProvider sm, Object resultSet, ResultObjectFactory rof, AbstractListStore als) throws MappedDatastoreException {
        this.sm = sm;
        this.abstractListStore = als;
        ExecutionContext ec = sm.getExecutionContext();
        ArrayList results = new ArrayList();
        if (resultSet != null) {
            DatastoreContainerObject containerTable = als.getContainerTable();
            boolean elementsAreSerialised = als.isElementsAreSerialised();
            boolean elementsAreEmbedded = als.isElementsAreEmbedded();
            JavaTypeMapping elementMapping = als.getElementMapping();
            while (next(resultSet)) {
                Object nextElement;
                if (elementsAreEmbedded || elementsAreSerialised) {
                    int param[] = new int[elementMapping.getNumberOfDatastoreMappings()];
                    for (int i = 0; i < param.length; ++i) {
                        param[i] = i + 1;
                    }
                    if (elementMapping instanceof SerialisedPCMapping || elementMapping instanceof SerialisedReferenceMapping || elementMapping instanceof EmbeddedElementPCMapping) {
                        int ownerFieldNumber = -1;
                        if (containerTable != null) {
                            ownerFieldNumber = getOwnerMemberMetaData(abstractListStore.containerTable).getAbsoluteFieldNumber();
                        }
                        nextElement = elementMapping.getObject(ec, resultSet, param, sm, ownerFieldNumber);
                    } else {
                        nextElement = elementMapping.getObject(ec, resultSet, param);
                    }
                } else if (elementMapping instanceof ReferenceMapping) {
                    int param[] = new int[elementMapping.getNumberOfDatastoreMappings()];
                    for (int i = 0; i < param.length; ++i) {
                        param[i] = i + 1;
                    }
                    nextElement = elementMapping.getObject(ec, resultSet, param);
                } else {
                    nextElement = rof.getObject(ec, resultSet);
                }
                results.add(nextElement);
            }
        }
        delegate = results.listIterator();
    }

    public void add(Object o) {
        currentIndex = delegate.nextIndex();
        abstractListStore.add(sm, o, currentIndex, -1);
        delegate.add(o);
        lastElement = null;
    }

    public boolean hasNext() {
        return delegate.hasNext();
    }

    public boolean hasPrevious() {
        return delegate.hasPrevious();
    }

    public Object next() {
        currentIndex = delegate.nextIndex();
        lastElement = delegate.next();
        return lastElement;
    }

    public int nextIndex() {
        return delegate.nextIndex();
    }

    public Object previous() {
        currentIndex = delegate.previousIndex();
        lastElement = delegate.previous();
        return lastElement;
    }

    public int previousIndex() {
        return delegate.previousIndex();
    }

    public synchronized void remove() {
        if (lastElement == null) {
            throw new IllegalStateException("No entry to remove");
        }
        abstractListStore.remove(sm, currentIndex, -1);
        delegate.remove();
        lastElement = null;
        currentIndex = -1;
    }

    public synchronized void set(Object o) {
        if (lastElement == null) {
            throw new IllegalStateException("No entry to replace");
        }
        abstractListStore.set(sm, currentIndex, o, true);
        delegate.set(o);
        lastElement = o;
    }

    /**
     * Method to move to the next row in the results.
     * @param resultSet The result set
     * @return Whether there is a next row
     * @throws MappedDatastoreException
     */
    protected abstract boolean next(Object resultSet) throws MappedDatastoreException;

    /**
     * Method to return the owner member metadata for the supplied table
     * @param containerTable The table for the list
     * @return the metadata for the owner member
     */
    protected abstract AbstractMemberMetaData getOwnerMemberMetaData(DatastoreContainerObject containerTable);
}

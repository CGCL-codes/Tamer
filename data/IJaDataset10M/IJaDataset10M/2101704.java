package org.exolab.jms.tools.migration;

import javax.jms.JMSException;
import org.exolab.jms.persistence.PersistenceException;

/**
 * <code>Store</code> represents a persistent collection.
 * It provides an API for importing and exporting the collection.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/04 07:07:12 $
 */
public interface Store {

    /**
     * Export the collection.
     *
     * @return an iterator over the collection
     * @throws JMSException for any JMS error
     * @throws PersistenceException for any persistence error
     */
    StoreIterator exportCollection() throws JMSException, PersistenceException;

    /**
     * Import a collection.
     *
     * @param iterator an iterator over the collection
     * @throws JMSException for any JMS error
     * @throws PersistenceException for any persistence error
     */
    void importCollection(StoreIterator iterator) throws JMSException, PersistenceException;

    /**
     * Returns the number of elements in the collection.
     *
     * @return the number of elements in the collection
     * @throws PersistenceException for any persistence error
     */
    int size() throws PersistenceException;
}

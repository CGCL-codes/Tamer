package org.objectstyle.cayenne.access;

import java.util.Collection;
import org.objectstyle.cayenne.map.DataMap;
import org.objectstyle.cayenne.map.EntityResolver;

/**
 * Defines methods used to run Cayenne queries.
 * <p>
 * <i>For more information see <a href="../../../../../../userguide/index.html"
 * target="_top">Cayenne User Guide. </a> </i>
 * </p>
 * 
 * @author Andrei Adamchik
 */
public interface QueryEngine {

    /**
     * Executes queries in the transactional context provided by the caller. It is
     * caller's responsibility to commit or rollback the Transaction and close any
     * connections that were added to it.
     * 
     * @since 1.1
     * @deprecated since 1.2 as thread-bound transactions are used.
     */
    public void performQueries(Collection queries, OperationObserver resultConsumer, Transaction transaction);

    /**
     * Executes a list of queries wrapping them in its own transaction. Results of
     * execution are passed to {@link OperationObserver}object via its callback methods.
     * 
     * @since 1.1 The signiture has changed from List to Collection.
     */
    public void performQueries(Collection queries, OperationObserver resultConsumer);

    /**
     * Returns a DataNode that should handle queries for all DataMap components.
     * 
     * @since 1.1
     * @deprecated since 1.2 not a part of the interface. Only DataDomain has meaningful
     *             implementation.
     */
    public DataNode lookupDataNode(DataMap dataMap);

    /**
     * Returns a resolver for this query engine that is capable of resolving between
     * classes, entity names, and obj/db entities
     */
    public EntityResolver getEntityResolver();

    /**
     * Returns a collection of DataMaps associated with this QueryEngine.
     * 
     * @deprecated since 1.2. Use 'getEntityResolver().getDataMaps()' instead.
     */
    public Collection getDataMaps();
}

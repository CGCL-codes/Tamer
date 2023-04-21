package org.datanucleus.store.rdbms.datasource.dbcp.pool;

/**
 * A factory interface for creating {@link ObjectPool}s.
 *
 * @see ObjectPool
 *
 * @author Rodney Waldhoff
 * @version $Revision: 777748 $ $Date: 2009-05-22 20:00:44 -0400 (Fri, 22 May 2009) $
 * @since Pool 1.0
 */
public interface ObjectPoolFactory {

    /**
     * Create and return a new {@link ObjectPool}.
     * @return a new {@link ObjectPool}
     * @throws IllegalStateException when this pool factory is not configured properly
     */
    ObjectPool createPool() throws IllegalStateException;
}

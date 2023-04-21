package idao.engine;

import idao.ConnectionManager;

/**
 * Build class used to create a DAO proxy.
 *
 * @author HuHao
 */
public interface DaoProxyBuilder {

    /**
     * Create a new proxy for the given DAO type.
     *
     * @param <T> type of the DAO interface
     * @param dao Class of the DAO interface
     * @param cm connection manager to get connection
     * @return new proxy
     */
    <T> T newDaoProxy(Class<T> dao, ConnectionManager cm);
}

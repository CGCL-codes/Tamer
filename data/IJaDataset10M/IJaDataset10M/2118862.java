package ursus.server.persistence;

/**
 * Use persistence manager to retrieve the global ConnectionPool and DAOFactory.
 *
 * @author Anthony
 */
public class PersistenceManager {

    protected static ConnectionPool pool;

    protected static DAOFactory factory;

    public static void init(DataSource source) {
        factory = new DAOFactory_impl();
        source.setCorePool(10);
        source.setKeepAlive(5000);
        source.setSleep(10000);
        pool = new ConnectionPool_impl(source);
        try {
            pool.init();
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionPool getConnectionPool() {
        return pool;
    }

    public static DAOFactory getDAOFactory() {
        return factory;
    }
}

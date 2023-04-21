package bill.util.persist;

/**
 * Generic Factory for entities. It defines a couple methods that specific
 * subclass factories must use.
 */
public abstract class EntityDataFactory {

    /** This variable holds an instance of a class that will allow us access to
       our persistent store, such as a java.sql.Connection. */
    protected static Object _connection = null;

    /**
    * Set the connection object's value.
    *
    * @param connection The connection object to use.
    */
    public static void setConnection(Object connection) {
        _connection = connection;
    }

    /**
    * Retrieves the connection object's value.
    *
    * @return The connection object.
    */
    public static Object getConnection() {
        return _connection;
    }
}

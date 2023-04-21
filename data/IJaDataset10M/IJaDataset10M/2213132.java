package de.mse.mogwai.utils.erdesigner.utils;

import java.util.HashMap;
import de.mse.mogwai.utils.erdesigner.database.DatabaseConnection;
import de.mse.mogwai.utils.erdesigner.types.EntityContainer;

/**
 * Factory for database connections.
 * 
 * @author Mirko Sertic
 */
public class DatabaseConnectionFactory {

    public static HashMap CONNECTIONS = new HashMap();

    /**
	 * Register a database connection.
	 */
    public static void registerConnection(String name, String cl) {
        CONNECTIONS.put(name, cl);
    }

    /**
	 * This is a factory class, so no direct instance is available.
	 */
    private DatabaseConnectionFactory() {
    }

    /**
	 * Create a new database connection.
	 * 
	 * @param name
	 *            the connection name.
	 * @param parent
	 *            the entity container
	 * @return a new DatabaseConnection or null if the connection could not be
	 *         created
	 */
    public static DatabaseConnection createConnection(String name, EntityContainer parent) {
        try {
            Class cl = Class.forName(CONNECTIONS.get(name).toString());
            return (DatabaseConnection) cl.getConstructor(new Class[] { EntityContainer.class }).newInstance(new Object[] { parent });
        } catch (Exception e) {
        }
        return null;
    }
}

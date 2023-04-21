package it.battlehorse.rcp.tools.dbbase;

import java.io.Serializable;

/**
 * Collects all the properties needed to fully qualify a datasource connection.
 * This class is purposefully simple, in order to avoid configuration of advanced
 * datasource details (es. : max num of idle connections , validation query ... )
 * which are rarely useful in the supposed platform classic usage (not db-intensive).
 * <p>
 * Different datasource configurations are uniquely identified by their {@code name}
 * property.
 * 
 * @author battlehorse
 * @since Nov 21, 2005
 */
public class DatasourceConfig implements Serializable {

    /**
	 * The serial version UID for serialization
	 */
    private static final long serialVersionUID = 6738948049814982923L;

    /**
	 * The configuration name. This name is used by clients to retrieve a
	 * connection from this configuration
	 */
    private String name;

    /**
	 * The connection URl to the database
	 */
    private String connectionUrl;

    /**
	 * The connection user name
	 */
    private String user;

    /**
	 * The connection password
	 */
    private String password;

    /**
	 * The connection database. Must be one present in {@code rdbms.properties}
	 */
    private DbConfig database;

    /**
	 * Create an explicitly unconfigured instance of the class. This constructor exists
	 * only for serialization / deserialization purposes.
	 *
	 */
    public DatasourceConfig() {
    }

    /**
	 * Creates a new instance of the class
	 * 
	 * @param database The connection database
	 * @param connectionUrl The connection URl to the database
	 * @param user The connection user name
	 * @param password The connection password
	 * @param name The configuration name
	 * @throws IllegalArgumentException if a {@code null} name is provided
	 */
    public DatasourceConfig(DbConfig database, String connectionUrl, String user, String password, String name) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("Datasource name cannot be null");
        this.name = name;
        this.connectionUrl = connectionUrl;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    /**
	 * @return Returns the connectionUrl.
	 */
    public String getConnectionUrl() {
        return connectionUrl;
    }

    /**
	 * @return Returns the database.
	 */
    public DbConfig getDatabase() {
        return database;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return Returns the password.
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @return Returns the user.
	 */
    public String getUser() {
        return user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DatasourceConfig)) return false;
        DatasourceConfig other = (DatasourceConfig) obj;
        return other.getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

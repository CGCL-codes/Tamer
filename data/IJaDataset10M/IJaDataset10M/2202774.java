package insta.tp.persistence;

/**
 * This interface lists all the constants that the system uses to get a Database
 * connection.
 */
public interface DataAccessConstants {

    /**
	 * Database error code when we want to insert an id that already exists.
	 */
    int DATA_ALREADY_EXIST = 1062;

    String JDBC_DRIVER = "org.hsqldb.jdbcDriver";

    String URL_DB = "jdbc:hsqldb:file:src/main/resources/data/librairieDb";

    /**
	 * Username to access the database.
	 */
    String USER_DB = "sa";

    /**
	 * Password to access the database.
	 */
    String PASSWD_DB = "";
}

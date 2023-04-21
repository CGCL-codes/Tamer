package dao.jdbc;

import java.sql.Connection;
import dao.*;

/**
 * Factory class for the creation of data access objects using the jdbc technology.
 *  
 * @author damo
 * @version 0.1
 */
public class JdbcDAOFactory extends DAOFactory {

    /**
	 * Returns a data access object for a Document using jdbc.
	 */
    public DocumentDAO getDocumentDAO(Connection c) {
        return new DocumentJdbcDAO(c);
    }

    /**
	 * Returns a data access object for a Keyword using jdbc.
	 */
    public KeywordDAO getKeywordDAO(Connection c) {
        return new KeywordJdbcDAO(c);
    }

    public ConnectionManager getConnectionManager() {
        return new JdbcConnectionManager();
    }
}

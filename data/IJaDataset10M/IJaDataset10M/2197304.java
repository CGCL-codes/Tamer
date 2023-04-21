package net.sourceforge.simplejdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Define interface for the callback. Use JdbcTemplace helper methods to return
 * List or T object in classical cases.
 * 
 * @return object to match all possible use cases.
 */
public interface TransactionCallback {

    public Object doInTransaction(Connection connection) throws SQLException;
}

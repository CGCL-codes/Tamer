package bravo.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Represents something that can be committed to the database.
 */
public interface Committable extends Undoable {

    /**
	 * Return the SQL statements for this transaction.
	 */
    public List<PreparedStatement> getSQL(Connection dbConn) throws SQLException;

    public String toString();
}

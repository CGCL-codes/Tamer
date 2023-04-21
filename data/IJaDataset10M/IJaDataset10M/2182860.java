package net.sourceforge.squirrel_sql.client.session;

import java.sql.SQLException;
import javax.swing.Action;
import net.sourceforge.squirrel_sql.client.session.mainpanel.objecttree.INodeExpander;

/**
 * The current session.
 */
public interface IClientSession extends ISession {

    /**
	 * Close this session.
	 */
    void close();

    /**
	 * Close the current connection to the database.
	 *
	 * @throws	SQLException  if an SQL error occurs.
	 */
    void closeSQLConnection() throws SQLException;

    /**
	 * Set the session sheet for this session.
	 * 
	 * @param	sheet	Sheet for this session.
	 */
    void setSessionSheet(SessionSheet child);
}

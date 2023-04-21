package net.jforum.dao;

import java.sql.Connection;
import net.jforum.entities.UserSession;

/**
 * @author Rafael Steil
 * @version $Id: UserSessionDAO.java,v 1.6 2006/08/23 02:13:34 rafaelsteil Exp $
 */
public interface UserSessionDAO {

    /**
	 * Writes a new <code>UserSession</code> to the database.
	 * 
	 * @param us The <code>UserSession</code> to store
	 * @param conn The {@link java.sql.Connection} object to use. 
	 * As many times user session management will be done in places where 
	 * a valid request is not available, we cannot try to retrieve the 
	 * conneciton from the thread local implementation. <br>
	 * If any driver implementation of this method will not use a database
	 * ( eg, where a <code>Connection</code> is not required ), when just
	 * pass <code>null</code> as argument.
	 */
    public void add(UserSession us, Connection conn);

    /**
	 * Updates an <code>UserSession</code> 
	 * 
	 * @param us The <code>UserSession</code> to update
	 * @param conn The {@link java.sql.Connection} object to use. 
	 * As many times user session management will be done in places where 
	 * a valid request is not available, we cannot try to retrieve the 
	 * conneciton from the thread local implementation. <br>
	 * If any driver implementation of this method will not use a database
	 * ( eg, where a <code>Connection</code> is not required ), when just
	 * pass <code>null</code> as argument.

	 */
    public void update(UserSession us, Connection conn);

    /**
	 * Gets an <code>UserSession</code> from the database.
	 * The object passed as argument should at least have the user id 
	 * in order to find the correct register. 
	 * 
	 * @param us The complete <code>UserSession</code> object data
	 * @param conn The {@link java.sql.Connection} object to use. 
	 * As many times user session management will be done in places where 
	 * a valid request is not available, we cannot try to retrieve the 
	 * conneciton from the thread local implementation. <br>
	 * If any driver implementation of this method will not use a database
	 * ( eg, where a <code>Connection</code> is not required ), when just
	 * pass <code>null</code> as argument.
	 * 
	 * @return UserSession
	 */
    public UserSession selectById(UserSession us, Connection conn);
}

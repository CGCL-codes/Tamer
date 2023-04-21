package org.wsp.dao.jar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*******
 * 
 * Author kumar
 * Created On Dec 5, 2007
 * WSPConnectionFactory.java
 * 
 * 
 * Project Name : wsp
 * 
 * This software is provided "AS IS," without a warranty of any kind. 
 *
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED.
 */
public class WSPConnectionFactory {

    private static Connection connection = null;

    /***
	 * 
	 */
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/wsp", "root", "root");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
	 * 
	 *
	 */
    private WSPConnectionFactory() {
    }

    /*****
	 * **
	 * Dec 5, 2007
	 * @return
	 * WSPConnectionFactory
	 * WSPConnectionFactory
	 * created by kumar
	 * TODO
	 */
    public static WSPConnectionFactory getInstance() {
        return new WSPConnectionFactory();
    }

    /******
	 * **
	 * Dec 5, 2007
	 * @return
	 * WSPConnectionFactory
	 * Connection
	 * created by kumar
	 * TODO
	 */
    public Connection getConnection() {
        return connection;
    }
}

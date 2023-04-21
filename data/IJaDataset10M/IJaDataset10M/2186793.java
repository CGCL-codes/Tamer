package org.aubit4gl.remote_client.connection;

/**
 * Driver manager to handle how to make connections from a 4gl
 * UI client to a 4gl program acting as a XML client.
 * 
 * The basic ideas come from DriverManager of JDBC.
 *  
 * @author S�rgio
 *
 */
public class DriverManager {

    /**
	 * Establish a connection to a 4gl program.
	 * In the momment of this writing uses ssh to start the program.
	 * 
	 * @param hostname
	 * @param userName
	 * @param password
	 * @param programName
	 * @return The connection
	 * @throws FGLUIException If cannot connect to the 4gl remote program
	 */
    public static Connection getConnection(String hostname, String userName, String password, String programName) throws FGLUIException {
        Driver driver = new Aubit4glDriver();
        Connection conn = driver.connect(hostname, userName, password, programName);
        return conn;
    }
}

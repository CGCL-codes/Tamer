package org.ladon.userserver;

import org.ladon.userserver.exceptions.*;
import org.ladon.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.io.OutputStream;
import java.sql.*;
import java.util.*;

/** 
 * This Server Stores a list of all the users on the system.
 * @author Jonathan Shriver-Blake
 * @version .02
 **/
public class UserServer extends UnicastRemoteObject implements UserServerInterface {

    /** A URL referring to our Database.
    *
    * Note that with the mm-mysql driver it should look something like this.
    *
    * jdbc:mysql:server.hostname.domain:port?user=name&password=pass
    */
    String mDataBaseURL;

    /**
     * The Custom Properties for this program
     **/
    Properties mProperties;

    /** Default Constructor
    * @throws RemoteException
    */
    public UserServer() throws RemoteException {
        super(0, new RMISSLClientSocketFactory(), new RMISSLServerSocketFactory());
        mProperties = PropertyTool.loadProperties();
        mDataBaseURL = mProperties.getProperty("ladon.userServer.dataBase");
    }

    /** This method is called remotely by a project server to register itself.
     *
     * @param desc A description of the Project to be registered
     * @throws RemoteException
     * @throws SQLException
     * @throws UserNameConflictException if that username is taken
     * @throws UserAlreadYRegisteredException if the user has registered before
     *
     */
    public void register(ClientDescriptor desc) throws RemoteException, UserAlreadyRegisteredException, UserNameConflictException {
        try {
            Connection dbConnection = DriverManager.getConnection(mDataBaseURL);
            Statement query = dbConnection.createStatement();
            ResultSet result = query.executeQuery("SELECT username, " + "password, email, realname from users " + "where username = '" + desc.getUserName() + "'");
            if (result.next()) {
                if ((result.getString("password").compareTo(desc.getPassword()) == 0) && (result.getString("email").compareTo(desc.getEmail()) == 0) && (result.getString("realname").compareTo(desc.getRealName()) == 0)) {
                    throw (new UserAlreadyRegisteredException("This user has already registered"));
                } else {
                    throw (new UserNameConflictException("This Username is already in use"));
                }
            } else {
                int results = query.executeUpdate("INSERT INTO users (username, password, email, realname, state) " + "VALUES('" + desc.getUserName() + "', '" + desc.getPassword() + "', '" + desc.getEmail() + "', '" + desc.getRealName() + "', 'active')");
            }
        } catch (SQLException e) {
            System.out.println("Exception while registering user: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /** 
    * This method is called by project server to verify a user.
    *  
    * @param desc, The description of the user to be verified.
    *
    * @throws RemoteException
    * @throws SQLException
    * @return users id if he exists 0 otherwise
    */
    public int isValidUser(ClientDescriptor desc) throws RemoteException {
        try {
            Connection dbConnection = DriverManager.getConnection(mDataBaseURL);
            Statement query = dbConnection.createStatement();
            String select = "SELECT (userid) From users where " + "username = '" + desc.getUserName() + "' AND " + "password = '" + desc.getPassword() + "' AND " + "email = '" + desc.getEmail() + "' AND " + "realname = '" + desc.getRealName() + "' AND " + "state = 'active'";
            ResultSet results = query.executeQuery(select);
            if (results.first() == false) return 0; else {
                return results.getInt("UserID");
            }
        } catch (SQLException e) {
            System.out.println("SQL exception in isValidUser: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    /** 
    * This command updates a User,
    * Note that the username can never be changed.
    * @param desc The New Description.
    *
    * Note that the Username and password must refer to a valid entry and can not be
    * changed with this function.
    *
    * @throws RemoteException
    * @throws SQLException
    * @throws InvalidUserException
    */
    public void update(ClientDescriptor desc) throws RemoteException, InvalidUserException {
        try {
            Connection dbConnection = DriverManager.getConnection(mDataBaseURL);
            Statement query = dbConnection.createStatement();
            String update = "UPDATE users SET email= '" + desc.getEmail() + "', realname = '" + desc.getRealName() + "' WHERE username = '" + desc.getUserName() + "' AND password = '" + desc.getPassword() + "'";
            System.out.println(update);
            int rows = query.executeUpdate(update);
            if (rows == 0) throw new InvalidUserException("User not foud to update");
        } catch (SQLException e) {
            System.out.println("Exception while returning data: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * This method changes a users password
     *
     * @param usernName The clients username
     * @param oldPassword The clients oldPassword
     * @param newPassword The cleints newPassword
     */
    public void ChangePassword(String userName, String oldPassword, String newPassword) throws RemoteException, InvalidUserException {
        try {
            Connection dbConnection = DriverManager.getConnection(mDataBaseURL);
            Statement query = dbConnection.createStatement();
            String update = "UPDATE users SET password= '" + newPassword + "' WHERE username = '" + userName + "' AND password = '" + newPassword + "'";
            System.out.println(update);
            int rows = query.executeUpdate(update);
            if (rows == 0) throw new InvalidUserException("User not foud to update");
        } catch (SQLException e) {
            System.out.println("Exception in ChangePassword: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /** Creates and Registers the IndexServer
    * @param argv Command Line Arguments.
    * argv[0] should by the url of the databse.
    */
    public static void main(String argv[]) {
        try {
            Class.forName("org.gjt.mm.mysql.Driver").newInstance();
        } catch (Exception E) {
            System.err.println("Unable to load driver.");
            E.printStackTrace();
        }
        try {
            String name = "UserServer";
            System.out.println("Registering User Server as " + name + "...");
            UserServer server = new UserServer();
            Naming.rebind(name, server);
            System.out.println("User Server Ready.");
        } catch (Exception e) {
            System.out.println("Exception while Registering User Server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

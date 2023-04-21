package de.ios.framework.db;

import java.sql.*;
import de.ios.framework.basic.*;

public interface DBResultSet extends java.rmi.Remote {

    /**
   * Get next Record
   */
    public DBObject next() throws java.rmi.RemoteException, java.sql.SQLException;

    /**
   * Get previous Record
   */
    public DBObject prev() throws java.rmi.RemoteException;

    /**
   * Get current Record
   */
    public DBObject current() throws java.rmi.RemoteException;
}

package com.daffodilwoods.tools;

import java.rmi.*;
import java.sql.*;
import java.sql.Connection;
import java.util.*;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.rmi.*;
import com.daffodilwoods.rmi.interfaces.*;
import in.co.daffodil.db.jdbc.*;

/**
 * <p>Title: Daffodil DB backup and restore utility.</p>
     * <p>Description: Backup and restore utility for online and offline backup.</p>
 * <p>Copyright: Daffodil software Limited (INDIA)</p>
 * <p>Company:Daffodil Software Limited. (INIDA) </p>
 * <p><br>
 * First version date : Feb. 5 , 2005
 * @author Manoj Kr. Sheoran < manoj.kumar@daffodildb.com >
 * @version 1.0
 */
public class BackupUtility extends AbstractDaffodilDBDriver {

    private int workingMode = EMBEDDED_MODE;

    private boolean isOffline = false;

    private String hostName = "localhost";

    private int portNumber = 3456;

    public static int EMBEDDED_MODE = 0;

    public static int SERVER_MODE = 1;

    private boolean verbose = false;

    public BackupUtility() {
    }

    public Connection getConnection(String driverClass, String url) throws ClassNotFoundException, SQLException {
        Class.forName(driverClass);
        return DriverManager.getConnection(url);
    }

    public void setWorkingMode(int mode) {
        workingMode = mode;
    }

    protected _Server getServer(String url) throws SQLException {
        _Server server = super.server;
        if (server != null) return server;
        if (!isOffline) {
            throw new SQLException("Connect before taking online backup...");
        }
        try {
            if (workingMode == SERVER_MODE) {
                _RmiServer rmiServer = (_RmiServer) Naming.lookup("rmi://" + hostName + ":" + portNumber + "/DaffodilDB");
                return new RmiServer(rmiServer);
            } else {
                return ((_Server) Class.forName("com.daffodilwoods.daffodildb.server.serversystem.ServerSystem").newInstance());
            }
        } catch (Exception ex) {
            throw new SQLException("DaffodilDB Engine not found.....");
        }
    }

    protected Properties breakURL(String string, Properties properties) throws SQLException {
        throw new UnsupportedOperationException("Method breakURL yet not implemented");
    }

    protected Connection getConnection(String string, Properties properties) throws SQLException {
        throw new UnsupportedOperationException("Method getConnection yet not implemented");
    }

    public boolean acceptsURL(String string) throws SQLException {
        throw new UnsupportedOperationException("Method acceptsURL yet not implemented");
    }

    public DriverPropertyInfo[] getPropertyInfo(String string, Properties properties) throws SQLException {
        throw new UnsupportedOperationException("Method getPropertyInfo yet not implemented");
    }

    public void takeOfflinebakupServer(String hostName, int port, String userName, String password, String databaseNameSource, String databaseNameDestination, String path, boolean overwrite) throws SQLException {
        print("Going to take offline backup ........");
        this.hostName = hostName;
        this.portNumber = port;
        try {
            this.isOffline = true;
            getServer("DaffodilDB").offlineBackup(userName, password, path, databaseNameSource, databaseNameDestination, overwrite);
        } catch (DException ex) {
            throw new SQLException(ex.getMessage());
        }
        print("Backup taken successfully............");
    }

    public void takeOfflinebakupEmbedded(String sourcePath, String userName, String password, String databaseNameSource, String databaseNameDestination, String path, boolean overwrite) throws SQLException {
        print("Going to take offline backup ........");
        try {
            this.isOffline = true;
            System.setProperty("daffodilDB_home", sourcePath);
            getServer("DaffodilDB").offlineBackup(userName, password, path, databaseNameSource, databaseNameDestination, overwrite);
        } catch (DException ex) {
            throw new SQLException(ex.getMessage());
        }
        print("Backup taken successfully............");
    }

    public void takeOnlineBackup(String path, String sourceDatabase, String destinationDatabase, boolean overwrite) throws SQLException {
        print("Going to take offline backup ........");
        try {
            getServer("DaffodilDB").getInconsistentOnlineBackup(path, sourceDatabase, destinationDatabase, overwrite);
        } catch (DException ex) {
            throw new SQLException(ex.getMessage());
        }
        print("Backup taken successfully............");
    }

    public void restoreBackup(String userName, String password, String databaseNameSource, String databaseNameDestination, String path, boolean overwrite) throws SQLException {
        print("Going to take offline backup ........");
        try {
            getServer("DaffodilDB").restore(userName, password, path, databaseNameSource, databaseNameDestination, overwrite);
        } catch (DException ex) {
            throw new SQLException(ex.getMessage());
        }
        print("Backup taken successfully............");
    }

    private void print(String str) {
        if (verbose) ;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}

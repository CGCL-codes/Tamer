package easyJ.database.connection;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import easyJ.common.EasyJException;

public class ConnectionControllerTomcatImpl implements ConnectionController, java.io.Serializable {

    private ConnectionControllerTomcatImpl() {
    }

    private static ConnectionControllerTomcatImpl instance;

    private static String dsName;

    private static Context ic = null;

    private static DataSource ds = null;

    public static ConnectionController getInstance() {
        if (instance == null) instance = new ConnectionControllerTomcatImpl();
        return instance;
    }

    public static void initialContext() throws EasyJException {
        if (ic != null) return;
        try {
            Context initCtx = new InitialContext();
            ic = (Context) initCtx.lookup("java:comp/env");
        } catch (NamingException ne) {
            ne.printStackTrace();
            String clientMessage = "服务器忙";
            String location = "easyJ.database.connection.ConnectionControllerTomcatImpl.getEnvironmentContext()";
            String logMessage = "can't find the environment with JNDI name 'java:comp/env' Please make sure the datasource is configured with the JNDI name.";
            EasyJException ee = new EasyJException(ne, location, logMessage, clientMessage);
            throw ee;
        }
    }

    private static void lookupDataSource() throws EasyJException {
        if (ds == null) {
            initialContext();
            try {
                ds = (DataSource) ic.lookup(dsName);
            } catch (NamingException ne) {
                ne.printStackTrace();
                String clientMessage = "服务器忙";
                String location = "easyJ.database.connection.ConnectionControllerTomcatImpl.lookupDataSource()";
                String logMessage = "can't find the datasource with JNDI name " + dsName + ". Please make sure the datasource is configured with the JNDI name.";
                EasyJException ee = new EasyJException(ne, location, logMessage, clientMessage);
                throw ee;
            } catch (Exception e) {
                if (e instanceof EasyJException) {
                    throw (EasyJException) e;
                }
                e.printStackTrace();
                String clientMessage = "服务器忙";
                String location = "easyJ.database.connection.ConnectionControllerTomcatImpl.lookupDataSource()";
                String logMessage = "Unexcepted exception occurs, can't initial context because of exception:" + e.getMessage();
                EasyJException ee = new EasyJException(e, location, logMessage, clientMessage);
                throw ee;
            }
        }
    }

    public Connection getConnection() throws EasyJException {
        Connection conn = null;
        try {
            if (ds == null) lookupDataSource();
            conn = ds.getConnection();
            return conn;
        } catch (SQLException se) {
            se.printStackTrace();
            String clientMessage = "服务器忙";
            String location = "easyJ.database.connection.ConnectionControllerTomcatImpl.getConnection()";
            String logMessage = "can't get connection because of SQLException: " + se.getMessage();
            EasyJException ee = new EasyJException(se, location, logMessage, clientMessage);
            throw ee;
        }
    }
}

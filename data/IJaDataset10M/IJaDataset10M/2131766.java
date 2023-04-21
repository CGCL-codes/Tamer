package org.dbreplicator.replication;

import java.sql.*;
import org.dbreplicator.replication.DBHandler.*;
import org.apache.log4j.Logger;
import java.io.*;

/**
 * This class is used for finding out the database type. And according to the
 * identified database the handeling is done differently.
 */
public class Utility {

    public static int insertCount = 0;

    public static final int DataBase_General = 0;

    public static final int DataBase_DaffodilDB = 1;

    public static final int DataBase_Oracle = 2;

    public static final int DataBase_SqlServer = 3;

    public static final int DataBase_PointBase = 4;

    public static final int DataBase_Cloudscape = 5;

    public static final int DataBase_PostgreSQL = 6;

    public static final int DataBase_DB2 = 7;

    public static final int DataBase_Sybase = 8;

    public static final int DataBase_Firebird = 9;

    public static final int DataBase_MySQL = 10;

    public static final int CommonMetaDataInfo = 1;

    public static final int pgMetaDataInfo = 2;

    public static final int CloudScapeMetaDataInfo = 3;

    private static String Daffodil_ProductName = "DaffodilDB";

    private static String Oracle_ProductName = "Oracle";

    private static String SqlServer_ProductName = "Microsoft SQL Server";

    private static String PointBase_ProductName = "PointBase";

    private static String Cloudscape_ProductName = "Apache Derby";

    private static String PostgreSQL_ProductName = "PostgreSQL";

    private static String DB2_ProductName = "DB2/NT";

    private static String DB2_6000_ProductName = "DB2/6000";

    private static String DB2_AS_400_ProductName = "DB2 UDB";

    private static String Sybase_ASE = "Adaptive Server Enterprise";

    private static String Sybase_ASA = "Adaptive Server AnyWhere";

    public static String FireBird_ProductName = "Firebird";

    public static String MySQL_ProductName = "MySQL";

    public static boolean createTransactionLogFile = true;

    protected static Logger log = Logger.getLogger(Utility.class.getName());

    /**
   * This method returns the perticular object of Handler for perticular
   * database. So that different database operations can be handeled
   * differently.
   *
   * @param connectionPool
   * @param pubsubName
   * @return DatabaseHandler
   * @throws RepException
   */
    public static AbstractDataBaseHandler getDatabaseHandler(ConnectionPool connectionPool, String pubsubName) throws RepException {
        Connection con = connectionPool.getConnection(pubsubName);
        String vendorName = getVendorName(con);
        connectionPool.returnConnection(con);
        log.info(vendorName);
        if (vendorName.equalsIgnoreCase(PointBase_ProductName)) {
            return new PointBaseHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(Daffodil_ProductName)) {
            return new DaffodilDBHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(Oracle_ProductName)) {
            return new OracleHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(Cloudscape_ProductName)) {
            return new CloudScapeHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(SqlServer_ProductName)) {
            return new SQLServerHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(PostgreSQL_ProductName)) {
            return new PostgreSQLHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(Sybase_ASA) || vendorName.equalsIgnoreCase(Sybase_ASE)) {
            return new SybaseHandler(connectionPool);
        } else if (vendorName.toUpperCase().startsWith("DB2")) {
            return new DB2Handler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(DB2_6000_ProductName)) {
            return new DB2Handler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(FireBird_ProductName)) {
            return new FireBirdDatabaseHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(MySQL_ProductName)) {
            return new MYSQLHandler(connectionPool);
        } else {
            return new GeneralDataBaseHandler(connectionPool);
        }
    }

    public static AbstractDataBaseHandler getDatabaseHandler(ConnectionPool connectionPool, Connection connection) throws RepException {
        String vendorName = getVendorName(connection);
        if (vendorName.equalsIgnoreCase(PointBase_ProductName)) {
            return new PointBaseHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(Daffodil_ProductName)) {
            return new DaffodilDBHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(Oracle_ProductName)) {
            return new OracleHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(Cloudscape_ProductName)) {
            return new CloudScapeHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(SqlServer_ProductName)) {
            return new SQLServerHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(Sybase_ASA) || vendorName.equalsIgnoreCase(Sybase_ASE)) {
            return new SybaseHandler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(PostgreSQL_ProductName)) {
            return new PostgreSQLHandler(connectionPool);
        } else if (vendorName.toUpperCase().equalsIgnoreCase("DB2")) {
            return new DB2Handler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(DB2_6000_ProductName)) {
            return new DB2Handler(connectionPool);
        } else if (vendorName.equalsIgnoreCase(MySQL_ProductName)) {
            return new MYSQLHandler(connectionPool);
        } else {
            return new GeneralDataBaseHandler(connectionPool);
        }
    }

    public static MetaDataInfo getDatabaseMataData(Connection connection) throws RepException {
        String vendorName = getVendorName(connection);
        MetaDataInfo metaData = null;
        if (vendorName.equalsIgnoreCase(PointBase_ProductName)) {
            metaData = new CommonMetaDataInfo(connection);
        } else if (vendorName.equalsIgnoreCase(Daffodil_ProductName)) {
            metaData = new CommonMetaDataInfo(connection);
        } else if (vendorName.equalsIgnoreCase(Oracle_ProductName)) {
            metaData = new CommonMetaDataInfo(connection);
        } else if (vendorName.equalsIgnoreCase(Cloudscape_ProductName)) {
            metaData = new CloudscapeMataDataInfo(connection);
        } else if (vendorName.equalsIgnoreCase(SqlServer_ProductName)) {
            metaData = new CommonMetaDataInfo(connection);
        } else if (vendorName.equalsIgnoreCase(PostgreSQL_ProductName)) {
            metaData = new pgMetaDataInfo(connection);
        } else if (vendorName.toUpperCase().equalsIgnoreCase("DB2")) {
            metaData = new CommonMetaDataInfo(connection);
        } else if (vendorName.toUpperCase().equalsIgnoreCase("Adaptive Server Anywhere") || vendorName.toUpperCase().equalsIgnoreCase("Adaptive Server Enterprise")) {
            metaData = new pgMetaDataInfo(connection);
        } else if (vendorName.equalsIgnoreCase(DB2_6000_ProductName)) {
            metaData = new CommonMetaDataInfo(connection);
        } else if (vendorName.equalsIgnoreCase(DB2_AS_400_ProductName)) {
            metaData = new CommonMetaDataInfo(connection);
        } else if (vendorName.equalsIgnoreCase("Firebird")) {
            metaData = new CommonMetaDataInfo(connection);
        } else if (vendorName.equalsIgnoreCase(MySQL_ProductName)) {
            metaData = new CommonMetaDataInfo(connection);
        } else {
            metaData = new CommonMetaDataInfo(connection);
        }
        return metaData;
    }

    /**
   * This method was implemented for the same reason to  identify the databse.
   * but it is different from getDataBaseHandler because it returns the vendorType
   * by checking the vendor name. This string is passed at the subscriber end where
   * by this vendor type a proper handler is chosen.
   *
   * @param connectionPool
   * @param pubsubName
   * @return string VendorType
   * @throws RepException
   */
    public static int getVendorType(ConnectionPool connectionPool, String pubsubName) throws RepException {
        Connection con = connectionPool.getConnection(pubsubName);
        try {
            String vendorName = getVendorName(con);
            if (vendorName.equalsIgnoreCase(PointBase_ProductName)) {
                return DataBase_SqlServer;
            } else if (vendorName.equalsIgnoreCase(Daffodil_ProductName)) {
                return DataBase_DaffodilDB;
            } else if (vendorName.equalsIgnoreCase(Oracle_ProductName)) {
                return DataBase_Oracle;
            } else if (vendorName.equalsIgnoreCase(Cloudscape_ProductName)) {
                return DataBase_Cloudscape;
            } else if (vendorName.equalsIgnoreCase(SqlServer_ProductName)) {
                return DataBase_SqlServer;
            } else if (vendorName.equalsIgnoreCase(PostgreSQL_ProductName)) {
                return DataBase_PostgreSQL;
            } else if (vendorName.toUpperCase().startsWith("DB2")) {
                return DataBase_DB2;
            } else if (vendorName.equalsIgnoreCase(DB2_6000_ProductName)) {
                return DataBase_DB2;
            } else if (vendorName.equalsIgnoreCase(DB2_AS_400_ProductName)) {
                return DataBase_DB2;
            } else if (vendorName.toUpperCase().equalsIgnoreCase("Adaptive Server Anywhere") || vendorName.toUpperCase().equalsIgnoreCase("Adaptive Server Enterprise")) {
                return DataBase_Sybase;
            } else if (vendorName.equalsIgnoreCase("Firebird")) {
                return DataBase_Firebird;
            } else if (vendorName.equalsIgnoreCase(MySQL_ProductName)) {
                return DataBase_MySQL;
            } else {
                return DataBase_General;
            }
        } finally {
            connectionPool.returnConnection(con);
            connectionPool.removeSubPubFromMap(pubsubName);
        }
    }

    /**
   * This method helps in identifying the databse. It accomplishses this work
   * by getting the connection of the specified sub or pub and then by taking
   * the database product name.
   *
   * @param connectionPool
   * @param pubsubName
   * @return
   * @throws RepException
   */
    private static String getVendorName(Connection connection) throws RepException {
        try {
            return connection.getMetaData().getDatabaseProductName();
        } catch (SQLException ex) {
            throw new RepException("REP006", new Object[] { ex.getMessage() });
        }
    }

    public static AbstractDataBaseHandler getDatabaseHandler(int tgtVendorType) {
        switch(tgtVendorType) {
            case DataBase_DaffodilDB:
                return new DaffodilDBHandler();
            case DataBase_Oracle:
                return new OracleHandler();
            case DataBase_SqlServer:
                return new SQLServerHandler();
            case DataBase_PointBase:
                return new PointBaseHandler();
            case DataBase_Cloudscape:
                return new CloudScapeHandler();
            case DataBase_PostgreSQL:
                return new PostgreSQLHandler();
            case DataBase_DB2:
                return new DB2Handler();
            case DataBase_Sybase:
                return new SybaseHandler();
            case DataBase_Firebird:
                return new FireBirdDatabaseHandler();
            case DataBase_MySQL:
                return new MYSQLHandler();
            case DataBase_General:
            default:
                return new GeneralDataBaseHandler();
        }
    }
}

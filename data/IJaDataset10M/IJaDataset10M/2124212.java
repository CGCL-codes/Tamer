package com.patientis.upgrade.migrate;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import com.patientis.upgrade.common.AccessDataTypeException;
import com.patientis.upgrade.common.ConnectionList;
import com.patientis.upgrade.common.ConnectionProperty;
import com.patientis.upgrade.common.DataAccess;
import com.patientis.upgrade.common.DataNull;
import com.patientis.upgrade.common.DbConnection;
import com.patientis.upgrade.common.SQLResult;
import com.patientis.framework.logging.Log;
import com.patientis.framework.utility.FileSystemUtil;

/**
 * Create a clean database 
 *
 * 
 *   
 */
public class UpgradeProductionDatabase {

    /**
	 * Source
	 */
    private ConnectionProperty srcConnProp = null;

    /**
	 * Source
	 */
    private ConnectionProperty targetConnProp = null;

    /**
	 * Source
	 */
    private DataAccess source = null;

    /**
	 * Source
	 */
    private DbConnection sourceConn = null;

    /**
	 * Destination
	 */
    private DataAccess target = null;

    /**
	 * Destination
	 */
    private DbConnection targetConn = null;

    /**
	 * Connect to source and destination
	 * 
	 * @param src
	 * @param dest
	 */
    public UpgradeProductionDatabase(ConnectionProperty srcConnProp, ConnectionProperty targetConnProp) {
        this.srcConnProp = srcConnProp;
        this.targetConnProp = targetConnProp;
        connect();
    }

    private class Migrate {

        public String tableName;

        public String whereClause;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) throws Exception {
        try {
            if (args.length != 2) {
                System.err.println("UpgradeProductionDatabase <template_source> <target_production>");
                System.err.println("e.g. UpgradeProductionDatabase upgrade_source.xml upgrade_target.xml");
                System.exit(1);
            }
            ConnectionList.setConfigFileName("upgrade_source.xml");
            ConnectionList connections = ConnectionList.loadConnections();
            ConnectionProperty source = connections.getEnvironment("upgrade_source");
            ConnectionList.setConfigFileName("upgrade_target.xml");
            connections = ConnectionList.loadConnections();
            ConnectionProperty target = connections.getEnvironment("upgrade_target");
            if (source == null || target == null) {
                throw new Exception("upgrade_source.xml must have environment upgrade_source and upgrade_target.xml must have environment upgrade_target.xml");
            } else {
                UpgradeProductionDatabase createProduction = new UpgradeProductionDatabase(source, target);
                createProduction.upgradeDatabase();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * Migrate all of the system, setup and configuration data
	 */
    private void upgradeDatabase() throws Exception {
        java.sql.Connection conn = targetConn.getConnection();
        conn.createStatement().executeUpdate("SET client_encoding = 'UNICODE'");
        String cmds = FileSystemUtil.getTextContents("C:\\dev\\patientis\\trunk\\build\\template25_ref_groups.sql");
        StringTokenizer st = new StringTokenizer(cmds, ";");
        while (st.hasMoreTokens()) {
            String sql = st.nextToken();
            if (!(sql.trim().startsWith("-")) && !(sql.startsWith("--")) && !(sql.matches(" .*: .*"))) {
                Log.println(sql);
            }
        }
    }

    /**
	 * Get all tables to migrate
	 * 
	 * @return
	 * @throws Exception
	 */
    private List<Migrate> getTableList() throws Exception {
        List<Migrate> v = new ArrayList<Migrate>();
        String sql = "select database_table_name, migration_source_where_sql " + " from database_tables where migration_type_ref_id > 0" + " order by migration_sequence";
        SQLResult resultSet = source.executeQuery(sql);
        ResultSet rset = resultSet.getRset();
        while (rset.next()) {
            Migrate migrate = new Migrate();
            migrate.tableName = rset.getString(1);
            migrate.whereClause = rset.getString(2);
            v.add(migrate);
        }
        rset.close();
        return v;
    }

    private void cleanTable(String tablename) throws SQLException {
        String sql = "delete from " + tablename;
        try {
            target.executeUpdate(sql);
        } catch (SQLException ex) {
            System.err.println(sql);
            throw ex;
        }
    }

    /**
	 * Move the table from source to destination
	 * 
	 * @param tablename
	 * @return
	 * @throws SQLExceptionADMIN
	 * @throws AccessDataTypeException
	 */
    private int moveTable(Migrate migrate) throws Exception {
        connect();
        String sql = "select * from " + migrate.tableName + " " + (migrate.whereClause == null ? "" : migrate.whereClause);
        try {
            ResultSet rset = sourceConn.getConnection().createStatement().executeQuery(sql);
            ResultSetMetaData meta = rset.getMetaData();
            String inssql = TestTableMigration.getInsertSql(meta, migrate.tableName);
            int colcount = meta.getColumnCount();
            int count = 0;
            while (rset.next()) {
                Vector<Object> v = new Vector<Object>(colcount);
                for (int i = 0; i < colcount; i++) {
                    Object o = rset.getObject(i + 1);
                    if (o == null) {
                        if (meta.getColumnClassName(i + 1).equals(String.class.getName())) {
                            o = new DataNull(new String());
                        } else if (meta.getColumnClassName(i + 1).equals(Integer.class.getName())) {
                            o = new DataNull(new Integer(0));
                        } else if (meta.getColumnClassName(i + 1).equals(Double.class.getName())) {
                            o = new DataNull(new Double(0));
                        } else if (meta.getColumnClassName(i + 1).equals(java.sql.Timestamp.class.getName())) {
                            o = new DataNull(new java.sql.Timestamp(0));
                        } else if (meta.getColumnClassName(i + 1).equals(java.lang.Short.class.getName())) {
                            o = new DataNull(new Integer(0));
                        } else {
                            Log.println(meta.getColumnName(i + 1) + ":" + meta.getColumnClassName(i + 1));
                        }
                    }
                    v.add(o);
                }
                target.executeUpdate(inssql, v);
                count++;
            }
            rset.close();
            disconnect();
            Log.println("drop sequence " + migrate.tableName + "_s;");
            Log.println("create sequence " + migrate.tableName + "_s start 50000001;");
            return count;
        } catch (Exception ex) {
            System.err.println(sql);
            throw ex;
        }
    }

    private void disconnect() throws Exception {
        sourceConn.close();
        target.getConnection().commit();
        targetConn.close();
    }

    private void connect() {
        try {
            source = new DataAccess(srcConnProp.getEnvironment());
            source.connect(srcConnProp);
            sourceConn = source.getDbConnection();
            target = new DataAccess(targetConnProp.getEnvironment());
            target.connect(targetConnProp);
            targetConn = target.getDbConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

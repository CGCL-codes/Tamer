package ch.sahits.phpclassgenerator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
* This class handles the connection to a MySQL DB.
* It queries the DB for the structure of a table.
* @author Andi Hotz, (c) 2007
* @version 1.0
* $Revision: 1.11 $ changed by $Author: p_kerspe $ at $Date: 2012/03/12 15:54:48 $
*/
public class MySQLConnection extends DBConnection {

    /**
	 * Constructor initializes the connection data
	 * @param product what kind of DB (MySQL, PostgeSQL, Oracle, Derby, MS SQL Server, ...)
	 * @param hostname of the database e.g. 'localhost'
	 * @param uname user name to connect to the DB
	 * @param pwd password of the user
	 * @param database or schema of the table
	 * @param tableName table name
	 * @throws SQLException Connecting failed probably due to incorrect input
	 */
    public MySQLConnection(String product, String hostname, String uname, String pwd, String database, String tableName) throws SQLException {
        super(product, hostname, uname, pwd, database, tableName);
    }

    /**
	 * Query the INFORMATION_SCHEMA to retrieve the meta data
	 * @return DBStructure
	 * @throws SQLException 
	 */
    @Override
    public DBStructure getStructure(String table) throws SQLException {
        String sql = "select * from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME='" + table + "' and TABLE_SCHEMA='" + db + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        DBStructure struct = new DBStructure();
        if (rs.isBeforeFirst()) {
            while (rs.next()) {
                String fieldName = rs.getString("column_name");
                String type = rs.getString("DATA_TYPE");
                String maxLength = rs.getString("CHARACTER_MAXIMUM_LENGTH");
                String charSet = rs.getString("CHARACTER_SET_NAME");
                String comment = rs.getString("COLUMN_COMMENT");
                boolean nullAllowed = rs.getString("IS_NULLABLE").equals("YES");
                struct.addDBField(table, fieldName, type, nullAllowed, maxLength, charSet, comment);
            }
        } else {
            throw new SQLException("Table " + table + "does not exist.");
        }
        sql = "select * from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where TABLE_NAME='" + table + "' and TABLE_SCHEMA='" + db + "'";
        rs = stmt.executeQuery(sql);
        if (rs.isBeforeFirst()) {
            while (rs.next()) {
                if (rs.getString("CONSTRAINT_TYPE").equals("PRIMARY KEY")) {
                    String pkName = rs.getString("CONSTRAINT_NAME");
                    struct.setPkName(retriveMySQLPrimaryKey(pkName, table, this.db));
                } else if (rs.getString("CONSTRAINT_TYPE").equals("FOREIGN KEY")) {
                    System.out.println(rs.getString("CONSTRAINT_NAME"));
                    String fkName = rs.getString("CONSTRAINT_NAME");
                    struct.addUniqueKeys(retriveMySQLForeignKey(fkName, table, this.db));
                }
            }
        } else {
            throw new SQLException("Table " + table + "does not exist.");
        }
        return struct;
    }

    /**
	 * retrieve a list of all table names in the selected database
	 * @return List<String> holding all table names
	 */
    public List<String> getAvailableTableNames() throws SQLException {
        List<String> tableNames = new ArrayList<String>();
        String sql = "";
        if (this.getDatabaseMajorVersion() >= 5 && (this.getDatabaseMinorVersion() > 0 || this.getDatabaseSubVersion() > 1)) {
            sql = "SHOW FULL TABLES IN `" + this.db + "`";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    if ("BASE TABLE".equals(rs.getString(2))) {
                        tableNames.add(rs.getString(1));
                    }
                }
            } else {
                throw new SQLException("no tables found in database " + db + " or error while fetching table information. " + conn.getWarnings());
            }
        } else {
            sql = "SHOW TABLES IN `" + this.db + "`";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    tableNames.add(rs.getString(1));
                }
            } else {
                throw new SQLException("no tables found in database " + db + " or error while fetching table information. " + conn.getWarnings());
            }
        }
        return tableNames;
    }

    /**
	 * return the database version string in a hopefully usable format
	 * @return
	 * @throws SQLException
	 */
    public String getDatabaseVersion() {
        String versionString = "-1.-1.-1";
        String sql = "SHOW VARIABLES LIKE 'version';";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.isBeforeFirst() && rs.next()) {
                versionString = rs.getString(2);
            }
        } catch (SQLException ex) {
        }
        return versionString;
    }

    /**
	 * return the Major version number (format of mysql version numbers: {<b>major version</b>}.{minor version}.{sub version}) for the database or -1 in case of an error
	 */
    public int getDatabaseMajorVersion() {
        String[] versionSplits = this.getDatabaseVersion().split("\\.");
        return Integer.parseInt(versionSplits[0]);
    }

    /**
	 * return the minor version number (format of mysql version numbers: {major version}.{<b>minor version</b>}.{sub version}) for the database or -1 in case of an error
	 */
    public int getDatabaseMinorVersion() {
        String[] versionSplits = this.getDatabaseVersion().split("\\.");
        return Integer.parseInt(versionSplits[1]);
    }

    /**
	 * return the sub version number (format of mysql version numbers: {major version}.{minor version}.{<b>sub version</b>}) for the database or -1 in case of an error
	 */
    public int getDatabaseSubVersion() {
        String[] versionSplits = this.getDatabaseVersion().split("\\.");
        return Integer.parseInt(versionSplits[2]);
    }

    /**
	 * Retrieve the primary key column name(s) of the table
	 * @param pkName Primary name of the index that is the primary key
	 * @return String array with column names
	 * @throws SQLException
	 */
    private String[] retriveMySQLPrimaryKey(String pkName, String table, String db) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "select * from INFORMATION_SCHEMA.STATISTICS where TABLE_NAME='" + table + "' and INDEX_NAME='" + pkName + "' and TABLE_SCHEMA='" + db + "'";
        ResultSet rs2 = stmt.executeQuery(sql);
        Vector<String> v = new Vector<String>();
        if (rs2.isBeforeFirst()) {
            while (rs2.next()) {
                String col = rs2.getString("COLUMN_NAME");
                v.add(col);
            }
        } else {
            throw new SQLException("The Index " + pkName + " on table " + table + "does not exist.");
        }
        String[] pkCols = new String[v.size()];
        v.copyInto(pkCols);
        return pkCols;
    }

    /**
	 * Retrieve the foreign key of the table
	 * @param fkName Foreign key name
	 * @return String array with column names
	 * @throws SQLException 
	 * @throws SQLException
	 */
    private String[] retriveMySQLForeignKey(String fkName, String table, String db) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "select * from INFORMATION_SCHEMA.KEY_COLUMN_USAGE where TABLE_NAME='" + table + "' AND CONSTRAINT_NAME='" + fkName + "' and TABLE_SCHEMA='" + db + "'";
        ResultSet rs2 = stmt.executeQuery(sql);
        Vector<String> v = new Vector<String>();
        if (rs2.isBeforeFirst()) {
            while (rs2.next()) {
                String col = rs2.getString("COLUMN_NAME");
                v.add(col);
            }
        } else {
            throw new SQLException("The Index " + fkName + " on table " + table + "does not exist.");
        }
        String[] fkCols = new String[v.size()];
        v.copyInto(fkCols);
        return fkCols;
    }

    /**
 	 * check if the driver is available
	 */
    @Override
    protected void loadDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}

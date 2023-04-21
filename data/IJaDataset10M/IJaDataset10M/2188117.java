package net.confex.db.ora;

import java.sql.SQLException;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

public class OraUtils {

    /**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
	 * Insert with returning number // //ins_sql = "INSERT INTO PSI_ATTRIBUTES
	 * (AID, NAME, Data_Type) "+ // " VALUES ('115','ttt',2) RETURNING ID INTO
	 * :1";
	 */
    public static long insertWithReturningNum(OraConnection connection, String ins_sql) throws SQLException {
        String sql = "begin ? := COM_W3_UPLOAD.insertWithReturningNum(?);  end;";
        OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection().prepareCall(sql);
        ocs.setString(2, ins_sql);
        ocs.registerOutParameter(1, OracleTypes.NUMBER);
        ocs.execute();
        long ret = ocs.getLong(1);
        ocs.close();
        return ret;
    }

    /**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ //
	 * Insert with returning varchar2 // //ins_sql = "INSERT INTO PSI_ATTRIBUTES
	 * (AID, NAME, Data_Type) "+ // " VALUES ('115','ttt',2) RETURNING AID INTO
	 * :1";
	 */
    public static String insertWithReturningStr(OraConnection connection, String ins_sql) throws SQLException {
        String sql = "begin ? := COM_W3_UPLOAD.insertWithReturningStr(?);  end;";
        OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection().prepareCall(sql);
        ocs.setString(2, ins_sql);
        ocs.registerOutParameter(1, OracleTypes.VARCHAR);
        ocs.execute();
        String ret = ocs.getString(1);
        ocs.close();
        return ret;
    }

    /**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
    public static String execSqlFunc0RSEx(OraConnection connection, String sql) throws SQLException {
        OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection().prepareCall(sql);
        ocs.registerOutParameter(1, OracleTypes.VARCHAR);
        ocs.execute();
        String ret = ocs.getString(1);
        ocs.close();
        return ret;
    }

    /**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
    public static String execSqlFunc1I2IRSEx(OraConnection connection, String sql, int p1, int p2) throws SQLException {
        OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection().prepareCall(sql);
        ocs.registerOutParameter(1, OracleTypes.VARCHAR);
        ocs.setInt(2, p1);
        ocs.setInt(3, p2);
        ocs.execute();
        String ret = ocs.getString(1);
        ocs.close();
        return ret;
    }

    /**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
    public static long execSqlFunc1LRLEx(OraConnection connection, String sql, long p1) throws SQLException {
        OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection().prepareCall(sql);
        ocs.registerOutParameter(1, OracleTypes.NUMBER);
        ocs.setLong(2, p1);
        ocs.execute();
        long ret = ocs.getLong(1);
        ocs.close();
        return ret;
    }

    /**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
    public static String execSqlFunc1S2SRSEx(OraConnection connection, String sql, String p1, String p2) throws SQLException {
        OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection().prepareCall(sql);
        ocs.registerOutParameter(1, OracleTypes.VARCHAR);
        ocs.setString(2, p1);
        ocs.setString(3, p2);
        ocs.execute();
        String ret = ocs.getString(1);
        ocs.close();
        return ret;
    }

    /**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
    public static long execSqlFuncP0RL(OraConnection connection, String sql) throws SQLException {
        OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection().prepareCall(sql);
        ocs.registerOutParameter(1, OracleTypes.NUMBER);
        ocs.execute();
        long ret = ocs.getLong(1);
        ocs.close();
        return ret;
    }

    /**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
    public static void execSqlProcP1S2SR0(OraConnection connection, String sql, String sp1, String sp2) throws SQLException {
        OracleCallableStatement ocs = (OracleCallableStatement) connection.getConnection().prepareCall(sql);
        ocs.setString(1, sp1);
        ocs.setString(2, sp2);
        ocs.execute();
        ocs.close();
    }
}

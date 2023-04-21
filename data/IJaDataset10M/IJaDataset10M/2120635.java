package se.ucr.openqregdemo.bean.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import se.ucr.db.DbHandler;
import se.ucr.db.PrimaryKey;
import se.ucr.openqregdemo.bean.DischargeBean;
import se.ucr.openqregdemo.bean.DischargeKey;

/**
* This is the FinderBase / Helper class for the discharge table.
*/
public abstract class DischargeFinderBase {

    /**
* COLUMNS is a String with all columns in the table
*/
    public static final String COLUMNS = "discharge.MCEID, discharge.CENTREID, discharge.TYPE, discharge.ADMISSIONDATE, discharge.CIVILSTATUS, discharge.BOFORM, discharge.KLINIK, discharge.VARDGIVARE, discharge.WEIGHT_PRE, discharge.WEIGHT_PRE_MISS, discharge.HEIGHT, discharge.HEIGHT_MISS, discharge.SMOKE, discharge.USERCOMMENT, discharge.USERFEEDBACK, discharge.MONITORFEEDBACK, discharge.STATUS, discharge.TSUPDATED, discharge.UPDATEDBY, discharge.TSCREATED, discharge.CREATEDBY";

    /**
* COLUMNSCOLLATE is a String with all columns in the table
*/
    public static final String COLUMNSCOLLATE = "discharge.MCEID, discharge.CENTREID #collate# as CENTREID, discharge.TYPE, discharge.ADMISSIONDATE, discharge.CIVILSTATUS, discharge.BOFORM, discharge.KLINIK, discharge.VARDGIVARE, discharge.WEIGHT_PRE, discharge.WEIGHT_PRE_MISS, discharge.HEIGHT, discharge.HEIGHT_MISS, discharge.SMOKE, discharge.USERCOMMENT #collate# as USERCOMMENT, discharge.USERFEEDBACK #collate# as USERFEEDBACK, discharge.MONITORFEEDBACK #collate# as MONITORFEEDBACK, discharge.STATUS, discharge.TSUPDATED, discharge.UPDATEDBY #collate# as UPDATEDBY, discharge.TSCREATED, discharge.CREATEDBY #collate# as CREATEDBY";

    /**
* Populate a valueobjext from a resultset row
* @param rs a ResultSet posisioned on the right row for reading the row for this valueObject
* @return A populated DischargeBean
* 
* @throws SQLException
*/
    protected static DischargeBean populate(ResultSet rs) throws SQLException {
        DischargeBean valueObject = new DischargeBean();
        valueObject.setMceid((Long) rs.getObject(1));
        valueObject.setCentreid((String) rs.getObject(2));
        valueObject.setType((Integer) rs.getObject(3));
        valueObject.setAdmissiondate((java.sql.Date) rs.getObject(4));
        valueObject.setCivilstatus((Integer) rs.getObject(5));
        valueObject.setBoform((Integer) rs.getObject(6));
        valueObject.setKlinik((Integer) rs.getObject(7));
        valueObject.setVardgivare((Integer) rs.getObject(8));
        valueObject.setWeight_pre((java.math.BigDecimal) rs.getObject(9));
        valueObject.setWeight_pre_miss((Integer) rs.getObject(10));
        valueObject.setHeight((Integer) rs.getObject(11));
        valueObject.setHeight_miss((Integer) rs.getObject(12));
        valueObject.setSmoke((Integer) rs.getObject(13));
        valueObject.setUsercomment((String) rs.getObject(14));
        valueObject.setUserfeedback((String) rs.getObject(15));
        valueObject.setMonitorfeedback((String) rs.getObject(16));
        valueObject.setStatus((Integer) rs.getObject(17));
        valueObject.setTsupdated((java.sql.Timestamp) rs.getObject(18));
        valueObject.setUpdatedby((String) rs.getObject(19));
        valueObject.setTscreated((java.sql.Timestamp) rs.getObject(20));
        valueObject.setCreatedby((String) rs.getObject(21));
        return valueObject;
    }

    /**
	 * FIND_ALL_STATEMENT is a String with a sql as an preparedstatement that retrives
	 * ALL from the database
	 */
    public static final String FIND_ALL_STATEMENT = "SELECT  " + COLUMNS + "  FROM discharge";

    /**
* create a collection with all rows from this table
* @return A collection with all rows
* 
* @throws SQLException
*/
    public static Collection<DischargeBean> findAll() throws SQLException {
        Connection con = null;
        try {
            con = DbHandler.getConnection();
            return findAll(con);
        } finally {
            if (null != con) {
                con.close();
                con = null;
            }
        }
    }

    /**
* create a collection with all rows from this table
* @param con a connection to use
* @return A collection with all rows
* 
* @throws SQLException
*/
    public static Collection<DischargeBean> findAll(Connection con) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        DischargeBean valueObject = null;
        Collection<DischargeBean> col = Collections.synchronizedList(new ArrayList<DischargeBean>());
        try {
            pStmt = con.prepareStatement(FIND_ALL_STATEMENT);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                valueObject = populate(rs);
                valueObject.afterPopulate(con);
                col.add(valueObject);
            }
            return col;
        } finally {
            if (null != rs) {
                rs.close();
                rs = null;
            }
            if (null != pStmt) {
                pStmt.close();
                pStmt = null;
            }
        }
    }

    /**
* returns the bean as an object that is represented by the primary key
* @param key The PrimaryKey representing the row we want
* @return A populated object
* 
* @throws SQLException
*/
    public static Object findByPrimaryKey(PrimaryKey key) throws SQLException {
        Connection con = null;
        try {
            con = DbHandler.getConnection();
            return findByPrimaryKey(con, key);
        } finally {
            if (null != con) {
                con.close();
                con = null;
            }
        }
    }

    /**
* returns the bean as an object that is represented by the primary key
* @param con a connection to use
* @param key The PrimaryKey representing the row we want
* @return A populated object
* 
* @throws SQLException
*/
    public static Object findByPrimaryKey(Connection con, PrimaryKey key) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        DischargeBean valueObject = null;
        DischargeKey primaryKey = (DischargeKey) key;
        try {
            pStmt = con.prepareStatement(DischargeBeanBase.SELECT_STATEMENT);
            pStmt.setObject(1, primaryKey.getMceid());
            rs = pStmt.executeQuery();
            while (rs.next()) {
                valueObject = populate(rs);
                valueObject.afterPopulate(con);
            }
            return valueObject;
        } finally {
            if (null != rs) {
                rs.close();
                rs = null;
            }
            if (null != pStmt) {
                pStmt.close();
                pStmt = null;
            }
        }
    }
}

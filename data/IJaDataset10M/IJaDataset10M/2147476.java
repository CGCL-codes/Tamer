package org.openqreg.openqregdemo.bean.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.openqreg.db.DbHandler;
import org.openqreg.db.PrimaryKey;
import org.openqreg.openqregdemo.bean.PatientBean;
import org.openqreg.openqregdemo.bean.PatientKey;

/**
* This is the FinderBase / Helper class for the patient table.
*/
public abstract class PatientFinderBase {

    /**
* COLUMNS is a String with all columns in the table
*/
    public static final String COLUMNS = "patient.PID, patient.SSN, patient.DOB, patient.SSNTYPE, patient.COUNTRYID, patient.FIRSTNAME, patient.LASTNAME, patient.GENDER, patient.LKF, patient.ZIPCODE, patient.ALIVE, patient.TSUPDATED, patient.UPDATEDBY, patient.TSCREATED, patient.CREATEDBY";

    /**
* COLUMNSCOLLATE is a String with all columns in the table
*/
    public static final String COLUMNSCOLLATE = "patient.PID, patient.SSN #collate# as SSN, patient.DOB, patient.SSNTYPE, patient.COUNTRYID #collate# as COUNTRYID, patient.FIRSTNAME #collate# as FIRSTNAME, patient.LASTNAME #collate# as LASTNAME, patient.GENDER, patient.LKF #collate# as LKF, patient.ZIPCODE #collate# as ZIPCODE, patient.ALIVE, patient.TSUPDATED, patient.UPDATEDBY #collate# as UPDATEDBY, patient.TSCREATED, patient.CREATEDBY #collate# as CREATEDBY";

    /**
* Populate a valueobjext from a resultset row
* @param rs a ResultSet posisioned on the right row for reading the row for this valueObject
* @return A populated PatientBean
* 
* @throws SQLException
*/
    protected static PatientBean populate(ResultSet rs) throws SQLException {
        PatientBean valueObject = new PatientBean();
        valueObject.setPid((Long) rs.getObject(1));
        valueObject.setSsn((String) rs.getObject(2));
        valueObject.setDob((java.sql.Date) rs.getObject(3));
        valueObject.setSsntype((Integer) rs.getObject(4));
        valueObject.setCountryid((String) rs.getObject(5));
        valueObject.setFirstname((String) rs.getObject(6));
        valueObject.setLastname((String) rs.getObject(7));
        valueObject.setGender((Integer) rs.getObject(8));
        valueObject.setLkf((String) rs.getObject(9));
        valueObject.setZipcode((String) rs.getObject(10));
        valueObject.setAlive((Integer) rs.getObject(11));
        valueObject.setTsupdated((java.sql.Timestamp) rs.getObject(12));
        valueObject.setUpdatedby((String) rs.getObject(13));
        valueObject.setTscreated((java.sql.Timestamp) rs.getObject(14));
        valueObject.setCreatedby((String) rs.getObject(15));
        return valueObject;
    }

    /**
	 * FIND_ALL_STATEMENT is a String with a sql as an preparedstatement that retrives
	 * ALL from the database
	 */
    public static final String FIND_ALL_STATEMENT = "SELECT  " + COLUMNS + "  FROM patient";

    /**
* create a collection with all rows from this table
* @return A collection with all rows
* 
* @throws SQLException
*/
    public static Collection<PatientBean> findAll() throws SQLException {
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
    public static Collection<PatientBean> findAll(Connection con) throws SQLException {
        PreparedStatement pStmt = null;
        ResultSet rs = null;
        PatientBean valueObject = null;
        Collection<PatientBean> col = Collections.synchronizedList(new ArrayList<PatientBean>());
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
        PatientBean valueObject = null;
        PatientKey primaryKey = (PatientKey) key;
        try {
            pStmt = con.prepareStatement(PatientBeanBase.SELECT_STATEMENT);
            pStmt.setObject(1, primaryKey.getPid());
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

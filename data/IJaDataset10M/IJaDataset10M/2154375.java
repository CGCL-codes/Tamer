package com.vlee.ejb.customer;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.BigDecimal;
import javax.ejb.*;
import javax.naming.*;
import com.vlee.local.*;
import com.vlee.util.*;
import com.vlee.ejb.supplier.*;
import com.vlee.ejb.user.UserObject;

public class DeptCodeBean implements EntityBean {

    public static final String PKID = "pkid";

    public static final String CODE = "code";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    public static final String CATID = "catid";

    public static final String DEPT_TYPE = "dept_type";

    public static final String STATUS = "status";

    public static final String LASTUPDATE = "lastupdate";

    public static final String USERID_EDIT = "userid_edit";

    DeptCodeObject valueObject;

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String TABLENAME = "cust_deptcode_index";

    public static final String STATUS_ACTIVE = "active";

    public static final String STATUS_INACTIVE = "inactive";

    private static final String strObjectName = "DeptCodeBean: ";

    private EntityContext context = null;

    /***************************************************************************
	 * Getters
	 **************************************************************************/
    public DeptCodeObject getObject() {
        DeptCodeObject valueObject = this.valueObject;
        return valueObject;
    }

    public void setObject(DeptCodeObject valueObject) {
        Integer pkid = this.valueObject.pkid;
        this.valueObject = valueObject;
        this.valueObject.pkid = pkid;
    }

    public Integer getPrimaryKey() {
        return this.valueObject.pkid;
    }

    public void setPrimaryKey(Integer pkid) {
        this.valueObject.pkid = pkid;
    }

    /***************************************************************************
	 * ejbCreate
	 **************************************************************************/
    public Integer ejbCreate(DeptCodeObject valueObject) throws CreateException {
        Integer newKey = null;
        Log.printVerbose(strObjectName + "in ejbCreate");
        try {
            this.valueObject = insertObject(valueObject);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException("ejbCreate: " + ex.getMessage());
        }
        Log.printVerbose(strObjectName + "about to leave ejbCreate");
        return this.valueObject.pkid;
    }

    /***************************************************************************
	 * ejbFindByPrimaryKey
	 **************************************************************************/
    public Integer ejbFindByPrimaryKey(Integer primaryKey) throws FinderException {
        Log.printVerbose(strObjectName + "in ejbFindByPrimaryKey");
        Log.printVerbose(" the primary key is " + primaryKey.toString());
        boolean result = false;
        try {
            result = selectByPrimaryKey(primaryKey);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException("ejbFindByPrimaryKey: " + ex.getMessage());
        }
        if (result) {
            return primaryKey;
        } else {
            throw new ObjectNotFoundException("Row for id " + primaryKey.toString() + "not found.");
        }
    }

    /***************************************************************************
	 * ejbRemove
	 **************************************************************************/
    public void ejbRemove() {
        Log.printVerbose(strObjectName + " In ejbRemove");
        try {
            deleteObject(this.valueObject.pkid);
        } catch (Exception ex) {
            throw new EJBException("ejbRemove: " + ex.getMessage());
        }
    }

    /***************************************************************************
	 * setEntityContext
	 **************************************************************************/
    public void setEntityContext(EntityContext context) {
        Log.printVerbose(strObjectName + " In setEntityContext");
        this.context = context;
    }

    /***************************************************************************
	 * unsetEntityContext
	 **************************************************************************/
    public void unsetEntityContext() {
        Log.printVerbose(strObjectName + " In unsetEntityContext");
        this.context = null;
    }

    /***************************************************************************
	 * ejbActivate
	 **************************************************************************/
    public void ejbActivate() {
        Log.printVerbose(strObjectName + " In ejbActivate");
        try {
            this.valueObject = new DeptCodeObject();
            this.valueObject.pkid = (Integer) context.getPrimaryKey();
        } catch (Exception ex) {
            Log.printVerbose("the getmsg returns:" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /***************************************************************************
	 * ejbPassivate
	 **************************************************************************/
    public void ejbPassivate() {
        Log.printVerbose(strObjectName + " In ejbPassivate");
        this.valueObject = null;
    }

    /***************************************************************************
	 * ejbLoad
	 **************************************************************************/
    public void ejbLoad() {
        Log.printVerbose(strObjectName + " In ejbLoad");
        try {
            loadObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException("ejbLoad: " + ex.getMessage());
        }
        Log.printVerbose(strObjectName + " Leaving ejbLoad");
    }

    /***************************************************************************
	 * ejbStore
	 **************************************************************************/
    public void ejbStore() {
        Log.printVerbose(strObjectName + " In ejbStore");
        try {
            storeObject();
        } catch (Exception ex) {
            throw new EJBException("ejbStore: " + ex.getMessage());
        }
        Log.printVerbose(strObjectName + " Leaving ejbStore");
    }

    /***************************************************************************
	 * ejbPostCreate (1)
	 **************************************************************************/
    public void ejbPostCreate(DeptCodeObject valueObject) {
    }

    public Vector ejbHomeGetValueObjectsGiven(String fieldName1, String value1, String fieldName2, String value2) {
        Vector vecValObj = null;
        try {
            vecValObj = selectValueObjectsGiven(fieldName1, value1, fieldName2, value2);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.printDebug("ERROR : " + ex.getMessage());
        }
        return vecValObj;
    }

    public Collection ejbHomeGetObjects(QueryObject query) {
        Collection vecValObj = new Vector();
        try {
            vecValObj = selectObjects(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecValObj;
    }

    /** ********************* Database Routines ************************ */
    private Connection makeConnection() throws NamingException, SQLException {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(dsName);
            return ds.getConnection();
        } catch (Exception ex) {
            throw new EJBException("Unable to connect to database. " + ex.getMessage());
        }
    }

    private void closeConnection(Connection con) throws NamingException, SQLException {
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            throw new EJBException("closeConnection: " + ex.getMessage());
        }
    }

    private DeptCodeObject insertObject(DeptCodeObject valueObject) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " insertObject: ");
            con = makeConnection();
            try {
                valueObject.pkid = getNextPkid();
            } catch (Exception ex) {
                throw new EJBException(strObjectName + ex.getMessage());
            }
            try {
                String insertStatement = "INSERT INTO " + TABLENAME + "( " + PKID + ", " + CODE + ", " + NAME + ", " + DESCRIPTION + ", " + CATID + ", " + DEPT_TYPE + ", " + STATUS + ", " + LASTUPDATE + ", " + USERID_EDIT + ") values ( ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
                prepStmt = con.prepareStatement(insertStatement);
                prepStmt.setInt(1, valueObject.pkid.intValue());
                prepStmt.setString(2, valueObject.code);
                prepStmt.setString(3, valueObject.name);
                prepStmt.setString(4, valueObject.description);
                prepStmt.setInt(5, valueObject.catId.intValue());
                prepStmt.setString(6, valueObject.deptType);
                prepStmt.setString(7, valueObject.status);
                prepStmt.setTimestamp(8, valueObject.lastUpdate);
                prepStmt.setInt(9, valueObject.userIdEdit.intValue());
                prepStmt.executeUpdate();
                Log.printVerbose(strObjectName + " Leaving insertObject: ");
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw ex;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            closeConnection(con);
        }
        return valueObject;
    }

    private Collection selectObjects(QueryObject query) throws NamingException, SQLException {
        Connection con = null;
        Collection coll = new Vector();
        Log.printVerbose(strObjectName + " loadObject: ");
        con = makeConnection();
        String selectStatement = " SELECT * " + " FROM " + TABLENAME;
        selectStatement = query.appendQuery(selectStatement);
        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
        ResultSet rs = prepStmt.executeQuery();
        while (rs.next()) {
            DeptCodeObject usrObj = getObject(rs, "");
            coll.add(usrObj);
        }
        prepStmt.close();
        closeConnection(con);
        Log.printVerbose(strObjectName + " Leaving loadObject: ");
        return coll;
    }

    private DeptCodeObject getObject(ResultSet rs, String prefix) {
        DeptCodeObject usrObj = null;
        try {
            usrObj = new DeptCodeObject();
            usrObj.pkid = new Integer(rs.getInt(PKID));
            usrObj.code = rs.getString(CODE);
            usrObj.name = rs.getString(NAME);
            usrObj.description = rs.getString(DESCRIPTION);
            usrObj.catId = new Integer(rs.getInt(CATID));
            usrObj.deptType = rs.getString(DEPT_TYPE);
            usrObj.status = rs.getString(STATUS);
            usrObj.lastUpdate = rs.getTimestamp(LASTUPDATE);
            usrObj.userIdEdit = new Integer(rs.getInt(USERID_EDIT));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return usrObj;
    }

    private boolean selectByPrimaryKey(Integer primaryKey) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " selectByPrimaryKey: ");
            con = makeConnection();
            String selectStmt = "SELECT " + PKID + " from " + TABLENAME + " where " + PKID + " = ? ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setInt(1, primaryKey.intValue());
            ResultSet rs = prepStmt.executeQuery();
            boolean result = rs.next();
            Log.printVerbose(strObjectName + " Leaving selectByPrimaryKey:");
            return result;
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            closeConnection(con);
        }
    }

    private void deleteObject(Integer pkid) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " deleteObject: ");
            con = makeConnection();
            String deleteStatement = "delete from " + TABLENAME + " where " + PKID + " = ?";
            prepStmt = con.prepareStatement(deleteStatement);
            prepStmt.setInt(1, pkid.intValue());
            prepStmt.executeUpdate();
            Log.printVerbose(strObjectName + " Leaving deleteObject: ");
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            closeConnection(con);
        }
    }

    private void loadObject() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            con = makeConnection();
            String selectStmt = " SELECT " + PKID + ", " + CODE + ", " + NAME + ", " + DESCRIPTION + ", " + CATID + ", " + DEPT_TYPE + ", " + STATUS + ", " + LASTUPDATE + ", " + USERID_EDIT + " FROM " + TABLENAME + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setInt(1, this.valueObject.pkid.intValue());
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next()) {
                this.valueObject.pkid = new Integer(rs.getInt(PKID));
                this.valueObject.code = rs.getString(CODE);
                this.valueObject.name = rs.getString(NAME);
                this.valueObject.description = rs.getString(DESCRIPTION);
                this.valueObject.catId = new Integer(rs.getInt(CATID));
                this.valueObject.deptType = rs.getString(DEPT_TYPE);
                this.valueObject.status = rs.getString(STATUS);
                this.valueObject.lastUpdate = rs.getTimestamp(LASTUPDATE);
                this.valueObject.userIdEdit = new Integer(rs.getInt(USERID_EDIT));
            } else {
                throw new NoSuchEntityException("Row for pkid " + this.valueObject.pkid.toString() + " not found in database.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            closeConnection(con);
        }
    }

    private Vector selectValueObjectsGiven(String fieldName1, String value1, String fieldName2, String value2) throws NamingException, SQLException {
        Vector vecValObj = new Vector();
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " loadObject: ");
            con = makeConnection();
            String selectStmt = "select " + PKID + ", " + CODE + ", " + NAME + ", " + DESCRIPTION + ", " + CATID + ", " + DEPT_TYPE + ", " + STATUS + ", " + LASTUPDATE + ", " + USERID_EDIT + " from " + TABLENAME + " where " + fieldName1 + " = ? ";
            if (fieldName2 != null && value2 != null) {
                selectStmt += " AND " + fieldName2 + " = ? ";
            }
            selectStmt += " ORDER BY " + CODE + " ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setString(1, value1);
            if (fieldName2 != null && value2 != null) {
                prepStmt.setString(2, value2);
            }
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                DeptCodeObject valueObject = new DeptCodeObject();
                valueObject.pkid = new Integer(rs.getInt(PKID));
                valueObject.code = rs.getString(CODE);
                valueObject.name = rs.getString(NAME);
                valueObject.description = rs.getString(DESCRIPTION);
                valueObject.catId = new Integer(rs.getInt(CATID));
                valueObject.deptType = rs.getString(CATID);
                valueObject.status = rs.getString(STATUS);
                valueObject.lastUpdate = rs.getTimestamp(LASTUPDATE);
                valueObject.userIdEdit = new Integer(rs.getInt(USERID_EDIT));
                vecValObj.add(valueObject);
            }
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            closeConnection(con);
        }
        return vecValObj;
    }

    private void storeObject() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            con = makeConnection();
            String updateStatement = "update " + TABLENAME + " set " + PKID + " = ?, " + CODE + " = ?, " + NAME + " = ?, " + DESCRIPTION + " = ?, " + CATID + " = ?, " + DEPT_TYPE + " = ?, " + STATUS + " = ?, " + LASTUPDATE + " = ?, " + USERID_EDIT + " = ? " + "where " + PKID + " = ?";
            prepStmt = con.prepareStatement(updateStatement);
            prepStmt.setInt(1, valueObject.pkid.intValue());
            prepStmt.setString(2, valueObject.code);
            prepStmt.setString(3, valueObject.name);
            prepStmt.setString(4, valueObject.description);
            prepStmt.setInt(5, valueObject.catId.intValue());
            prepStmt.setString(6, valueObject.deptType);
            prepStmt.setString(7, valueObject.status);
            prepStmt.setTimestamp(8, valueObject.lastUpdate);
            prepStmt.setInt(9, valueObject.userIdEdit.intValue());
            prepStmt.setInt(10, valueObject.pkid.intValue());
            int rowCount = prepStmt.executeUpdate();
            if (rowCount == 0) {
                throw new EJBException("Storing row for pkid " + this.valueObject.pkid.toString() + " failed.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            closeConnection(con);
        }
    }

    private Collection selectObjectsGiven(String fieldName, String value) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(" criteria : " + fieldName + " " + value);
            ArrayList objectSet = new ArrayList();
            con = makeConnection();
            String selectStmt = " select " + PKID + " from " + TABLENAME + "  where " + fieldName + " = ? ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setString(1, value);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                objectSet.add(new Integer(rs.getInt(PKID)));
            }
            return objectSet;
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            closeConnection(con);
        }
    }

    private Integer getNextPkid() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + "In getNextPKId()");
            con = makeConnection();
            String findMaxPKIdStmt = " select max(" + PKID + ") as max_pkid from " + TABLENAME + " ";
            prepStmt = con.prepareStatement(findMaxPKIdStmt);
            ResultSet rs = prepStmt.executeQuery();
            int nextId = 0;
            if (rs.next()) {
                nextId = rs.getInt("max_pkid") + 1;
                Log.printVerbose(strObjectName + "next pkid = " + nextId);
            } else {
                throw new EJBException(strObjectName + "Error while retrieving max(pkid)");
            }
            return new Integer(nextId);
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            closeConnection(con);
        }
    }
}

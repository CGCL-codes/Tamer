package com.vlee.ejb.customer;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import com.vlee.local.*;
import com.vlee.util.*;

public class CustAccountUserBean implements EntityBean {

    public static final String PKID = "pkid";

    public static final String CUST_ACCOUNT_ID = "cust_account_id";

    public static final String CUST_USER_ID = "cust_user_id";

    public static final String REMARKS = "remarks";

    public static final String STATUS = "status";

    public static final String LASTUPDATE = "lastupdate";

    public static final String USERID_EDIT = "userid_edit";

    public static final String STATUS_ACTIVE = "active";

    public static final String STATUS_INACTIVE = "inactive";

    private Integer pkid;

    private Integer custAccountId;

    private Integer custUserId;

    private String remarks;

    private String status;

    private Timestamp lastUpdate;

    private Integer userIdUpdate;

    private Connection con = null;

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String TABLENAME = "cust_account_user_link";

    private static final String strObjectName = "CustAccountUserBean: ";

    private EntityContext context = null;

    /***************************************************************************
	 * Getters
	 **************************************************************************/
    public Integer getPkid() {
        return pkid;
    }

    public Integer getCustAccountId() {
        return custAccountId;
    }

    public Integer getCustUserId() {
        return custUserId;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public Integer getUserIdUpdate() {
        return userIdUpdate;
    }

    /***************************************************************************
	 * Setters
	 **************************************************************************/
    public void setPkid(Integer pkid) {
        this.pkid = pkid;
    }

    public void setCustAccountId(Integer custAccountId) {
        this.custAccountId = custAccountId;
    }

    public void setCustUserId(Integer custUserId) {
        this.custUserId = custUserId;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setUserIdUpdate(Integer userIdUpdate) {
        this.userIdUpdate = userIdUpdate;
    }

    /***************************************************************************
	 * ejbCreate
	 **************************************************************************/
    public Integer ejbCreate(Integer custAccountId, Integer custUserId, String remarks, Timestamp tsCreate, Integer userIdUpdate) throws CreateException {
        Integer newKey = null;
        Log.printVerbose(strObjectName + "in ejbCreate");
        try {
            newKey = insertObject(custAccountId, custUserId, remarks, tsCreate, userIdUpdate);
        } catch (Exception ex) {
            throw new EJBException("ejbCreate: " + ex.getMessage());
        }
        if (newKey != null) {
            this.pkid = newKey;
            this.custAccountId = custAccountId;
            this.custUserId = custUserId;
            this.remarks = remarks;
            this.status = STATUS_ACTIVE;
            this.lastUpdate = tsCreate;
            this.userIdUpdate = userIdUpdate;
        }
        Log.printVerbose(strObjectName + "about to leave ejbCreate");
        return pkid;
    }

    /***************************************************************************
	 * ejbFindByPrimaryKey
	 **************************************************************************/
    public Integer ejbFindByPrimaryKey(Integer primaryKey) throws FinderException {
        Log.printVerbose(strObjectName + "in ejbFindByPrimaryKey");
        boolean result;
        try {
            result = selectByPrimaryKey(primaryKey);
        } catch (Exception ex) {
            throw new EJBException("ejbFindByPrimaryKey: " + ex.getMessage());
        }
        if (result) {
            return primaryKey;
        } else {
            throw new ObjectNotFoundException("Row for id " + primaryKey.toString() + "not found.");
        }
    }

    /***************************************************************************
	 * ejbFindAllObjects
	 **************************************************************************/
    public Collection ejbFindAllObjects() throws FinderException {
        Log.printVerbose(strObjectName + " In ejbFindAllObjects");
        Collection result;
        try {
            result = selectAll();
        } catch (Exception ex) {
            throw new EJBException("ejbFindAllObjects: " + ex.getMessage());
        }
        return result;
    }

    /***************************************************************************
	 * ejbFindObjectsGiven
	 **************************************************************************/
    public Collection ejbFindObjectsGiven(String fieldName, String value) throws FinderException {
        try {
            Collection bufAL = selectObjectsGiven(fieldName, value);
            return bufAL;
        } catch (Exception ex) {
            Log.printDebug(strObjectName + "ejbFindObjectsGiven: " + ex.getMessage());
            return null;
        }
    }

    /***************************************************************************
	 * ejbHomeGetNextPkid()
	 **************************************************************************/
    public Integer ejbHomeGetNextPkid() {
        try {
            makeConnection();
            Integer nextPkid = getNextPKId();
            closeConnection();
            return nextPkid;
        } catch (Exception ex) {
            Log.printDebug(strObjectName + "ejbHomeGetNextPkid: " + ex.getMessage());
            throw new EJBException(ex.getMessage());
        }
    }

    /***************************************************************************
	 * ejbRemove
	 **************************************************************************/
    public void ejbRemove() {
        Log.printVerbose(strObjectName + " In ejbRemove");
        try {
            deleteObject(this.pkid);
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
        this.pkid = (Integer) context.getPrimaryKey();
    }

    /***************************************************************************
	 * ejbPassivate
	 **************************************************************************/
    public void ejbPassivate() {
        Log.printVerbose(strObjectName + " In ejbPassivate");
        this.pkid = null;
    }

    /***************************************************************************
	 * ejbLoad
	 **************************************************************************/
    public void ejbLoad() {
        Log.printVerbose(strObjectName + " In ejbLoad");
        try {
            loadObject();
        } catch (Exception ex) {
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
	 * ejbPostCreate
	 **************************************************************************/
    public void ejbPostCreate(Integer custAccountId, Integer custUserId, String remarks, Timestamp tsCreate, Integer userIdUpdate) {
    }

    /** ********************* Database Routines ************************ */
    private void makeConnection() throws NamingException, SQLException {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(dsName);
            con = ds.getConnection();
        } catch (Exception ex) {
            throw new EJBException("Unable to connect to database. " + ex.getMessage());
        }
    }

    private void closeConnection() throws NamingException, SQLException {
        try {
            con.close();
        } catch (SQLException ex) {
            throw new EJBException("closeConnection: " + ex.getMessage());
        }
    }

    private Integer insertObject(Integer custAccountId, Integer custUserId, String remarks, Timestamp tsCreate, Integer userIdUpdate) throws NamingException, SQLException {
        Integer nextPKId = null;
        Log.printVerbose(strObjectName + " insertObject: ");
        makeConnection();
        try {
            nextPKId = getNextPKId();
        } catch (Exception ex) {
            throw new EJBException(strObjectName + ex.getMessage());
        }
        String insertStatement = "insert into " + TABLENAME + "(" + PKID + ", " + CUST_ACCOUNT_ID + ", " + CUST_USER_ID + ", " + REMARKS + ", " + STATUS + ", " + LASTUPDATE + ", " + USERID_EDIT + ") values ( ?, ?, ?, ?, ?, ?, ? )";
        PreparedStatement prepStmt = con.prepareStatement(insertStatement);
        prepStmt.setInt(1, nextPKId.intValue());
        prepStmt.setInt(2, custAccountId.intValue());
        prepStmt.setInt(3, custUserId.intValue());
        prepStmt.setString(4, remarks);
        prepStmt.setString(5, STATUS_ACTIVE);
        prepStmt.setTimestamp(6, tsCreate);
        prepStmt.setInt(7, userIdUpdate.intValue());
        prepStmt.executeUpdate();
        prepStmt.close();
        closeConnection();
        Log.printVerbose(strObjectName + " Leaving insertObject: ");
        return nextPKId;
    }

    private boolean selectByPrimaryKey(Integer primaryKey) throws NamingException, SQLException {
        Log.printVerbose(strObjectName + " selectByPrimaryKey: ");
        makeConnection();
        String selectStatement = "select " + PKID + " from " + TABLENAME + " where " + PKID + " = ?";
        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
        prepStmt.setInt(1, primaryKey.intValue());
        ResultSet rs = prepStmt.executeQuery();
        boolean result = rs.next();
        prepStmt.close();
        closeConnection();
        Log.printVerbose(strObjectName + " Leaving selectByPrimaryKey:");
        return result;
    }

    private void deleteObject(Integer pkid) throws NamingException, SQLException {
        Log.printVerbose(strObjectName + " deleteObject: ");
        makeConnection();
        String deleteStatement = "delete from " + TABLENAME + " where " + PKID + " = ?";
        PreparedStatement prepStmt = con.prepareStatement(deleteStatement);
        prepStmt.setInt(1, pkid.intValue());
        prepStmt.executeUpdate();
        prepStmt.close();
        closeConnection();
        Log.printVerbose(strObjectName + " Leaving deleteObject: ");
    }

    private void loadObject() throws NamingException, SQLException {
        Log.printVerbose(strObjectName + " loadObject: ");
        makeConnection();
        String selectStatement = "select " + PKID + ", " + CUST_ACCOUNT_ID + ", " + CUST_USER_ID + ", " + REMARKS + ", " + STATUS + ", " + LASTUPDATE + ", " + USERID_EDIT + " from " + TABLENAME + " where " + PKID + " = ? ";
        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
        prepStmt.setInt(1, this.pkid.intValue());
        ResultSet rs = prepStmt.executeQuery();
        if (rs.next()) {
            this.pkid = new Integer(rs.getInt(PKID));
            this.custAccountId = new Integer(rs.getInt(CUST_ACCOUNT_ID));
            this.custUserId = new Integer(rs.getInt(CUST_USER_ID));
            this.remarks = rs.getString(REMARKS);
            this.status = rs.getString(STATUS);
            this.lastUpdate = rs.getTimestamp(LASTUPDATE);
            this.userIdUpdate = new Integer(rs.getInt(USERID_EDIT));
            prepStmt.close();
        } else {
            prepStmt.close();
            throw new NoSuchEntityException("Row for pkid " + this.pkid.toString() + " not found in database.");
        }
        closeConnection();
        Log.printVerbose(strObjectName + " Leaving loadObject: ");
    }

    private void storeObject() throws NamingException, SQLException {
        Log.printVerbose(strObjectName + " storeObject ");
        makeConnection();
        String updateStatement = "update " + TABLENAME + " set " + PKID + " = ?, " + CUST_ACCOUNT_ID + " = ?, " + CUST_USER_ID + " = ?, " + REMARKS + " = ?, " + STATUS + " = ?, " + LASTUPDATE + " = ?, " + USERID_EDIT + " = ? " + "where " + PKID + " = ?";
        PreparedStatement prepStmt = con.prepareStatement(updateStatement);
        prepStmt.setInt(1, this.pkid.intValue());
        prepStmt.setInt(2, this.custAccountId.intValue());
        prepStmt.setInt(3, this.custUserId.intValue());
        prepStmt.setString(4, this.remarks);
        prepStmt.setString(5, this.status);
        prepStmt.setTimestamp(6, this.lastUpdate);
        prepStmt.setInt(7, this.userIdUpdate.intValue());
        prepStmt.setInt(8, this.pkid.intValue());
        int rowCount = prepStmt.executeUpdate();
        prepStmt.close();
        closeConnection();
        if (rowCount == 0) {
            throw new EJBException("Storing row for pkid " + pkid + " failed.");
        }
        Log.printVerbose(strObjectName + " Leaving storeObject: ");
    }

    private Collection selectAll() throws NamingException, SQLException {
        Log.printVerbose(strObjectName + " selectAll: ");
        makeConnection();
        String selectStatement = "select " + PKID + " from " + TABLENAME;
        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
        ResultSet rs = prepStmt.executeQuery();
        ArrayList pkIdList = new ArrayList();
        while (rs.next()) {
            pkIdList.add(new Integer(rs.getInt(1)));
        }
        prepStmt.close();
        closeConnection();
        Log.printVerbose(strObjectName + " Leaving selectAll: ");
        return pkIdList;
    }

    private Collection selectObjectsGiven(String fieldName, String value) throws NamingException, SQLException {
        Log.printVerbose(" criteria : " + fieldName + " " + value);
        ArrayList objectSet = new ArrayList();
        makeConnection();
        String selectStatement = " select " + PKID + " from " + TABLENAME + "  where " + fieldName + " = ? ";
        Log.printVerbose("selectStmt = " + selectStatement);
        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
        prepStmt.setString(1, value);
        ResultSet rs = prepStmt.executeQuery();
        while (rs.next()) {
            Log.printVerbose("Found a match ... ");
            objectSet.add(new Integer(rs.getInt(1)));
        }
        prepStmt.close();
        closeConnection();
        return objectSet;
    }

    private Integer getNextPKId() throws NamingException, SQLException {
        Log.printVerbose(strObjectName + "In getNextPKId()");
        String findMaxPKIdStmt = " select max(" + PKID + ") as max_pkid from " + TABLENAME + " ";
        PreparedStatement prepStmt = con.prepareStatement(findMaxPKIdStmt);
        ResultSet rs = prepStmt.executeQuery();
        int nextId = 0;
        if (rs.next()) {
            nextId = rs.getInt("max_pkid") + 1;
            Log.printVerbose(strObjectName + "next pkid = " + nextId);
        } else throw new EJBException(strObjectName + "Error while retrieving max(pkid)");
        prepStmt.close();
        Log.printVerbose(strObjectName + "Leaving  getNextPKId()");
        return new Integer(nextId);
    }

    public Timestamp getCurrentTime() {
        Log.printVerbose(strObjectName + "In getCurrentTime()");
        GregorianCalendar cal = new GregorianCalendar();
        Log.printVerbose(strObjectName + "currentTime = " + cal.getTime());
        Timestamp ts = new Timestamp((long) cal.getTimeInMillis());
        Log.printVerbose(strObjectName + "Leaving getCurrentTime()");
        return ts;
    }
}

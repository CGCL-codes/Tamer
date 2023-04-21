package com.vlee.ejb.customer;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import com.vlee.local.*;
import com.vlee.util.*;

public class CustUserDetailsBean implements EntityBean {

    public static final String PKID = "pkid";

    public static final String CUST_USERID = "cust_userid";

    public static final String DOB = "dob";

    public static final String TITLE = "title";

    public static final String SEX = "sex";

    public static final String ETHNIC = "ethnic";

    public static final String IC_NO = "ic_no";

    public static final String IC_TYPE = "ic_type";

    public static final String ADDR1 = "addr1";

    public static final String ADDR2 = "addr2";

    public static final String ADDR3 = "addr3";

    public static final String ZIP = "zip";

    public static final String STATE = "state";

    public static final String COUNTRYCODE = "countrycode";

    public static final String LASTUPDATE = "lastupdate";

    public static final String USERID_EDIT = "userid_edit";

    public static final String STATUS_ACTIVE = "active";

    public static final String STATUS_INACTIVE = "inactive";

    private Integer pkid;

    private Integer custUserId;

    private Calendar dateOfBirth;

    private String title;

    private String sex;

    private String ethnic;

    private String ICNo;

    private String ICType;

    private String addr1;

    private String addr2;

    private String addr3;

    private String zip;

    private String state;

    private String countryCode;

    private Timestamp lastUpdate;

    private Integer userIdUpdate;

    private Connection con = null;

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String TABLENAME = "cust_userdetails_index";

    private static final String strObjectName = "CustUserDetailsBean: ";

    private EntityContext context = null;

    /***************************************************************************
	 * Getters
	 **************************************************************************/
    public Integer getPkid() {
        return pkid;
    }

    public Integer getCustUserId() {
        return custUserId;
    }

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public String getTitle() {
        return title;
    }

    public String getSex() {
        return sex;
    }

    public String getEthnic() {
        return ethnic;
    }

    public String getICNo() {
        return ICNo;
    }

    public String getICType() {
        return ICType;
    }

    public String getAddr1() {
        return addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public String getAddr3() {
        return addr3;
    }

    public String getZip() {
        return zip;
    }

    public String getState() {
        return state;
    }

    public String getCountryCode() {
        return countryCode;
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

    public void setCustUserId(Integer custUserId) {
        this.custUserId = custUserId;
    }

    public void setDateOfBirth(Calendar dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public void setICNo(String ICNo) {
        this.ICNo = ICNo;
    }

    public void setICType(String ICType) {
        this.ICType = ICType;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public void setAddr3(String addr3) {
        this.addr3 = addr3;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setUserIdUpdate(Integer userIdUpdate) {
        this.userIdUpdate = userIdUpdate;
    }

    /***************************************************************************
	 * ejbCreate (1)
	 **************************************************************************/
    public Integer ejbCreate(Integer custUserId, Calendar dateOfBirth, String title, String sex, String ethnic, String ICNo, String ICType, String addr1, String addr2, String addr3, String zip, String state, String countryCode, Timestamp tsCreate, Integer userIdUpdate) throws CreateException {
        Integer newKey = null;
        Log.printVerbose(strObjectName + "in ejbCreate");
        try {
            newKey = insertObject(custUserId, dateOfBirth, title, sex, ethnic, ICNo, ICType, addr1, addr2, addr3, zip, state, countryCode, tsCreate, userIdUpdate);
        } catch (Exception ex) {
            throw new EJBException("ejbCreate: " + ex.getMessage());
        }
        if (newKey != null) {
            this.pkid = newKey;
            this.custUserId = custUserId;
            this.dateOfBirth = dateOfBirth;
            this.title = title;
            this.sex = sex;
            this.ethnic = ethnic;
            this.ICNo = ICNo;
            this.ICType = ICType;
            this.addr1 = addr1;
            this.addr2 = addr2;
            this.addr3 = addr3;
            this.zip = zip;
            this.state = state;
            this.countryCode = countryCode;
            this.lastUpdate = tsCreate;
            this.userIdUpdate = userIdUpdate;
        }
        Log.printVerbose(strObjectName + "about to leave ejbCreate");
        return pkid;
    }

    /***************************************************************************
	 * ejbCreate (2)
	 **************************************************************************/
    public Integer ejbCreate(Integer custUserId, Timestamp tsCreate, Integer userIdCreate) throws CreateException {
        Integer newKey = null;
        Log.printVerbose(strObjectName + "in ejbCreate(Integer custUserId)");
        return ejbCreate(custUserId, new GregorianCalendar(1970, 0, 1), "", "", "", "", "", "", "", "", "", "", "", tsCreate, userIdCreate);
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
	 * ejbPostCreate (1)
	 **************************************************************************/
    public void ejbPostCreate(Integer custUserId, Calendar dateOfBirth, String title, String sex, String ethnic, String ICNo, String ICType, String addr1, String addr2, String addr3, String zip, String state, String countryCode, Timestamp tsCreate, Integer userIdUpdate) {
    }

    /***************************************************************************
	 * ejbPostCreate (2)
	 **************************************************************************/
    public void ejbPostCreate(Integer custUserId, Timestamp tsCreate, Integer userIdUpdate) {
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

    private Integer insertObject(Integer custUserId, Calendar dateOfBirth, String title, String sex, String ethnic, String ICNo, String ICType, String addr1, String addr2, String addr3, String zip, String state, String countryCode, Timestamp tsCreate, Integer userIdUpdate) throws NamingException, SQLException {
        Integer nextPKId = null;
        Log.printVerbose(strObjectName + " insertObject: ");
        makeConnection();
        try {
            nextPKId = getNextPKId();
        } catch (Exception ex) {
            throw new EJBException(strObjectName + ex.getMessage());
        }
        String insertStatement = "insert into " + TABLENAME + "(" + PKID + ", " + CUST_USERID + ", " + DOB + ", " + TITLE + ", " + SEX + ", " + ETHNIC + ", " + IC_NO + ", " + IC_TYPE + ", " + ADDR1 + ", " + ADDR2 + ", " + ADDR3 + ", " + ZIP + ", " + STATE + ", " + COUNTRYCODE + ", " + LASTUPDATE + ", " + USERID_EDIT + ") values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        PreparedStatement prepStmt = con.prepareStatement(insertStatement);
        prepStmt.setInt(1, nextPKId.intValue());
        prepStmt.setInt(2, custUserId.intValue());
        prepStmt.setDate(3, new java.sql.Date(dateOfBirth.getTime().getTime()));
        prepStmt.setString(4, title);
        prepStmt.setString(5, sex);
        prepStmt.setString(6, ethnic);
        prepStmt.setString(7, ICNo);
        prepStmt.setString(8, ICType);
        prepStmt.setString(9, addr1);
        prepStmt.setString(10, addr2);
        prepStmt.setString(11, addr3);
        prepStmt.setString(12, zip);
        prepStmt.setString(13, state);
        prepStmt.setString(14, countryCode);
        prepStmt.setTimestamp(15, tsCreate);
        prepStmt.setInt(16, userIdUpdate.intValue());
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
        String selectStatement = "select " + PKID + ", " + CUST_USERID + ", " + DOB + ", " + TITLE + ", " + SEX + ", " + ETHNIC + ", " + IC_NO + ", " + IC_TYPE + ", " + ADDR1 + ", " + ADDR2 + ", " + ADDR3 + ", " + ZIP + ", " + STATE + ", " + COUNTRYCODE + ", " + LASTUPDATE + ", " + USERID_EDIT + " from " + TABLENAME + " where " + PKID + " = ? ";
        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
        prepStmt.setInt(1, this.pkid.intValue());
        ResultSet rs = prepStmt.executeQuery();
        if (rs.next()) {
            this.pkid = new Integer(rs.getInt(PKID));
            Log.printVerbose("pkid = " + rs.getInt(PKID));
            this.custUserId = new Integer(rs.getInt(CUST_USERID));
            Log.printVerbose("custUserId = " + rs.getInt(CUST_USERID));
            this.dateOfBirth = new GregorianCalendar();
            this.dateOfBirth.setTime(rs.getDate(DOB));
            Log.printVerbose("dateOfBirth = " + rs.getDate(DOB));
            this.title = rs.getString(TITLE);
            Log.printVerbose("title = " + rs.getString(TITLE));
            this.sex = rs.getString(SEX);
            Log.printVerbose("sex = " + rs.getString(SEX));
            this.ethnic = rs.getString(ETHNIC);
            this.ICNo = rs.getString(IC_NO);
            this.ICType = rs.getString(IC_TYPE);
            this.addr1 = rs.getString(ADDR1);
            Log.printVerbose("addr1 = " + rs.getString(ADDR1));
            this.addr2 = rs.getString(ADDR2);
            Log.printVerbose("addr2 " + rs.getString(ADDR2));
            this.addr3 = rs.getString(ADDR3);
            Log.printVerbose("addr3 = " + rs.getString(ADDR3));
            this.zip = rs.getString(ZIP);
            this.state = rs.getString(STATE);
            this.countryCode = rs.getString(COUNTRYCODE);
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
        String updateStatement = "update " + TABLENAME + " set " + PKID + " = ?, " + CUST_USERID + " = ?, " + DOB + " = ?, " + TITLE + " = ?, " + SEX + " = ?, " + ETHNIC + " = ?, " + IC_NO + " = ?, " + IC_TYPE + " = ?, " + ADDR1 + " = ?, " + ADDR2 + " = ?, " + ADDR3 + " = ?, " + ZIP + " = ?, " + STATE + " = ?, " + COUNTRYCODE + " = ?, " + LASTUPDATE + " = ?, " + USERID_EDIT + " = ? " + "where " + PKID + " = ?";
        PreparedStatement prepStmt = con.prepareStatement(updateStatement);
        prepStmt.setInt(1, this.pkid.intValue());
        prepStmt.setInt(2, this.custUserId.intValue());
        prepStmt.setDate(3, new java.sql.Date(dateOfBirth.getTime().getTime()));
        prepStmt.setString(4, this.title);
        prepStmt.setString(5, this.sex);
        prepStmt.setString(6, this.ethnic);
        prepStmt.setString(7, this.ICNo);
        prepStmt.setString(8, this.ICType);
        prepStmt.setString(9, this.addr1);
        prepStmt.setString(10, this.addr2);
        prepStmt.setString(11, this.addr3);
        prepStmt.setString(12, this.zip);
        prepStmt.setString(13, this.state);
        prepStmt.setString(14, this.countryCode);
        prepStmt.setTimestamp(15, this.lastUpdate);
        prepStmt.setInt(16, this.userIdUpdate.intValue());
        prepStmt.setInt(17, this.pkid.intValue());
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

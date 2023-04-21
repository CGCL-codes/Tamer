package com.vlee.ejb.accounting;

import java.io.*;
import java.sql.*;
import java.rmi.*;
import javax.sql.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;
import com.vlee.local.*;
import com.vlee.util.*;
import java.math.*;

public class ProfitCostCenterBean implements EntityBean {

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String TABLENAME = "acc_pccenter_index";

    protected final String strObjectName = "ProfitCostCenterBean: ";

    private Connection con;

    private EntityContext mContext;

    protected final String strTimeBegin = "0000-01-01 00:00:00.000000000";

    public static final String ACTIVE = "active";

    public static final String INACTIVE = "inactive";

    public static final String PKID = "pccenterid";

    public static final String CODE = "pccentercode";

    public static final String BIZ_UNIT_ID = "bizunitid";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    public static final String STATUS = "status";

    public static final String LAST_UPDATE = "lastupdate";

    public static final String USERID_EDIT = "userid_edit";

    public static final String FULLNAME = "fullname";

    public static final String CURRENCY = "currency";

    public static final String STATUS_ACTIVE = "active";

    public static final String STATUS_INACTIVE = "inactive";

    public static final String STATUS_DELETED = "deleted";

    private ProfitCostCenterObject valObj;

    public ProfitCostCenterObject getValueObject() {
        return this.valObj;
    }

    public void setValueObject(ProfitCostCenterObject pcco) throws Exception {
        if (pcco == null) {
            throw new Exception("Object undefined");
        }
        Integer pkid = this.valObj.mPkid;
        this.valObj = pcco;
        this.valObj.mPkid = pkid;
    }

    public Integer getPkid() {
        return this.valObj.mPkid;
    }

    public void setPkid(Integer pkid) {
        this.valObj.mPkid = pkid;
    }

    public Integer getProfitCostCenterId() {
        return this.valObj.mPkid;
    }

    public void setProfitCostCenterId(Integer pccid) {
        this.valObj.mPkid = pccid;
    }

    public String getCode() {
        return this.valObj.mCode;
    }

    public void setCode(String strPCCC) {
        this.valObj.mCode = strPCCC;
    }

    public Integer getBizUnitId() {
        return this.valObj.mBizUnitId;
    }

    public void setBizUnitId(Integer buid) {
        this.valObj.mBizUnitId = buid;
    }

    public String getName() {
        return this.valObj.mName;
    }

    public void setName(String strName) {
        this.valObj.mName = strName;
    }

    public String getDescription() {
        return this.valObj.mDescription;
    }

    public void setDescription(String strDesc) {
        this.valObj.mDescription = strDesc;
    }

    public String getStatus() {
        return this.valObj.mStatus;
    }

    public void setStatus(String stts) {
        this.valObj.mStatus = stts;
    }

    public Timestamp getLastUpdateTime() {
        return this.valObj.mLastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp tsTime) {
        this.valObj.mLastUpdateTime = tsTime;
    }

    public Integer getUserIdEdit() {
        return this.valObj.mUserIdEdit;
    }

    public void setUserIdEdit(Integer intUserId) {
        this.valObj.mUserIdEdit = intUserId;
    }

    public Integer getPrimaryKey() {
        return this.valObj.mPkid;
    }

    public void setPrimaryKey(Integer jtid) {
        this.valObj.mPkid = jtid;
    }

    public Integer ejbCreate(ProfitCostCenterObject newObj) throws CreateException {
        Integer pkid = null;
        Log.printVerbose(strObjectName + " in ejbCreate ");
        try {
            pkid = insertNewRow(newObj);
        } catch (Exception ex) {
            throw new EJBException("ejbCreate: " + ex.getMessage());
        }
        if (pkid != null) {
            this.valObj = newObj;
            this.valObj.mPkid = pkid;
            ejbStore();
        }
        Log.printVerbose(strObjectName + " leaving ejbCreate ");
        return pkid;
    }

    public void ejbPostCreate(ProfitCostCenterObject pccObj) {
    }

    public Integer ejbFindByPrimaryKey(Integer pkid) throws FinderException {
        Log.printVerbose(strObjectName + " in ejbFindByPrimaryKey ");
        boolean result;
        try {
            result = selectByPrimaryKey(pkid);
        } catch (Exception ex) {
            throw new EJBException("ejbFindByPrimaryKey: " + ex.getMessage());
        }
        Log.printVerbose(strObjectName + " leaving ejbFindByPrimaryKey ");
        if (result) {
            return pkid;
        } else {
            throw new ObjectNotFoundException("Row for id " + pkid.toString() + " not found.");
        }
    }

    public void ejbRemove() {
        try {
            deleteObject(this.valObj.mPkid);
        } catch (Exception ex) {
            throw new EJBException("ejbRemove: " + ex.getMessage());
        }
    }

    public void setEntityContext(EntityContext mContext) {
        Log.printVerbose(strObjectName + " in setEntityContext");
        this.mContext = mContext;
        Log.printVerbose(strObjectName + " Leaving setEntityContext");
    }

    public void unsetEntityContext() {
        Log.printVerbose(strObjectName + " In unsetEntityContext");
        this.mContext = null;
        Log.printVerbose(strObjectName + " Leaving unsetEntityContext");
    }

    public void ejbActivate() {
        Log.printVerbose(strObjectName + " In ejbActivate");
        this.valObj = new ProfitCostCenterObject();
        this.valObj.mPkid = (Integer) mContext.getPrimaryKey();
        Log.printVerbose(strObjectName + " Leaving ejbActivate");
    }

    public void ejbPassivate() {
        Log.printVerbose(strObjectName + " In ejbPassivate");
        this.valObj = null;
        Log.printVerbose(strObjectName + " In ejbPassivate");
    }

    public void ejbLoad() {
        Log.printVerbose(strObjectName + " In ejbLoad");
        try {
            loadObject();
        } catch (Exception ex) {
            Log.printDebug(strObjectName + "ejbLoad: " + ex.getMessage());
        }
        Log.printVerbose(strObjectName + " Leaving ejbLoad");
    }

    public void ejbStore() {
        Log.printVerbose(strObjectName + " In ejbStore");
        try {
            storeObject();
        } catch (Exception ex) {
            throw new EJBException("ejbStore: " + ex.getMessage());
        }
        Log.printVerbose(strObjectName + " Leaving ejbStore");
    }

    public Collection ejbFindAllObjects() throws FinderException {
        try {
            Collection bufAL = selectAllObjects();
            return bufAL;
        } catch (Exception ex) {
            Log.printDebug(strObjectName + "ejbFindAllObjects: " + ex);
            return null;
        }
    }

    public Collection ejbFindObjectsGiven(String fieldName, String value) throws FinderException {
        try {
            Collection bufAL = selectObjectsGiven(fieldName, value);
            return bufAL;
        } catch (Exception ex) {
            Log.printDebug(strObjectName + "ejbFindObjectsGiven: " + ex.getMessage());
            return null;
        }
    }

    public Vector ejbHomeGetAllValueObjects() {
        Vector vecValObj = null;
        try {
            vecValObj = selectAllValueObjects();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.printDebug("ERROR : " + ex.getMessage());
        }
        return vecValObj;
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

    /** ***************** Database Routines ************************ */
    private void makeConnection() {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(dsName);
            con = ds.getConnection();
        } catch (Exception ex) {
            throw new EJBException("Unable to connect to database. " + ex.getMessage());
        }
    }

    private void releaseConnection() {
        try {
            con.close();
        } catch (SQLException ex) {
            throw new EJBException("releaseConnection: " + ex.getMessage());
        }
    }

    private Integer insertNewRow(ProfitCostCenterObject newObj) throws SQLException {
        makeConnection();
        Log.printVerbose(strObjectName + "insertNewRow: ");
        String findMaxPkidStmt = " select pccenterid from " + TABLENAME + " ";
        PreparedStatement prepStmt = con.prepareStatement(findMaxPkidStmt);
        ResultSet rs = prepStmt.executeQuery();
        int bufInt = 0;
        while (rs.next()) {
            if (bufInt < rs.getInt("pccenterid")) {
                bufInt = rs.getInt("pccenterid");
            }
        }
        Integer newPkid = new Integer(bufInt + 1);
        Log.printVerbose("The new objectid is :" + newPkid.toString());
        newObj.mPkid = newPkid;
        String insertStatement = "insert into " + TABLENAME + " (pccenterid , pccentercode, bizunitid,  " + " name , description , " + " status , lastupdate , userid_edit, fullname, currency ) " + " values ( ?, ? , ? , ? ,? ,?, ?, ?, ?, ? )";
        prepStmt = con.prepareStatement(insertStatement);
        prepStmt.setInt(1, newPkid.intValue());
        prepStmt.setString(2, newObj.mCode);
        prepStmt.setInt(3, newObj.mBizUnitId.intValue());
        prepStmt.setString(4, newObj.mName);
        prepStmt.setString(5, newObj.mDescription);
        prepStmt.setString(6, ProfitCostCenterBean.ACTIVE);
        prepStmt.setTimestamp(7, newObj.mLastUpdateTime);
        prepStmt.setInt(8, newObj.mUserIdEdit.intValue());
        prepStmt.setString(9, newObj.mFullname);
        prepStmt.setString(10, newObj.mCurrency);
        prepStmt.executeUpdate();
        prepStmt.close();
        Log.printAudit(strObjectName + "Created New Row:" + newPkid.toString());
        Log.printVerbose(strObjectName + "leaving insertNewRow");
        releaseConnection();
        return newPkid;
    }

    private boolean selectByPrimaryKey(Integer pkid) throws SQLException {
        makeConnection();
        String selectStatement = "select * " + "from " + TABLENAME + " where pccenterid = ? ";
        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
        prepStmt.setInt(1, pkid.intValue());
        ResultSet rs = prepStmt.executeQuery();
        boolean result = false;
        result = rs.next();
        prepStmt.close();
        releaseConnection();
        return result;
    }

    private void deleteObject(Integer pkid) throws SQLException {
        makeConnection();
        String deleteStatement = "delete from " + TABLENAME + "  " + "where pccenterid = ?";
        PreparedStatement prepStmt = con.prepareStatement(deleteStatement);
        prepStmt.setInt(1, pkid.intValue());
        prepStmt.executeUpdate();
        prepStmt.close();
        Log.printAudit(strObjectName + "Deleted Object : " + pkid.toString());
        releaseConnection();
    }

    private void loadObject() throws SQLException, Exception {
        makeConnection();
        String selectStatement = "select  *  from " + TABLENAME + " where pccenterid = ? ";
        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
        prepStmt.setInt(1, this.valObj.mPkid.intValue());
        ResultSet rs = prepStmt.executeQuery();
        if (rs.next()) {
            this.valObj = getObject(rs, "");
            prepStmt.close();
        } else {
            prepStmt.close();
            throw new NoSuchEntityException("Row for this EJB Object" + " is not found in database.");
        }
        releaseConnection();
    }

    private Vector selectAllValueObjects() throws SQLException {
        Vector vecValObj = new Vector();
        makeConnection();
        String selectStmt = "SELECT " + TABLENAME + "." + PKID + ", " + TABLENAME + "." + NAME + ", acc_bizunit_index.name, acc_bizentity_index.name FROM " + TABLENAME + ", acc_bizunit_index, acc_bizentity_index WHERE " + TABLENAME + "." + BIZ_UNIT_ID + " = acc_bizunit_index.pkid AND " + "acc_bizunit_index.bizentityid = acc_bizentity_index.pkid AND " + TABLENAME + "." + STATUS + " = ? ORDER BY acc_bizentity_index.bizentitycode";
        PreparedStatement prepStmt = con.prepareStatement(selectStmt);
        prepStmt.setString(1, "active");
        ResultSet rs = prepStmt.executeQuery();
        while (rs.next()) {
            ProfitCostCenterObject pccObj = new ProfitCostCenterObject();
            pccObj.mPkid = new Integer(rs.getInt(1));
            pccObj.mFullname = rs.getString(4) + "-" + rs.getString(3) + "-" + rs.getString(2);
            vecValObj.add(pccObj);
        }
        prepStmt.close();
        releaseConnection();
        return vecValObj;
    }

    private Vector selectValueObjectsGiven(String fieldName1, String value1, String fieldName2, String value2) throws SQLException {
        Vector vecValObj = new Vector();
        makeConnection();
        String selectStmt = " SELECT *  FROM " + TABLENAME + " WHERE " + fieldName1 + " =  ? ";
        if (fieldName2 != null && value2 != null) {
            selectStmt += " AND " + fieldName2 + " = ? ";
        }
        selectStmt += " ORDER BY " + CODE;
        PreparedStatement prepStmt = con.prepareStatement(selectStmt);
        prepStmt.setString(1, value1);
        if (fieldName2 != null && value2 != null) {
            prepStmt.setString(2, value2);
        }
        ResultSet rs = prepStmt.executeQuery();
        while (rs.next()) {
            try {
                ProfitCostCenterObject pccObj = getObject(rs, "");
                vecValObj.add(pccObj);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        prepStmt.close();
        releaseConnection();
        return vecValObj;
    }

    private Collection selectAllObjects() throws SQLException {
        ArrayList objectSet = new ArrayList();
        makeConnection();
        String selectStatement = " select pccenterid from " + TABLENAME + "  ";
        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
        ResultSet rs = prepStmt.executeQuery();
        while (rs.next()) {
            objectSet.add(new Integer(rs.getInt(1)));
        }
        prepStmt.close();
        releaseConnection();
        return objectSet;
    }

    private Collection selectObjectsGiven(String fieldName, String value) throws SQLException {
        Log.printVerbose(" criteria : " + fieldName + " " + value);
        ArrayList objectSet = new ArrayList();
        makeConnection();
        String selectStatement = "select pccenterid from " + TABLENAME + "  where " + fieldName + " = ? ";
        PreparedStatement prepStmt = con.prepareStatement(selectStatement);
        prepStmt.setString(1, value);
        ResultSet rs = prepStmt.executeQuery();
        while (rs.next()) {
            objectSet.add(new Integer(rs.getInt(1)));
        }
        prepStmt.close();
        releaseConnection();
        return objectSet;
    }

    private void storeObject() throws SQLException {
        makeConnection();
        String updateStatement = "update " + TABLENAME + " set pccenterid = ? , " + " pccentercode =  ? , " + " bizunitid = ? , " + " name = ? , " + " description = ? , " + " status = ? , " + " lastupdate = ? , " + " userid_edit = ?,   " + " fullname = ?,   " + " currency = ?  " + " where pccenterid = ?;";
        PreparedStatement prepStmt = con.prepareStatement(updateStatement);
        prepStmt.setInt(1, this.valObj.mPkid.intValue());
        prepStmt.setString(2, this.valObj.mCode);
        prepStmt.setInt(3, this.valObj.mBizUnitId.intValue());
        prepStmt.setString(4, this.valObj.mName);
        prepStmt.setString(5, this.valObj.mDescription);
        prepStmt.setString(6, this.valObj.mStatus);
        prepStmt.setTimestamp(7, this.valObj.mLastUpdateTime);
        prepStmt.setInt(8, this.valObj.mUserIdEdit.intValue());
        prepStmt.setString(9, this.valObj.mFullname);
        prepStmt.setString(10, this.valObj.mCurrency);
        prepStmt.setInt(11, this.valObj.mPkid.intValue());
        int rowCount = prepStmt.executeUpdate();
        prepStmt.close();
        if (rowCount == 0) {
            throw new EJBException("Storing ejb object " + this.valObj.mPkid.toString() + " failed.");
        }
        releaseConnection();
    }

    public static ProfitCostCenterObject getObject(ResultSet rs, String options) throws Exception {
        ProfitCostCenterObject theObj = null;
        try {
            theObj = new ProfitCostCenterObject();
            theObj.mPkid = new Integer(rs.getInt(PKID));
            theObj.mCode = rs.getString(CODE);
            theObj.mBizUnitId = new Integer(rs.getInt(BIZ_UNIT_ID));
            theObj.mName = rs.getString(NAME);
            theObj.mDescription = rs.getString(DESCRIPTION);
            theObj.mStatus = rs.getString(STATUS);
            theObj.mLastUpdateTime = rs.getTimestamp(LAST_UPDATE);
            theObj.mUserIdEdit = new Integer(rs.getInt(USERID_EDIT));
            theObj.mFullname = rs.getString(FULLNAME);
            theObj.mCurrency = rs.getString(CURRENCY);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return theObj;
    }
}

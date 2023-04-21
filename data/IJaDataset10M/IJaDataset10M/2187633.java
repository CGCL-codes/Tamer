package com.vlee.ejb.ecommerce;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import com.vlee.local.ServerConfig;
import com.vlee.util.AppTableCounterUtil;
import com.vlee.util.Log;
import com.vlee.util.QueryObject;

public class EPaymentInboxBean implements EntityBean {

    public static final String PKID = "pkid";

    public static final String PAYMENT_MODE = "payment_mode";

    public static final String MERCHANT_ID = "merchant_id";

    public static final String MERCHANT_TRANX_ID = "merchant_tranx_id";

    public static final String TRANX_AMT = "tranx_amt";

    public static final String TRANX_STATUS = "tranx_status";

    public static final String TRANX_ERR_CODE = "tranx_err_code";

    public static final String TRANX_DATE = "tranx_date";

    public static final String TRANX_APPR_CODE = "tranx_appr_code";

    public static final String BANK_REF_NO = "bank_ref_no";

    public static final String PROPERTY1 = "property1";

    public static final String PROPERTY2 = "property2";

    public static final String PROPERTY3 = "property3";

    public static final String PROPERTY4 = "property4";

    public static final String PROPERTY5 = "property5";

    public static final Long PKID_START = new Long(100001);

    public static final String PAYMENT_MOBILEMONEY = "MM";

    public static final String PAYMENT_MAYBANK2U = "M2U";

    public static final String PAYMENT_RHBBANK = "RHB";

    public static final String PAYMENT_PAYDIRECT = "PD";

    public static final String PAYMENT_FPX = "FPX";

    public static final String PAYMENT_HLB = "HLB";

    public static final String PAYMENT_AMBANK = "AMBW2W";

    public static final String PAYMENT_POSPAY = "PP";

    public static final String PAYMENT_VISA = "VISA";

    public static final String PAYMENT_MASTER = "MASTER";

    public static final String STATUS_3RD_PARTY_APPROVAL_WAITING = "01";

    public static final String STATUS_3RD_PARTY_APPROVAL_APPROVED = "02";

    public static final String STATUS_3RD_PARTY_APPROVAL_REJECTED = "03";

    public static final String STATUS_3RD_PARTY_APPROVAL_TIMEOUT = "04";

    public static final String STATUS_3RD_PARTY_CONNECTION_FAIL = "05";

    EPaymentInboxObject valObj;

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String MODULENAME = "ecommerce";

    public static final String TABLENAME = "ecom_payment_inbox";

    private static final String strObjectName = "EPaymentInboxBean: ";

    private EntityContext context = null;

    /***************************************************************************
	 * Getters
	 **************************************************************************/
    public EPaymentInboxObject getObject() {
        return this.valObj;
    }

    public void setObject(EPaymentInboxObject newVal) {
        Long pkid = this.valObj.pkid;
        this.valObj = newVal;
        this.valObj.pkid = pkid;
    }

    public Long getPkid() {
        return this.valObj.pkid;
    }

    public Long getPrimaryKey() {
        return this.valObj.pkid;
    }

    public void setPrimaryKey(Long pkid) {
        this.valObj.pkid = pkid;
    }

    /***************************************************************************
	 * ejbCreate
	 **************************************************************************/
    public Long ejbCreate(EPaymentInboxObject newObj) throws CreateException {
        Log.printVerbose(strObjectName + "in ejbCreate");
        try {
            this.valObj = insertObject(newObj);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException("ejbCreate: " + ex.getMessage());
        }
        Log.printVerbose(strObjectName + "about to leave ejbCreate");
        return this.valObj.pkid;
    }

    /***************************************************************************
	 * ejbFindByPrimaryKey
	 **************************************************************************/
    public Long ejbFindByPrimaryKey(Long primaryKey) throws FinderException {
        Log.printVerbose(strObjectName + "in ejbFindByPrimaryKey");
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
	 * ejbFindByTransId
	 **************************************************************************/
    public Long ejbFindByTransId(Long id) throws FinderException {
        Log.printVerbose(strObjectName + "in ejbFindByTransId");
        boolean result = false;
        try {
            result = selectByTransId(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException("ejbFindByTransId: " + ex.getMessage());
        }
        if (result) {
            return id;
        } else {
            throw new ObjectNotFoundException("Row for id " + id.toString() + "not found.");
        }
    }

    /***************************************************************************
	 * ejbRemove
	 **************************************************************************/
    public void ejbRemove() {
        Log.printVerbose(strObjectName + " In ejbRemove");
        try {
            deleteObject(this.valObj.pkid);
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
            this.valObj = new EPaymentInboxObject();
            this.valObj.pkid = (Long) context.getPrimaryKey();
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
        this.valObj = null;
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
    public void ejbPostCreate(EPaymentInboxObject newObj) {
    }

    public Collection ejbHomeGetObjects(QueryObject query) {
        Collection col = null;
        try {
            col = selectObjects(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return col;
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

    private EPaymentInboxObject insertObject(EPaymentInboxObject newObj) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " insertObject: ");
            con = makeConnection();
            try {
                newObj.pkid = getNextPKId(con);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(strObjectName + ex.getMessage());
            }
            try {
                String insertStatement = " INSERT INTO " + TABLENAME + "( " + PKID + ", " + PAYMENT_MODE + ", " + MERCHANT_ID + ", " + MERCHANT_TRANX_ID + ", " + TRANX_AMT + ", " + TRANX_STATUS + ", " + TRANX_ERR_CODE + ", " + TRANX_DATE + ", " + TRANX_APPR_CODE + ", " + BANK_REF_NO + ", " + PROPERTY1 + ", " + PROPERTY2 + ", " + PROPERTY3 + ", " + PROPERTY4 + ", " + PROPERTY5 + ") values ( " + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?) ";
                prepStmt = con.prepareStatement(insertStatement);
                prepStmt.setLong(1, newObj.pkid.longValue());
                prepStmt.setString(2, newObj.payment_mode);
                prepStmt.setString(3, newObj.merchant_id);
                prepStmt.setInt(4, newObj.merchant_tranx_id.intValue());
                prepStmt.setBigDecimal(5, newObj.tranx_amt);
                prepStmt.setString(6, newObj.tranx_status);
                prepStmt.setString(7, newObj.tranx_err_code);
                prepStmt.setTimestamp(8, newObj.tranx_date);
                prepStmt.setString(9, newObj.tranx_appr_code);
                prepStmt.setString(10, newObj.bank_ref_no);
                prepStmt.setString(11, newObj.property1);
                prepStmt.setString(12, newObj.property2);
                prepStmt.setString(13, newObj.property3);
                prepStmt.setString(14, newObj.property4);
                prepStmt.setString(15, newObj.property5);
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
        return newObj;
    }

    private boolean selectByPrimaryKey(Long primaryKey) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " selectByPrimaryKey: ");
            con = makeConnection();
            String selectStmt = " SELECT " + PKID + " FROM " + TABLENAME + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setLong(1, primaryKey.longValue());
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

    private boolean selectByTransId(Long id) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " selectBySalesOrderId: ");
            con = makeConnection();
            String selectStmt = " SELECT " + PKID + " FROM " + TABLENAME + " WHERE " + MERCHANT_TRANX_ID + " = ? ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setLong(1, id.longValue());
            ResultSet rs = prepStmt.executeQuery();
            boolean result = rs.next();
            Log.printVerbose(strObjectName + " Leaving selectBySalesOrderId:");
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

    private void deleteObject(Long pkid) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " deleteObject: ");
            con = makeConnection();
            String deleteStatement = " DELETE FROM " + TABLENAME + " WHERE " + PKID + " = ?";
            prepStmt = con.prepareStatement(deleteStatement);
            prepStmt.setLong(1, pkid.longValue());
            prepStmt.executeUpdate();
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
            Log.printVerbose(strObjectName + " loadObject: ");
            con = makeConnection();
            String selectStmt = " SELECT * FROM " + TABLENAME + " WHERE " + MERCHANT_TRANX_ID + " = ? ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setLong(1, this.valObj.pkid.longValue());
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next()) {
                this.valObj = getObject(rs, "");
            } else {
                throw new NoSuchEntityException("Row for pkid " + this.valObj.pkid.toString() + " not found in database.");
            }
            Log.printVerbose(strObjectName + " Leaving loadObject: ");
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

    private void storeObject() throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " storeObject ");
            con = makeConnection();
            String updateStatement = " UPDATE " + TABLENAME + " SET " + PKID + " = ?, " + PAYMENT_MODE + " = ?, " + MERCHANT_ID + " = ?, " + MERCHANT_TRANX_ID + " = ?, " + TRANX_AMT + " = ?, " + TRANX_STATUS + " = ?, " + TRANX_ERR_CODE + " = ?, " + TRANX_DATE + " = ?, " + TRANX_APPR_CODE + " = ?, " + BANK_REF_NO + " = ?, " + PROPERTY1 + " = ?, " + PROPERTY2 + " = ?, " + PROPERTY3 + " = ?, " + PROPERTY4 + " = ?, " + PROPERTY5 + " = ? WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(updateStatement);
            prepStmt.setLong(1, this.valObj.pkid.longValue());
            prepStmt.setString(2, this.valObj.payment_mode);
            prepStmt.setString(3, this.valObj.merchant_id);
            prepStmt.setInt(4, this.valObj.merchant_tranx_id.intValue());
            prepStmt.setBigDecimal(5, this.valObj.tranx_amt);
            prepStmt.setString(6, this.valObj.tranx_status);
            prepStmt.setString(7, this.valObj.tranx_err_code);
            prepStmt.setTimestamp(8, this.valObj.tranx_date);
            prepStmt.setString(9, this.valObj.tranx_appr_code);
            prepStmt.setString(10, this.valObj.bank_ref_no);
            prepStmt.setString(11, this.valObj.property1);
            prepStmt.setString(12, this.valObj.property2);
            prepStmt.setString(13, this.valObj.property3);
            prepStmt.setString(14, this.valObj.property4);
            prepStmt.setString(15, this.valObj.property5);
            prepStmt.setLong(16, this.valObj.pkid.longValue());
            Log.printVerbose("updateStatement: " + updateStatement);
            int rowCount = prepStmt.executeUpdate();
            if (rowCount == 0) {
                throw new EJBException("Storing row for pkid " + this.valObj.pkid + " failed.");
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

    private static synchronized Long getNextPKId(Connection con) throws NamingException, SQLException {
        return AppTableCounterUtil.getNextPKId(con, PKID, TABLENAME, MODULENAME, PKID_START);
    }

    public static EPaymentInboxObject getObject(ResultSet rs, String prefix) throws Exception {
        EPaymentInboxObject theObj = null;
        try {
            theObj = new EPaymentInboxObject();
            theObj.pkid = new Long(rs.getLong(prefix + PKID));
            theObj.payment_mode = rs.getString(prefix + PAYMENT_MODE);
            theObj.merchant_id = rs.getString(prefix + MERCHANT_ID);
            theObj.merchant_tranx_id = new Integer(rs.getInt(prefix + MERCHANT_TRANX_ID));
            theObj.tranx_amt = rs.getBigDecimal(prefix + TRANX_AMT);
            theObj.tranx_status = rs.getString(prefix + TRANX_STATUS);
            theObj.tranx_err_code = rs.getString(prefix + TRANX_ERR_CODE);
            theObj.tranx_date = rs.getTimestamp(prefix + TRANX_DATE);
            theObj.tranx_appr_code = rs.getString(prefix + TRANX_APPR_CODE);
            theObj.bank_ref_no = rs.getString(prefix + BANK_REF_NO);
            theObj.property1 = rs.getString(prefix + PROPERTY1);
            theObj.property2 = rs.getString(prefix + PROPERTY2);
            theObj.property3 = rs.getString(prefix + PROPERTY3);
            theObj.property4 = rs.getString(prefix + PROPERTY4);
            theObj.property5 = rs.getString(prefix + PROPERTY5);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return theObj;
    }

    private Collection selectObjects(QueryObject query) throws NamingException, SQLException {
        Collection result = new Vector();
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " selectObjects: ");
            con = makeConnection();
            String selectStmt = " SELECT * FROM " + TABLENAME;
            selectStmt = query.appendQuery(selectStmt);
            Log.printVerbose(selectStmt);
            prepStmt = con.prepareStatement(selectStmt);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                EPaymentInboxObject theObj = getObject(rs, "");
                result.add(theObj);
            }
            Log.printVerbose(strObjectName + " Leaving selectObjects: ");
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
        return result;
    }
}

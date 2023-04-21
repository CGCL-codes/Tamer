package com.vlee.ejb.accounting;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.BigDecimal;
import javax.ejb.*;
import javax.naming.*;
import com.vlee.local.*;
import com.vlee.util.*;
import com.vlee.ejb.supplier.*;
import com.vlee.ejb.customer.*;

public class CardPaymentConfigBean implements EntityBean {

    public static final String PKID = "pkid";

    public static final String NAMESPACE = "namespace";

    public static final String SORTCODE = "sortcode";

    public static final String BANK_CODE = "bank_code";

    public static final String BANK_NAME = "bank_name";

    public static final String PAYMENT_MODE = "payment_mode";

    public static final String POLICY_CHARGES = "policy_charges";

    public static final String PCT_CHARGES = "pct_charges";

    public static final String LIMIT_MIN = "limit_min";

    public static final String LIMIT_MAX = "limit_max";

    public static final String PCCENTER_OPT = "pccenter_opt";

    public static final String PCCENTER = "pccenter";

    public static final String BRANCH_OPT = "branch_opt";

    public static final String BRANCH = "branch";

    public static final String CASHBOOK_OPT = "cashbook_opt";

    public static final String CASHBOOK = "cashbook";

    public static final String STATUS = "status";

    public static final String PAYMENT_TYPE = "payment_type";

    public static final String APPROVAL_PARTY = "approval_party";

    public static final String DEFAULT_PAYMENT_STATUS = "default_payment_status";

    public static final String DEFAULT_CARD_TYPE = "default_card_type";

    public static final String DEFAULT_PAYMENT_REMARKS = "default_payment_remarks";

    public static final String PROPERTY1 = "property1";

    public static final String PROPERTY2 = "property2";

    public static final String PROPERTY3 = "property3";

    public static final String CUST_ADMIN_FEE_OPTION = "cust_admin_fee_option";

    public static final String CUST_ADMIN_FEE_ABS = "cust_admin_fee_abs";

    public static final String CUST_ADMIN_FEE_RATIO = "cust_admin_fee_ratio";

    public static final String CUST_ADMIN_FEE_ITEMID = "cust_admin_fee_itemid";

    public static final String CUST_ADMIN_FEE_ROUNDING_MODE = "cust_admin_fee_rounding_mode";

    public static final String CUST_ADMIN_FEE_ROUNDING_SCALE = "cust_admin_fee_rounding_scale";

    public static final String CUST_ADMIN_FEE_MIN_AMOUNT = "cust_admin_fee_min_amount";

    public static final String CUST_ADMIN_FEE_MAX_AMOUNT = "cust_admin_fee_max_amount";

    public static final String STATUS_ACTIVE = "act";

    public static final String STATUS_DELETED = "del";

    public static final String STATE_CREATED = "cre";

    public static final String PT_CASH = "CASH";

    public static final String PT_CARD = "CARD";

    public static final String PT_CHEQUE = "CHEQUE";

    public static final String PT_VOUCHER = "VOUCHER";

    public static final String PT_CRV = "CRV";

    public static final String PT_INET = "INET";

    public static final String PT_NONE = "";

    public static final String NS_CARD_CHARGES = "card_charges";

    public static final String NS_BANK_CODE_LISTING = "b_code_list";

    public static final String POLICY_NONE = "";

    public static final String POLICY_ABSORB_CHARGES = "absorb";

    public static final String POLICY_SURCHARGE = "surcharge";

    public static final String CB_NONE = "";

    public static final String CB_AUTO_SELECT = "autoSelect";

    public static final String CUST_ADMIN_FEE_OPTION_DEFAULT = "";

    public static final String CUST_ADMIN_FEE_OPTION_ABS = "ABS";

    public static final String CUST_ADMIN_FEE_OPTION_RATIO = "RATIO";

    public static final String MODULENAME = "accounting";

    public static final Integer PKID_START = new Integer(100001);

    CardPaymentConfigObject valObj;

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String TABLENAME = "acc_card_payment_config";

    private static final String strObjectName = "CardPaymentConfigBean: ";

    public static final String BRANCH_OPTION_ACTIVE = "act";

    public static final String BRANCH_OPTION_INACTIVE = "not";

    private EntityContext context = null;

    /***************************************************************************
	 * Getters
	 **************************************************************************/
    public CardPaymentConfigObject getObject() {
        return this.valObj;
    }

    public void setObject(CardPaymentConfigObject newVal) {
        Integer pkid = this.valObj.pkid;
        this.valObj = newVal;
        this.valObj.pkid = pkid;
    }

    public Integer getPkid() {
        return this.valObj.pkid;
    }

    public Integer getPrimaryKey() {
        return this.valObj.pkid;
    }

    public void setPrimaryKey(Integer pkid) {
        this.valObj.pkid = pkid;
    }

    /***************************************************************************
	 * ejbCreate
	 **************************************************************************/
    public Integer ejbCreate(CardPaymentConfigObject newObj) throws CreateException {
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
    public Integer ejbFindByPrimaryKey(Integer primaryKey) throws FinderException {
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
            this.valObj = new CardPaymentConfigObject();
            this.valObj.pkid = (Integer) context.getPrimaryKey();
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
    public void ejbPostCreate(CardPaymentConfigObject newObj) {
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

    private CardPaymentConfigObject insertObject(CardPaymentConfigObject newObj) throws NamingException, SQLException {
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
                String insertStatement = " INSERT INTO " + TABLENAME + "( " + PKID + ", " + NAMESPACE + ", " + SORTCODE + ", " + BANK_CODE + ", " + BANK_NAME + ", " + PAYMENT_MODE + ", " + POLICY_CHARGES + ", " + PCT_CHARGES + ", " + LIMIT_MIN + ", " + LIMIT_MAX + ", " + PCCENTER_OPT + ", " + PCCENTER + ", " + BRANCH_OPT + ", " + BRANCH + ", " + CASHBOOK_OPT + ", " + CASHBOOK + ", " + STATUS + ", " + PAYMENT_TYPE + ", " + APPROVAL_PARTY + ", " + DEFAULT_PAYMENT_STATUS + ", " + DEFAULT_CARD_TYPE + ", " + DEFAULT_PAYMENT_REMARKS + ", " + PROPERTY1 + ", " + PROPERTY2 + ", " + PROPERTY3 + ", " + CUST_ADMIN_FEE_OPTION + ", " + CUST_ADMIN_FEE_ABS + ", " + CUST_ADMIN_FEE_RATIO + ", " + CUST_ADMIN_FEE_ITEMID + ", " + CUST_ADMIN_FEE_ROUNDING_MODE + ", " + CUST_ADMIN_FEE_ROUNDING_SCALE + ", " + CUST_ADMIN_FEE_MIN_AMOUNT + ", " + CUST_ADMIN_FEE_MAX_AMOUNT + ") values ( " + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ? ,?) ";
                prepStmt = con.prepareStatement(insertStatement);
                prepStmt.setInt(1, newObj.pkid.intValue());
                prepStmt.setString(2, newObj.namespace);
                prepStmt.setString(3, newObj.sortcode);
                prepStmt.setString(4, newObj.bankCode);
                prepStmt.setString(5, newObj.bankName);
                prepStmt.setString(6, newObj.paymentMode);
                prepStmt.setString(7, newObj.policyCharges);
                prepStmt.setBigDecimal(8, newObj.pctCharges);
                prepStmt.setBigDecimal(9, newObj.limitMin);
                prepStmt.setBigDecimal(10, newObj.limitMax);
                prepStmt.setString(11, newObj.pccenterOpt);
                prepStmt.setInt(12, newObj.pccenter.intValue());
                prepStmt.setString(13, newObj.branchOpt);
                prepStmt.setInt(14, newObj.branch.intValue());
                prepStmt.setString(15, newObj.cashbookOpt);
                prepStmt.setInt(16, newObj.cashbook.intValue());
                prepStmt.setString(17, newObj.status);
                prepStmt.setString(18, newObj.paymentType);
                prepStmt.setString(19, newObj.approvalParty);
                prepStmt.setString(20, newObj.defaultPaymentStatus);
                prepStmt.setString(21, newObj.defaultCardType);
                prepStmt.setString(22, newObj.defaultPaymentRemarks);
                prepStmt.setString(23, newObj.property1);
                prepStmt.setString(24, newObj.property2);
                prepStmt.setString(25, newObj.property3);
                prepStmt.setString(26, newObj.custAdminFeeOption);
                prepStmt.setBigDecimal(27, newObj.custAdminFeeAbs);
                prepStmt.setBigDecimal(28, newObj.custAdminFeeRatio);
                prepStmt.setInt(29, newObj.custAdminFeeItemid.intValue());
                prepStmt.setInt(30, newObj.custAdminFeeRoundingMode.intValue());
                prepStmt.setInt(31, newObj.custAdminFeeRoundingScale.intValue());
                prepStmt.setBigDecimal(32, newObj.custAdminFeeMinAmount);
                prepStmt.setBigDecimal(33, newObj.custAdminFeeMaxAmount);
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

    private boolean selectByPrimaryKey(Integer primaryKey) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " selectByPrimaryKey: ");
            con = makeConnection();
            String selectStmt = " SELECT " + PKID + " FROM " + TABLENAME + " WHERE " + PKID + " = ? ";
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
            String deleteStatement = " DELETE FROM " + TABLENAME + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(deleteStatement);
            prepStmt.setInt(1, pkid.intValue());
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
            String selectStmt = " SELECT * FROM " + TABLENAME + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setInt(1, this.valObj.pkid.intValue());
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
            String updateStatement = " UPDATE " + TABLENAME + " SET " + PKID + " = ?, " + NAMESPACE + " = ?, " + SORTCODE + " = ?, " + BANK_CODE + " = ?, " + BANK_NAME + " = ?, " + PAYMENT_MODE + " = ?, " + POLICY_CHARGES + " = ?, " + PCT_CHARGES + " = ?, " + LIMIT_MIN + " = ?, " + LIMIT_MAX + " = ?, " + PCCENTER_OPT + " = ?, " + PCCENTER + " = ?, " + BRANCH_OPT + " = ?, " + BRANCH + " = ?, " + CASHBOOK_OPT + " = ?, " + CASHBOOK + " = ?, " + STATUS + " = ?, " + PAYMENT_TYPE + " = ?, " + APPROVAL_PARTY + " = ?, " + DEFAULT_PAYMENT_STATUS + " = ?, " + DEFAULT_CARD_TYPE + " = ?, " + DEFAULT_PAYMENT_REMARKS + " = ?, " + PROPERTY1 + " = ?, " + PROPERTY2 + " = ?, " + PROPERTY3 + " = ?, " + CUST_ADMIN_FEE_OPTION + " = ?, " + CUST_ADMIN_FEE_ABS + " = ?, " + CUST_ADMIN_FEE_RATIO + " = ?, " + CUST_ADMIN_FEE_ITEMID + " = ?, " + CUST_ADMIN_FEE_ROUNDING_MODE + " = ?, " + CUST_ADMIN_FEE_ROUNDING_SCALE + " = ?, " + CUST_ADMIN_FEE_MIN_AMOUNT + " = ?, " + CUST_ADMIN_FEE_MAX_AMOUNT + " = ? " + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(updateStatement);
            prepStmt.setInt(1, this.valObj.pkid.intValue());
            prepStmt.setString(2, this.valObj.namespace);
            prepStmt.setString(3, this.valObj.sortcode);
            prepStmt.setString(4, this.valObj.bankCode);
            prepStmt.setString(5, this.valObj.bankName);
            prepStmt.setString(6, this.valObj.paymentMode);
            prepStmt.setString(7, this.valObj.policyCharges);
            prepStmt.setBigDecimal(8, this.valObj.pctCharges);
            prepStmt.setBigDecimal(9, this.valObj.limitMin);
            prepStmt.setBigDecimal(10, this.valObj.limitMax);
            prepStmt.setString(11, this.valObj.pccenterOpt);
            prepStmt.setInt(12, this.valObj.pccenter.intValue());
            prepStmt.setString(13, this.valObj.branchOpt);
            prepStmt.setInt(14, this.valObj.branch.intValue());
            prepStmt.setString(15, this.valObj.cashbookOpt);
            prepStmt.setInt(16, this.valObj.cashbook.intValue());
            prepStmt.setString(17, this.valObj.status);
            prepStmt.setString(18, this.valObj.paymentType);
            prepStmt.setString(19, this.valObj.approvalParty);
            prepStmt.setString(20, this.valObj.defaultPaymentStatus);
            prepStmt.setString(21, this.valObj.defaultCardType);
            prepStmt.setString(22, this.valObj.defaultPaymentRemarks);
            prepStmt.setString(23, this.valObj.property1);
            prepStmt.setString(24, this.valObj.property2);
            prepStmt.setString(25, this.valObj.property3);
            prepStmt.setString(26, this.valObj.custAdminFeeOption);
            prepStmt.setBigDecimal(27, this.valObj.custAdminFeeAbs);
            prepStmt.setBigDecimal(28, this.valObj.custAdminFeeRatio);
            prepStmt.setInt(29, this.valObj.custAdminFeeItemid.intValue());
            prepStmt.setInt(30, this.valObj.custAdminFeeRoundingMode.intValue());
            prepStmt.setInt(31, this.valObj.custAdminFeeRoundingScale.intValue());
            prepStmt.setBigDecimal(32, this.valObj.custAdminFeeMinAmount);
            prepStmt.setBigDecimal(33, this.valObj.custAdminFeeMaxAmount);
            prepStmt.setInt(34, this.valObj.pkid.intValue());
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

    private static synchronized Integer getNextPKId(Connection con) throws NamingException, SQLException {
        return AppTableCounterUtil.getNextPKId(con, PKID, TABLENAME, MODULENAME, PKID_START);
    }

    private static synchronized Integer getNextStmtNo(Connection con, String stmtType, Integer pcCenter) throws NamingException, SQLException {
        return AppTableCounterUtil.getNextPKId(con, pcCenter.toString(), TABLENAME, stmtType, PKID_START);
    }

    public static CardPaymentConfigObject getObject(ResultSet rs, String prefix) throws Exception {
        CardPaymentConfigObject theValObj = null;
        try {
            theValObj = new CardPaymentConfigObject();
            theValObj.pkid = new Integer(rs.getInt(prefix + PKID));
            theValObj.namespace = rs.getString(prefix + NAMESPACE);
            theValObj.sortcode = rs.getString(prefix + SORTCODE);
            theValObj.bankCode = rs.getString(prefix + BANK_CODE);
            theValObj.bankName = rs.getString(prefix + BANK_NAME);
            theValObj.paymentMode = rs.getString(prefix + PAYMENT_MODE);
            theValObj.policyCharges = rs.getString(prefix + POLICY_CHARGES);
            theValObj.pctCharges = rs.getBigDecimal(prefix + PCT_CHARGES);
            theValObj.limitMin = rs.getBigDecimal(prefix + LIMIT_MIN);
            theValObj.limitMax = rs.getBigDecimal(prefix + LIMIT_MAX);
            theValObj.pccenterOpt = rs.getString(prefix + PCCENTER_OPT);
            theValObj.pccenter = new Integer(rs.getInt(prefix + PCCENTER));
            theValObj.branchOpt = rs.getString(BRANCH_OPT);
            theValObj.branch = new Integer(rs.getInt(prefix + BRANCH));
            theValObj.cashbookOpt = rs.getString(CASHBOOK_OPT);
            theValObj.cashbook = new Integer(rs.getInt(prefix + CASHBOOK));
            theValObj.status = rs.getString(prefix + STATUS);
            theValObj.paymentType = rs.getString(prefix + PAYMENT_TYPE);
            theValObj.approvalParty = rs.getString(prefix + APPROVAL_PARTY);
            theValObj.defaultPaymentStatus = rs.getString(prefix + DEFAULT_PAYMENT_STATUS);
            theValObj.defaultCardType = rs.getString(prefix + DEFAULT_CARD_TYPE);
            theValObj.defaultPaymentRemarks = rs.getString(prefix + DEFAULT_PAYMENT_REMARKS);
            theValObj.property1 = rs.getString(prefix + PROPERTY1);
            theValObj.property2 = rs.getString(prefix + PROPERTY2);
            theValObj.property3 = rs.getString(prefix + PROPERTY3);
            theValObj.custAdminFeeOption = rs.getString(prefix + CUST_ADMIN_FEE_OPTION);
            theValObj.custAdminFeeAbs = rs.getBigDecimal(prefix + CUST_ADMIN_FEE_ABS);
            theValObj.custAdminFeeRatio = rs.getBigDecimal(prefix + CUST_ADMIN_FEE_RATIO);
            theValObj.custAdminFeeItemid = new Integer(rs.getInt(CUST_ADMIN_FEE_ITEMID));
            theValObj.custAdminFeeRoundingMode = new Integer(rs.getInt(CUST_ADMIN_FEE_ROUNDING_MODE));
            theValObj.custAdminFeeRoundingScale = new Integer(rs.getInt(CUST_ADMIN_FEE_ROUNDING_SCALE));
            theValObj.custAdminFeeMinAmount = rs.getBigDecimal(CUST_ADMIN_FEE_MIN_AMOUNT);
            theValObj.custAdminFeeMaxAmount = rs.getBigDecimal(CUST_ADMIN_FEE_MAX_AMOUNT);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return theValObj;
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
                CardPaymentConfigObject theValObj = getObject(rs, "");
                if (theValObj != null) {
                    result.add(theValObj);
                }
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

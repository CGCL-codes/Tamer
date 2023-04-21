package com.vlee.ejb.customer;

import java.sql.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import javax.sql.*;
import com.vlee.bean.distribution.*;
import com.vlee.ejb.inventory.*;
import com.vlee.local.*;
import com.vlee.util.*;

public class CustQuotationItemBean implements EntityBean {

    public static final String PKID = "pkid";

    public static final String INDEX_ID = "index_id";

    public static final String ITEM_TYPE = "item_type";

    public static final String ITEM_ID = "item_id";

    public static final String ITEM_CODE = "item_code";

    public static final String ITEM_BARCODE = "item_barcode";

    public static final String QUANTITY = "quantity";

    public static final String QTY_DELIVERED = "qty_delivered";

    public static final String QTY_OUTSTANDING = "qty_outstanding";

    public static final String QTY_BILLED = "qty_billed";

    public static final String QTY_RESERVED = "qty_reserved";

    public static final String CURRENCY1 = "currency1";

    public static final String UNIT_PRICE1 = "unit_price1";

    public static final String UNIT_DISCOUNT1 = "unit_discount1";

    public static final String UNIT_TAX1 = "unit_tax1";

    public static final String UNIT_NET_PRICE1 = "unit_net_price1";

    public static final String CURRENCY2 = "currency2";

    public static final String UNIT_PRICE2 = "unit_price2";

    public static final String UNIT_DISCOUNT2 = "unit_discount2";

    public static final String UNIT_TAX2 = "unit_tax2";

    public static final String UNIT_NET_PRICE2 = "unit_net_price2";

    public static final String SERIALIZED = "serialized";

    public static final String NAME = "name";

    public static final String REMARKS = "remarks";

    public static final String DESCRIPTION = "description";

    public static final String CUST_ITEM_CODE = "cust_item_code";

    public static final String CUST_ITEM_NAME = "cust_item_name";

    public static final String IS_PACKAGE = "is_package";

    public static final String PARENT_ID = "parent_id";

    public static final String PIC1 = "pic1";

    public static final String PIC2 = "pic2";

    public static final String PIC3 = "pic3";

    public static final String TAXAMT2 = "taxamt2";

    public static final String STATUS = "status";

    public static final String STATE = "state";

    public static final String PRODUCTION_REQUIRED = "production_required";

    public static final String PRODUCTION_LEAD_DAY = "production_lead_day";

    public static final String PRODUCTION_STATUS = "production_status";

    public static final String PRODUCTION_START = "production_start";

    public static final String PRODUCTION_END = "production_end";

    public static final String DELIVERY_REQUIRED = "delivery_required";

    public static final String DELIVERY_LEAD_DAY = "delivery_lead_day";

    public static final String DELIVERY_STATUS = "delivery_status";

    public static final String DELIVERY_START = "delivery_start";

    public static final String DELIVERY_END = "delivery_end";

    public static final String UUID = "uuid";

    public static final String CODE_PROJECT = "code_project";

    public static final String CODE_DEPARTMENT = "code_department";

    public static final String CODE_DEALER = "code_dealer";

    public static final String CODE_SALESMAN = "code_salesman";

    public static final String ID_SALESMAN = "id_salesman";

    public static final String STATUS_ACTIVE = "ACTIVE";

    public static final String STATUS_DELETED = "DELETED";

    public static final String MODULENAME = "customer";

    public static final Long PKID_START = new Long(1001);

    CustQuotationItemObject valObj;

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String TABLENAME = "cust_quotation_item";

    private static final String strObjectName = "CustQuotationItemBean: ";

    private EntityContext context = null;

    /***************************************************************************
	 * Getters
	 **************************************************************************/
    public CustQuotationItemObject getObject() {
        return this.valObj;
    }

    public void setObject(CustQuotationItemObject newVal) {
        Long pkid = this.valObj.pkid;
        this.valObj = newVal;
        this.valObj.pkid = pkid;
    }

    public void setProductionStatus(String buf) {
        this.valObj.productionStatus = buf;
    }

    public void setDeliveryStatus(String buf) {
        this.valObj.deliveryStatus = buf;
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
    public Long ejbCreate(CustQuotationItemObject newObj) throws CreateException {
        Log.printVerbose(strObjectName + "in ejbCreate");
        try {
            newObj.failSafe();
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
            this.valObj = new CustQuotationItemObject();
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
    public void ejbPostCreate(CustQuotationItemObject newObj) {
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

    private CustQuotationItemObject insertObject(CustQuotationItemObject newObj) throws NamingException, SQLException {
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
                String insertStatement = " INSERT INTO " + TABLENAME + "( " + PKID + ", " + INDEX_ID + ", " + ITEM_TYPE + ", " + ITEM_ID + ", " + ITEM_CODE + ", " + ITEM_BARCODE + ", " + QUANTITY + ", " + QTY_DELIVERED + ", " + QTY_OUTSTANDING + ", " + QTY_BILLED + ", " + QTY_RESERVED + ", " + CURRENCY1 + ", " + UNIT_PRICE1 + ", " + UNIT_DISCOUNT1 + ", " + UNIT_TAX1 + ", " + UNIT_NET_PRICE1 + ", " + CURRENCY2 + ", " + UNIT_PRICE2 + ", " + UNIT_DISCOUNT2 + ", " + UNIT_TAX2 + ", " + UNIT_NET_PRICE2 + ", " + SERIALIZED + ", " + NAME + ", " + REMARKS + ", " + DESCRIPTION + ", " + CUST_ITEM_CODE + ", " + CUST_ITEM_NAME + ", " + IS_PACKAGE + ", " + PARENT_ID + ", " + PIC1 + ", " + PIC2 + ", " + PIC3 + ", " + TAXAMT2 + ", " + STATUS + ", " + STATE + ", " + PRODUCTION_REQUIRED + ", " + PRODUCTION_LEAD_DAY + ", " + PRODUCTION_STATUS + ", " + PRODUCTION_START + ", " + PRODUCTION_END + ", " + DELIVERY_REQUIRED + ", " + DELIVERY_LEAD_DAY + ", " + DELIVERY_STATUS + ", " + DELIVERY_START + ", " + DELIVERY_END + ", " + UUID + ", " + CODE_PROJECT + ", " + CODE_DEPARTMENT + ", " + CODE_DEALER + ", " + CODE_SALESMAN + ", " + ID_SALESMAN + ") values ( " + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?) ";
                prepStmt = con.prepareStatement(insertStatement);
                prepStmt.setLong(1, newObj.pkid.longValue());
                prepStmt.setLong(2, newObj.indexId.longValue());
                prepStmt.setString(3, newObj.itemType);
                prepStmt.setInt(4, newObj.itemId.intValue());
                prepStmt.setString(5, newObj.itemCode);
                prepStmt.setString(6, newObj.itemBarcode);
                prepStmt.setBigDecimal(7, newObj.quantity);
                prepStmt.setBigDecimal(8, newObj.qtyDelivered);
                prepStmt.setBigDecimal(9, newObj.qtyOutstanding);
                prepStmt.setBigDecimal(10, newObj.qtyBilled);
                prepStmt.setBigDecimal(11, newObj.qtyReserved);
                prepStmt.setString(12, newObj.currency1);
                prepStmt.setBigDecimal(13, newObj.unitPrice1);
                prepStmt.setBigDecimal(14, newObj.unitDiscount1);
                prepStmt.setBigDecimal(15, newObj.unitTax1);
                prepStmt.setBigDecimal(16, newObj.unitNetPrice1);
                prepStmt.setString(17, newObj.currency2);
                prepStmt.setBigDecimal(18, newObj.unitPrice2);
                prepStmt.setBigDecimal(19, newObj.unitDiscount2);
                prepStmt.setBigDecimal(20, newObj.unitTax2);
                prepStmt.setBigDecimal(21, newObj.unitNetPrice2);
                prepStmt.setBoolean(22, newObj.serialized);
                prepStmt.setString(23, newObj.name);
                prepStmt.setString(24, newObj.remarks);
                prepStmt.setString(25, newObj.description);
                prepStmt.setString(26, newObj.custItemCode);
                prepStmt.setString(27, newObj.custItemName);
                prepStmt.setBoolean(28, newObj.isPackage);
                prepStmt.setLong(29, newObj.parentId.longValue());
                prepStmt.setInt(30, newObj.pic1.intValue());
                prepStmt.setInt(31, newObj.pic2.intValue());
                prepStmt.setInt(32, newObj.pic3.intValue());
                prepStmt.setBigDecimal(33, newObj.taxamt2);
                prepStmt.setString(34, newObj.status);
                prepStmt.setString(35, newObj.state);
                prepStmt.setBoolean(36, newObj.productionRequired);
                prepStmt.setInt(37, newObj.productionLeadDay.intValue());
                prepStmt.setString(38, newObj.productionStatus);
                prepStmt.setTimestamp(39, newObj.productionStart);
                prepStmt.setTimestamp(40, newObj.productionEnd);
                prepStmt.setBoolean(41, newObj.deliveryRequired);
                prepStmt.setInt(42, newObj.deliveryLeadDay.intValue());
                prepStmt.setString(43, newObj.deliveryStatus);
                prepStmt.setTimestamp(44, newObj.deliveryStart);
                prepStmt.setTimestamp(45, newObj.deliveryEnd);
                prepStmt.setString(46, newObj.uuid);
                prepStmt.setString(47, newObj.codeProject);
                prepStmt.setString(48, newObj.codeDepartment);
                prepStmt.setString(49, newObj.codeDealer);
                prepStmt.setString(50, newObj.codeSalesman);
                prepStmt.setInt(51, newObj.idSalesman.intValue());
                prepStmt.executeUpdate();
                Log.printVerbose("Insert statement : " + insertStatement);
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
            String selectStmt = " SELECT * FROM " + TABLENAME + " WHERE " + PKID + " = ? ";
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
            String updateStatement = " UPDATE " + TABLENAME + " SET " + PKID + " = ?, " + INDEX_ID + " = ?, " + ITEM_TYPE + " = ?, " + ITEM_ID + " = ?, " + ITEM_CODE + " = ?, " + ITEM_BARCODE + " = ?, " + QUANTITY + " = ?, " + QTY_DELIVERED + " = ?, " + QTY_OUTSTANDING + " = ?, " + QTY_BILLED + " = ?, " + QTY_RESERVED + " = ?, " + CURRENCY1 + " = ?, " + UNIT_PRICE1 + " = ?, " + UNIT_DISCOUNT1 + " = ?, " + UNIT_TAX1 + " = ?, " + UNIT_NET_PRICE1 + " = ?, " + CURRENCY2 + " = ?, " + UNIT_PRICE2 + " = ?, " + UNIT_DISCOUNT2 + " = ?, " + UNIT_TAX2 + " = ?, " + UNIT_NET_PRICE2 + " = ?, " + SERIALIZED + " = ?, " + NAME + " = ?, " + REMARKS + " = ?, " + DESCRIPTION + " = ?, " + CUST_ITEM_CODE + " = ?, " + CUST_ITEM_NAME + " = ?, " + IS_PACKAGE + " = ?, " + PARENT_ID + " = ?, " + PIC1 + " = ?, " + PIC2 + " = ?, " + PIC3 + " = ?, " + TAXAMT2 + " = ?, " + STATUS + " = ?, " + STATE + " = ?, " + PRODUCTION_REQUIRED + " = ?, " + PRODUCTION_LEAD_DAY + " = ?, " + PRODUCTION_STATUS + " = ?, " + PRODUCTION_START + " = ?, " + PRODUCTION_END + " = ?, " + DELIVERY_REQUIRED + " = ?, " + DELIVERY_LEAD_DAY + " = ?, " + DELIVERY_STATUS + " = ?, " + DELIVERY_START + " = ?, " + DELIVERY_END + " = ?, " + UUID + " = ?, " + CODE_PROJECT + " = ?, " + CODE_DEPARTMENT + " = ?, " + CODE_DEALER + " = ?, " + CODE_SALESMAN + " = ?, " + ID_SALESMAN + " = ? " + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(updateStatement);
            prepStmt.setLong(1, this.valObj.pkid.longValue());
            prepStmt.setLong(2, this.valObj.indexId.longValue());
            prepStmt.setString(3, this.valObj.itemType);
            prepStmt.setInt(4, this.valObj.itemId.intValue());
            prepStmt.setString(5, this.valObj.itemCode);
            prepStmt.setString(6, this.valObj.itemBarcode);
            prepStmt.setBigDecimal(7, this.valObj.quantity);
            prepStmt.setBigDecimal(8, this.valObj.qtyDelivered);
            prepStmt.setBigDecimal(9, this.valObj.qtyOutstanding);
            prepStmt.setBigDecimal(10, this.valObj.qtyBilled);
            prepStmt.setBigDecimal(11, this.valObj.qtyReserved);
            prepStmt.setString(12, this.valObj.currency1);
            prepStmt.setBigDecimal(13, this.valObj.unitPrice1);
            prepStmt.setBigDecimal(14, this.valObj.unitDiscount1);
            prepStmt.setBigDecimal(15, this.valObj.unitTax1);
            prepStmt.setBigDecimal(16, this.valObj.unitNetPrice1);
            prepStmt.setString(17, this.valObj.currency2);
            prepStmt.setBigDecimal(18, this.valObj.unitPrice2);
            prepStmt.setBigDecimal(19, this.valObj.unitDiscount2);
            prepStmt.setBigDecimal(20, this.valObj.unitTax2);
            prepStmt.setBigDecimal(21, this.valObj.unitNetPrice2);
            prepStmt.setBoolean(22, this.valObj.serialized);
            prepStmt.setString(23, this.valObj.name);
            prepStmt.setString(24, this.valObj.remarks);
            prepStmt.setString(25, this.valObj.description);
            prepStmt.setString(26, this.valObj.custItemCode);
            prepStmt.setString(27, this.valObj.custItemName);
            prepStmt.setBoolean(28, this.valObj.isPackage);
            prepStmt.setLong(29, this.valObj.parentId.longValue());
            prepStmt.setInt(30, this.valObj.pic1.intValue());
            prepStmt.setInt(31, this.valObj.pic2.intValue());
            prepStmt.setInt(32, this.valObj.pic3.intValue());
            prepStmt.setBigDecimal(33, this.valObj.taxamt2);
            prepStmt.setString(34, this.valObj.status);
            prepStmt.setString(35, this.valObj.state);
            prepStmt.setBoolean(36, this.valObj.productionRequired);
            prepStmt.setInt(37, this.valObj.productionLeadDay.intValue());
            prepStmt.setString(38, this.valObj.productionStatus);
            prepStmt.setTimestamp(39, this.valObj.productionStart);
            prepStmt.setTimestamp(40, this.valObj.productionEnd);
            prepStmt.setBoolean(41, this.valObj.deliveryRequired);
            prepStmt.setInt(42, this.valObj.deliveryLeadDay.intValue());
            prepStmt.setString(43, this.valObj.deliveryStatus);
            prepStmt.setTimestamp(44, this.valObj.deliveryStart);
            prepStmt.setTimestamp(45, this.valObj.deliveryEnd);
            prepStmt.setString(46, this.valObj.uuid);
            prepStmt.setString(47, this.valObj.codeProject);
            prepStmt.setString(48, this.valObj.codeDepartment);
            prepStmt.setString(49, this.valObj.codeDealer);
            prepStmt.setString(50, this.valObj.codeSalesman);
            prepStmt.setInt(51, this.valObj.idSalesman.intValue());
            prepStmt.setLong(52, this.valObj.pkid.longValue());
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

    private static synchronized Long getNextStmtNo(Connection con, String stmtType, Long pcCenter) throws NamingException, SQLException {
        return AppTableCounterUtil.getNextPKId(con, pcCenter.toString(), TABLENAME, stmtType, PKID_START);
    }

    public static CustQuotationItemObject getObject(ResultSet rs, String prefix) throws Exception {
        CustQuotationItemObject theObj = null;
        try {
            theObj = new CustQuotationItemObject();
            theObj.pkid = new Long(rs.getLong(prefix + PKID));
            theObj.indexId = new Long(rs.getLong(prefix + INDEX_ID));
            theObj.itemType = rs.getString(prefix + ITEM_TYPE);
            theObj.itemId = new Integer(rs.getInt(ITEM_ID));
            theObj.itemCode = rs.getString(prefix + ITEM_CODE);
            theObj.itemBarcode = rs.getString(prefix + ITEM_BARCODE);
            theObj.quantity = rs.getBigDecimal(prefix + QUANTITY);
            theObj.qtyDelivered = rs.getBigDecimal(prefix + QTY_DELIVERED);
            theObj.qtyOutstanding = rs.getBigDecimal(prefix + QTY_OUTSTANDING);
            theObj.qtyBilled = rs.getBigDecimal(prefix + QTY_BILLED);
            theObj.qtyReserved = rs.getBigDecimal(prefix + QTY_RESERVED);
            theObj.currency1 = rs.getString(prefix + CURRENCY1);
            theObj.unitPrice1 = rs.getBigDecimal(prefix + UNIT_PRICE1);
            theObj.unitDiscount1 = rs.getBigDecimal(prefix + UNIT_DISCOUNT1);
            theObj.unitTax1 = rs.getBigDecimal(prefix + UNIT_TAX1);
            theObj.unitNetPrice1 = rs.getBigDecimal(prefix + UNIT_NET_PRICE1);
            theObj.currency2 = rs.getString(prefix + CURRENCY2);
            theObj.unitPrice2 = rs.getBigDecimal(prefix + UNIT_PRICE2);
            theObj.unitDiscount2 = rs.getBigDecimal(prefix + UNIT_DISCOUNT2);
            theObj.unitTax2 = rs.getBigDecimal(prefix + UNIT_TAX2);
            theObj.unitNetPrice2 = rs.getBigDecimal(prefix + UNIT_NET_PRICE2);
            theObj.serialized = rs.getBoolean(prefix + SERIALIZED);
            theObj.name = rs.getString(prefix + NAME);
            theObj.remarks = rs.getString(prefix + REMARKS);
            theObj.description = rs.getString(prefix + DESCRIPTION);
            theObj.custItemCode = rs.getString(prefix + CUST_ITEM_CODE);
            theObj.custItemName = rs.getString(prefix + CUST_ITEM_NAME);
            theObj.isPackage = rs.getBoolean(prefix + IS_PACKAGE);
            theObj.parentId = new Long(rs.getLong(prefix + PARENT_ID));
            theObj.pic1 = new Integer(rs.getInt(prefix + PIC1));
            theObj.pic2 = new Integer(rs.getInt(prefix + PIC2));
            theObj.pic3 = new Integer(rs.getInt(prefix + PIC3));
            theObj.taxamt2 = rs.getBigDecimal(prefix + TAXAMT2);
            theObj.status = rs.getString(prefix + STATUS);
            theObj.state = rs.getString(prefix + STATE);
            theObj.productionRequired = rs.getBoolean(prefix + PRODUCTION_REQUIRED);
            theObj.productionLeadDay = new Integer(rs.getInt(prefix + PRODUCTION_LEAD_DAY));
            theObj.productionStatus = rs.getString(prefix + PRODUCTION_STATUS);
            theObj.productionStart = rs.getTimestamp(prefix + PRODUCTION_START);
            theObj.productionEnd = rs.getTimestamp(prefix + PRODUCTION_END);
            theObj.deliveryRequired = rs.getBoolean(prefix + DELIVERY_REQUIRED);
            theObj.deliveryLeadDay = new Integer(rs.getInt(prefix + DELIVERY_LEAD_DAY));
            theObj.deliveryStatus = rs.getString(prefix + DELIVERY_STATUS);
            theObj.deliveryStart = rs.getTimestamp(prefix + DELIVERY_START);
            theObj.deliveryEnd = rs.getTimestamp(prefix + DELIVERY_END);
            theObj.uuid = rs.getString(prefix + UUID);
            theObj.codeProject = rs.getString(prefix + CODE_PROJECT);
            theObj.codeDepartment = rs.getString(prefix + CODE_DEPARTMENT);
            theObj.codeDealer = rs.getString(prefix + CODE_DEALER);
            theObj.codeSalesman = rs.getString(prefix + CODE_SALESMAN);
            theObj.idSalesman = new Integer(rs.getInt(prefix + ID_SALESMAN));
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
                CustQuotationItemObject theObj = getObject(rs, "");
                if (theObj != null) {
                    result.add(theObj);
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

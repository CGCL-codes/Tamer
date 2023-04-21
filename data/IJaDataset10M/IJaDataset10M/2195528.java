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

public class CustProformaInvoiceItemBean implements EntityBean {

    public static final String PKID = "pkid";

    public static final String INDEX_ID = "index_id";

    public static final String ITEM_TYPE = "item_type";

    public static final String ITEM_ID_INQUIRY = "item_id_inquiry";

    public static final String ITEM_ID_INVOICE = "item_id_invoice";

    public static final String ITEM_ID_GRN = "item_id_grn";

    public static final String ITEM_ID_PURCHASE_ORDER = "item_id_purchase_order";

    public static final String ITEM_ID = "item_id";

    public static final String ITEM_CODE = "item_code";

    public static final String ITEM_BARCODE = "item_barcode";

    public static final String QUANTITY = "quantity";

    public static final String QTY_SHORTAGE = "qty_shortage";

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

    public static final String STATUS_INTERNAL = "status_internal";

    public static final String PIC1 = "pic1";

    public static final String PIC2 = "pic2";

    public static final String PIC3 = "pic3";

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

    public static final String SALESMAN = "salesman";

    public static final String CUST_ITEM_CODE = "cust_item_code";

    public static final String CUST_ITEM_NAME = "cust_item_name";

    public static final String CUST_REMARKS = "cust_remarks";

    public static final String CUST_CURRENCY = "cust_currency";

    public static final String CUST_QUANTITY = "cust_quantity";

    public static final String CUST_PREVIOUS_PRICE = "cust_previous_price";

    public static final String CUST_ESTIMATED_PRICE = "cust_estimated_price";

    public static final String CUST_QUOTED_PRICE = "cust_quoted_price";

    public static final String CUST_STATUS_ACCEPTANCE = "cust_status_acceptance";

    public static final String SUPP_LOGIC = "supp_logic";

    public static final String SUPP_INQUIRY_ITEM = "supp_inquiry_item";

    public static final String SUPP_ITEM_CODE = "supp_item_code";

    public static final String SUPP_ITEM_NAME = "supp_item_name";

    public static final String SUPP_REMARKS = "supp_remarks";

    public static final String SUPP_CURRENCY = "supp_currency";

    public static final String SUPP_MIN_ORDER_QUANTITY = "supp_min_order_quantity";

    public static final String SUPP_AVAILABLE_QUANTITY = "supp_available_quantity";

    public static final String SUPP_PREVIOUS_PRICE = "supp_previous_price";

    public static final String SUPP_LIST_PRICE = "supp_list_price";

    public static final String SUPP_QUOTED_PRICE = "supp_quoted_price";

    public static final String SUPP_STATUS_RESPOND = "supp_status_respond";

    public static final String SUPP_RESPONDED_ON = "supp_responded_on";

    public static final String STATUS_ACTIVE = "ACTIVE";

    public static final String STATUS_DELETED = "DELETED";

    public static final String SI_TO_BE_PROCESSED = "TO_BE_PROCESSED";

    public static final String SI_SUPPLIERS_SOURCING = "SUPPLIERS_SOURCING";

    public static final String SI_SUPPLIERS_NO_ANSWER = "SUPPLIERS_NO_ANSWER";

    public static final String SI_SUPPLIERS_NO_STOCK = "SUPPLIERS_NO_STOCK";

    public static final String SI_SUPPLIERS_QUOTED = "SUPPLIERS_QUOTED";

    public static final String SI_INTERNAL_SOURCING = "INTERNAL_SOURCING";

    public static final String SI_INTERNAL_HAS_STOCK = "INTERNAL_HAS_STOCK";

    public static final String SI_INTERNAL_NO_STOCK = "INTERNAL_NO_STOCK";

    public static final String SI_PROBLEM = "PROBLEM";

    public static final String SI_SELLING_PRICE_APPROVED = "SELLING_PRICE_APPROVED";

    public static final String SI_QUOTED_CUSTOMER = "QUOTED_CUSTOMER";

    public static final String SI_CANNOT_FULFIL = "CANNOT_FULFIL";

    public static final String SI_CUSTOMER_ACCEPTED = "CUSTOMER_ACCEPTED";

    public static final String SI_CUSTOMER_REJECTED = "CUSTOMER_REJECTED";

    public static final String SI_CLOSED = "CLOSED";

    public static final String MODULENAME = "customer";

    public static final Long PKID_START = new Long(1001);

    CustProformaInvoiceItemObject valObj;

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String TABLENAME = "cust_proforma_invoice_item";

    private static final String strObjectName = "CustProformaInvoiceItemBean: ";

    private EntityContext context = null;

    /***************************************************************************
	 * Getters
	 **************************************************************************/
    public CustProformaInvoiceItemObject getObject() {
        return this.valObj;
    }

    public void setObject(CustProformaInvoiceItemObject newVal) {
        Long pkid = this.valObj.pkid;
        this.valObj = newVal;
        this.valObj.pkid = pkid;
    }

    public void setStatusInternal(String buf) {
        this.valObj.statusInternal = buf;
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
    public Long ejbCreate(CustProformaInvoiceItemObject newObj) throws CreateException {
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
            this.valObj = new CustProformaInvoiceItemObject();
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
    public void ejbPostCreate(CustProformaInvoiceItemObject newObj) {
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

    private CustProformaInvoiceItemObject insertObject(CustProformaInvoiceItemObject newObj) throws NamingException, SQLException {
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
                String insertStatement = " INSERT INTO " + TABLENAME + "( " + PKID + ", " + INDEX_ID + ", " + ITEM_TYPE + ", " + ITEM_ID_INQUIRY + ", " + ITEM_ID_INVOICE + ", " + ITEM_ID_GRN + ", " + ITEM_ID_PURCHASE_ORDER + ", " + ITEM_ID + ", " + ITEM_CODE + ", " + ITEM_BARCODE + ", " + QUANTITY + ", " + QTY_SHORTAGE + ", " + QTY_DELIVERED + ", " + QTY_OUTSTANDING + ", " + QTY_BILLED + ", " + QTY_RESERVED + ", " + CURRENCY1 + ", " + UNIT_PRICE1 + ", " + UNIT_DISCOUNT1 + ", " + UNIT_TAX1 + ", " + UNIT_NET_PRICE1 + ", " + CURRENCY2 + ", " + UNIT_PRICE2 + ", " + UNIT_DISCOUNT2 + ", " + UNIT_TAX2 + ", " + UNIT_NET_PRICE2 + ", " + SERIALIZED + ", " + NAME + ", " + REMARKS + ", " + DESCRIPTION + ", " + STATUS_INTERNAL + ", " + PIC1 + ", " + PIC2 + ", " + PIC3 + ", " + STATUS + ", " + STATE + ", " + PRODUCTION_REQUIRED + ", " + PRODUCTION_LEAD_DAY + ", " + PRODUCTION_STATUS + ", " + PRODUCTION_START + ", " + PRODUCTION_END + ", " + DELIVERY_REQUIRED + ", " + DELIVERY_LEAD_DAY + ", " + DELIVERY_STATUS + ", " + DELIVERY_START + ", " + DELIVERY_END + ", " + UUID + ", " + CODE_PROJECT + ", " + CODE_DEPARTMENT + ", " + CODE_DEALER + ", " + CODE_SALESMAN + ", " + SALESMAN + ", " + CUST_ITEM_CODE + ", " + CUST_ITEM_NAME + ", " + CUST_REMARKS + ", " + CUST_CURRENCY + ", " + CUST_QUANTITY + ", " + CUST_PREVIOUS_PRICE + ", " + CUST_ESTIMATED_PRICE + ", " + CUST_QUOTED_PRICE + ", " + CUST_STATUS_ACCEPTANCE + ", " + SUPP_LOGIC + ", " + SUPP_INQUIRY_ITEM + ", " + SUPP_ITEM_CODE + ", " + SUPP_ITEM_NAME + ", " + SUPP_REMARKS + ", " + SUPP_CURRENCY + ", " + SUPP_MIN_ORDER_QUANTITY + ", " + SUPP_AVAILABLE_QUANTITY + ", " + SUPP_PREVIOUS_PRICE + ", " + SUPP_LIST_PRICE + ", " + SUPP_QUOTED_PRICE + ", " + SUPP_STATUS_RESPOND + ", " + SUPP_RESPONDED_ON + ") values ( " + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ? ) ";
                prepStmt = con.prepareStatement(insertStatement);
                prepStmt.setLong(1, newObj.pkid.longValue());
                prepStmt.setLong(2, newObj.indexId.longValue());
                prepStmt.setString(3, newObj.itemType);
                prepStmt.setLong(4, newObj.itemIdInquiry.longValue());
                prepStmt.setLong(5, newObj.itemIdInvoice.longValue());
                prepStmt.setLong(6, newObj.itemIdGrn.longValue());
                prepStmt.setLong(7, newObj.itemIdPurchaseOrder.longValue());
                prepStmt.setInt(8, newObj.itemId.intValue());
                prepStmt.setString(9, newObj.itemCode);
                prepStmt.setString(10, newObj.itemBarcode);
                prepStmt.setBigDecimal(11, newObj.quantity);
                prepStmt.setBigDecimal(12, newObj.qtyShortage);
                prepStmt.setBigDecimal(13, newObj.qtyDelivered);
                prepStmt.setBigDecimal(14, newObj.qtyOutstanding);
                prepStmt.setBigDecimal(15, newObj.qtyBilled);
                prepStmt.setBigDecimal(16, newObj.qtyReserved);
                prepStmt.setString(17, newObj.currency1);
                prepStmt.setBigDecimal(18, newObj.unitPrice1);
                prepStmt.setBigDecimal(19, newObj.unitDiscount1);
                prepStmt.setBigDecimal(20, newObj.unitTax1);
                prepStmt.setBigDecimal(21, newObj.unitNetPrice1);
                prepStmt.setString(22, newObj.currency2);
                prepStmt.setBigDecimal(23, newObj.unitPrice2);
                prepStmt.setBigDecimal(24, newObj.unitDiscount2);
                prepStmt.setBigDecimal(25, newObj.unitTax2);
                prepStmt.setBigDecimal(26, newObj.unitNetPrice2);
                prepStmt.setBoolean(27, newObj.serialized);
                prepStmt.setString(28, newObj.name);
                prepStmt.setString(29, newObj.remarks);
                prepStmt.setString(30, newObj.description);
                prepStmt.setString(31, newObj.statusInternal);
                prepStmt.setInt(32, newObj.pic1.intValue());
                prepStmt.setInt(33, newObj.pic2.intValue());
                prepStmt.setInt(34, newObj.pic3.intValue());
                prepStmt.setString(35, newObj.status);
                prepStmt.setString(36, newObj.state);
                prepStmt.setBoolean(37, newObj.productionRequired);
                prepStmt.setInt(38, newObj.productionLeadDay.intValue());
                prepStmt.setString(39, newObj.productionStatus);
                prepStmt.setTimestamp(40, newObj.productionStart);
                prepStmt.setTimestamp(41, newObj.productionEnd);
                prepStmt.setBoolean(42, newObj.deliveryRequired);
                prepStmt.setInt(43, newObj.deliveryLeadDay.intValue());
                prepStmt.setString(44, newObj.deliveryStatus);
                prepStmt.setTimestamp(45, newObj.deliveryStart);
                prepStmt.setTimestamp(46, newObj.deliveryEnd);
                prepStmt.setString(47, newObj.uuid);
                prepStmt.setString(48, newObj.codeProject);
                prepStmt.setString(49, newObj.codeDepartment);
                prepStmt.setString(50, newObj.codeDealer);
                prepStmt.setString(51, newObj.codeSalesman);
                prepStmt.setInt(52, newObj.salesman.intValue());
                prepStmt.setString(53, newObj.custItemCode);
                prepStmt.setString(54, newObj.custItemName);
                prepStmt.setString(55, newObj.custRemarks);
                prepStmt.setString(56, newObj.custCurrency);
                prepStmt.setBigDecimal(57, newObj.custQuantity);
                prepStmt.setBigDecimal(58, newObj.custPreviousPrice);
                prepStmt.setBigDecimal(59, newObj.custEstimatedPrice);
                prepStmt.setBigDecimal(60, newObj.custQuotedPrice);
                prepStmt.setString(61, newObj.custStatusAcceptance);
                prepStmt.setString(62, newObj.suppLogic);
                prepStmt.setLong(63, newObj.suppInquiryItem.longValue());
                prepStmt.setString(64, newObj.suppItemCode);
                prepStmt.setString(65, newObj.suppItemName);
                prepStmt.setString(66, newObj.suppRemarks);
                prepStmt.setString(67, newObj.suppCurrency);
                prepStmt.setBigDecimal(68, newObj.suppMinOrderQuantity);
                prepStmt.setBigDecimal(69, newObj.suppAvailableQuantity);
                prepStmt.setBigDecimal(70, newObj.suppPreviousPrice);
                prepStmt.setBigDecimal(71, newObj.suppListPrice);
                prepStmt.setBigDecimal(72, newObj.suppQuotedPrice);
                prepStmt.setString(73, newObj.suppStatusRespond);
                prepStmt.setTimestamp(74, newObj.suppRespondedOn);
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
            String updateStatement = " UPDATE " + TABLENAME + " SET " + PKID + " = ?, " + INDEX_ID + " = ?, " + ITEM_TYPE + " = ?, " + ITEM_ID_INQUIRY + " = ?, " + ITEM_ID_INVOICE + " = ?, " + ITEM_ID_GRN + " = ?, " + ITEM_ID_PURCHASE_ORDER + " = ?, " + ITEM_ID + " = ?, " + ITEM_CODE + " = ?, " + ITEM_BARCODE + " = ?, " + QUANTITY + " = ?, " + QTY_SHORTAGE + " = ?, " + QTY_DELIVERED + " = ?, " + QTY_OUTSTANDING + " = ?, " + QTY_BILLED + " = ?, " + QTY_RESERVED + " = ?, " + CURRENCY1 + " = ?, " + UNIT_PRICE1 + " = ?, " + UNIT_DISCOUNT1 + " = ?, " + UNIT_TAX1 + " = ?, " + UNIT_NET_PRICE1 + " = ?, " + CURRENCY2 + " = ?, " + UNIT_PRICE2 + " = ?, " + UNIT_DISCOUNT2 + " = ?, " + UNIT_TAX2 + " = ?, " + UNIT_NET_PRICE2 + " = ?, " + SERIALIZED + " = ?, " + NAME + " = ?, " + REMARKS + " = ?, " + DESCRIPTION + " = ?, " + STATUS_INTERNAL + " = ?, " + PIC1 + " = ?, " + PIC2 + " = ?, " + PIC3 + " = ?, " + STATUS + " = ?, " + STATE + " = ?, " + PRODUCTION_REQUIRED + " = ?, " + PRODUCTION_LEAD_DAY + " = ?, " + PRODUCTION_STATUS + " = ?, " + PRODUCTION_START + " = ?, " + PRODUCTION_END + " = ?, " + DELIVERY_REQUIRED + " = ?, " + DELIVERY_LEAD_DAY + " = ?, " + DELIVERY_STATUS + " = ?, " + DELIVERY_START + " = ?, " + DELIVERY_END + " = ?, " + UUID + " = ?, " + CODE_PROJECT + " = ?, " + CODE_DEPARTMENT + " = ?, " + CODE_DEALER + " = ?, " + CODE_SALESMAN + " = ?, " + SALESMAN + " = ?, " + CUST_ITEM_CODE + " = ?, " + CUST_ITEM_NAME + " = ?, " + CUST_REMARKS + " = ?, " + CUST_CURRENCY + " = ?, " + CUST_QUANTITY + " = ?, " + CUST_PREVIOUS_PRICE + " = ?, " + CUST_ESTIMATED_PRICE + " = ?, " + CUST_QUOTED_PRICE + " = ?, " + CUST_STATUS_ACCEPTANCE + " = ?, " + SUPP_LOGIC + " = ?, " + SUPP_INQUIRY_ITEM + " = ?, " + SUPP_ITEM_CODE + " = ?, " + SUPP_ITEM_NAME + " = ?, " + SUPP_REMARKS + " = ?, " + SUPP_CURRENCY + " = ?, " + SUPP_MIN_ORDER_QUANTITY + " = ?, " + SUPP_AVAILABLE_QUANTITY + " = ?, " + SUPP_PREVIOUS_PRICE + " = ?, " + SUPP_LIST_PRICE + " = ?, " + SUPP_QUOTED_PRICE + " = ?, " + SUPP_STATUS_RESPOND + " = ?, " + SUPP_RESPONDED_ON + " = ? " + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(updateStatement);
            prepStmt.setLong(1, this.valObj.pkid.longValue());
            prepStmt.setLong(2, this.valObj.indexId.longValue());
            prepStmt.setString(3, this.valObj.itemType);
            prepStmt.setLong(4, this.valObj.itemIdInquiry.longValue());
            prepStmt.setLong(5, this.valObj.itemIdInvoice.longValue());
            prepStmt.setLong(6, this.valObj.itemIdGrn.longValue());
            prepStmt.setLong(7, this.valObj.itemIdPurchaseOrder.longValue());
            prepStmt.setInt(8, this.valObj.itemId.intValue());
            prepStmt.setString(9, this.valObj.itemCode);
            prepStmt.setString(10, this.valObj.itemBarcode);
            prepStmt.setBigDecimal(11, this.valObj.quantity);
            prepStmt.setBigDecimal(12, this.valObj.qtyShortage);
            prepStmt.setBigDecimal(13, this.valObj.qtyDelivered);
            prepStmt.setBigDecimal(14, this.valObj.qtyOutstanding);
            prepStmt.setBigDecimal(15, this.valObj.qtyBilled);
            prepStmt.setBigDecimal(16, this.valObj.qtyReserved);
            prepStmt.setString(17, this.valObj.currency1);
            prepStmt.setBigDecimal(18, this.valObj.unitPrice1);
            prepStmt.setBigDecimal(19, this.valObj.unitDiscount1);
            prepStmt.setBigDecimal(20, this.valObj.unitTax1);
            prepStmt.setBigDecimal(21, this.valObj.unitNetPrice1);
            prepStmt.setString(22, this.valObj.currency2);
            prepStmt.setBigDecimal(23, this.valObj.unitPrice2);
            prepStmt.setBigDecimal(24, this.valObj.unitDiscount2);
            prepStmt.setBigDecimal(25, this.valObj.unitTax2);
            prepStmt.setBigDecimal(26, this.valObj.unitNetPrice2);
            prepStmt.setBoolean(27, this.valObj.serialized);
            prepStmt.setString(28, this.valObj.name);
            prepStmt.setString(29, this.valObj.remarks);
            prepStmt.setString(30, this.valObj.description);
            prepStmt.setString(31, this.valObj.statusInternal);
            prepStmt.setInt(32, this.valObj.pic1.intValue());
            prepStmt.setInt(33, this.valObj.pic2.intValue());
            prepStmt.setInt(34, this.valObj.pic3.intValue());
            prepStmt.setString(35, this.valObj.status);
            prepStmt.setString(36, this.valObj.state);
            prepStmt.setBoolean(37, this.valObj.productionRequired);
            prepStmt.setInt(38, this.valObj.productionLeadDay.intValue());
            prepStmt.setString(39, this.valObj.productionStatus);
            prepStmt.setTimestamp(40, this.valObj.productionStart);
            prepStmt.setTimestamp(41, this.valObj.productionEnd);
            prepStmt.setBoolean(42, this.valObj.deliveryRequired);
            prepStmt.setInt(43, this.valObj.deliveryLeadDay.intValue());
            prepStmt.setString(44, this.valObj.deliveryStatus);
            prepStmt.setTimestamp(45, this.valObj.deliveryStart);
            prepStmt.setTimestamp(46, this.valObj.deliveryEnd);
            prepStmt.setString(47, this.valObj.uuid);
            prepStmt.setString(48, this.valObj.codeProject);
            prepStmt.setString(49, this.valObj.codeDepartment);
            prepStmt.setString(50, this.valObj.codeDealer);
            prepStmt.setString(51, this.valObj.codeSalesman);
            prepStmt.setInt(52, this.valObj.salesman.intValue());
            prepStmt.setString(53, this.valObj.custItemCode);
            prepStmt.setString(54, this.valObj.custItemName);
            prepStmt.setString(55, this.valObj.custRemarks);
            prepStmt.setString(56, this.valObj.custCurrency);
            prepStmt.setBigDecimal(57, this.valObj.custQuantity);
            prepStmt.setBigDecimal(58, this.valObj.custPreviousPrice);
            prepStmt.setBigDecimal(59, this.valObj.custEstimatedPrice);
            prepStmt.setBigDecimal(60, this.valObj.custQuotedPrice);
            prepStmt.setString(61, this.valObj.custStatusAcceptance);
            prepStmt.setString(62, this.valObj.suppLogic);
            prepStmt.setLong(63, this.valObj.suppInquiryItem.longValue());
            prepStmt.setString(64, this.valObj.suppItemCode);
            prepStmt.setString(65, this.valObj.suppItemName);
            prepStmt.setString(66, this.valObj.suppRemarks);
            prepStmt.setString(67, this.valObj.suppCurrency);
            prepStmt.setBigDecimal(68, this.valObj.suppMinOrderQuantity);
            prepStmt.setBigDecimal(69, this.valObj.suppAvailableQuantity);
            prepStmt.setBigDecimal(70, this.valObj.suppPreviousPrice);
            prepStmt.setBigDecimal(71, this.valObj.suppListPrice);
            prepStmt.setBigDecimal(72, this.valObj.suppQuotedPrice);
            prepStmt.setString(73, this.valObj.suppStatusRespond);
            prepStmt.setTimestamp(74, this.valObj.suppRespondedOn);
            prepStmt.setLong(75, this.valObj.pkid.longValue());
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

    public static CustProformaInvoiceItemObject getObject(ResultSet rs, String prefix) throws Exception {
        CustProformaInvoiceItemObject theObj = null;
        try {
            theObj = new CustProformaInvoiceItemObject();
            theObj.pkid = new Long(rs.getLong(prefix + PKID));
            theObj.indexId = new Long(rs.getLong(prefix + INDEX_ID));
            theObj.itemType = rs.getString(prefix + ITEM_TYPE);
            theObj.itemIdInquiry = new Long(rs.getLong(prefix + ITEM_ID_INQUIRY));
            theObj.itemIdInvoice = new Long(rs.getLong(prefix + ITEM_ID_INVOICE));
            theObj.itemIdGrn = new Long(rs.getLong(prefix + ITEM_ID_GRN));
            theObj.itemIdPurchaseOrder = new Long(rs.getLong(prefix + ITEM_ID_PURCHASE_ORDER));
            theObj.itemId = new Integer(rs.getInt(ITEM_ID));
            theObj.itemCode = rs.getString(prefix + ITEM_CODE);
            theObj.itemBarcode = rs.getString(prefix + ITEM_BARCODE);
            theObj.quantity = rs.getBigDecimal(prefix + QUANTITY);
            theObj.qtyShortage = rs.getBigDecimal(prefix + QTY_SHORTAGE);
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
            theObj.statusInternal = rs.getString(prefix + STATUS_INTERNAL);
            theObj.pic1 = new Integer(rs.getInt(prefix + PIC1));
            theObj.pic2 = new Integer(rs.getInt(prefix + PIC2));
            theObj.pic3 = new Integer(rs.getInt(prefix + PIC3));
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
            theObj.salesman = new Integer(rs.getInt(prefix + SALESMAN));
            theObj.custItemCode = rs.getString(prefix + CUST_ITEM_CODE);
            theObj.custItemName = rs.getString(prefix + CUST_ITEM_NAME);
            theObj.custRemarks = rs.getString(prefix + CUST_REMARKS);
            theObj.custCurrency = rs.getString(prefix + CUST_CURRENCY);
            theObj.custQuantity = rs.getBigDecimal(prefix + CUST_QUANTITY);
            theObj.custPreviousPrice = rs.getBigDecimal(prefix + CUST_PREVIOUS_PRICE);
            theObj.custEstimatedPrice = rs.getBigDecimal(prefix + CUST_ESTIMATED_PRICE);
            theObj.custQuotedPrice = rs.getBigDecimal(prefix + CUST_QUOTED_PRICE);
            theObj.custStatusAcceptance = rs.getString(prefix + CUST_STATUS_ACCEPTANCE);
            theObj.suppLogic = rs.getString(prefix + SUPP_LOGIC);
            theObj.suppInquiryItem = new Long(rs.getLong(prefix + SUPP_INQUIRY_ITEM));
            theObj.suppItemCode = rs.getString(prefix + SUPP_ITEM_CODE);
            theObj.suppItemName = rs.getString(prefix + SUPP_ITEM_NAME);
            theObj.suppRemarks = rs.getString(prefix + SUPP_REMARKS);
            theObj.suppCurrency = rs.getString(prefix + SUPP_CURRENCY);
            theObj.suppMinOrderQuantity = rs.getBigDecimal(prefix + SUPP_MIN_ORDER_QUANTITY);
            theObj.suppAvailableQuantity = rs.getBigDecimal(prefix + SUPP_AVAILABLE_QUANTITY);
            theObj.suppPreviousPrice = rs.getBigDecimal(prefix + SUPP_PREVIOUS_PRICE);
            theObj.suppListPrice = rs.getBigDecimal(prefix + SUPP_LIST_PRICE);
            theObj.suppQuotedPrice = rs.getBigDecimal(prefix + SUPP_QUOTED_PRICE);
            theObj.suppStatusRespond = rs.getString(prefix + SUPP_STATUS_RESPOND);
            theObj.suppRespondedOn = rs.getTimestamp(prefix + SUPP_RESPONDED_ON);
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
                CustProformaInvoiceItemObject theObj = getObject(rs, "");
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

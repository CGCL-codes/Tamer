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
import com.vlee.ejb.customer.*;
import com.vlee.ejb.accounting.*;

public class CustProductInquiryIndexBean implements EntityBean {

    public static final String PKID = "pkid";

    public static final String STMT_NO = "stmt_no";

    public static final String GUID = "guid";

    public static final String BRANCH = "branch";

    public static final String PCCENTER = "pccenter";

    public static final String LOCATION = "location";

    public static final String TXNTYPE = "txntype";

    public static final String STMT_TYPE = "stmt_type";

    public static final String REFERENCE_NO = "reference_no";

    public static final String DESCRIPTION = "description";

    public static final String SALESMAN = "salesman";

    public static final String ID_INVOICE = "id_invoice";

    public static final String ID_QUOTATION = "id_quotation";

    public static final String ID_ORDER = "id_order";

    public static final String ID_RECEIPT = "id_receipt";

    public static final String CURRENCY = "currency";

    public static final String AMOUNT = "amount";

    public static final String AMOUNT_OUTSTANDING = "amount_outstanding";

    public static final String AMOUNT_COMMISSION = "amount_commission";

    public static final String AMOUNT_DISCOUNT = "amount_discount";

    public static final String AMOUNT_TAX = "amount_tax";

    public static final String CURRENCY2 = "currency2";

    public static final String AMOUNT2 = "amount2";

    public static final String AMOUNT_OUTSTANDING2 = "amount_outstanding2";

    public static final String AMOUNT_COMMISSION2 = "amount_commission2";

    public static final String AMOUNT_DISCOUNT2 = "amount_discount2";

    public static final String AMOUNT_TAX2 = "amount_tax2";

    public static final String TERMS_CREDIT = "terms_credit";

    public static final String REMARKS1 = "remarks1";

    public static final String REMARKS2 = "remarks2";

    public static final String STATE = "state";

    public static final String STATUS = "status";

    public static final String USERID_CREATE = "userid_create";

    public static final String USERID_EDIT = "userid_edit";

    public static final String TIME_CREATE = "time_create";

    public static final String TIME_UPDATE = "time_update";

    public static final String TIME_INQUIRY = "time_inquiry";

    public static final String TIME_RESPOND_BEFORE = "time_respond_before";

    public static final String FLAG_SENDER = "flag_sender";

    public static final String FLAG_RECEIVER = "flag_receiver";

    public static final String FLAG_INTERNAL = "flag_internal";

    public static final String RECEIVER_TABLE = "receiver_table";

    public static final String RECEIVER_KEY = "receiver_key";

    public static final String RECEIVER_TITLE = "receiver_title";

    public static final String RECEIVER_NAME = "receiver_name";

    public static final String RECEIVER_TYPE = "receiver_type";

    public static final String RECEIVER_IDENTITY_NUMBER = "receiver_identity_number";

    public static final String RECEIVER_CONTACT_PERSON = "receiver_contact_person";

    public static final String RECEIVER_HANDPHONE = "receiver_handphone";

    public static final String RECEIVER_PHONE1 = "receiver_phone1";

    public static final String RECEIVER_PHONE2 = "receiver_phone2";

    public static final String RECEIVER_FAX = "receiver_fax";

    public static final String RECEIVER_EMAIL = "receiver_email";

    public static final String RECEIVER_COMPANY_NAME = "receiver_company_name";

    public static final String RECEIVER_ADD1 = "receiver_add1";

    public static final String RECEIVER_ADD2 = "receiver_add2";

    public static final String RECEIVER_ADD3 = "receiver_add3";

    public static final String RECEIVER_CITY = "receiver_city";

    public static final String RECEIVER_ZIP = "receiver_zip";

    public static final String RECEIVER_STATE = "receiver_state";

    public static final String RECEIVER_COUNTRY = "receiver_country";

    public static final String RECEIVER_FOREIGN_TABLE = "receiver_foreign_table";

    public static final String RECEIVER_FOREIGN_KEY = "receiver_foreign_key";

    public static final String RECEIVER_FOREIGN_TEXT = "receiver_foreign_text";

    public static final String SENDER_TABLE1 = "sender_table1";

    public static final String SENDER_KEY1 = "sender_key1";

    public static final String SENDER_TABLE2 = "sender_table2";

    public static final String SENDER_KEY2 = "sender_key2";

    public static final String SENDER_TITLE = "sender_title";

    public static final String SENDER_NAME = "sender_name";

    public static final String SENDER_TYPE = "sender_type";

    public static final String SENDER_IDENTITY_NUMBER = "sender_identity_number";

    public static final String SENDER_CONTACT_PERSON = "sender_contact_person";

    public static final String SENDER_HANDPHONE = "sender_handphone";

    public static final String SENDER_PHONE1 = "sender_phone1";

    public static final String SENDER_PHONE2 = "sender_phone2";

    public static final String SENDER_FAX = "sender_fax";

    public static final String SENDER_EMAIL = "sender_email";

    public static final String SENDER_INTERNET_NO = "sender_internet_no";

    public static final String SENDER_COMPANY_NAME = "sender_company_name";

    public static final String SENDER_ADD1 = "sender_add1";

    public static final String SENDER_ADD2 = "sender_add2";

    public static final String SENDER_ADD3 = "sender_add3";

    public static final String SENDER_CITY = "sender_city";

    public static final String SENDER_ZIP = "sender_zip";

    public static final String SENDER_STATE = "sender_state";

    public static final String SENDER_COUNTRY = "sender_country";

    public static final String SENDER_LOYALTY_CARD_NAME = "sender_loyalty_card_name";

    public static final String SENDER_LOYALTY_CARD_NO = "sender_loyalty_card_no";

    public static final String SENDER_LOYALTY_CARD_PTS_GAIN = "sender_loyalty_card_pts_gain";

    public static final String SENDER_LOYALTY_CARD_PTS_CONSUME = "sender_loyalty_card_pts_consume";

    public static final String SENDER_FOREIGN_TABLE = "sender_foreign_table";

    public static final String SENDER_FOREIGN_KEY = "sender_foreign_key";

    public static final String SENDER_FOREIGN_TEXT = "sender_foreign_text";

    public static final String EXP_DELIVERY_TIME = "exp_delivery_time";

    public static final String EXP_DELIVERY_TIME_START = "exp_delivery_time_start";

    public static final String EXP_DELIVERY_TIME_END = "exp_delivery_time_end";

    public static final String DELIVERY_TO = "delivery_to";

    public static final String DELIVERY_TO_NAME = "delivery_to_name";

    public static final String DELIVERY_MSG1 = "delivery_msg1";

    public static final String DELIVERY_MSG2 = "delivery_msg2";

    public static final String DELIVERY_MSG3 = "delivery_msg3";

    public static final String DELIVERY_FROM = "delivery_from";

    public static final String DELIVERY_FROM_NAME = "delivery_from_name";

    public static final String DELIVERY_PREFERENCES = "delivery_preferences";

    public static final String ORDERTAKER_USERID = "ordertaker_userid";

    public static final String ORDERTAKER_NAME = "ordertaker_name";

    public static final String ORDERTAKER_TIME = "ordertaker_time";

    public static final String ORDERTAKER_INSTRUCTION = "ordertaker_instruction";

    public static final String CUSTOMER_COMPLAINTS = "customer_complaints";

    public static final String FULFIL_PERCENTAGE = "fulfil_percentage";

    public static final String FULFIL_STAGE = "fulfil_stage";

    public static final String FULFIL_USER = "fulfil_user";

    public static final String PRINT_COUNT_INVOICE = "print_count_invoice";

    public static final String PRINT_COUNT_RECEIPT = "print_count_receipt";

    public static final String PRINT_COUNT_WORKSHOP = "print_count_workshop";

    public static final String PRINT_COUNT_DELIVERY_ORDER = "print_count_delivery_order";

    public static final String PRINT_COUNT_SALES_ORDER = "print_count_sales_order";

    public static final String REQUIRE_INVOICE = "require_invoice";

    public static final String REQUIRE_RECEIPT = "require_receipt";

    public static final String DISPLAY_FORMAT = "display_format";

    public static final String DOC_TYPE = "doc_type";

    public static final String TYPE1 = "type1";

    public static final String TYPE2 = "type2";

    public static final String OCCASION = "occasion";

    public static final String RECEIPT_STATUS = "receipt_status";

    public static final String RECEIPT_MODE = "receipt_mode";

    public static final String RECEIPT_REMARKS = "receipt_remarks";

    public static final String RECEIPT_APPROVAL_PARTY = "receipt_approval_party";

    public static final String RECEIPT_APPROVAL_CODE = "receipt_approval_code";

    public static final String RECEIPT_APPROVAL_TIME = "receipt_approval_time";

    public static final String RECEIPT_BRANCH = "receipt_branch";

    public static final String PRODUCTION_BRANCH = "production_branch";

    public static final String PRODUCTION_LOCATION = "production_location";

    public static final String PROMO_TYPE = "promo_type";

    public static final String PROMO_CODE = "promo_code";

    public static final String PROMO_NUMBER = "promo_number";

    public static final String PROMO_NAME = "promo_name";

    public static final String PROMO_DISCOUNT_AMOUNT = "promo_discount_amount";

    public static final String PROMO_DISCOUNT_PCT = "promo_discount_pct";

    public static final String ETXN_TYPE = "etxn_type";

    public static final String ETXN_CODE = "etxn_code";

    public static final String ETXN_MODE = "etxn_mode";

    public static final String ETXN_NAME = "etxn_name";

    public static final String ETXN_ACCOUNT = "etxn_account";

    public static final String ETXN_REMARKS = "etxn_remarks";

    public static final String ETXN_STATE = "etxn_state";

    public static final String ETXN_STATUS = "etxn_status";

    public static final String ETXN_ERROR_CODE = "etxn_error_code";

    public static final String MODULENAME = "customer";

    public static final Long PKID_START = new Long(1001);

    public static final Long STMT_NO_START = new Long(1001);

    CustProductInquiryIndexObject valObj;

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String TABLENAME = "cust_product_inquiry_index";

    private static final String strObjectName = "CustProductInquiryIndexBean: ";

    private EntityContext context = null;

    /***************************************************************************
	 * Getters
	 **************************************************************************/
    public CustProductInquiryIndexObject getObject() {
        return this.valObj;
    }

    public void setObject(CustProductInquiryIndexObject newVal) {
        Long pkid = this.valObj.pkid;
        this.valObj = newVal;
        this.valObj.pkid = pkid;
        this.valObj.failSafe();
    }

    public Long getPkid() {
        return this.valObj.pkid;
    }

    public Long getStmtNo() {
        return this.valObj.stmtNo;
    }

    public void adjustOutstanding(BigDecimal delta) {
        this.valObj.amountOutstanding = this.valObj.amountOutstanding.add(delta);
    }

    public Long getPrimaryKey() {
        return this.valObj.pkid;
    }

    public void setPrimaryKey(Long pkid) {
        this.valObj.pkid = pkid;
    }

    public void addPrintCounterInvoice() {
        this.valObj.printCountInvoice = new Integer(this.valObj.printCountInvoice.intValue() + 1);
    }

    public void addPrintCounterReceipt() {
        this.valObj.printCountReceipt = new Integer(this.valObj.printCountReceipt.intValue() + 1);
    }

    public void addPrintCounterWorkshop() {
        this.valObj.printCountWorkshop = new Integer(this.valObj.printCountWorkshop.intValue() + 1);
    }

    public void addPrintCounterDeliveryOrder() {
        this.valObj.printCountDeliveryOrder = new Integer(this.valObj.printCountDeliveryOrder.intValue() + 1);
    }

    public void addPrintCounterSalesOrder() {
        this.valObj.printCountSalesOrder = new Integer(this.valObj.printCountSalesOrder.intValue() + 1);
    }

    /***************************************************************************
	 * ejbCreate
	 **************************************************************************/
    public Long ejbCreate(CustProductInquiryIndexObject newObj) throws CreateException {
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

    public Long ejbFindByInvoice(Long invoiceId) throws FinderException {
        Log.printVerbose(strObjectName + "in ejbFindByInvoice");
        Long primaryKey = null;
        boolean result = false;
        try {
            primaryKey = selectByInvoice(invoiceId);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EJBException("ejbFindByInvoice : " + ex.getMessage());
        }
        if (primaryKey != null) {
            return primaryKey;
        } else {
            throw new ObjectNotFoundException("Row for id " + invoiceId.toString() + "not found.");
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
            this.valObj = new CustProductInquiryIndexObject();
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
    public void ejbPostCreate(CustProductInquiryIndexObject newObj) {
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

    public Vector ejbHomeGetObjectsWithDocumentProcessingItem(String dateFilter, Timestamp dateFrom, Timestamp dateTo, String processType, String category, String docRef) {
        Vector vecResult = new Vector();
        try {
            vecResult = selectObjectsWithDocumentProcessingItem(dateFilter, dateFrom, dateTo, processType, category, docRef);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecResult;
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

    private CustProductInquiryIndexObject insertObject(CustProductInquiryIndexObject newObj) throws NamingException, SQLException {
        newObj.failSafe();
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Long nextStmtNo = null;
            Log.printVerbose(strObjectName + " insertObject: ");
            con = makeConnection();
            try {
                newObj.pkid = getNextPKId(con);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(strObjectName + ex.getMessage());
            }
            try {
                nextStmtNo = getNextStmtNo(con, BRANCH, newObj.branch.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new EJBException(strObjectName + ex.getMessage());
            }
            try {
                String insertStatement = " INSERT INTO " + TABLENAME + "( " + PKID + ", " + STMT_NO + ", " + GUID + ", " + BRANCH + ", " + PCCENTER + ", " + LOCATION + ", " + TXNTYPE + ", " + STMT_TYPE + ", " + REFERENCE_NO + ", " + DESCRIPTION + ", " + SALESMAN + ", " + ID_INVOICE + ", " + ID_QUOTATION + ", " + ID_ORDER + ", " + ID_RECEIPT + ", " + CURRENCY + ", " + AMOUNT + ", " + AMOUNT_OUTSTANDING + ", " + AMOUNT_COMMISSION + ", " + AMOUNT_DISCOUNT + ", " + AMOUNT_TAX + ", " + CURRENCY2 + ", " + AMOUNT2 + ", " + AMOUNT_OUTSTANDING2 + ", " + AMOUNT_COMMISSION2 + ", " + AMOUNT_DISCOUNT2 + ", " + AMOUNT_TAX2 + ", " + TERMS_CREDIT + ", " + REMARKS1 + ", " + REMARKS2 + ", " + STATE + ", " + STATUS + ", " + USERID_CREATE + ", " + USERID_EDIT + ", " + TIME_CREATE + ", " + TIME_UPDATE + ", " + TIME_INQUIRY + ", " + TIME_RESPOND_BEFORE + ", " + FLAG_SENDER + ", " + FLAG_RECEIVER + ", " + FLAG_INTERNAL + ", " + RECEIVER_TABLE + ", " + RECEIVER_KEY + ", " + RECEIVER_TITLE + ", " + RECEIVER_NAME + ", " + RECEIVER_TYPE + ", " + RECEIVER_IDENTITY_NUMBER + ", " + RECEIVER_CONTACT_PERSON + ", " + RECEIVER_HANDPHONE + ", " + RECEIVER_PHONE1 + ", " + RECEIVER_PHONE2 + ", " + RECEIVER_FAX + ", " + RECEIVER_EMAIL + ", " + RECEIVER_COMPANY_NAME + ", " + RECEIVER_ADD1 + ", " + RECEIVER_ADD2 + ", " + RECEIVER_ADD3 + ", " + RECEIVER_CITY + ", " + RECEIVER_ZIP + ", " + RECEIVER_STATE + ", " + RECEIVER_COUNTRY + ", " + RECEIVER_FOREIGN_TABLE + ", " + RECEIVER_FOREIGN_KEY + ", " + RECEIVER_FOREIGN_TEXT + ", " + SENDER_TABLE1 + ", " + SENDER_KEY1 + ", " + SENDER_TABLE2 + ", " + SENDER_KEY2 + ", " + SENDER_TITLE + ", " + SENDER_NAME + ", " + SENDER_TYPE + ", " + SENDER_IDENTITY_NUMBER + ", " + SENDER_CONTACT_PERSON + ", " + SENDER_HANDPHONE + ", " + SENDER_PHONE1 + ", " + SENDER_PHONE2 + ", " + SENDER_FAX + ", " + SENDER_EMAIL + ", " + SENDER_INTERNET_NO + ", " + SENDER_COMPANY_NAME + ", " + SENDER_ADD1 + ", " + SENDER_ADD2 + ", " + SENDER_ADD3 + ", " + SENDER_CITY + ", " + SENDER_ZIP + ", " + SENDER_STATE + ", " + SENDER_COUNTRY + ", " + SENDER_LOYALTY_CARD_NAME + ", " + SENDER_LOYALTY_CARD_NO + ", " + SENDER_LOYALTY_CARD_PTS_GAIN + ", " + SENDER_LOYALTY_CARD_PTS_CONSUME + ", " + SENDER_FOREIGN_TABLE + ", " + SENDER_FOREIGN_KEY + ", " + SENDER_FOREIGN_TEXT + ", " + EXP_DELIVERY_TIME + ", " + EXP_DELIVERY_TIME_START + ", " + EXP_DELIVERY_TIME_END + ", " + DELIVERY_TO + ", " + DELIVERY_TO_NAME + ", " + DELIVERY_MSG1 + ", " + DELIVERY_MSG2 + ", " + DELIVERY_MSG3 + ", " + DELIVERY_FROM + ", " + DELIVERY_FROM_NAME + ", " + DELIVERY_PREFERENCES + ", " + ORDERTAKER_USERID + ", " + ORDERTAKER_NAME + ", " + ORDERTAKER_TIME + ", " + ORDERTAKER_INSTRUCTION + ", " + CUSTOMER_COMPLAINTS + ", " + FULFIL_PERCENTAGE + ", " + FULFIL_STAGE + ", " + FULFIL_USER + ", " + PRINT_COUNT_INVOICE + ", " + PRINT_COUNT_RECEIPT + ", " + PRINT_COUNT_WORKSHOP + ", " + PRINT_COUNT_DELIVERY_ORDER + ", " + PRINT_COUNT_SALES_ORDER + ", " + REQUIRE_INVOICE + ", " + REQUIRE_RECEIPT + ", " + DISPLAY_FORMAT + ", " + DOC_TYPE + ", " + TYPE1 + ", " + TYPE2 + ", " + OCCASION + ", " + RECEIPT_STATUS + ", " + RECEIPT_MODE + ", " + RECEIPT_REMARKS + ", " + RECEIPT_APPROVAL_PARTY + ", " + RECEIPT_APPROVAL_CODE + ", " + RECEIPT_APPROVAL_TIME + ", " + RECEIPT_BRANCH + ", " + PRODUCTION_BRANCH + ", " + PRODUCTION_LOCATION + ", " + PROMO_TYPE + ", " + PROMO_CODE + ", " + PROMO_NUMBER + ", " + PROMO_NAME + ", " + PROMO_DISCOUNT_AMOUNT + ", " + PROMO_DISCOUNT_PCT + ", " + ETXN_TYPE + ", " + ETXN_CODE + ", " + ETXN_MODE + ", " + ETXN_NAME + ", " + ETXN_ACCOUNT + ", " + ETXN_REMARKS + ", " + ETXN_STATE + ", " + ETXN_STATUS + ", " + ETXN_ERROR_CODE + ") values ( " + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, ?, ?, ?, ? ); ";
                prepStmt = con.prepareStatement(insertStatement);
                if (newObj.guid.length() == 0) newObj.guid = String.valueOf(newObj.pkid);
                prepStmt.setLong(1, newObj.pkid.longValue());
                prepStmt.setLong(2, newObj.stmtNo.longValue());
                prepStmt.setString(3, newObj.guid);
                prepStmt.setInt(4, newObj.branch.intValue());
                prepStmt.setInt(5, newObj.pccenter.intValue());
                prepStmt.setInt(6, newObj.location.intValue());
                prepStmt.setString(7, newObj.txntype);
                prepStmt.setString(8, newObj.stmtType);
                prepStmt.setString(9, newObj.referenceNo);
                prepStmt.setString(10, newObj.description);
                prepStmt.setInt(11, newObj.salesman.intValue());
                prepStmt.setLong(12, newObj.idInvoice.longValue());
                prepStmt.setLong(13, newObj.idQuotation.longValue());
                prepStmt.setLong(14, newObj.idOrder.longValue());
                prepStmt.setLong(15, newObj.idReceipt.longValue());
                prepStmt.setString(16, newObj.currency);
                prepStmt.setBigDecimal(17, newObj.amount);
                prepStmt.setBigDecimal(18, newObj.amountOutstanding);
                prepStmt.setBigDecimal(19, newObj.amountCommission);
                prepStmt.setBigDecimal(20, newObj.amountDiscount);
                prepStmt.setBigDecimal(21, newObj.amountTax);
                prepStmt.setString(22, newObj.currency2);
                prepStmt.setBigDecimal(23, newObj.amount2);
                prepStmt.setBigDecimal(24, newObj.amountOutstanding2);
                prepStmt.setBigDecimal(25, newObj.amountCommission2);
                prepStmt.setBigDecimal(26, newObj.amountDiscount2);
                prepStmt.setBigDecimal(27, newObj.amountTax2);
                prepStmt.setInt(28, newObj.termsCredit.intValue());
                prepStmt.setString(29, newObj.remarks1);
                prepStmt.setString(30, newObj.remarks2);
                prepStmt.setString(31, newObj.state);
                prepStmt.setString(32, newObj.status);
                prepStmt.setInt(33, newObj.useridCreate.intValue());
                prepStmt.setInt(34, newObj.useridEdit.intValue());
                prepStmt.setTimestamp(35, newObj.timeCreate);
                prepStmt.setTimestamp(36, newObj.timeUpdate);
                prepStmt.setTimestamp(37, newObj.timeInquiry);
                prepStmt.setTimestamp(38, newObj.timeRespondBefore);
                prepStmt.setString(39, newObj.flagSender);
                prepStmt.setString(40, newObj.flagReceiver);
                prepStmt.setString(41, newObj.flagInternal);
                prepStmt.setString(42, newObj.receiverTable);
                prepStmt.setInt(43, newObj.receiverKey.intValue());
                prepStmt.setString(44, newObj.receiverTitle);
                prepStmt.setString(45, newObj.receiverName);
                prepStmt.setString(46, newObj.receiverType);
                prepStmt.setString(47, newObj.receiverIdentityNumber);
                prepStmt.setString(48, newObj.receiverContactPerson);
                prepStmt.setString(49, newObj.receiverHandphone);
                prepStmt.setString(50, newObj.receiverPhone1);
                prepStmt.setString(51, newObj.receiverPhone2);
                prepStmt.setString(52, newObj.receiverFax);
                prepStmt.setString(53, newObj.receiverEmail);
                prepStmt.setString(54, newObj.receiverCompanyName);
                prepStmt.setString(55, newObj.receiverAdd1);
                prepStmt.setString(56, newObj.receiverAdd2);
                prepStmt.setString(57, newObj.receiverAdd3);
                prepStmt.setString(58, newObj.receiverCity);
                prepStmt.setString(59, newObj.receiverZip);
                prepStmt.setString(60, newObj.receiverState);
                prepStmt.setString(61, newObj.receiverCountry);
                prepStmt.setString(62, newObj.receiverForeignTable);
                prepStmt.setInt(63, newObj.receiverForeignKey.intValue());
                prepStmt.setString(64, newObj.receiverForeignText);
                prepStmt.setString(65, newObj.senderTable1);
                prepStmt.setInt(66, newObj.senderKey1.intValue());
                prepStmt.setString(67, newObj.senderTable2);
                prepStmt.setInt(68, newObj.senderKey2.intValue());
                prepStmt.setString(69, newObj.senderTitle);
                prepStmt.setString(70, newObj.senderName);
                prepStmt.setString(71, newObj.senderType);
                prepStmt.setString(72, newObj.senderIdentityNumber);
                prepStmt.setString(73, newObj.senderContactPerson);
                prepStmt.setString(74, newObj.senderHandphone);
                prepStmt.setString(75, newObj.senderPhone1);
                prepStmt.setString(76, newObj.senderPhone2);
                prepStmt.setString(77, newObj.senderFax);
                prepStmt.setString(78, newObj.senderEmail);
                prepStmt.setString(79, newObj.senderInternetNo);
                prepStmt.setString(80, newObj.senderCompanyName);
                prepStmt.setString(81, newObj.senderAdd1);
                prepStmt.setString(82, newObj.senderAdd2);
                prepStmt.setString(83, newObj.senderAdd3);
                prepStmt.setString(84, newObj.senderCity);
                prepStmt.setString(85, newObj.senderZip);
                prepStmt.setString(86, newObj.senderState);
                prepStmt.setString(87, newObj.senderCountry);
                prepStmt.setString(88, newObj.senderLoyaltyCardName);
                prepStmt.setString(89, newObj.senderLoyaltyCardNo);
                prepStmt.setBigDecimal(90, newObj.senderLoyaltyCardPtsGain);
                prepStmt.setBigDecimal(91, newObj.senderLoyaltyCardPtsConsume);
                prepStmt.setString(92, newObj.senderForeignTable);
                prepStmt.setInt(93, newObj.senderForeignKey.intValue());
                prepStmt.setString(94, newObj.senderForeignText);
                prepStmt.setString(95, newObj.expDeliveryTime);
                prepStmt.setTimestamp(96, newObj.expDeliveryTimeStart);
                prepStmt.setTimestamp(97, newObj.expDeliveryTimeEnd);
                prepStmt.setString(98, newObj.deliveryTo);
                prepStmt.setString(99, newObj.deliveryToName);
                prepStmt.setString(100, newObj.deliveryMsg1);
                prepStmt.setString(101, newObj.deliveryMsg2);
                prepStmt.setString(102, newObj.deliveryMsg3);
                prepStmt.setString(103, newObj.deliveryFrom);
                prepStmt.setString(104, newObj.deliveryFromName);
                prepStmt.setString(105, newObj.deliveryPreferences);
                prepStmt.setInt(106, newObj.ordertakerUserid.intValue());
                prepStmt.setString(107, newObj.ordertakerName);
                prepStmt.setTimestamp(108, newObj.ordertakerTime);
                prepStmt.setString(109, newObj.ordertakerInstruction);
                prepStmt.setString(110, newObj.customerComplaints);
                prepStmt.setBigDecimal(111, newObj.fulfilPercentage);
                prepStmt.setString(112, newObj.fulfilStage);
                prepStmt.setInt(113, newObj.fulfilUser.intValue());
                prepStmt.setInt(114, newObj.printCountInvoice.intValue());
                prepStmt.setInt(115, newObj.printCountReceipt.intValue());
                prepStmt.setInt(116, newObj.printCountWorkshop.intValue());
                prepStmt.setInt(117, newObj.printCountDeliveryOrder.intValue());
                prepStmt.setInt(118, newObj.printCountSalesOrder.intValue());
                prepStmt.setBoolean(119, newObj.requireInvoice);
                prepStmt.setBoolean(120, newObj.requireReceipt);
                prepStmt.setString(121, newObj.displayFormat);
                prepStmt.setString(122, newObj.docType);
                prepStmt.setString(123, newObj.type1);
                prepStmt.setString(124, newObj.type2);
                prepStmt.setString(125, newObj.occasion);
                prepStmt.setString(126, newObj.receiptStatus);
                prepStmt.setString(127, newObj.receiptMode);
                prepStmt.setString(128, newObj.receiptRemarks);
                prepStmt.setString(129, newObj.receiptApprovalParty);
                prepStmt.setString(130, newObj.receiptApprovalCode);
                prepStmt.setTimestamp(131, newObj.receiptApprovalTime);
                prepStmt.setInt(132, newObj.receiptBranch.intValue());
                prepStmt.setInt(133, newObj.productionBranch.intValue());
                prepStmt.setInt(134, newObj.productionLocation.intValue());
                prepStmt.setString(135, newObj.promoType);
                prepStmt.setString(136, newObj.promoCode);
                prepStmt.setString(137, newObj.promoNumber);
                prepStmt.setString(138, newObj.promoName);
                prepStmt.setBigDecimal(139, newObj.promoDiscountAmount);
                prepStmt.setBigDecimal(140, newObj.promoDiscountPct);
                prepStmt.setString(141, newObj.etxnType);
                prepStmt.setString(142, newObj.etxnCode);
                prepStmt.setString(143, newObj.etxnMode);
                prepStmt.setString(144, newObj.etxnName);
                prepStmt.setString(145, newObj.etxnAccount);
                prepStmt.setString(146, newObj.etxnRemarks);
                prepStmt.setString(147, newObj.etxnState);
                prepStmt.setString(148, newObj.etxnStatus);
                prepStmt.setString(149, newObj.etxnErrorCode);
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

    private Long selectByInvoice(Long invoiceId) throws NamingException, SQLException {
        Connection con = null;
        PreparedStatement prepStmt = null;
        Long primaryKey = null;
        try {
            Log.printVerbose(strObjectName + " selectByInvoice: ");
            con = makeConnection();
            String selectStmt = " SELECT " + PKID + " FROM " + TABLENAME + " WHERE " + ID_INVOICE + " = ? ";
            prepStmt = con.prepareStatement(selectStmt);
            prepStmt.setLong(1, invoiceId.longValue());
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next()) {
                primaryKey = new Long(rs.getLong(PKID));
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
        return primaryKey;
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
            String updateStatement = " UPDATE " + TABLENAME + " SET " + PKID + " = ?, " + STMT_NO + " = ?, " + GUID + " = ?, " + BRANCH + " = ?, " + PCCENTER + " = ?, " + LOCATION + " = ?, " + TXNTYPE + " = ?, " + STMT_TYPE + " = ?, " + REFERENCE_NO + " = ?, " + DESCRIPTION + " = ?, " + SALESMAN + " = ?, " + ID_INVOICE + " = ?, " + ID_QUOTATION + " = ?, " + ID_ORDER + " = ?, " + ID_RECEIPT + " = ?, " + CURRENCY + " = ?, " + AMOUNT + " = ?, " + AMOUNT_OUTSTANDING + " = ?, " + AMOUNT_COMMISSION + " = ?, " + AMOUNT_DISCOUNT + " = ?, " + AMOUNT_TAX + " = ?, " + CURRENCY2 + " = ?, " + AMOUNT2 + " = ?, " + AMOUNT_OUTSTANDING2 + " = ?, " + AMOUNT_COMMISSION2 + " = ?, " + AMOUNT_DISCOUNT2 + " = ?, " + AMOUNT_TAX2 + " = ?, " + TERMS_CREDIT + " = ?, " + REMARKS1 + " = ?, " + REMARKS2 + " = ?, " + STATE + " = ?, " + STATUS + " = ?, " + USERID_CREATE + " = ?, " + USERID_EDIT + " = ?, " + TIME_CREATE + " = ?, " + TIME_UPDATE + " = ?, " + TIME_INQUIRY + " = ?, " + TIME_RESPOND_BEFORE + " = ?, " + FLAG_SENDER + " = ?, " + FLAG_RECEIVER + " = ?, " + FLAG_INTERNAL + " = ?, " + RECEIVER_TABLE + " = ?, " + RECEIVER_KEY + " = ?, " + RECEIVER_TITLE + " = ?, " + RECEIVER_NAME + " = ?, " + RECEIVER_TYPE + " = ?, " + RECEIVER_IDENTITY_NUMBER + " = ?, " + RECEIVER_CONTACT_PERSON + " = ?, " + RECEIVER_HANDPHONE + " = ?, " + RECEIVER_PHONE1 + " = ?, " + RECEIVER_PHONE2 + " = ?, " + RECEIVER_FAX + " = ?, " + RECEIVER_EMAIL + " = ?, " + RECEIVER_COMPANY_NAME + " = ?, " + RECEIVER_ADD1 + " = ?, " + RECEIVER_ADD2 + " = ?, " + RECEIVER_ADD3 + " = ?, " + RECEIVER_CITY + " = ?, " + RECEIVER_ZIP + " = ?, " + RECEIVER_STATE + " = ?, " + RECEIVER_COUNTRY + " = ?, " + RECEIVER_FOREIGN_TABLE + " = ?, " + RECEIVER_FOREIGN_KEY + " = ?, " + RECEIVER_FOREIGN_TEXT + " = ?, " + SENDER_TABLE1 + " = ?, " + SENDER_KEY1 + " = ?, " + SENDER_TABLE2 + " = ?, " + SENDER_KEY2 + " = ?, " + SENDER_TITLE + " = ?, " + SENDER_NAME + " = ?, " + SENDER_TYPE + " = ?, " + SENDER_IDENTITY_NUMBER + " = ?, " + SENDER_CONTACT_PERSON + " = ?, " + SENDER_HANDPHONE + " = ?, " + SENDER_PHONE1 + " = ?, " + SENDER_PHONE2 + " = ?, " + SENDER_FAX + " = ?, " + SENDER_EMAIL + " = ?, " + SENDER_INTERNET_NO + " = ?, " + SENDER_COMPANY_NAME + " = ?, " + SENDER_ADD1 + " = ?, " + SENDER_ADD2 + " = ?, " + SENDER_ADD3 + " = ?, " + SENDER_CITY + " = ?, " + SENDER_ZIP + " = ?, " + SENDER_STATE + " = ?, " + SENDER_COUNTRY + " = ?, " + SENDER_LOYALTY_CARD_NAME + " = ?, " + SENDER_LOYALTY_CARD_NO + " = ?, " + SENDER_LOYALTY_CARD_PTS_GAIN + " = ?, " + SENDER_LOYALTY_CARD_PTS_CONSUME + " = ?, " + SENDER_FOREIGN_TABLE + " = ?, " + SENDER_FOREIGN_KEY + " = ?, " + SENDER_FOREIGN_TEXT + " = ?, " + EXP_DELIVERY_TIME + " = ?, " + EXP_DELIVERY_TIME_START + " = ?, " + EXP_DELIVERY_TIME_END + " = ?, " + DELIVERY_TO + " = ?, " + DELIVERY_TO_NAME + " = ?, " + DELIVERY_MSG1 + " = ?, " + DELIVERY_MSG2 + " = ?, " + DELIVERY_MSG3 + " = ?, " + DELIVERY_FROM + " = ?, " + DELIVERY_FROM_NAME + " = ?, " + DELIVERY_PREFERENCES + " = ?, " + ORDERTAKER_USERID + " = ?, " + ORDERTAKER_NAME + " = ?, " + ORDERTAKER_TIME + " = ?, " + ORDERTAKER_INSTRUCTION + " = ?, " + CUSTOMER_COMPLAINTS + " = ?, " + FULFIL_PERCENTAGE + " = ?, " + FULFIL_STAGE + " = ?, " + FULFIL_USER + " = ?, " + PRINT_COUNT_INVOICE + " = ?, " + PRINT_COUNT_RECEIPT + " = ?, " + PRINT_COUNT_WORKSHOP + " = ?, " + PRINT_COUNT_DELIVERY_ORDER + " = ?, " + PRINT_COUNT_SALES_ORDER + " = ?, " + REQUIRE_INVOICE + " = ?, " + REQUIRE_RECEIPT + " = ?, " + DISPLAY_FORMAT + " = ?, " + DOC_TYPE + " = ?, " + TYPE1 + " = ?, " + TYPE2 + " = ?, " + OCCASION + " = ?, " + RECEIPT_STATUS + " = ?, " + RECEIPT_MODE + " = ?, " + RECEIPT_REMARKS + " = ?, " + RECEIPT_APPROVAL_PARTY + " = ?, " + RECEIPT_APPROVAL_CODE + " = ?, " + RECEIPT_APPROVAL_TIME + " = ?, " + RECEIPT_BRANCH + " = ?, " + PRODUCTION_BRANCH + " = ?, " + PRODUCTION_LOCATION + " = ?, " + PROMO_TYPE + " = ?, " + PROMO_CODE + " = ?, " + PROMO_NUMBER + " = ?, " + PROMO_NAME + " = ?, " + PROMO_DISCOUNT_AMOUNT + " = ?, " + PROMO_DISCOUNT_PCT + " = ?, " + ETXN_TYPE + " = ?, " + ETXN_CODE + " = ?, " + ETXN_MODE + " = ?, " + ETXN_NAME + " = ?, " + ETXN_ACCOUNT + " = ?, " + ETXN_REMARKS + " = ?, " + ETXN_STATE + " = ?, " + ETXN_STATUS + " = ?, " + ETXN_ERROR_CODE + " = ? " + " WHERE " + PKID + " = ? ";
            prepStmt = con.prepareStatement(updateStatement);
            prepStmt.setLong(1, this.valObj.pkid.longValue());
            prepStmt.setLong(2, this.valObj.stmtNo.longValue());
            prepStmt.setString(3, this.valObj.guid);
            prepStmt.setInt(4, this.valObj.branch.intValue());
            prepStmt.setInt(5, this.valObj.pccenter.intValue());
            prepStmt.setInt(6, this.valObj.location.intValue());
            prepStmt.setString(7, this.valObj.txntype);
            prepStmt.setString(8, this.valObj.stmtType);
            prepStmt.setString(9, this.valObj.referenceNo);
            prepStmt.setString(10, this.valObj.description);
            prepStmt.setInt(11, this.valObj.salesman.intValue());
            prepStmt.setLong(12, this.valObj.idInvoice.longValue());
            prepStmt.setLong(13, this.valObj.idQuotation.longValue());
            prepStmt.setLong(14, this.valObj.idOrder.longValue());
            prepStmt.setLong(15, this.valObj.idReceipt.longValue());
            prepStmt.setString(16, this.valObj.currency);
            prepStmt.setBigDecimal(17, this.valObj.amount);
            prepStmt.setBigDecimal(18, this.valObj.amountOutstanding);
            prepStmt.setBigDecimal(19, this.valObj.amountCommission);
            prepStmt.setBigDecimal(20, this.valObj.amountDiscount);
            prepStmt.setBigDecimal(21, this.valObj.amountTax);
            prepStmt.setString(22, this.valObj.currency2);
            prepStmt.setBigDecimal(23, this.valObj.amount2);
            prepStmt.setBigDecimal(24, this.valObj.amountOutstanding2);
            prepStmt.setBigDecimal(25, this.valObj.amountCommission2);
            prepStmt.setBigDecimal(26, this.valObj.amountDiscount2);
            prepStmt.setBigDecimal(27, this.valObj.amountTax2);
            prepStmt.setInt(28, this.valObj.termsCredit.intValue());
            prepStmt.setString(29, this.valObj.remarks1);
            prepStmt.setString(30, this.valObj.remarks2);
            prepStmt.setString(31, this.valObj.state);
            prepStmt.setString(32, this.valObj.status);
            prepStmt.setInt(33, this.valObj.useridCreate.intValue());
            prepStmt.setInt(34, this.valObj.useridEdit.intValue());
            prepStmt.setTimestamp(35, this.valObj.timeCreate);
            prepStmt.setTimestamp(36, this.valObj.timeUpdate);
            prepStmt.setTimestamp(37, this.valObj.timeInquiry);
            prepStmt.setTimestamp(38, this.valObj.timeRespondBefore);
            prepStmt.setString(39, this.valObj.flagSender);
            prepStmt.setString(40, this.valObj.flagReceiver);
            prepStmt.setString(41, this.valObj.flagInternal);
            prepStmt.setString(42, this.valObj.receiverTable);
            prepStmt.setInt(43, this.valObj.receiverKey.intValue());
            prepStmt.setString(44, this.valObj.receiverTitle);
            prepStmt.setString(45, this.valObj.receiverName);
            prepStmt.setString(46, this.valObj.receiverType);
            prepStmt.setString(47, this.valObj.receiverIdentityNumber);
            prepStmt.setString(48, this.valObj.receiverContactPerson);
            prepStmt.setString(49, this.valObj.receiverHandphone);
            prepStmt.setString(50, this.valObj.receiverPhone1);
            prepStmt.setString(51, this.valObj.receiverPhone2);
            prepStmt.setString(52, this.valObj.receiverFax);
            prepStmt.setString(53, this.valObj.receiverEmail);
            prepStmt.setString(54, this.valObj.receiverCompanyName);
            prepStmt.setString(55, this.valObj.receiverAdd1);
            prepStmt.setString(56, this.valObj.receiverAdd2);
            prepStmt.setString(57, this.valObj.receiverAdd3);
            prepStmt.setString(58, this.valObj.receiverCity);
            prepStmt.setString(59, this.valObj.receiverZip);
            prepStmt.setString(60, this.valObj.receiverState);
            prepStmt.setString(61, this.valObj.receiverCountry);
            prepStmt.setString(62, this.valObj.receiverForeignTable);
            prepStmt.setInt(63, this.valObj.receiverForeignKey.intValue());
            prepStmt.setString(64, this.valObj.receiverForeignText);
            prepStmt.setString(65, this.valObj.senderTable1);
            prepStmt.setInt(66, this.valObj.senderKey1.intValue());
            prepStmt.setString(67, this.valObj.senderTable2);
            prepStmt.setInt(68, this.valObj.senderKey2.intValue());
            prepStmt.setString(69, this.valObj.senderTitle);
            prepStmt.setString(70, this.valObj.senderName);
            prepStmt.setString(71, this.valObj.senderType);
            prepStmt.setString(72, this.valObj.senderIdentityNumber);
            prepStmt.setString(73, this.valObj.senderContactPerson);
            prepStmt.setString(74, this.valObj.senderHandphone);
            prepStmt.setString(75, this.valObj.senderPhone1);
            prepStmt.setString(76, this.valObj.senderPhone2);
            prepStmt.setString(77, this.valObj.senderFax);
            prepStmt.setString(78, this.valObj.senderEmail);
            prepStmt.setString(79, this.valObj.senderInternetNo);
            prepStmt.setString(80, this.valObj.senderCompanyName);
            prepStmt.setString(81, this.valObj.senderAdd1);
            prepStmt.setString(82, this.valObj.senderAdd2);
            prepStmt.setString(83, this.valObj.senderAdd3);
            prepStmt.setString(84, this.valObj.senderCity);
            prepStmt.setString(85, this.valObj.senderZip);
            prepStmt.setString(86, this.valObj.senderState);
            prepStmt.setString(87, this.valObj.senderCountry);
            prepStmt.setString(88, this.valObj.senderLoyaltyCardName);
            prepStmt.setString(89, this.valObj.senderLoyaltyCardNo);
            prepStmt.setBigDecimal(90, this.valObj.senderLoyaltyCardPtsGain);
            prepStmt.setBigDecimal(91, this.valObj.senderLoyaltyCardPtsConsume);
            prepStmt.setString(92, this.valObj.senderForeignTable);
            prepStmt.setInt(93, this.valObj.senderForeignKey.intValue());
            prepStmt.setString(94, this.valObj.senderForeignText);
            prepStmt.setString(95, this.valObj.expDeliveryTime);
            prepStmt.setTimestamp(96, this.valObj.expDeliveryTimeStart);
            prepStmt.setTimestamp(97, this.valObj.expDeliveryTimeEnd);
            prepStmt.setString(98, this.valObj.deliveryTo);
            prepStmt.setString(99, this.valObj.deliveryToName);
            prepStmt.setString(100, this.valObj.deliveryMsg1);
            prepStmt.setString(101, this.valObj.deliveryMsg2);
            prepStmt.setString(102, this.valObj.deliveryMsg3);
            prepStmt.setString(103, this.valObj.deliveryFrom);
            prepStmt.setString(104, this.valObj.deliveryFromName);
            prepStmt.setString(105, this.valObj.deliveryPreferences);
            prepStmt.setInt(106, this.valObj.ordertakerUserid.intValue());
            prepStmt.setString(107, this.valObj.ordertakerName);
            prepStmt.setTimestamp(108, this.valObj.ordertakerTime);
            prepStmt.setString(109, this.valObj.ordertakerInstruction);
            prepStmt.setString(110, this.valObj.customerComplaints);
            prepStmt.setBigDecimal(111, this.valObj.fulfilPercentage);
            prepStmt.setString(112, this.valObj.fulfilStage);
            prepStmt.setInt(113, this.valObj.fulfilUser.intValue());
            prepStmt.setInt(114, this.valObj.printCountInvoice.intValue());
            prepStmt.setInt(115, this.valObj.printCountReceipt.intValue());
            prepStmt.setInt(116, this.valObj.printCountWorkshop.intValue());
            prepStmt.setInt(117, this.valObj.printCountDeliveryOrder.intValue());
            prepStmt.setInt(118, this.valObj.printCountSalesOrder.intValue());
            prepStmt.setBoolean(119, this.valObj.requireInvoice);
            prepStmt.setBoolean(120, this.valObj.requireReceipt);
            prepStmt.setString(121, this.valObj.displayFormat);
            prepStmt.setString(122, this.valObj.docType);
            prepStmt.setString(123, this.valObj.type1);
            prepStmt.setString(124, this.valObj.type2);
            prepStmt.setString(125, this.valObj.occasion);
            prepStmt.setString(126, this.valObj.receiptStatus);
            prepStmt.setString(127, this.valObj.receiptMode);
            prepStmt.setString(128, this.valObj.receiptRemarks);
            prepStmt.setString(129, this.valObj.receiptApprovalParty);
            prepStmt.setString(130, this.valObj.receiptApprovalCode);
            prepStmt.setTimestamp(131, this.valObj.receiptApprovalTime);
            prepStmt.setInt(132, this.valObj.receiptBranch.intValue());
            prepStmt.setInt(133, this.valObj.productionBranch.intValue());
            prepStmt.setInt(134, this.valObj.productionLocation.intValue());
            prepStmt.setString(135, this.valObj.promoType);
            prepStmt.setString(136, this.valObj.promoCode);
            prepStmt.setString(137, this.valObj.promoNumber);
            prepStmt.setString(138, this.valObj.promoName);
            prepStmt.setBigDecimal(139, this.valObj.promoDiscountAmount);
            prepStmt.setBigDecimal(140, this.valObj.promoDiscountPct);
            prepStmt.setString(141, this.valObj.etxnType);
            prepStmt.setString(142, this.valObj.etxnCode);
            prepStmt.setString(143, this.valObj.etxnMode);
            prepStmt.setString(144, this.valObj.etxnName);
            prepStmt.setString(145, this.valObj.etxnAccount);
            prepStmt.setString(146, this.valObj.etxnRemarks);
            prepStmt.setString(147, this.valObj.etxnState);
            prepStmt.setString(148, this.valObj.etxnStatus);
            prepStmt.setString(149, this.valObj.etxnErrorCode);
            prepStmt.setLong(150, this.valObj.pkid.longValue());
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

    private static synchronized Long getNextStmtNo(Connection con, String stmtType, Long branch) throws NamingException, SQLException {
        return AppTableCounterUtil.getNextPKId(con, branch.toString(), TABLENAME, stmtType, PKID_START);
    }

    public static CustProductInquiryIndexObject getObject(ResultSet rs, String prefix) throws Exception {
        CustProductInquiryIndexObject theObj = null;
        try {
            theObj = new CustProductInquiryIndexObject();
            theObj.pkid = new Long(rs.getLong(prefix + PKID));
            theObj.stmtNo = new Long(rs.getLong(prefix + STMT_NO));
            theObj.guid = rs.getString(prefix + GUID);
            theObj.branch = new Integer(rs.getInt(prefix + BRANCH));
            theObj.pccenter = new Integer(rs.getInt(prefix + PCCENTER));
            theObj.location = new Integer(rs.getInt(prefix + LOCATION));
            theObj.txntype = rs.getString(prefix + TXNTYPE);
            theObj.stmtType = rs.getString(prefix + STMT_TYPE);
            theObj.referenceNo = rs.getString(prefix + REFERENCE_NO);
            theObj.description = rs.getString(prefix + DESCRIPTION);
            theObj.salesman = new Integer(rs.getInt(prefix + SALESMAN));
            theObj.idInvoice = new Long(rs.getLong(prefix + ID_INVOICE));
            theObj.idQuotation = new Long(rs.getLong(prefix + ID_QUOTATION));
            theObj.idOrder = new Long(rs.getLong(prefix + ID_ORDER));
            theObj.idReceipt = new Long(rs.getLong(prefix + ID_RECEIPT));
            theObj.currency = rs.getString(prefix + CURRENCY);
            theObj.amount = rs.getBigDecimal(prefix + AMOUNT);
            theObj.amountOutstanding = rs.getBigDecimal(prefix + AMOUNT_OUTSTANDING);
            theObj.amountCommission = rs.getBigDecimal(prefix + AMOUNT_COMMISSION);
            theObj.amountDiscount = rs.getBigDecimal(prefix + AMOUNT_DISCOUNT);
            theObj.amountTax = rs.getBigDecimal(prefix + AMOUNT_TAX);
            theObj.currency2 = rs.getString(prefix + CURRENCY2);
            theObj.amount2 = rs.getBigDecimal(prefix + AMOUNT2);
            theObj.amountOutstanding2 = rs.getBigDecimal(prefix + AMOUNT_OUTSTANDING2);
            theObj.amountCommission2 = rs.getBigDecimal(prefix + AMOUNT_COMMISSION2);
            theObj.amountDiscount2 = rs.getBigDecimal(prefix + AMOUNT_DISCOUNT2);
            theObj.amountTax2 = rs.getBigDecimal(prefix + AMOUNT_TAX2);
            theObj.termsCredit = new Integer(rs.getInt(prefix + TERMS_CREDIT));
            theObj.remarks1 = rs.getString(prefix + REMARKS1);
            theObj.remarks2 = rs.getString(prefix + REMARKS2);
            theObj.state = rs.getString(prefix + STATE);
            theObj.status = rs.getString(prefix + STATUS);
            theObj.useridCreate = new Integer(rs.getInt(prefix + USERID_CREATE));
            theObj.useridEdit = new Integer(rs.getInt(prefix + USERID_EDIT));
            theObj.timeCreate = rs.getTimestamp(prefix + TIME_CREATE);
            theObj.timeUpdate = rs.getTimestamp(prefix + TIME_UPDATE);
            theObj.timeInquiry = rs.getTimestamp(prefix + TIME_INQUIRY);
            theObj.timeRespondBefore = rs.getTimestamp(prefix + TIME_RESPOND_BEFORE);
            theObj.flagSender = rs.getString(prefix + FLAG_SENDER);
            theObj.flagReceiver = rs.getString(prefix + FLAG_RECEIVER);
            theObj.flagInternal = rs.getString(prefix + FLAG_INTERNAL);
            theObj.receiverTable = rs.getString(prefix + RECEIVER_TABLE);
            theObj.receiverKey = new Integer(rs.getInt(prefix + RECEIVER_KEY));
            theObj.receiverTitle = rs.getString(prefix + RECEIVER_TITLE);
            theObj.receiverName = rs.getString(prefix + RECEIVER_NAME);
            theObj.receiverType = rs.getString(prefix + RECEIVER_TYPE);
            theObj.receiverIdentityNumber = rs.getString(prefix + RECEIVER_IDENTITY_NUMBER);
            theObj.receiverContactPerson = rs.getString(prefix + RECEIVER_CONTACT_PERSON);
            theObj.receiverHandphone = rs.getString(prefix + RECEIVER_HANDPHONE);
            theObj.receiverPhone1 = rs.getString(prefix + RECEIVER_PHONE1);
            theObj.receiverPhone2 = rs.getString(prefix + RECEIVER_PHONE2);
            theObj.receiverFax = rs.getString(prefix + RECEIVER_FAX);
            theObj.receiverEmail = rs.getString(prefix + RECEIVER_EMAIL);
            theObj.receiverCompanyName = rs.getString(prefix + RECEIVER_COMPANY_NAME);
            theObj.receiverAdd1 = rs.getString(prefix + RECEIVER_ADD1);
            theObj.receiverAdd2 = rs.getString(prefix + RECEIVER_ADD2);
            theObj.receiverAdd3 = rs.getString(prefix + RECEIVER_ADD3);
            theObj.receiverCity = rs.getString(prefix + RECEIVER_CITY);
            theObj.receiverZip = rs.getString(prefix + RECEIVER_ZIP);
            theObj.receiverState = rs.getString(prefix + RECEIVER_STATE);
            theObj.receiverCountry = rs.getString(prefix + RECEIVER_COUNTRY);
            theObj.receiverForeignTable = rs.getString(prefix + RECEIVER_TABLE);
            theObj.receiverForeignKey = new Integer(rs.getInt(prefix + RECEIVER_FOREIGN_KEY));
            theObj.receiverForeignText = rs.getString(prefix + RECEIVER_FOREIGN_TEXT);
            theObj.senderTable1 = rs.getString(prefix + SENDER_TABLE1);
            theObj.senderKey1 = new Integer(rs.getInt(prefix + SENDER_KEY1));
            theObj.senderTable2 = rs.getString(prefix + SENDER_TABLE2);
            theObj.senderKey2 = new Integer(rs.getInt(prefix + SENDER_KEY2));
            theObj.senderTitle = rs.getString(prefix + SENDER_TITLE);
            theObj.senderName = rs.getString(prefix + SENDER_NAME);
            theObj.senderType = rs.getString(prefix + SENDER_TYPE);
            theObj.senderIdentityNumber = rs.getString(prefix + SENDER_IDENTITY_NUMBER);
            theObj.senderContactPerson = rs.getString(prefix + SENDER_CONTACT_PERSON);
            theObj.senderHandphone = rs.getString(prefix + SENDER_HANDPHONE);
            theObj.senderPhone1 = rs.getString(prefix + SENDER_PHONE1);
            theObj.senderPhone2 = rs.getString(prefix + SENDER_PHONE2);
            theObj.senderFax = rs.getString(prefix + SENDER_FAX);
            theObj.senderEmail = rs.getString(prefix + SENDER_EMAIL);
            theObj.senderInternetNo = rs.getString(prefix + SENDER_INTERNET_NO);
            theObj.senderCompanyName = rs.getString(prefix + SENDER_COMPANY_NAME);
            theObj.senderAdd1 = rs.getString(prefix + SENDER_ADD1);
            theObj.senderAdd2 = rs.getString(prefix + SENDER_ADD2);
            theObj.senderAdd3 = rs.getString(prefix + SENDER_ADD3);
            theObj.senderCity = rs.getString(prefix + SENDER_CITY);
            theObj.senderZip = rs.getString(prefix + SENDER_ZIP);
            theObj.senderState = rs.getString(prefix + SENDER_STATE);
            theObj.senderCountry = rs.getString(prefix + SENDER_COUNTRY);
            theObj.senderLoyaltyCardName = rs.getString(prefix + SENDER_LOYALTY_CARD_NAME);
            theObj.senderLoyaltyCardNo = rs.getString(prefix + SENDER_LOYALTY_CARD_NO);
            theObj.senderLoyaltyCardPtsGain = rs.getBigDecimal(prefix + SENDER_LOYALTY_CARD_PTS_GAIN);
            theObj.senderLoyaltyCardPtsConsume = rs.getBigDecimal(prefix + SENDER_LOYALTY_CARD_PTS_CONSUME);
            theObj.senderForeignTable = rs.getString(prefix + SENDER_FOREIGN_TABLE);
            theObj.senderForeignKey = new Integer(rs.getInt(prefix + SENDER_FOREIGN_KEY));
            theObj.senderForeignText = rs.getString(prefix + SENDER_FOREIGN_TEXT);
            theObj.expDeliveryTime = rs.getString(prefix + EXP_DELIVERY_TIME);
            theObj.expDeliveryTimeStart = rs.getTimestamp(prefix + EXP_DELIVERY_TIME_START);
            theObj.expDeliveryTimeEnd = rs.getTimestamp(prefix + EXP_DELIVERY_TIME_END);
            theObj.deliveryTo = rs.getString(prefix + DELIVERY_TO);
            theObj.deliveryToName = rs.getString(prefix + DELIVERY_TO_NAME);
            theObj.deliveryMsg1 = rs.getString(prefix + DELIVERY_MSG1);
            theObj.deliveryMsg2 = rs.getString(prefix + DELIVERY_MSG2);
            theObj.deliveryMsg3 = rs.getString(prefix + DELIVERY_MSG3);
            theObj.deliveryFrom = rs.getString(prefix + DELIVERY_FROM);
            theObj.deliveryFromName = rs.getString(prefix + DELIVERY_FROM_NAME);
            theObj.deliveryPreferences = rs.getString(prefix + DELIVERY_PREFERENCES);
            theObj.ordertakerUserid = new Integer(rs.getInt(prefix + ORDERTAKER_USERID));
            theObj.ordertakerName = rs.getString(prefix + ORDERTAKER_NAME);
            theObj.ordertakerTime = rs.getTimestamp(prefix + ORDERTAKER_TIME);
            theObj.ordertakerInstruction = rs.getString(prefix + ORDERTAKER_INSTRUCTION);
            theObj.customerComplaints = rs.getString(prefix + CUSTOMER_COMPLAINTS);
            theObj.fulfilPercentage = rs.getBigDecimal(prefix + FULFIL_PERCENTAGE);
            theObj.fulfilStage = rs.getString(prefix + FULFIL_STAGE);
            theObj.fulfilUser = new Integer(rs.getInt(prefix + FULFIL_USER));
            theObj.printCountInvoice = new Integer(rs.getInt(prefix + PRINT_COUNT_INVOICE));
            theObj.printCountReceipt = new Integer(rs.getInt(prefix + PRINT_COUNT_RECEIPT));
            theObj.printCountWorkshop = new Integer(rs.getInt(prefix + PRINT_COUNT_WORKSHOP));
            theObj.printCountDeliveryOrder = new Integer(rs.getInt(prefix + PRINT_COUNT_DELIVERY_ORDER));
            theObj.printCountSalesOrder = new Integer(rs.getInt(prefix + PRINT_COUNT_SALES_ORDER));
            theObj.requireInvoice = rs.getBoolean(prefix + REQUIRE_INVOICE);
            theObj.requireReceipt = rs.getBoolean(prefix + REQUIRE_RECEIPT);
            theObj.displayFormat = rs.getString(prefix + DISPLAY_FORMAT);
            theObj.docType = rs.getString(prefix + DOC_TYPE);
            theObj.type1 = rs.getString(prefix + TYPE1);
            theObj.type2 = rs.getString(prefix + TYPE2);
            theObj.occasion = rs.getString(prefix + OCCASION);
            theObj.receiptStatus = rs.getString(prefix + RECEIPT_STATUS);
            theObj.receiptMode = rs.getString(prefix + RECEIPT_MODE);
            theObj.receiptRemarks = rs.getString(prefix + RECEIPT_REMARKS);
            theObj.receiptApprovalParty = rs.getString(prefix + RECEIPT_APPROVAL_PARTY);
            theObj.receiptApprovalCode = rs.getString(prefix + RECEIPT_APPROVAL_CODE);
            theObj.receiptApprovalTime = rs.getTimestamp(prefix + RECEIPT_APPROVAL_TIME);
            theObj.receiptBranch = new Integer(rs.getInt(prefix + RECEIPT_BRANCH));
            theObj.productionBranch = new Integer(rs.getInt(prefix + PRODUCTION_BRANCH));
            theObj.productionLocation = new Integer(rs.getInt(prefix + PRODUCTION_LOCATION));
            theObj.promoType = rs.getString(prefix + PROMO_TYPE);
            theObj.promoCode = rs.getString(prefix + PROMO_CODE);
            theObj.promoNumber = rs.getString(prefix + PROMO_NUMBER);
            theObj.promoName = rs.getString(prefix + PROMO_NAME);
            theObj.promoDiscountAmount = rs.getBigDecimal(prefix + PROMO_DISCOUNT_AMOUNT);
            theObj.promoDiscountPct = rs.getBigDecimal(prefix + PROMO_DISCOUNT_PCT);
            theObj.etxnType = rs.getString(prefix + ETXN_TYPE);
            theObj.etxnCode = rs.getString(prefix + ETXN_CODE);
            theObj.etxnMode = rs.getString(prefix + ETXN_MODE);
            theObj.etxnName = rs.getString(prefix + ETXN_NAME);
            theObj.etxnAccount = rs.getString(prefix + ETXN_ACCOUNT);
            theObj.etxnRemarks = rs.getString(prefix + ETXN_REMARKS);
            theObj.etxnState = rs.getString(prefix + ETXN_STATE);
            theObj.etxnStatus = rs.getString(prefix + ETXN_STATUS);
            theObj.etxnErrorCode = rs.getString(prefix + ETXN_ERROR_CODE);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
        return theObj;
    }

    private Vector selectObjectsWithDocumentProcessingItem(String dateFilter, Timestamp dateFrom, Timestamp dateTo, String processType, String category, String docRef) throws NamingException, SQLException {
        Vector result = new Vector();
        Connection con = null;
        PreparedStatement prepStmt = null;
        Timestamp dateNextTo = TimeFormat.add(dateTo, 0, 0, 1);
        try {
            Log.printVerbose(strObjectName + " selectObjects: ");
            con = makeConnection();
            String selectStmt = " SELECT distinct(odr.*)  FROM " + TABLENAME + " AS odr INNER JOIN " + DocumentProcessingItemBean.TABLENAME + " AS dpi ON (odr.pkid=dpi.doc_id) WHERE odr." + dateFilter + " >= '" + TimeFormat.strDisplayDate(dateFrom) + "' AND " + " odr." + dateFilter + "< '" + TimeFormat.strDisplayDate(dateNextTo) + "' ";
            if (category != null) {
                selectStmt += " AND dpi.category = '" + category + "' ";
            }
            if (processType != null) {
                selectStmt += " AND  dpi.process_type = '" + processType + "' ";
            }
            if (docRef != null) {
                selectStmt += " AND dpi.doc_ref = '" + docRef + "' ";
            }
            Log.printVerbose(selectStmt);
            prepStmt = con.prepareStatement(selectStmt);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                CustProductInquiryIndexObject theObj = getObject(rs, "");
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
                CustProductInquiryIndexObject theObj = getObject(rs, "");
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

    private synchronized Long getNextStmtNo(Connection con, String foreignTable, String foreignKey) throws NamingException, SQLException {
        Long nextStmtNo = AppTableCounterUtil.getNextNumber(con, STMT_NO, TABLENAME, MODULENAME, STMT_NO_START, foreignTable, foreignKey, "", "");
        if (nextStmtNo == null) {
            Long max = getNextStmtNoByMax(con, foreignKey);
            AppTableCounterUtil.createNumber(con, STMT_NO, TABLENAME, MODULENAME, foreignTable, foreignKey, "", "", max);
            if (max != null) {
                nextStmtNo = max;
            }
        }
        return nextStmtNo;
    }

    private static Long getNextStmtNoByMax(Connection con, String branch) throws NamingException, SQLException {
        Log.printVerbose(strObjectName + "In getNextStmtNo()");
        String findMaxStmtNo = " SELECT MAX(stmt_no) as max_stmt_no from " + TABLENAME + " WHERE branch = '" + branch + "' ";
        PreparedStatement prepStmt = con.prepareStatement(findMaxStmtNo);
        ResultSet rs = prepStmt.executeQuery();
        Long nextId = new Long(1);
        if (rs.next()) {
            nextId = new Long(rs.getLong("max_stmt_no") + 1);
        } else {
            nextId = STMT_NO_START;
        }
        prepStmt.close();
        return nextId;
    }
}

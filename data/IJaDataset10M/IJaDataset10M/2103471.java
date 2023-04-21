package com.vlee.bean.distribution;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.io.Serializable;
import java.math.BigDecimal;
import com.vlee.util.*;
import com.vlee.ejb.supplier.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.inventory.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.user.*;
import com.vlee.util.*;

public class OrderProcessingForm extends java.lang.Object implements Serializable {

    Integer userId = null;

    Integer iBranch = null;

    String dateType = SalesOrderIndexBean.EXP_DELIVERY_TIME_START;

    Timestamp dateFrom = null;

    Timestamp dateTo = null;

    String processFlow;

    String recipient;

    String sender;

    String deliveryFromName;

    String receiptMode;

    String receiptRemarks;

    boolean floristFilter;

    String floristId;

    boolean productionStatusFilter;

    String productionStatus;

    String senderAcc;

    String orderNumber;

    String orderTakerId;

    public Vector vecOrderRow = null;

    public String sortBy = "";

    public OrderProcessingForm(Integer userId) {
        this.userId = userId;
        this.iBranch = new Integer(-1);
        this.dateFrom = TimeFormat.getTimestamp();
        this.dateTo = TimeFormat.getTimestamp();
        this.dateTo = TimeFormat.add(this.dateTo, 0, 0, 0);
        this.recipient = "";
        this.sender = "";
        this.deliveryFromName = "";
        this.receiptMode = "all";
        this.receiptRemarks = "";
        this.floristFilter = false;
        this.floristId = "";
        this.productionStatusFilter = false;
        this.productionStatus = "";
        this.vecOrderRow = new Vector();
        this.sortBy = "";
        this.senderAcc = "";
        this.orderNumber = "";
        this.orderTakerId = "all";
    }

    public String getFloristId(String buf) {
        return this.floristId;
    }

    public String getOrderTakerId(String buf) {
        return this.orderTakerId;
    }

    public void setOrderTakerId(String buf) {
        try {
            this.orderTakerId = buf;
        } catch (Exception ex) {
            this.orderTakerId = buf;
        }
    }

    public String getAccID(String buf) {
        return this.senderAcc;
    }

    public void setAccID(String buf) {
        try {
            this.senderAcc = buf;
        } catch (Exception ex) {
            this.senderAcc = buf;
        }
    }

    public String getOrderNumber(String buf) {
        return this.orderNumber;
    }

    public void setOrderNumber(String buf) {
        try {
            this.orderNumber = buf;
        } catch (Exception ex) {
            this.orderNumber = buf;
        }
    }

    public void setFloristFilter(boolean buf, String floristId) {
        this.floristFilter = buf;
        this.floristId = floristId;
    }

    public void setProductionFilter(boolean buf, String status) {
        this.productionStatusFilter = buf;
        this.productionStatus = status;
    }

    public String getSender(String buf) {
        return this.sender;
    }

    public void setSender(String buf) {
        try {
            this.sender = buf;
        } catch (Exception ex) {
            this.sender = buf;
        }
    }

    public String getRecipient(String buf) {
        return this.recipient;
    }

    public void setRecipient(String buf) {
        try {
            this.recipient = buf;
        } catch (Exception ex) {
            this.recipient = buf;
        }
    }

    public String getDeliveryFromName(String buf) {
        return this.deliveryFromName;
    }

    public void setDeliveryFromName(String buf) {
        try {
            this.deliveryFromName = buf;
        } catch (Exception ex) {
            this.deliveryFromName = buf;
        }
    }

    public String getBranchId(String buf) {
        return this.iBranch.toString();
    }

    public void setBranchId(String buf) {
        try {
            this.iBranch = new Integer(buf);
        } catch (Exception ex) {
            this.iBranch = new Integer(-1);
        }
    }

    public String getDateFrom(String buf) {
        return TimeFormat.strDisplayDate(this.dateFrom);
    }

    public String getDateTo(String buf) {
        return TimeFormat.strDisplayDate(this.dateTo);
    }

    public void setDateRange(String dtFrom, String dtTo) {
        this.dateFrom = TimeFormat.createTimestamp(dtFrom);
        this.dateTo = TimeFormat.createTimestamp(dtTo);
    }

    public void setDateRange(Timestamp tsFrom, Timestamp tsTo) {
        this.dateFrom = tsFrom;
        this.dateTo = tsTo;
    }

    public void setDateType(String buf) {
        this.dateType = buf;
    }

    public String getDateType() {
        return this.dateType;
    }

    public String getProductionStatus() {
        return this.productionStatus;
    }

    public void setProductionStatus(String buf) {
        this.productionStatus = buf;
    }

    public String getProcessFlow() {
        return this.processFlow;
    }

    public void setProcessFlow(String buf) {
        this.processFlow = buf;
    }

    public String getReceiptMode() {
        return this.receiptMode;
    }

    public void setReceiptMode(String buf) {
        this.receiptMode = buf;
    }

    public String getReceiptRemarks() {
        return this.receiptRemarks;
    }

    public void setReceiptRemarks(String buf) {
        this.receiptRemarks = buf;
    }

    public void getList() {
        System.out.println("Order Taker :" + this.orderTakerId);
        System.out.println("Florist Id :" + this.floristId);
        Vector vecTemp = SalesOrderItemNut.getProductionListing(this.orderTakerId, this.senderAcc, this.orderNumber, this.sender, this.recipient, this.deliveryFromName, this.iBranch, this.dateType, this.dateFrom, this.dateTo, this.receiptMode, this.receiptRemarks, (boolean) this.floristFilter, (String) this.floristId, (boolean) this.productionStatusFilter, (String) this.productionStatus);
        QueryObject queryRecycleBin = new QueryObject(new String[] { DocumentProcessingItemBean.CATEGORY + " = 'RECYCLE-BIN' ", DocumentProcessingItemBean.PROCESS_TYPE + " = 'ORDER-RECYCLE' ", DocumentProcessingItemBean.DOC_REF + " ='" + SalesOrderIndexBean.TABLENAME + "' " });
        queryRecycleBin.setOrder(" ORDER BY " + DocumentProcessingItemBean.DOC_ID);
        Vector vecRecycleBin = new Vector(DocumentProcessingItemNut.getObjects(queryRecycleBin));
        QueryObject queryUnsavedOrder = new QueryObject(new String[] { DocumentProcessingItemBean.CATEGORY + " = 'UNSAVED-ORDER-BIN' ", DocumentProcessingItemBean.PROCESS_TYPE + " = 'ORDER-CREATION' ", DocumentProcessingItemBean.DOC_REF + " ='" + SalesOrderIndexBean.TABLENAME + "' " });
        queryUnsavedOrder.setOrder(" ORDER BY " + DocumentProcessingItemBean.DOC_ID);
        Vector vecUnsavedOrder = new Vector(DocumentProcessingItemNut.getObjects(queryUnsavedOrder));
        TreeMap treeTemp = new TreeMap();
        for (int cnt1 = 0; cnt1 < vecTemp.size(); cnt1++) {
            OrderRow orow = (OrderRow) vecTemp.get(cnt1);
            treeTemp.put(orow.timeCreate.toString() + "-" + orow.pkid.toString(), orow);
        }
        vecTemp = new Vector(treeTemp.values());
        this.vecOrderRow = new Vector();
        for (int cnt1 = 0; cnt1 < vecTemp.size(); cnt1++) {
            try {
                OrderRow orow = (OrderRow) vecTemp.get(cnt1);
                boolean insideBin = false;
                SalesOrderIndexObject soObj = (SalesOrderIndexObject) vecTemp.get(cnt1);
                for (int cnt2 = 0; cnt2 < vecUnsavedOrder.size(); cnt2++) {
                    DocumentProcessingItemObject dpiObj = (DocumentProcessingItemObject) vecUnsavedOrder.get(cnt2);
                    if (dpiObj.docId.equals(orow.pkid)) {
                        insideBin = true;
                    }
                }
                for (int cnt2 = 0; cnt2 < vecRecycleBin.size(); cnt2++) {
                    DocumentProcessingItemObject dpiObj = (DocumentProcessingItemObject) vecRecycleBin.get(cnt2);
                    if (dpiObj.docId.equals(orow.pkid)) {
                        insideBin = true;
                    }
                }
                if (insideBin == false) {
                    orow.qoss = new QuickOrderStatusSummary(this.userId);
                    orow.qoss.setOrderNo(orow.pkid);
                    this.vecOrderRow.add(orow);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void getWorkshopList() {
        Vector vecTemp = SalesOrderItemNut.getWorkshopListing(this.sender, this.recipient, this.deliveryFromName, this.iBranch, this.dateType, this.dateFrom, this.dateTo, this.receiptMode, this.receiptRemarks, this.floristFilter, this.floristId, this.productionStatusFilter, this.productionStatus);
        QueryObject queryRecycleBin = new QueryObject(new String[] { DocumentProcessingItemBean.CATEGORY + " = 'RECYCLE-BIN' ", DocumentProcessingItemBean.PROCESS_TYPE + " = 'ORDER-RECYCLE' ", DocumentProcessingItemBean.DOC_REF + " ='" + SalesOrderIndexBean.TABLENAME + "' " });
        queryRecycleBin.setOrder(" ORDER BY " + DocumentProcessingItemBean.DOC_ID);
        Vector vecRecycleBin = new Vector(DocumentProcessingItemNut.getObjects(queryRecycleBin));
        QueryObject queryUnsavedOrder = new QueryObject(new String[] { DocumentProcessingItemBean.CATEGORY + " = 'UNSAVED-ORDER-BIN' ", DocumentProcessingItemBean.PROCESS_TYPE + " = 'ORDER-CREATION' ", DocumentProcessingItemBean.DOC_REF + " ='" + SalesOrderIndexBean.TABLENAME + "' " });
        queryUnsavedOrder.setOrder(" ORDER BY " + DocumentProcessingItemBean.DOC_ID);
        Vector vecUnsavedOrder = new Vector(DocumentProcessingItemNut.getObjects(queryUnsavedOrder));
        this.vecOrderRow = new Vector();
        for (int cnt1 = 0; cnt1 < vecTemp.size(); cnt1++) {
            try {
                OrderRow orow = (OrderRow) vecTemp.get(cnt1);
                boolean insideBin = false;
                SalesOrderIndexObject soObj = (SalesOrderIndexObject) vecTemp.get(cnt1);
                for (int cnt2 = 0; cnt2 < vecUnsavedOrder.size(); cnt2++) {
                    DocumentProcessingItemObject dpiObj = (DocumentProcessingItemObject) vecUnsavedOrder.get(cnt2);
                    if (dpiObj.docId.equals(orow.pkid)) {
                        insideBin = true;
                    }
                }
                for (int cnt2 = 0; cnt2 < vecRecycleBin.size(); cnt2++) {
                    DocumentProcessingItemObject dpiObj = (DocumentProcessingItemObject) vecRecycleBin.get(cnt2);
                    if (dpiObj.docId.equals(orow.pkid)) {
                        insideBin = true;
                    }
                }
                if (insideBin == false) {
                    orow.qoss = new QuickOrderStatusSummary(this.userId);
                    orow.qoss.setOrderNo(orow.pkid);
                    this.vecOrderRow.add(orow);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setSort(String buf) {
        this.sortBy = buf;
        if (this.sortBy.length() > 0) {
            TreeMap treeOrderRow = new TreeMap();
            for (int cnt1 = 0; cnt1 < this.vecOrderRow.size(); cnt1++) {
                OrderRow orow = (OrderRow) this.vecOrderRow.get(cnt1);
                treeOrderRow.put(orow.getKey(this.sortBy), orow);
            }
            this.vecOrderRow = new Vector(treeOrderRow.values());
        }
    }

    public Vector getOrderRow() {
        return this.vecOrderRow;
    }

    public BigDecimal getTotalAmt() {
        BigDecimal total = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.vecOrderRow.size(); cnt1++) {
            OrderRow orow = (OrderRow) this.vecOrderRow.get(cnt1);
            total = total.add(orow.itemNetPrice.multiply(orow.itemQuantity));
        }
        return total;
    }

    public BigDecimal getTotalCreativePoints() {
        BigDecimal total = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.vecOrderRow.size(); cnt1++) {
            OrderRow orow = (OrderRow) this.vecOrderRow.get(cnt1);
            total = total.add(orow.itemValueadd2Pts);
        }
        return total;
    }

    public BigDecimal getTotalProductivePoints() {
        BigDecimal total = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.vecOrderRow.size(); cnt1++) {
            OrderRow orow = (OrderRow) this.vecOrderRow.get(cnt1);
            total = total.add(orow.itemValueadd1Pts);
        }
        return total;
    }

    public static class OrderRow extends SalesOrderIndexObject {

        public Long itemPkid;

        public String itemCode;

        public String itemName;

        public String itemDescription;

        public BigDecimal itemQuantity;

        public BigDecimal itemPrice1;

        public BigDecimal itemDiscount;

        public BigDecimal itemNetPrice;

        public String itemRemarks;

        public String itemProductionStatus;

        public String itemProcessFlow;

        public boolean itemDeliveryRequired;

        public String itemDeliveryStatus;

        public String itemValueadd1Type;

        public Integer itemValueadd1User;

        public BigDecimal itemValueadd1Pts;

        public String itemValueadd1Name;

        public String itemValueadd2Type;

        public Integer itemValueadd2User;

        public BigDecimal itemValueadd2Pts;

        public String itemValueadd2Name;

        public String soType2;

        public QuickOrderStatusSummary qoss;

        public OrderRow() {
            super();
            this.itemPkid = new Long(0);
            this.itemCode = "";
            this.itemName = "";
            this.itemDescription = "";
            this.itemQuantity = new BigDecimal(0);
            this.itemPrice1 = new BigDecimal(0);
            this.itemDiscount = new BigDecimal(0);
            this.itemNetPrice = new BigDecimal(0);
            this.itemRemarks = "";
            this.itemProductionStatus = "";
            this.itemProcessFlow = "";
            this.itemDeliveryRequired = false;
            this.itemDeliveryStatus = "";
            this.itemValueadd1Type = "";
            this.itemValueadd1User = new Integer(0);
            this.itemValueadd1Pts = new BigDecimal(0);
            this.itemValueadd1Name = "";
            this.itemValueadd2Type = "";
            this.itemValueadd2User = new Integer(0);
            this.itemValueadd2Pts = new BigDecimal(0);
            this.itemValueadd2Name = "";
            this.soType2 = "";
        }

        public String getKey(String sortBy) {
            if (sortBy.length() == 0 || sortBy.equals("orderNo")) {
                return this.pkid.toString();
            }
            if (sortBy.equals(SalesOrderIndexBean.SENDER_NAME)) {
                return this.senderName + " " + this.pkid.toString();
            }
            if (sortBy.equals(SalesOrderIndexBean.RECEIVER_NAME)) {
                return this.receiverName + " " + this.pkid.toString();
            }
            if (sortBy.equals(SalesOrderIndexBean.RECEIPT_MODE)) {
                return this.receiptMode + " " + this.pkid.toString();
            }
            if (sortBy.equals(SalesOrderIndexBean.RECEIPT_REMARKS)) {
                return this.receiptRemarks + " " + this.pkid.toString();
            }
            return this.pkid.toString();
        }

        public void copyValues(SalesOrderIndexObject soObj) {
            this.pkid = soObj.pkid;
            this.stmtNo = soObj.stmtNo;
            this.guid = soObj.guid;
            this.branch = soObj.branch;
            this.pccenter = soObj.pccenter;
            this.location = soObj.location;
            this.txntype = soObj.txntype;
            this.stmtType = soObj.stmtType;
            this.referenceNo = soObj.referenceNo;
            this.description = soObj.description;
            this.idInvoice = soObj.idInvoice;
            this.idDeliveryOrder = soObj.idDeliveryOrder;
            this.idReceipt = soObj.idReceipt;
            this.currency = soObj.currency;
            this.amount = soObj.amount;
            this.amountOutstanding = soObj.amountOutstanding;
            this.paymentTermsId = soObj.paymentTermsId;
            this.remarks1 = soObj.remarks1;
            this.remarks2 = soObj.remarks2;
            this.state = soObj.state;
            this.status = soObj.status;
            this.useridCreate = soObj.useridCreate;
            this.useridEdit = soObj.useridEdit;
            this.timeCreate = soObj.timeCreate;
            this.timeUpdate = soObj.timeUpdate;
            this.flagSender = soObj.flagSender;
            this.flagReceiver = soObj.flagReceiver;
            this.flagInternal = soObj.flagInternal;
            this.flagInternalBool = soObj.flagInternalBool;
            this.processProduction = soObj.processProduction;
            this.processDelivery = soObj.processDelivery;
            this.receiverTable = soObj.receiverTable;
            this.receiverKey = soObj.receiverKey;
            this.receiverTitle = soObj.receiverTitle;
            this.receiverName = soObj.receiverName;
            this.receiverType = soObj.receiverType;
            this.receiverIdentityNumber = soObj.receiverIdentityNumber;
            this.receiverContactPerson = soObj.receiverContactPerson;
            this.receiverHandphone = soObj.receiverHandphone;
            this.receiverPhone1 = soObj.receiverPhone1;
            this.receiverPhone2 = soObj.receiverPhone2;
            this.receiverFax = soObj.receiverFax;
            this.receiverEmail = soObj.receiverEmail;
            this.receiverAdd1 = soObj.receiverAdd1;
            this.receiverAdd2 = soObj.receiverAdd2;
            this.receiverAdd3 = soObj.receiverAdd3;
            this.receiverCity = soObj.receiverCity;
            this.receiverZip = soObj.receiverZip;
            this.receiverState = soObj.receiverState;
            this.receiverCountry = soObj.receiverCountry;
            this.receiverForeignTable = soObj.receiverForeignTable;
            this.receiverForeignKey = soObj.receiverForeignKey;
            this.receiverForeignText = soObj.receiverForeignText;
            this.senderTable1 = soObj.senderTable1;
            this.senderKey1 = soObj.senderKey1;
            this.senderTable2 = soObj.senderTable2;
            this.senderKey2 = soObj.senderKey2;
            this.senderName = soObj.senderName;
            this.senderType = soObj.senderType;
            this.senderIdentityNumber = soObj.senderIdentityNumber;
            this.senderContactPerson = soObj.senderContactPerson;
            this.senderHandphone = soObj.senderHandphone;
            this.senderPhone1 = soObj.senderPhone1;
            this.senderPhone2 = soObj.senderPhone2;
            this.senderFax = soObj.senderFax;
            this.senderEmail = soObj.senderEmail;
            this.senderInternetNo = soObj.senderInternetNo;
            this.senderAdd1 = soObj.senderAdd1;
            this.senderAdd2 = soObj.senderAdd2;
            this.senderAdd3 = soObj.senderAdd3;
            this.senderCity = soObj.senderCity;
            this.senderZip = soObj.senderZip;
            this.senderState = soObj.senderState;
            this.senderCountry = soObj.senderCountry;
            this.senderLoyaltyCardName = soObj.senderLoyaltyCardName;
            this.senderLoyaltyCardNo = soObj.senderLoyaltyCardNo;
            this.senderLoyaltyCardPtsGain = soObj.senderLoyaltyCardPtsGain;
            this.senderLoyaltyCardPtsConsume = soObj.senderLoyaltyCardPtsConsume;
            this.senderForeignTable = soObj.senderForeignTable;
            this.senderForeignKey = soObj.senderForeignKey;
            this.senderForeignText = soObj.senderForeignText;
            this.expDeliveryTime = soObj.expDeliveryTime;
            this.expDeliveryTimeStart = soObj.expDeliveryTimeStart;
            this.expDeliveryTimeEnd = soObj.expDeliveryTimeEnd;
            this.deliveryTo = soObj.deliveryTo;
            this.deliveryMsg1 = soObj.deliveryMsg1;
            this.deliveryMsg2 = soObj.deliveryMsg2;
            this.deliveryMsg3 = soObj.deliveryMsg3;
            this.deliveryFrom = soObj.deliveryFrom;
            this.deliveryFromName = soObj.deliveryFromName;
            this.deliveryPreferences = soObj.deliveryPreferences;
            this.ordertakerUserid = soObj.ordertakerUserid;
            this.ordertakerName = soObj.ordertakerName;
            this.ordertakerTime = soObj.ordertakerTime;
            this.ordertakerInstruction = soObj.ordertakerInstruction;
            this.customerComplaints = soObj.customerComplaints;
            this.fulfilPercentage = soObj.fulfilPercentage;
            this.fulfilStage = soObj.fulfilStage;
            this.fulfilUser = soObj.fulfilUser;
            this.printCountInvoice = soObj.printCountInvoice;
            this.printCountReceipt = soObj.printCountReceipt;
            this.printCountWorkshop = soObj.printCountWorkshop;
            this.printCountDeliveryOrder = soObj.printCountDeliveryOrder;
            this.printCountSalesOrder = soObj.printCountSalesOrder;
            this.requireInvoice = soObj.requireInvoice;
            this.requireReceipt = soObj.requireReceipt;
            this.process1Dept = soObj.process1Dept;
            this.process1TeamId = soObj.process1TeamId;
            this.process1WorkerId = soObj.process1WorkerId;
            this.process1WorkerName = soObj.process1WorkerName;
            this.process1Points = soObj.process1Points;
            this.process2Dept = soObj.process2Dept;
            this.process2TeamId = soObj.process2TeamId;
            this.process2WorkerId = soObj.process2WorkerId;
            this.process2WorkerName = soObj.process2WorkerName;
            this.process2Points = soObj.process2Points;
            this.displayFormat = soObj.displayFormat;
            this.docType = soObj.docType;
            this.vecItem = soObj.vecItem;
            this.soType2 = soObj.soType2;
            this.receiptMode = soObj.receiptMode;
            this.receiptRemarks = soObj.receiptRemarks;
            this.statusPayment = soObj.statusPayment;
            this.ordertakerName = soObj.ordertakerName;
            this.printCountInvoice = soObj.printCountInvoice;
            this.printCountReceipt = soObj.printCountReceipt;
            this.printCountSalesOrder = soObj.printCountSalesOrder;
            this.printCountWorkshop = soObj.printCountWorkshop;
            this.printCountDeliveryOrder = soObj.printCountDeliveryOrder;
            this.messageCardPrintCount = soObj.messageCardPrintCount;
            this.messageCardRequired = soObj.messageCardRequired;
            this.messageCardType = soObj.messageCardType;
            this.messageCardCheck = soObj.messageCardCheck;
            this.checkOrderDetails = soObj.checkOrderDetails;
        }
    }
}

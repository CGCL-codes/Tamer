package com.vlee.bean.distribution;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Vector;
import com.vlee.util.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.user.*;
import com.vlee.util.Log;
import com.vlee.ejb.inventory.*;
import com.vlee.ejb.accounting.*;

public class ProcessingTripForm extends java.lang.Object implements Serializable {

    public SalesOrderIndexObject soObj;

    public String productionStatus;

    public Integer productionWorker;

    public BigDecimal prodPts;

    public BigDecimal creaPts;

    public Integer userId;

    public DeliveryTripObject tripObj;

    public ProcessingTripForm(Integer userId) {
        this.userId = userId;
        this.productionStatus = "";
        this.productionWorker = new Integer(0);
        this.prodPts = new BigDecimal(0);
        this.creaPts = new BigDecimal(0);
        this.tripObj = null;
    }

    public void setSalesOrder(Long orderPkid) {
        this.soObj = SalesOrderIndexNut.getObjectTree(orderPkid);
        if (soObj == null) {
            reset();
        } else {
            this.prodPts = new BigDecimal(0);
            if (this.soObj.vecItem.size() > 0) {
                Vector vecOrderRow = this.soObj.vecItem;
                SalesOrderItemObject soItmObj = (SalesOrderItemObject) vecOrderRow.get(0);
                this.productionStatus = soItmObj.productionStatus;
                this.productionWorker = soItmObj.valueadd1Userid;
                this.creaPts = soItmObj.valueadd2Points;
                for (int cnt1 = 0; cnt1 < this.soObj.vecItem.size(); cnt1++) {
                    SalesOrderItemObject soItmObj2 = (SalesOrderItemObject) this.soObj.vecItem.get(cnt1);
                    if (!soItmObj2.itemCode.equals(ItemBean.CODE_DELIVERY)) {
                        this.prodPts = this.prodPts.add(soItmObj2.valueadd1Points);
                    }
                }
                if (this.prodPts.signum() == 0) {
                    this.prodPts = getInventoryItemAmount();
                }
            }
        }
    }

    public SalesOrderIndexObject getSalesOrder() {
        return this.soObj;
    }

    public void reset() {
        Log.printVerbose("Reset");
        this.soObj = null;
        this.productionStatus = "";
        this.productionWorker = new Integer(0);
        this.prodPts = new BigDecimal(0);
        this.creaPts = new BigDecimal(0);
    }

    public String getProductionStatus() {
        Log.printVerbose("Production Status" + this.productionStatus);
        return this.productionStatus;
    }

    public void setProductionStatus(String buf) {
        this.productionStatus = buf;
    }

    public Integer getProductionWorker() {
        Log.printVerbose("ProductionWorker" + this.productionWorker);
        return this.productionWorker;
    }

    public void setProductionWorker(Integer buf) {
        this.productionWorker = buf;
    }

    public BigDecimal getProductivePoints() {
        Log.printVerbose("ProductivePoints" + this.prodPts);
        return this.prodPts;
    }

    public BigDecimal getInventoryItemAmount() {
        BigDecimal totalAmt = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.soObj.vecItem.size(); cnt1++) {
            SalesOrderItemObject soItmObj = (SalesOrderItemObject) this.soObj.vecItem.get(cnt1);
            if (!soItmObj.itemCode.equals(ItemBean.CODE_DELIVERY)) {
                totalAmt = totalAmt.add(soItmObj.price1.multiply(soItmObj.quantity));
            }
        }
        return totalAmt;
    }

    public void setProductivePoints(BigDecimal buf) {
        if (buf == null) {
            return;
        }
        this.prodPts = buf;
        for (int cnt1 = 0; cnt1 < this.soObj.vecItem.size(); cnt1++) {
            SalesOrderItemObject soItmObj = (SalesOrderItemObject) this.soObj.vecItem.get(cnt1);
            soItmObj.valueadd1Points = new BigDecimal(0);
            try {
                if (!soItmObj.itemCode.equals(ItemBean.CODE_DELIVERY)) {
                    soItmObj.valueadd1Points = this.prodPts.divide(getInventoryItemAmount(), 12, BigDecimal.ROUND_HALF_EVEN).multiply(soItmObj.price1.multiply(soItmObj.quantity));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public BigDecimal getCreativePoints() {
        Log.printVerbose("CreatePoints" + this.creaPts);
        return this.creaPts;
    }

    public void setCreativePoints(BigDecimal buf) {
        this.creaPts = buf;
    }

    public void setWorkshopDetails(String productionStatus, Integer iUserProcessing, BigDecimal bdProd, BigDecimal bdCrea, Integer userId) {
        String docInserted = "false";
        this.productionStatus = productionStatus;
        this.productionWorker = iUserProcessing;
        this.prodPts = bdProd;
        setProductivePoints(bdProd);
        setCreativePoints(bdCrea);
        Log.printVerbose("Just enter setWorkshopDetails");
        Log.printVerbose(productionStatus);
        Log.printVerbose(productionWorker.toString());
        Log.printVerbose(prodPts.toString());
        Log.printVerbose(creaPts.toString());
        for (int cnt1 = 0; cnt1 < this.soObj.vecItem.size(); cnt1++) {
            SalesOrderItemObject soItmObj = (SalesOrderItemObject) this.soObj.vecItem.get(cnt1);
            try {
                if (!soItmObj.valueadd1Userid.equals(this.productionWorker) && docInserted.equals("false")) {
                    Integer oldFlorist = soItmObj.valueadd1Userid;
                    UserObject floristOld = UserNut.getObject(oldFlorist);
                    UserObject floristNew = UserNut.getObject(this.productionWorker);
                    DocumentProcessingItemObject dpiObj = new DocumentProcessingItemObject();
                    dpiObj.processType = "UPDATE-ORDER";
                    dpiObj.category = "ASSIGN-FLORIST";
                    dpiObj.auditLevel = new Integer(1);
                    dpiObj.userid = userId;
                    dpiObj.docRef = SalesOrderIndexBean.TABLENAME;
                    dpiObj.docId = soItmObj.indexId;
                    dpiObj.description1 = DocumentProcessingItemNut.appendDocTrail("FLORIST", floristOld.userName, floristNew.userName, "");
                    dpiObj.time = TimeFormat.getTimestamp();
                    dpiObj.state = DocumentProcessingItemBean.STATE_CREATED;
                    dpiObj.status = DocumentProcessingItemBean.STATUS_ACTIVE;
                    DocumentProcessingItemNut.fnCreate(dpiObj);
                    docInserted = "true";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Log.printVerbose("for loop");
            Log.printVerbose(productionStatus);
            Log.printVerbose(productionWorker.toString());
            Log.printVerbose(prodPts.toString());
            Log.printVerbose(creaPts.toString());
            soItmObj.productionStatus = this.productionStatus;
            soItmObj.valueadd1Userid = this.productionWorker;
            soItmObj.valueadd2Userid = this.productionWorker;
            soItmObj.valueadd2Points = this.creaPts;
            SalesOrderItem soItmEJB = SalesOrderItemNut.getHandle(soItmObj.pkid);
            if (soItmEJB != null) {
                try {
                    soItmEJB.setObject(soItmObj);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        Log.printVerbose("End of For");
    }

    public void setTrip(Long tripPkid) {
        this.tripObj = DeliveryTripNut.getObject(tripPkid);
        if (tripObj != null) {
            for (int cnt1 = 0; cnt1 < this.soObj.vecItem.size(); cnt1++) {
                SalesOrderItemObject soItmObj = (SalesOrderItemObject) this.soObj.vecItem.get(cnt1);
                Vector vecTripLink = new Vector(DeliveryTripSOLinkNut.getObjectsBySalesOrderItem(soItmObj.pkid));
                for (int cnt2 = 0; cnt2 < vecTripLink.size(); cnt2++) {
                    DeliveryTripSOLinkObject tripLinkObj = (DeliveryTripSOLinkObject) vecTripLink.get(cnt2);
                    try {
                        DeliveryTripSOLink tripLinkEJB = DeliveryTripSOLinkNut.getHandle(tripLinkObj.pkid);
                        tripLinkEJB.remove();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                DeliveryTripSOLinkObject tripLinkObj = new DeliveryTripSOLinkObject();
                tripLinkObj.soItem = soItmObj.pkid;
                tripLinkObj.tripId = this.tripObj.pkid;
                tripLinkObj.timeExpectedStr = this.soObj.expDeliveryTime;
                tripLinkObj.timePlannedCollection = this.tripObj.timePlannedCollection;
                tripLinkObj.timePlannedArrival = tripLinkObj.timePlannedCollection;
                tripLinkObj.userTripDriver = tripObj.userTripDriver;
                tripLinkObj.userTripOrganizer = tripObj.userTripOrganizer;
                tripLinkObj.userTripCoordinator = tripObj.userTripCoordinator;
                try {
                    GeneralEntityIndexObject geiObj = GeneralEntityIndexNut.getObject(tripObj.userTripDriver);
                    tripLinkObj.deliveryName = geiObj.name;
                    tripLinkObj.deliveryPhone = geiObj.phoneMobile;
                    tripLinkObj.deliveryCompany = geiObj.name;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                tripLinkObj.currency = this.soObj.currency;
                tripLinkObj.deliveryBillAmount = new BigDecimal(0);
                tripLinkObj.deliveryBillOutstanding = tripLinkObj.deliveryBillAmount;
                tripLinkObj.remarks = soItmObj.remarks;
                tripLinkObj.instructions = soObj.deliveryPreferences;
                tripLinkObj.useridEdit = this.userId;
                String description = "";
                description += " TRIP-NO:" + tripLinkObj.tripId.toString();
                description += " DELIVERY-TIME:" + tripLinkObj.timeExpectedStr;
                description += " PLANNED-COLLECTION:" + tripLinkObj.timePlannedCollection.toString();
                description += " TRIP-ORGANIZER:" + UserNut.getUserName(tripObj.userTripOrganizer);
                description += " TRIP-COORDINATOR:" + UserNut.getUserName(tripObj.userTripOrganizer);
                description += " DRIVER:" + tripLinkObj.deliveryName;
                description += " DRIVER-PHONE:" + tripLinkObj.deliveryPhone;
                try {
                    DocumentProcessingItemObject dpiObj = new DocumentProcessingItemObject();
                    dpiObj.module = DocumentProcessingItemBean.MODULE_DISTRIBUTION;
                    dpiObj.processType = "UPDATE-ORDER";
                    dpiObj.category = "ORDER-ASSIGNED-TRIP";
                    dpiObj.auditLevel = new Integer(0);
                    dpiObj.processId = new Long(0);
                    dpiObj.userid = userId;
                    dpiObj.docRef = SalesOrderIndexBean.TABLENAME;
                    dpiObj.docId = soObj.pkid;
                    dpiObj.entityRef = CustAccountBean.TABLENAME;
                    dpiObj.entityId = soObj.senderKey1;
                    dpiObj.description1 = description;
                    dpiObj.description2 = "";
                    dpiObj.remarks = "";
                    dpiObj.time = TimeFormat.getTimestamp();
                    DocumentProcessingItem dpiEJB = DocumentProcessingItemNut.fnCreate(dpiObj);
                    DeliveryTripSOLink tripLinkEJB = DeliveryTripSOLinkNut.fnCreate(tripLinkObj);
                    if (tripLinkEJB == null) {
                        return;
                    }
                    SalesOrderItem soItemEJB = SalesOrderItemNut.getHandle(soItmObj.pkid);
                    soItmObj.deliveryStatus = SalesOrderItemBean.DELIVERY_STATUS_SCHEDULED;
                    soItemEJB.setDeliveryStatus(SalesOrderItemBean.DELIVERY_STATUS_SCHEDULED);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

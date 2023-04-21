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

public class CreateDeliveryTripForm extends java.lang.Object implements Serializable {

    DeliveryTripObject tripObj = null;

    Integer userId = null;

    TreeMap treeOrder = null;

    public CreateDeliveryTripForm(Integer userId) throws Exception {
        this.tripObj = new DeliveryTripObject();
        this.tripObj.timePlannedCollection = TimeFormat.getTimestamp();
        this.userId = userId;
        this.treeOrder = new TreeMap();
    }

    public DeliveryTripObject getTrip() {
        System.out.println("Trip Pkid : " + this.tripObj.pkid.toString());
        return this.tripObj;
    }

    public void setTripDetails(Timestamp tsPlannedCollection, Integer tripOrganizer, Integer tripCoordinator, Integer tripDriver, Integer tripHelper, String remarks) throws Exception {
        this.tripObj.timePlannedCollection = tsPlannedCollection;
        this.tripObj.userTripOrganizer = tripOrganizer;
        this.tripObj.userTripCoordinator = tripCoordinator;
        this.tripObj.userTripDriver = tripDriver;
        this.tripObj.userTripHelper = tripHelper;
        GeneralEntityIndexObject geiObj = GeneralEntityIndexNut.getObject(tripDriver);
        this.tripObj.deliveryName = geiObj.name;
        this.tripObj.deliveryPhone = geiObj.phoneMobile;
        this.tripObj.remarks = remarks;
    }

    public void setDispatchDetails(Integer dispatchController, String dispatchLocation, String dispatchCTL, String dispatchVehicleLoc, String dispatchDescription, Timestamp dispatchTime, String tripCheckinTime, String tripCheckoutTime) throws Exception {
        this.tripObj.dispatchController = dispatchController;
        this.tripObj.dispatchLocation = dispatchLocation;
        this.tripObj.locationCompletedOrder = dispatchCTL;
        this.tripObj.locationVehicle = dispatchVehicleLoc;
        this.tripObj.dispatchDescription = dispatchDescription;
        this.tripObj.dispatchTime = dispatchTime;
        this.tripObj.tripCheckinTime = tripCheckinTime;
        this.tripObj.tripCheckoutTime = tripCheckoutTime;
    }

    public TreeMap getTreeOrder() {
        return this.treeOrder;
    }

    public void addOrder(SalesOrderIndexObject soObj, Timestamp tsPlannedArrival, BigDecimal charges) {
        try {
            PerOrder perOrder = new PerOrder(soObj);
            perOrder.tsPlannedArrival = tsPlannedArrival;
            perOrder.bdCharges = charges;
            this.treeOrder.put(perOrder.salesOrder.pkid, perOrder);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void removeOrderFromTrip(Long orderId) {
        this.treeOrder.remove(orderId);
    }

    public synchronized DeliveryTripObject confirmAndSave() throws Exception {
        DeliveryTrip dTripEJB = DeliveryTripNut.fnCreate(this.tripObj);
        DeliveryTripObject bufTrip = this.tripObj;
        String orderExists = "";
        Vector vecTmpOrder = new Vector(this.treeOrder.values());
        for (int cnt1 = 0; cnt1 < vecTmpOrder.size(); cnt1++) {
            PerOrder perOrder = (PerOrder) vecTmpOrder.get(cnt1);
            for (int cnt2 = 0; cnt2 < perOrder.salesOrder.vecItem.size(); cnt2++) {
                SalesOrderItemObject soItmObj = (SalesOrderItemObject) perOrder.salesOrder.vecItem.get(cnt2);
                Vector vecLink = new Vector(DeliveryTripSOLinkNut.getObjectsBySalesOrderItem(soItmObj.pkid));
                for (int cnt3 = 0; cnt3 < vecLink.size(); cnt3++) {
                    DeliveryTripSOLinkObject dtslObj = (DeliveryTripSOLinkObject) vecLink.get(cnt3);
                    DeliveryTripSOLink tripLinkEJB = DeliveryTripSOLinkNut.getHandle(dtslObj.pkid);
                    if (tripLinkEJB != null) {
                        tripLinkEJB.remove();
                    } else {
                        continue;
                    }
                    SalesOrderItemObject soItemObj = SalesOrderItemNut.getObject(dtslObj.soItem);
                    SalesOrderItem soItemEJB = SalesOrderItemNut.getHandle(dtslObj.soItem);
                    if (soItemObj != null) {
                        soItemObj.deliveryStatus = SalesOrderItemBean.DELIVERY_STATUS_NONE;
                    }
                    if (soItemEJB != null) {
                        soItemEJB.setDeliveryStatus(SalesOrderItemBean.DELIVERY_STATUS_NONE);
                    }
                }
            }
        }
        EditDeliveryTripForm edtForm = new EditDeliveryTripForm(bufTrip.pkid, this.userId);
        Vector vecOrder = new Vector(this.treeOrder.values());
        for (int cnt1 = 0; cnt1 < vecOrder.size(); cnt1++) {
            PerOrder perOrder = (PerOrder) vecOrder.get(cnt1);
            for (int cnt2 = 0; cnt2 < perOrder.salesOrder.vecItem.size(); cnt2++) {
                SalesOrderItemObject soItmObj = (SalesOrderItemObject) perOrder.salesOrder.vecItem.get(cnt2);
                if (soItmObj.deliveryRequired) {
                    edtForm.addLink(soItmObj.pkid, perOrder.tsPlannedArrival, perOrder.bdCharges);
                }
            }
        }
        this.tripObj = new DeliveryTripObject();
        this.tripObj.timePlannedCollection = TimeFormat.getTimestamp();
        this.treeOrder.clear();
        return bufTrip;
    }

    public static class PerOrder {

        public SalesOrderIndexObject salesOrder;

        public Timestamp tsPlannedArrival;

        public BigDecimal bdCharges;

        public String uuid = "";

        public PerOrder(SalesOrderIndexObject soObj) throws Exception {
            this.salesOrder = soObj;
            GUIDGenerator guidGen = new GUIDGenerator();
            this.uuid = guidGen.getUUID();
            this.tsPlannedArrival = TimeFormat.getTimestamp();
            this.bdCharges = new BigDecimal(0);
        }

        public String getKey() {
            return this.uuid;
        }
    }
}

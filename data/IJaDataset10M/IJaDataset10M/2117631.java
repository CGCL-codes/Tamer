package com.vlee.ejb.customer;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.*;
import java.io.Serializable;
import com.vlee.util.*;

public class DeliveryTripObject implements Serializable {

    public Long pkid;

    public Long stmtNo;

    public Timestamp timeLastInform;

    public Timestamp timeCreate;

    public String timeExpectedStr;

    public Timestamp timeExpectedCollection;

    public Timestamp timeExpectedJourney;

    public Timestamp timeExpectedArrival;

    public Timestamp timePlannedCollection;

    public Timestamp timePlannedJourney;

    public Timestamp timePlannedArrival;

    public Timestamp timeActualCollection;

    public Timestamp timeActualJourney;

    public Timestamp timeActualArrival;

    public Integer userTripDriver;

    public Integer userTripOrganizer;

    public Integer userTripCoordinator;

    public Integer userTripHelper;

    public Integer deliveryUserid;

    public String deliveryName;

    public String deliveryPhone;

    public String deliveryCompany;

    public String currency;

    public String deliveryBillReference;

    public BigDecimal deliveryBillAmount;

    public BigDecimal deliveryBillOutstanding;

    public String deliveryBillPayTo;

    public Timestamp deliveryBillPaidAt;

    public String remarks;

    public String instructions;

    public String complaints;

    public String state = DeliveryTripBean.STATE_CREATED;

    public String status = DeliveryTripBean.STATUS_ACTIVE;

    public Integer useridEdit;

    public String displayFormat;

    public String docType;

    public Integer dispatchController;

    public String dispatchLocation;

    public String dispatchDescription;

    public String dispatchRemarks;

    public Timestamp dispatchTime;

    public String tripCheckinTime;

    public String tripCheckoutTime;

    public String locationVehicle;

    public String locationCompletedOrder;

    public String statusQc;

    public DeliveryTripObject() {
        this.pkid = new Long(0);
        this.stmtNo = new Long(0);
        this.timeLastInform = TimeFormat.getTimestamp();
        this.timeCreate = TimeFormat.getTimestamp();
        this.timeExpectedStr = "";
        this.timeExpectedCollection = TimeFormat.createTimestamp("0001-01-01");
        this.timeExpectedJourney = TimeFormat.createTimestamp("0001-01-01");
        this.timeExpectedArrival = TimeFormat.createTimestamp("0001-01-01");
        this.timePlannedCollection = TimeFormat.createTimestamp("0001-01-01");
        this.timePlannedJourney = TimeFormat.createTimestamp("0001-01-01");
        this.timePlannedArrival = TimeFormat.createTimestamp("0001-01-01");
        this.timeActualCollection = TimeFormat.createTimestamp("0001-01-01");
        this.timeActualJourney = TimeFormat.createTimestamp("0001-01-01");
        this.timeActualArrival = TimeFormat.createTimestamp("0001-01-01");
        this.userTripDriver = new Integer(0);
        this.userTripOrganizer = new Integer(0);
        this.userTripCoordinator = new Integer(0);
        this.userTripHelper = new Integer(0);
        this.deliveryUserid = new Integer(0);
        this.deliveryName = "";
        this.deliveryPhone = "";
        this.deliveryCompany = "";
        this.currency = "";
        this.deliveryBillReference = "";
        this.deliveryBillAmount = new BigDecimal(0);
        this.deliveryBillOutstanding = new BigDecimal(0);
        this.deliveryBillPayTo = "";
        this.deliveryBillPaidAt = TimeFormat.createTimestamp("0001-01-01");
        this.remarks = "";
        this.instructions = "";
        this.complaints = "";
        this.state = DeliveryTripBean.STATE_CREATED;
        this.status = DeliveryTripBean.STATUS_ACTIVE;
        this.useridEdit = new Integer(0);
        this.displayFormat = "";
        this.docType = "";
        this.dispatchController = new Integer(0);
        this.dispatchLocation = "";
        this.dispatchDescription = "";
        this.dispatchRemarks = "";
        this.dispatchTime = TimeFormat.getTimestamp();
        this.tripCheckinTime = "";
        this.tripCheckoutTime = "";
        this.locationVehicle = "";
        this.locationCompletedOrder = "";
        this.statusQc = "NOT OK";
    }
}

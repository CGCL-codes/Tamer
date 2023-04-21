package com.vlee.ejb.customer;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.*;
import java.io.Serializable;
import com.vlee.util.*;

public class DeliveryTripSOLinkObject implements Serializable {

    public Long pkid;

    public Long soItem;

    public Long tripId;

    public Integer sequence;

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

    public Integer deliveryUserid;

    public String deliveryName;

    public String deliveryPhone;

    public String deliveryCompany;

    public String currency;

    public String deliveryBillReference;

    public BigDecimal deliveryBillAmount;

    public BigDecimal deliveryBillOutstanding;

    public String remarks;

    public String instructions;

    public String complaints;

    public String state = DeliveryTripSOLinkBean.DELIVERY_STATUS_SCHEDULED;

    public String status = DeliveryTripSOLinkBean.STATUS_ACTIVE;

    public Integer useridEdit;

    public String displayFormat;

    public String docType;

    public DeliveryTripSOLinkObject() {
        this.pkid = new Long(0);
        this.soItem = new Long(0);
        this.tripId = new Long(0);
        this.sequence = new Integer(0);
        this.timeExpectedStr = "";
        this.timeExpectedCollection = TimeFormat.createTimestamp("0001-01-01");
        this.timeExpectedJourney = this.timeExpectedCollection;
        this.timeExpectedArrival = this.timeExpectedCollection;
        this.timePlannedCollection = this.timeExpectedCollection;
        this.timePlannedJourney = this.timeExpectedCollection;
        this.timePlannedArrival = this.timeExpectedCollection;
        this.timeActualCollection = this.timeExpectedCollection;
        ;
        this.timeActualJourney = this.timeExpectedCollection;
        this.timeActualArrival = this.timeExpectedCollection;
        this.userTripDriver = new Integer(0);
        this.userTripOrganizer = new Integer(0);
        this.userTripCoordinator = new Integer(0);
        this.deliveryUserid = new Integer(0);
        this.deliveryName = "";
        this.deliveryPhone = "";
        this.deliveryCompany = "";
        this.currency = "";
        this.deliveryBillReference = "";
        this.deliveryBillAmount = new BigDecimal(0);
        this.deliveryBillOutstanding = new BigDecimal(0);
        this.remarks = "";
        this.instructions = "";
        this.complaints = "";
        this.state = DeliveryTripSOLinkBean.DELIVERY_STATUS_SCHEDULED;
        this.status = DeliveryTripSOLinkBean.STATUS_ACTIVE;
        this.useridEdit = new Integer(0);
        this.displayFormat = "";
        this.docType = "";
    }
}

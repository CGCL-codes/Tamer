package com.vlee.ejb.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import com.vlee.util.TimeFormat;

public class RentalItemObject implements Serializable {

    private static final long serialVersionUID = 0;

    public Long pkid;

    public Long index_id;

    public String namespace;

    public String item_type;

    public Integer pccenter;

    public Integer pickup_branch;

    public Integer pickup_location;

    public Integer pickup_checklist;

    public String pickup_status;

    public BigDecimal pickup_qty;

    public Integer temporary_branch;

    public Integer temporary_location;

    public Integer dropoff_branch;

    public Integer dropoff_location;

    public String dropoff_status;

    public Integer dropoff_checklist;

    public Timestamp date_order;

    public Timestamp date_pickup;

    public Timestamp date_dropoff;

    public Integer duration_planned;

    public Integer duration_actual;

    public Integer item_id;

    public String item_code;

    public String item_name;

    public String item_description;

    public String serial;

    public boolean serialized;

    public String remarks;

    public String reference_no;

    public String description;

    public String currency;

    public BigDecimal qty_rent;

    public String qty_rent_uom;

    public BigDecimal quantity;

    public BigDecimal unit_price;

    public String deposit_option;

    public String deposit_status;

    public BigDecimal deposit_rate;

    public String charges_option;

    public String charges_status;

    public BigDecimal charges_rate;

    public String refund_option;

    public String refund_status;

    public BigDecimal refund_rate;

    public String extension_remarks;

    public Integer extension_day;

    public Integer extension_hour;

    public String faulty_remarks;

    public String state;

    public String status;

    public RentalItemObject() {
        this.pkid = new Long(0);
        this.index_id = new Long(0);
        this.namespace = "";
        this.item_type = "";
        this.pccenter = new Integer(0);
        this.pickup_branch = new Integer(0);
        this.pickup_location = new Integer(0);
        this.pickup_checklist = new Integer(0);
        this.pickup_status = "";
        this.pickup_qty = new BigDecimal(0);
        this.temporary_branch = new Integer(0);
        this.temporary_location = new Integer(0);
        this.dropoff_branch = new Integer(0);
        this.dropoff_location = new Integer(0);
        this.dropoff_status = "";
        this.dropoff_checklist = new Integer(0);
        this.date_order = TimeFormat.createTimestamp("0001-01-01");
        this.date_pickup = TimeFormat.createTimestamp("0001-01-01");
        this.date_dropoff = TimeFormat.createTimestamp("0001-01-01");
        this.duration_planned = new Integer(0);
        this.duration_actual = new Integer(0);
        this.item_id = new Integer(0);
        this.item_code = "";
        this.item_name = "";
        this.item_description = "";
        this.serial = "";
        this.serialized = false;
        this.remarks = "";
        this.reference_no = "";
        this.description = "";
        this.currency = "";
        this.qty_rent = new BigDecimal(0);
        this.qty_rent_uom = "";
        this.quantity = new BigDecimal(0);
        this.unit_price = new BigDecimal(0);
        this.deposit_option = "";
        this.deposit_status = "";
        this.deposit_rate = new BigDecimal(0);
        this.charges_option = "";
        this.charges_status = "";
        this.charges_rate = new BigDecimal(0);
        this.refund_option = "";
        this.refund_status = "";
        this.refund_rate = new BigDecimal(0);
        this.extension_remarks = "";
        this.extension_day = new Integer(0);
        this.extension_hour = new Integer(0);
        this.faulty_remarks = "";
        this.state = "";
        this.status = "";
    }

    public BigDecimal getDepositAmount() {
        return this.deposit_rate.multiply(this.quantity);
    }

    public BigDecimal getRentalAmount() {
        return this.quantity.multiply(this.charges_rate).multiply(this.qty_rent);
    }
}

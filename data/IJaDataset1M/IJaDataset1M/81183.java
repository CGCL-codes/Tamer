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

public class OrderComplaintListingForm extends java.lang.Object implements Serializable {

    public Timestamp dateFrom;

    public Timestamp dateTo;

    public String dateFilter = SalesOrderIndexBean.TIME_CREATE;

    public String orderBy = "";

    public Vector vecResult;

    public OrderComplaintListingForm() {
        this.dateTo = TimeFormat.getTimestamp();
        this.dateFrom = TimeFormat.add(this.dateTo, 0, 0, 0);
        this.dateFilter = SalesOrderIndexBean.TIME_CREATE;
        this.orderBy = " ORDER BY " + SalesOrderIndexBean.PKID;
        this.vecResult = new Vector();
    }

    public void setOrder(String buf) {
        this.orderBy = buf;
    }

    public void setDateRange(String dateFilter, Timestamp dFrom, Timestamp dTo) {
        this.dateFilter = dateFilter;
        this.dateFrom = dFrom;
        this.dateTo = dTo;
    }

    public String getDateFrom(String buf) {
        return TimeFormat.strDisplayDate(this.dateFrom);
    }

    public String getDateTo(String buf) {
        return TimeFormat.strDisplayDate(this.dateTo);
    }

    public void setDateFilter(String buf) {
        this.dateFilter = buf;
    }

    public String getDateFilter() {
        return this.dateFilter;
    }

    public Vector getList() {
        Timestamp dateToNextDay = TimeFormat.add(this.dateTo, 0, 0, 1);
        QueryObject query = new QueryObject(new String[] { this.dateFilter + " >= '" + getDateFrom("") + "' ", this.dateFilter + " < '" + TimeFormat.strDisplayDate(dateToNextDay) + "' ", " length(" + SalesOrderIndexBean.CUSTOMER_COMPLAINTS + ") > '1' " });
        query.setOrder(this.orderBy);
        this.vecResult = new Vector(SalesOrderIndexNut.getObjects(query));
        return this.vecResult;
    }

    public Vector getResult() {
        return this.vecResult;
    }
}

package com.vlee.ejb.accounting;

import java.math.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.BigDecimal;
import java.io.Serializable;
import com.vlee.util.*;

public class CashStmtObject extends java.lang.Object implements Serializable {

    public Integer pkid;

    public Integer prevStmt;

    public Integer nextStmt;

    public Integer cashbook;

    public Integer pcCenter;

    public String name;

    public String title;

    public String remarks;

    public String description;

    public Timestamp dateOpen;

    public BigDecimal balOpen;

    public Timestamp dateClose;

    public BigDecimal balClose;

    public String state;

    public String status;

    public Timestamp dateEdit;

    public Integer userIdEdit;

    public CashStmtObject() {
        this.pkid = new Integer(0);
        this.prevStmt = new Integer(0);
        this.nextStmt = new Integer(0);
        this.cashbook = new Integer(0);
        this.pcCenter = new Integer(0);
        this.name = "";
        this.title = "";
        this.remarks = "";
        this.description = "";
        this.dateOpen = TimeFormat.getTimestamp();
        this.balOpen = new BigDecimal(0);
        this.dateClose = TimeFormat.getTimestamp();
        this.balClose = new BigDecimal(0);
        this.state = CashStmtBean.STATE_CREATED;
        this.status = CashStmtBean.STATUS_ACTIVE;
        this.dateEdit = TimeFormat.getTimestamp();
        this.userIdEdit = new Integer(0);
    }

    public void setPkid(Integer pkid) {
        this.pkid = pkid;
    }

    public void setDatesToPreviousMonth() {
        this.dateOpen = TimeFormat.add(this.dateOpen, 0, -1, 0);
        this.dateOpen = TimeFormat.set(this.dateOpen, Calendar.DATE, 1);
        this.dateClose = TimeFormat.add(this.dateOpen, 0, 1, 0);
        this.dateClose = TimeFormat.add(this.dateClose, 0, 0, -1);
    }

    public void setNextStmt(CashStmtObject nextObj) {
        nextObj.prevStmt = this.pkid;
        nextObj.cashbook = this.cashbook;
        nextObj.pcCenter = this.pcCenter;
        nextObj.name = this.name;
        nextObj.dateOpen = TimeFormat.add(this.dateClose, 0, 0, 1);
        nextObj.balOpen = this.balClose;
        nextObj.dateClose = TimeFormat.add(nextObj.dateOpen, 0, 1, 0);
        nextObj.dateClose = TimeFormat.add(nextObj.dateClose, 0, 0, -1);
    }
}

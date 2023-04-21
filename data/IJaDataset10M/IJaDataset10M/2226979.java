package com.vlee.ejb.mrp;

import java.math.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.BigDecimal;
import java.io.Serializable;
import com.vlee.util.*;
import com.vlee.ejb.inventory.*;

public class PrdTrackIndexObject extends java.lang.Object implements Serializable {

    public Long pkid;

    public Long daily_counter;

    public String state;

    public Long wo_index_id;

    public Long st_index_id;

    public Integer branch;

    public Integer location;

    public Integer bom_item_id;

    public String bom_item_code;

    public String bom_item_name;

    public String bom_item_uom;

    public Integer qty_production;

    public Integer qty_good;

    public Integer qty_reject;

    public String remarks;

    public Timestamp time_created;

    public Timestamp last_update;

    public Integer userid_create;

    public Integer userid_edit;

    public Vector vecItem;

    public PrdTrackIndexObject() {
        this.pkid = new Long(0);
        this.daily_counter = new Long(0);
        this.state = PrdTrackIndexBean.STATE_CREATED;
        this.wo_index_id = new Long(0);
        this.st_index_id = new Long(0);
        this.branch = new Integer(0);
        this.location = new Integer(0);
        this.bom_item_id = new Integer(0);
        this.bom_item_code = "";
        this.bom_item_name = "";
        this.bom_item_uom = "";
        this.qty_production = new Integer(1);
        this.qty_good = new Integer(0);
        this.qty_reject = new Integer(0);
        this.remarks = "";
        this.time_created = TimeFormat.getTimestamp();
        this.last_update = TimeFormat.getTimestamp();
        this.userid_create = new Integer(0);
        this.userid_edit = new Integer(0);
        this.vecItem = new Vector();
    }
}

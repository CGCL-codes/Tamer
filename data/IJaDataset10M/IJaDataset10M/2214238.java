package com.vlee.ejb.inventory;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.*;
import java.io.Serializable;
import com.vlee.util.*;

public class BOMObject implements Serializable {

    public Integer pkid;

    public String namespace;

    public String linkType;

    public Integer parentItemId;

    public String parentItemCode;

    public Integer refRevisionNum;

    public String refRevisionTag;

    public Integer refVersionNum;

    public String refVersionTag;

    public String refDrawing;

    public Integer processId;

    public String processCode;

    public String processType;

    public String processOption;

    public Integer plantId;

    public String plantCode;

    public String plantType;

    public Integer machineId;

    public String machineCode;

    public String machineType;

    public Integer assemblylineId;

    public String assemblylineCode;

    public String assemblylineType;

    public Integer lvlThis;

    public Integer lvlTop;

    public Integer lvlBot;

    public Integer categoryid;

    public String category1;

    public String category2;

    public String category3;

    public String uom;

    public BigDecimal weight;

    public BigDecimal length;

    public BigDecimal width;

    public BigDecimal depth;

    public BigDecimal rebate1Pct;

    public BigDecimal rebate1Price;

    public Timestamp rebate1Start;

    public Timestamp rebate1End;

    public BigDecimal disc1Pct;

    public BigDecimal disc1Amount;

    public Timestamp disc1Start;

    public Timestamp disc1End;

    public String reserved1;

    public String reserved2;

    public String reserved3;

    public String state;

    public String status;

    public Timestamp lastupdate;

    public Integer useridEdit;

    public Vector vecLink;

    public String display_logic;

    public BOMObject() {
        this.pkid = new Integer(0);
        this.namespace = BOMBean.NS_DEFAULT;
        this.linkType = BOMBean.LT_DEFAULT;
        this.parentItemId = new Integer(0);
        this.parentItemCode = "";
        this.refRevisionNum = new Integer(0);
        this.refRevisionTag = "";
        this.refVersionNum = new Integer(0);
        this.refVersionTag = "";
        this.refDrawing = "";
        this.processId = new Integer(0);
        this.processCode = "";
        this.processType = "";
        this.processOption = "";
        this.plantId = new Integer(0);
        this.plantCode = "";
        this.plantType = "";
        this.machineId = new Integer(0);
        this.machineCode = "";
        this.machineType = "";
        this.assemblylineId = new Integer(0);
        this.assemblylineCode = "";
        this.assemblylineType = "";
        this.lvlThis = new Integer(0);
        this.lvlTop = new Integer(0);
        this.lvlBot = new Integer(0);
        this.categoryid = new Integer(0);
        this.category1 = "";
        this.category2 = "";
        this.category3 = "";
        this.uom = "";
        this.weight = new BigDecimal(0);
        this.length = new BigDecimal(0);
        this.width = new BigDecimal(0);
        this.depth = new BigDecimal(0);
        this.rebate1Pct = new BigDecimal(0);
        this.rebate1Price = new BigDecimal(0);
        this.rebate1Start = TimeFormat.createTimestamp("0001-01-01");
        this.rebate1End = TimeFormat.createTimestamp("0001-01-01");
        this.disc1Pct = new BigDecimal(0);
        this.disc1Amount = new BigDecimal(0);
        this.disc1Start = TimeFormat.createTimestamp("0001-01-01");
        this.disc1End = TimeFormat.createTimestamp("0001-01-01");
        this.reserved1 = "";
        this.reserved2 = "";
        this.reserved3 = "";
        this.state = BOMBean.STATE_CREATED;
        this.status = BOMBean.STATUS_ACTIVE;
        this.lastupdate = TimeFormat.getTimestamp();
        this.useridEdit = new Integer(0);
        this.vecLink = new Vector();
        this.display_logic = "";
    }

    public BigDecimal getPriceList() {
        BigDecimal buffer = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.vecLink.size(); cnt1++) {
            BOMLinkObject blObj = (BOMLinkObject) this.vecLink.get(cnt1);
            buffer = buffer.add(blObj.priceList.multiply(blObj.getRatio()));
        }
        return buffer;
    }

    public BigDecimal getPriceSale() {
        BigDecimal buffer = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.vecLink.size(); cnt1++) {
            BOMLinkObject blObj = (BOMLinkObject) this.vecLink.get(cnt1);
            buffer = buffer.add(blObj.priceSale.multiply(blObj.getRatio()));
        }
        return buffer;
    }

    public BigDecimal getPriceDisc1() {
        BigDecimal buffer = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.vecLink.size(); cnt1++) {
            BOMLinkObject blObj = (BOMLinkObject) this.vecLink.get(cnt1);
            buffer = buffer.add(blObj.priceDisc1);
        }
        return buffer;
    }

    public BigDecimal getQtyParts() {
        BigDecimal buffer = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.vecLink.size(); cnt1++) {
            BOMLinkObject blObj = (BOMLinkObject) this.vecLink.get(cnt1);
            buffer = buffer.add(blObj.getRatio());
        }
        return buffer;
    }

    public BigDecimal getPriceDisc2() {
        BigDecimal buffer = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.vecLink.size(); cnt1++) {
            BOMLinkObject blObj = (BOMLinkObject) this.vecLink.get(cnt1);
            buffer = buffer.add(blObj.priceDisc2);
        }
        return buffer;
    }

    public BigDecimal getRebate1Price() {
        BigDecimal buffer = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.vecLink.size(); cnt1++) {
            BOMLinkObject blObj = (BOMLinkObject) this.vecLink.get(cnt1);
            buffer = buffer.add(blObj.getRebateAmt());
        }
        return buffer;
    }

    public BigDecimal getWastage() {
        BigDecimal buffer = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.vecLink.size(); cnt1++) {
            BOMLinkObject blObj = (BOMLinkObject) this.vecLink.get(cnt1);
            buffer = buffer.add(blObj.qtyWastage);
        }
        return buffer;
    }
}

package com.vlee.bean.distribution;

import java.io.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.supplier.*;
import com.vlee.ejb.inventory.*;
import com.vlee.ejb.user.*;
import com.vlee.util.*;

public class FloristPrintDocForm extends java.lang.Object implements Serializable {

    public Integer userId = null;

    PurchaseOrderObject poObj = null;

    SalesOrderIndexObject soObj = null;

    public SuppAccountObject supplier = null;

    public Timestamp todayDate = TimeFormat.getTimestamp();

    public String outstationMyReference = "";

    public String outstationAttentionTo = "";

    public String outstationProductInfo = "";

    public BigDecimal outstationAmount = new BigDecimal(0);

    public String outstationRemarks = "";

    public String interfloraOrderNo = "";

    public String interfloraExeMem = "";

    public String interfloraFlowers1 = "";

    public String interfloraFlowers2 = "";

    public BigDecimal interfloraPrice = new BigDecimal(0);

    public String interfloraSendingNo = "170";

    public String interfloraSendingFlorist = "BLOOMING FLORIST SDN BHD, PJ, SELANGOR, MALAYSIA.";

    public String interfloraOtherInstructions = "";

    public FloristPrintDocForm(Integer userId) {
        this.userId = userId;
        this.todayDate = TimeFormat.getTimestamp();
    }

    public SuppAccountObject getSupplier() {
        return this.supplier;
    }

    public void setPO(Long pkid) {
        this.poObj = PurchaseOrderNut.getObject(pkid);
        if (this.poObj != null) {
            this.supplier = SuppAccountNut.getObject(this.poObj.mEntityKey);
            if (this.supplier != null) {
                this.outstationAttentionTo = this.supplier.getName();
            }
        }
    }

    public PurchaseOrderObject getPO() {
        return this.poObj;
    }

    public String getPOId(String buf) {
        if (this.poObj != null) {
            return this.poObj.mPkid.toString();
        }
        return buf;
    }

    public void setOrder(Long pkid) {
        if (pkid == null) {
            return;
        }
        this.soObj = SalesOrderIndexNut.getObject(pkid);
        if (this.soObj == null) {
            return;
        }
        this.outstationMyReference = pkid.toString();
        this.interfloraOrderNo = this.soObj.interfloraReference;
        this.interfloraExeMem = this.soObj.interfloraExeMem;
        this.interfloraFlowers1 = this.soObj.interfloraFlowers1;
        this.interfloraFlowers2 = this.soObj.interfloraFlowers2;
        this.interfloraPrice = this.soObj.interfloraPrice;
        if (this.soObj.interfloraSendingNo.length() > 1) {
            this.interfloraSendingNo = this.soObj.interfloraSendingNo;
        }
        this.interfloraOtherInstructions = this.soObj.interfloraOtherInstructions;
    }

    public String getOrderId(String buf) {
        if (this.soObj != null) {
            return this.soObj.pkid.toString();
        }
        return buf;
    }

    public SalesOrderIndexObject getOrder() {
        return this.soObj;
    }

    public boolean validOrderPO() {
        if (this.poObj == null) {
            return false;
        }
        if (this.soObj == null) {
            return false;
        }
        return true;
    }

    public void setOutstationDetails(String myReference, String attention, String productInfo, String remarks) {
        this.outstationMyReference = myReference;
        this.outstationAttentionTo = attention;
        this.outstationProductInfo = productInfo;
        this.outstationRemarks = remarks;
    }

    public void setInterfloraDetails(String interfloraOrderNo, String interfloraExeMem, String interfloraFlowers1, String interfloraFlowers2, String interfloraPrice, String interfloraSendingNo, String interfloraSendingFlorist, String interfloraOtherInstructions) {
        this.interfloraOrderNo = interfloraOrderNo;
        this.interfloraExeMem = interfloraExeMem;
        this.interfloraFlowers1 = interfloraFlowers1;
        this.interfloraFlowers2 = interfloraFlowers2;
        try {
            this.interfloraPrice = new BigDecimal(interfloraPrice);
        } catch (Exception ex) {
        }
        this.interfloraSendingNo = interfloraSendingNo;
        this.interfloraSendingFlorist = interfloraSendingFlorist;
        this.interfloraOtherInstructions = interfloraOtherInstructions;
        saveInterfloraInfo();
    }

    public void saveInterfloraInfo() {
        try {
            SalesOrderIndex orderEJB = SalesOrderIndexNut.getHandle(this.soObj.pkid);
            orderEJB.setInterfloraDetails(this.interfloraOrderNo, "", "", this.interfloraExeMem, this.interfloraFlowers1, this.interfloraFlowers2, this.interfloraPrice, this.interfloraSendingNo, this.interfloraSendingFlorist, this.interfloraOtherInstructions);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

package com.vlee.bean.customer;

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

public class CusBillingStatmentPrintBatch extends java.lang.Object implements Serializable {

    public BranchObject branch = null;

    Integer salesmanId = new Integer(0);

    Integer branchId = new Integer(0);

    String month = "";

    String filterCustIdFrom = "";

    String filterCustIdTo = "";

    Vector vecCustPkid = null;

    String agingType = "";

    String batchPrinting = "";

    private boolean filterSalesman = false;

    Timestamp tsMonth = TimeFormat.getTimestamp();

    Timestamp tsFrom = TimeFormat.getTimestamp();

    Timestamp tsTo = TimeFormat.getTimestamp();

    CustServiceCenterObject custSvcCtrObj = null;

    public TreeMap treeStmt = null;

    public TreeMap treeAging = null;

    public CustAccountObject custAccObj;

    public CusBillingStatmentPrintBatch() throws Exception {
        this.salesmanId = new Integer(0);
        this.branchId = new Integer(0);
        this.month = "";
        this.filterCustIdFrom = "";
        this.filterCustIdTo = "";
        this.vecCustPkid = new Vector();
        this.agingType = "";
        this.batchPrinting = "";
        this.custSvcCtrObj = null;
        this.tsMonth = TimeFormat.getTimestamp();
        this.tsFrom = TimeFormat.getTimestamp();
        this.tsTo = TimeFormat.getTimestamp();
        this.treeStmt = new TreeMap();
        this.treeAging = new TreeMap();
        this.filterSalesman = false;
    }

    public void reset() {
        this.salesmanId = new Integer(0);
        this.branchId = new Integer(0);
        this.month = "";
        this.filterCustIdFrom = "";
        this.filterCustIdTo = "";
        this.vecCustPkid = new Vector();
        this.agingType = "";
        this.batchPrinting = "";
        this.tsMonth = TimeFormat.getTimestamp();
        this.tsFrom = TimeFormat.getTimestamp();
        this.tsTo = TimeFormat.getTimestamp();
        this.treeStmt = new TreeMap();
        this.treeAging = new TreeMap();
        this.filterSalesman = false;
        this.custAccObj = null;
    }

    public String getSalesmanId(String buf) {
        if (this.salesmanId != null) {
            return this.salesmanId.toString();
        }
        return buf;
    }

    public Integer getSalesmanId() {
        return this.salesmanId;
    }

    public void setSalesmanId(Integer salesmanId) {
        this.salesmanId = salesmanId;
    }

    public boolean getFilterSalesman() {
        return this.filterSalesman;
    }

    public void setFilterSalesman(boolean buf) {
        this.filterSalesman = buf;
    }

    public Integer getBranch() {
        return this.branchId;
    }

    public void setBranch(Integer branchId) throws Exception {
        if (branchId == null) {
            this.branchId = null;
            return;
        }
        this.custSvcCtrObj = CustServiceCenterNut.getObject(branchId);
        if (this.custSvcCtrObj == null) {
            this.branchId = new Integer(0);
        } else {
            this.branchId = branchId;
        }
    }

    public String getMonth() {
        return this.month;
    }

    public void setMonth(String month) {
        this.month = month;
        this.tsMonth = TimeFormat.createTimestamp(month);
        this.tsFrom = TimeFormat.set(this.tsMonth, Calendar.DATE, 1);
        this.tsTo = TimeFormat.add(this.tsFrom, 0, 1, 0);
        this.tsTo = TimeFormat.add(this.tsTo, 0, 0, -1);
    }

    public String getFilterCustIdFrom() {
        return this.filterCustIdFrom;
    }

    public void setFilterCustIdFrom(String filterCustIdFrom) {
        this.filterCustIdFrom = filterCustIdFrom;
    }

    public String getFilterCustIdTo() {
        return this.filterCustIdTo;
    }

    public void setFilterCustIdTo(String filterCustIdTo) {
        this.filterCustIdTo = filterCustIdTo;
    }

    public Vector getCustId() {
        return this.vecCustPkid;
    }

    public void setCustId(String[] custId) throws Exception {
        if (custId != null) {
            this.vecCustPkid.clear();
            for (int cnt1 = 0; cnt1 < custId.length; cnt1++) {
                Integer iCustId = new Integer(custId[cnt1]);
                CustAccountObject custObj = CustAccountNut.getObject(iCustId);
                if (custObj != null) {
                    this.vecCustPkid.add(iCustId);
                } else {
                    throw new Exception(" Failed to set customer " + iCustId.toString());
                }
            }
        }
    }

    public String getAgingtype() {
        return this.agingType;
    }

    public void setAgingtype(String agingType) {
        this.agingType = agingType;
    }

    public String getBatchPrinting() {
        return this.batchPrinting;
    }

    public void setBatchPrinting(String batchPrinting) {
        this.batchPrinting = batchPrinting;
    }

    public CustAccountObject getCustomer() {
        return this.custAccObj;
    }

    public void setCustomer(String custPkid) throws Exception {
        this.custAccObj = null;
        if (!custPkid.equals("")) {
            try {
                this.custAccObj = CustAccountNut.getObject(new Integer(custPkid));
            } catch (Exception ex) {
                throw new Exception("Invalid Customer Account!");
            }
        }
    }

    public void generateReport() {
        this.treeStmt.clear();
        this.treeAging.clear();
        this.vecCustPkid.clear();
        Vector vecCustAccount = new Vector();
        String[] theCondition = new String[] {};
        if (this.filterSalesman == true) {
            String[] buffer = new String[] { CustAccountBean.SALESMAN + " = '" + this.salesmanId.toString() + "' " };
            theCondition = StringManup.append(theCondition, buffer);
        }
        if (!"".equals(this.filterCustIdFrom) && !"".equals(this.filterCustIdTo)) {
            String[] buffer = new String[] { CustAccountBean.NAME + " >= '" + this.filterCustIdFrom + "' ", CustAccountBean.NAME + " <= '" + this.filterCustIdTo + "' " };
            theCondition = StringManup.append(theCondition, buffer);
        }
        if (this.custAccObj != null) {
            String[] buffer = new String[] { CustAccountBean.PKID + " = '" + this.custAccObj.pkid + "' " };
            theCondition = StringManup.append(theCondition, buffer);
        }
        QueryObject query = new QueryObject(theCondition);
        query.setOrder(" ORDER BY " + CustAccountBean.NAME);
        vecCustAccount = new Vector(CustAccountNut.getObjects(query));
        System.out.println("vecCustAccount.size : " + vecCustAccount.size());
        for (int cnt1 = 0; cnt1 < vecCustAccount.size(); cnt1++) {
            CustAccountObject custObj = (CustAccountObject) vecCustAccount.get(cnt1);
            CustBillingStmtObject cbsObj = new CustBillingStmtObject(this.tsFrom, this.tsTo, this.custSvcCtrObj, custObj);
            cbsObj = CustAccountNut.fnGetBillingStmt(cbsObj);
            if (cbsObj != null) {
                System.out.println("cbsObj not null");
                System.out.println("cbsObj.bdPrevBalance : " + cbsObj.bdPrevBalance.toString());
                System.out.println("cbsObj not null");
                this.vecCustPkid.add(custObj.pkid);
                this.treeStmt.put(custObj.pkid, cbsObj);
                {
                    CustStmtOfAccountForm stmtForm;
                    if ("MONTH".equals(this.agingType)) {
                        stmtForm = new CustStmtOfAccountForm(CustStmtOfAccountForm.AGING_BY_MONTH);
                    } else {
                        stmtForm = new CustStmtOfAccountForm();
                    }
                    stmtForm.setCustomer(custObj);
                    if (this.custSvcCtrObj != null) {
                        stmtForm.setBranch(this.custSvcCtrObj.pkid);
                    } else {
                        stmtForm.setBranch(new Integer(0));
                    }
                    stmtForm.setStmtMonth(this.tsMonth.toString().substring(0, 4), this.tsMonth.toString().substring(5, 7));
                    stmtForm.generateStmt("");
                    this.treeAging.put(custObj.pkid, stmtForm);
                }
            }
        }
    }
}

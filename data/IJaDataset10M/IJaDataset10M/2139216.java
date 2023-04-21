package com.vlee.bean.reports;

import java.sql.*;
import java.text.SimpleDateFormat;
import javax.sql.*;
import java.util.*;
import java.io.Serializable;
import java.math.BigDecimal;
import com.vlee.util.*;
import com.vlee.bean.application.AppConfigManager;
import com.vlee.ejb.supplier.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.inventory.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.user.*;
import com.vlee.util.*;

public class TransactionAgingBySalesmanForm extends java.lang.Object implements Serializable {

    public static final String STATE_DRAFT = "draft";

    public static final String SAVED = "saved";

    private String state = null;

    public Timestamp date = null;

    private long DAY_LONG = 0;

    public BigDecimal totalOutstanding;

    public BigDecimal totalDocument;

    public BranchObject branch = null;

    public Timestamp dateFrom;

    public Timestamp dateTo;

    public Integer salesmanId;

    public boolean outstandingDocOnly = true;

    public boolean filterCreditLimitPercentage;

    public BigDecimal creditLimitPercentage;

    public Vector vecSalesman;

    public TreeMap treeAgingPeriod = null;

    public CustAccountObject custAccObj = null;

    public boolean filterByAccount = false;

    public TransactionAgingBySalesmanForm() {
        this.date = TimeFormat.getTimestamp();
        Timestamp tomorrow = TimeFormat.add(this.date, 0, 0, 1);
        this.date = TimeFormat.add(tomorrow, 0, 0, -1);
        this.DAY_LONG = tomorrow.getTime() - this.date.getTime();
        this.totalDocument = new BigDecimal(0);
        this.totalOutstanding = new BigDecimal(0);
        this.branch = null;
        this.dateTo = TimeFormat.getTimestamp();
        this.dateFrom = TimeFormat.createTimestamp("0001-01-01");
        this.salesmanId = new Integer(0);
        this.outstandingDocOnly = true;
        this.filterCreditLimitPercentage = false;
        this.creditLimitPercentage = new BigDecimal(0);
        this.vecSalesman = new Vector();
        this.treeAgingPeriod = AgingPeriodNut.getAgingPeriod(AgingPeriodBean.NS_OUTSTANDING_DOC);
        this.custAccObj = null;
        this.filterByAccount = false;
    }

    public void resetAging() {
        this.treeAgingPeriod = AgingPeriodNut.getAgingPeriod(AgingPeriodBean.NS_OUTSTANDING_DOC, dateTo);
        Vector vecAging = new Vector(this.treeAgingPeriod.values());
        for (int cnt1 = 0; cnt1 < vecAging.size(); cnt1++) {
            AgingPeriodObject apObj = (AgingPeriodObject) vecAging.get(cnt1);
            apObj.amount = new BigDecimal(0);
        }
    }

    public void reset() {
        this.branch = null;
        this.date = TimeFormat.getTimestamp();
        this.salesmanId = new Integer(0);
        this.outstandingDocOnly = true;
        this.filterCreditLimitPercentage = false;
        this.creditLimitPercentage = new BigDecimal(0);
        this.vecSalesman = new Vector();
        this.custAccObj = null;
        this.filterByAccount = false;
    }

    public boolean getValidBranch() {
        return (this.branch != null);
    }

    public BranchObject getBranch() {
        return this.branch;
    }

    public String getBranchId(String buf) {
        if (this.branch == null) {
            return buf;
        }
        return this.branch.pkid.toString();
    }

    public void setBranch(Integer ipcc) {
        if (ipcc != null) {
            this.branch = BranchNut.getObject(ipcc);
        } else {
            this.branch = null;
        }
    }

    public String getDateFrom(String buf) {
        return TimeFormat.strDisplayDate(this.dateFrom);
    }

    public String getDateTo(String buf) {
        return TimeFormat.strDisplayDate(this.dateTo);
    }

    public void setDateRange(String from, String to) {
        this.dateFrom = TimeFormat.createTimestamp(from);
        this.dateTo = TimeFormat.createTimestamp(to);
    }

    public Integer getSalesmanId() {
        return this.salesmanId;
    }

    public void setSalesmanId(Integer buf) {
        this.salesmanId = buf;
    }

    public CustAccountObject getCustAccount() {
        return this.custAccObj;
    }

    public void setCustAccount(CustAccountObject custObj) {
        if (custObj != null) {
            this.filterByAccount = true;
        }
        this.custAccObj = custObj;
    }

    public void setCustUser(CustUserObject custUserObj) {
        CustAccountObject custAcc = CustAccountNut.getObject(custUserObj.accId);
        if (custAcc != null) {
            this.custAccObj = custAcc;
        }
    }

    public boolean getFilterByAccount() {
        return this.filterByAccount;
    }

    public void setFilterByAccount(boolean buf) {
        this.filterByAccount = buf;
    }

    public boolean getFilterOutstandingDoc() {
        return this.outstandingDocOnly;
    }

    public void setFilterOutstandingDoc(boolean buf) {
        this.outstandingDocOnly = buf;
    }

    public boolean getFilterCreditLimitPercentage() {
        return this.filterCreditLimitPercentage;
    }

    public void setFilterCreditLimitPercentage(boolean buf) {
        this.filterCreditLimitPercentage = buf;
    }

    public BigDecimal getCreditLimitPercentage() {
        return this.creditLimitPercentage;
    }

    public void setCreditLimitPercentage(BigDecimal creditLimitPercentage) {
        this.creditLimitPercentage = creditLimitPercentage;
    }

    public void generateReport() {
        this.vecSalesman = new Vector();
        Timestamp dayNextTo = TimeFormat.add(this.dateTo, 0, 0, 1);
        String invCondition = InvoiceBean.STATUS + " = '" + InvoiceBean.STATUS_ACTIVE + "' " + " AND " + InvoiceBean.ENTITY_TABLE + " = '" + CustAccountBean.TABLENAME + "' ";
        if (this.salesmanId.intValue() != 0) {
            invCondition += " AND " + InvoiceBean.SALES_ID + " = '" + this.salesmanId.toString() + "' ";
        }
        {
            invCondition += " AND " + InvoiceBean.TIMEISSUED + " >= '" + TimeFormat.strDisplayDate(this.dateFrom) + "' ";
            invCondition += " AND " + InvoiceBean.TIMEISSUED + " < '" + TimeFormat.strDisplayDate(dayNextTo) + "' ";
        }
        if (getFilterOutstandingDoc()) {
            invCondition += " AND " + InvoiceBean.OUTSTANDING_AMT + " > '0' ";
        }
        if (this.branch != null) {
            invCondition += " AND " + InvoiceBean.CUST_SVCCTR_ID + " = '" + this.branch.pkid.toString() + "' ";
        }
        if (this.custAccObj != null && getFilterByAccount() == true) {
            invCondition += " AND " + InvoiceBean.ENTITY_KEY + " = '" + this.custAccObj.pkid.toString() + "' ";
        }
        QueryObject query = new QueryObject(new String[] { invCondition });
        query.setOrder(" ORDER BY " + InvoiceBean.PKID);
        Vector vecInvoice = new Vector(InvoiceNut.getObjects(query));
        Vector vecCM = CreditMemoIndexNut.getCMForTraxAgingReportBySalesman(this.dateFrom, dayNextTo, getFilterOutstandingDoc(), this.branch, this.salesmanId, this.custAccObj, getFilterByAccount());
        Vector vecDeposit = OfficialReceiptNut.getRctForTraxAgingReportBySalesman(this.dateFrom, dayNextTo, this.branch, this.salesmanId, this.custAccObj, getFilterByAccount());
        TreeMap treeSalesman = new TreeMap();
        for (int cnt1 = 0; cnt1 < vecInvoice.size(); cnt1++) {
            InvoiceObject cinvObj = (InvoiceObject) vecInvoice.get(cnt1);
            PerSalesman perSalesmanTest = (PerSalesman) treeSalesman.get(cinvObj.salesId);
            if (perSalesmanTest == null) {
                perSalesmanTest = new PerSalesman();
                perSalesmanTest.salesmanId = cinvObj.salesId;
                perSalesmanTest.salesmanName = UserNut.getUserName(cinvObj.salesId);
                System.out.println("perSalesmanTest.salesmanName" + perSalesmanTest.salesmanName);
                treeSalesman.put(cinvObj.salesId, perSalesmanTest);
            }
            PerSalesman.PerCustomer perCustomer = null;
            for (int cnt2 = 0; cnt2 < perSalesmanTest.vecPerCustomer.size(); cnt2++) {
                PerSalesman.PerCustomer perCustomerBuf = (PerSalesman.PerCustomer) perSalesmanTest.vecPerCustomer.get(cnt2);
                if (perCustomerBuf.custObj.pkid.intValue() == cinvObj.mEntityKey.intValue()) {
                    perCustomer = perCustomerBuf;
                    continue;
                }
            }
            if (perCustomer == null) {
                perCustomer = new PerSalesman.PerCustomer();
                CustAccountObject custObj = CustAccountNut.getObject(cinvObj.mEntityKey);
                perCustomer.customer = custObj.pkid.intValue();
                perCustomer.custObj = custObj;
                System.out.println("perCustomer.customer" + perCustomer.customer);
                perSalesmanTest.vecPerCustomer.add(perCustomer);
            }
            PerSalesman.PerCustomer.PerDocument perDoc = new PerSalesman.PerCustomer.PerDocument();
            perDoc.docRef = InvoiceBean.TABLENAME;
            perDoc.pkid = cinvObj.mPkid.longValue();
            perDoc.stmtNo = cinvObj.mStmtNumber.longValue();
            perDoc.documentDate = cinvObj.mTimeIssued;
            perDoc.amount = cinvObj.mTotalAmt;
            perDoc.outstanding = cinvObj.mOutstandingAmt;
            perDoc.pic = cinvObj.mUserIdUpdate;
            perDoc.dateTo = this.dateTo;
            perCustomer.vecDocument.add(perDoc);
        }
        for (int cnt1 = 0; cnt1 < vecCM.size(); cnt1++) {
            RptRow rptRow = (RptRow) vecCM.get(cnt1);
            PerSalesman perSalesmanTest = (PerSalesman) treeSalesman.get(rptRow.salesmanId);
            if (perSalesmanTest == null) {
                perSalesmanTest = new PerSalesman();
                perSalesmanTest.salesmanId = rptRow.salesmanId;
                perSalesmanTest.salesmanName = UserNut.getUserName(rptRow.salesmanId);
                System.out.println("perSalesmanTest.salesmanName" + perSalesmanTest.salesmanName);
                treeSalesman.put(rptRow.salesmanId, perSalesmanTest);
            }
            PerSalesman.PerCustomer perCustomer = null;
            for (int cnt2 = 0; cnt2 < perSalesmanTest.vecPerCustomer.size(); cnt2++) {
                PerSalesman.PerCustomer perCustomerBuf = (PerSalesman.PerCustomer) perSalesmanTest.vecPerCustomer.get(cnt2);
                if (perCustomerBuf.custObj.pkid.intValue() == rptRow.custId.intValue()) {
                    perCustomer = perCustomerBuf;
                    continue;
                }
            }
            if (perCustomer == null) {
                perCustomer = new PerSalesman.PerCustomer();
                CustAccountObject custObj = CustAccountNut.getObject(rptRow.custId);
                perCustomer.customer = custObj.pkid.intValue();
                perCustomer.custObj = custObj;
                System.out.println("perCustomer.customer" + perCustomer.customer);
                perSalesmanTest.vecPerCustomer.add(perCustomer);
            }
            PerSalesman.PerCustomer.PerDocument perDoc = new PerSalesman.PerCustomer.PerDocument();
            perDoc.docRef = CreditMemoIndexBean.TABLENAME;
            perDoc.pkid = rptRow.docId.longValue();
            perDoc.stmtNo = rptRow.docId.longValue();
            perDoc.documentDate = rptRow.docCreateDate;
            perDoc.amount = rptRow.totalAmt;
            perDoc.outstanding = rptRow.bal;
            perDoc.pic = rptRow.docUserIdCrate;
            perDoc.dateTo = this.dateTo;
            perCustomer.vecDocument.add(perDoc);
        }
        for (int cnt1 = 0; cnt1 < vecDeposit.size(); cnt1++) {
            RptRow rptRow = (RptRow) vecDeposit.get(cnt1);
            PerSalesman perSalesmanTest = (PerSalesman) treeSalesman.get(rptRow.salesmanId);
            if (perSalesmanTest == null) {
                perSalesmanTest = new PerSalesman();
                perSalesmanTest.salesmanId = rptRow.salesmanId;
                perSalesmanTest.salesmanName = UserNut.getUserName(rptRow.salesmanId);
                System.out.println("perSalesmanTest.salesmanName" + perSalesmanTest.salesmanName);
                treeSalesman.put(rptRow.salesmanId, perSalesmanTest);
            }
            PerSalesman.PerCustomer perCustomer = null;
            for (int cnt2 = 0; cnt2 < perSalesmanTest.vecPerCustomer.size(); cnt2++) {
                PerSalesman.PerCustomer perCustomerBuf = (PerSalesman.PerCustomer) perSalesmanTest.vecPerCustomer.get(cnt2);
                if (perCustomerBuf.custObj.pkid.intValue() == rptRow.custId.intValue()) {
                    perCustomer = perCustomerBuf;
                    continue;
                }
            }
            if (perCustomer == null) {
                perCustomer = new PerSalesman.PerCustomer();
                CustAccountObject custObj = CustAccountNut.getObject(rptRow.custId);
                perCustomer.customer = custObj.pkid.intValue();
                perCustomer.custObj = custObj;
                System.out.println("perCustomer.customer" + perCustomer.customer);
                perSalesmanTest.vecPerCustomer.add(perCustomer);
            }
            PerSalesman.PerCustomer.PerDocument perDoc = new PerSalesman.PerCustomer.PerDocument();
            perDoc.docRef = OfficialReceiptBean.TABLENAME;
            perDoc.pkid = rptRow.docId.longValue();
            perDoc.stmtNo = rptRow.docId.longValue();
            perDoc.documentDate = rptRow.docCreateDate;
            perDoc.amount = rptRow.totalAmt.negate();
            perDoc.outstanding = rptRow.bal.negate();
            perDoc.pic = rptRow.docUserIdCrate;
            perDoc.dateTo = this.dateTo;
            perCustomer.vecDocument.add(perDoc);
        }
        this.vecSalesman = new Vector(treeSalesman.values());
        treeSalesman.clear();
        for (int cnt1 = 0; cnt1 < this.vecSalesman.size(); cnt1++) {
            PerSalesman perSalesman = (PerSalesman) this.vecSalesman.get(cnt1);
            treeSalesman.put(perSalesman.salesmanName, perSalesman);
            perSalesman.sortCustomer();
        }
        this.vecSalesman = new Vector(treeSalesman.values());
        calculateValues();
        Vector vecTmpSalesman = new Vector();
        if (this.filterCreditLimitPercentage && this.creditLimitPercentage.compareTo(new BigDecimal(0)) > 0) {
            for (int cnt1 = 0; cnt1 < this.vecSalesman.size(); cnt1++) {
                PerSalesman perSalesman = (PerSalesman) this.vecSalesman.get(cnt1);
                Vector vecTmp = new Vector();
                for (int cnt2 = 0; cnt2 < perSalesman.vecPerCustomer.size(); cnt2++) {
                    PerSalesman.PerCustomer perCustomer = (PerSalesman.PerCustomer) perSalesman.vecPerCustomer.get(cnt2);
                    BigDecimal creditLimitUsage = new BigDecimal(0);
                    CustAccountObject custObj = perCustomer.custObj;
                    System.out.println("this.creditLimitPercentage : " + this.creditLimitPercentage.toString());
                    System.out.println("perCustomer.totalOutstanding : " + perCustomer.totalOutstanding.toString());
                    System.out.println("custObj.creditLimit : " + custObj.creditLimit.toString());
                    try {
                        creditLimitUsage = perCustomer.totalOutstanding.divide(custObj.creditLimit, 2, BigDecimal.ROUND_UP);
                        creditLimitUsage = creditLimitUsage.multiply(new BigDecimal(100));
                    } catch (Exception ex) {
                        System.out.println("Exception : " + ex.toString());
                    }
                    System.out.println("creditLimitUsage : " + creditLimitUsage.toString());
                    if (creditLimitUsage.compareTo(this.creditLimitPercentage) >= 0) {
                        vecTmp.add(perCustomer);
                    }
                }
                perSalesman.vecPerCustomer = vecTmp;
                if (perSalesman.vecPerCustomer.size() != 0) {
                    vecTmpSalesman.add(perSalesman);
                }
            }
            this.vecSalesman = vecTmpSalesman;
            calculateValues();
        }
    }

    private void calculateValues() {
        this.totalOutstanding = new BigDecimal(0);
        this.totalDocument = new BigDecimal(0);
        resetAging();
        for (int cnt1 = 0; cnt1 < this.vecSalesman.size(); cnt1++) {
            PerSalesman perSalesman = (PerSalesman) this.vecSalesman.get(cnt1);
            perSalesman.totalDocument = new BigDecimal(0);
            perSalesman.totalOutstanding = new BigDecimal(0);
            for (int cnt2 = 0; cnt2 < perSalesman.vecPerCustomer.size(); cnt2++) {
                PerSalesman.PerCustomer perCustomer = (PerSalesman.PerCustomer) perSalesman.vecPerCustomer.get(cnt2);
                perCustomer.totalDocument = new BigDecimal(0);
                perCustomer.totalOutstanding = new BigDecimal(0);
                for (int cnt3 = 0; cnt3 < perCustomer.vecDocument.size(); cnt3++) {
                    PerSalesman.PerCustomer.PerDocument pInv = (PerSalesman.PerCustomer.PerDocument) perCustomer.vecDocument.get(cnt3);
                    String strAgingPeriodType = AppConfigManager.getProperty("CUSTOMER-STATEMENT-AGING-PERIOD-TYPE-OPTION");
                    if (strAgingPeriodType.equals("MONTH")) {
                        SimpleDateFormat formatterMonth = new SimpleDateFormat("MMM");
                        long lTime = pInv.documentDate.getTime();
                        java.util.Date bufDate = new java.util.Date(lTime);
                        String strMonthName = formatterMonth.format(bufDate);
                        int intTodayYear = this.dateTo.getYear();
                        int intTodayMonth = this.dateTo.getMonth();
                        int intTodayTotalMonth = (intTodayYear * 12) + intTodayMonth;
                        int intDocYear = pInv.documentDate.getYear();
                        int intDocMonth = pInv.documentDate.getMonth();
                        int intDocTotalMonth = (intDocYear * 12) + intDocMonth;
                        int intDifference = intTodayTotalMonth - intDocTotalMonth;
                        Log.printVerbose("----NEW DOCUMENT YO----");
                        Log.printVerbose("pInv.docRef: " + pInv.docRef);
                        Log.printVerbose("pInv.amount: " + pInv.amount);
                        Log.printVerbose("pInv.documentDate: " + pInv.documentDate);
                        Log.printVerbose("this.date: " + this.date);
                        Log.printVerbose("strMonthName: " + strMonthName);
                        Vector vecAgingPeriod = new Vector(treeAgingPeriod.values());
                        for (int cnt4 = 0; cnt4 < vecAgingPeriod.size(); cnt4++) {
                            AgingPeriodObject apObj = (AgingPeriodObject) vecAgingPeriod.get(cnt4);
                            if (strMonthName.equals(apObj.monthName)) {
                                if (intDifference < 6) apObj.amount = apObj.amount.add(pInv.outstanding);
                            } else if ("> 6 months".equals(apObj.monthName)) {
                                if (intDifference >= 6) apObj.amount = apObj.amount.add(pInv.outstanding);
                            }
                        }
                    } else {
                        Timestamp invDate = pInv.documentDate;
                        long count = (this.date.getTime() - invDate.getTime()) / DAY_LONG;
                        pInv.nDays = new Integer(TimeFormat.dayDiffNoGetTime(invDate, this.date));
                        Log.printVerbose("----NEW DOCUMENT YO----");
                        Log.printVerbose("pInv.docRef: " + pInv.docRef);
                        Log.printVerbose("pInv.amount: " + pInv.amount);
                        Log.printVerbose("pInv.documentDate: " + pInv.documentDate);
                        Log.printVerbose("this.date: " + this.date);
                        Log.printVerbose("invDate: " + invDate);
                        Log.printVerbose("DAY_LONG: " + DAY_LONG);
                        Log.printVerbose("count: " + count);
                        Log.printVerbose("dayDiff: " + TimeFormat.dayDiffNoGetTime(invDate, this.date));
                        Vector vecAgingPeriod = new Vector(treeAgingPeriod.values());
                        for (int cnt4 = 0; cnt4 < vecAgingPeriod.size(); cnt4++) {
                            AgingPeriodObject apObj = (AgingPeriodObject) vecAgingPeriod.get(cnt4);
                            if (pInv.nDays.intValue() >= apObj.durationStart.intValue() && pInv.nDays.intValue() <= apObj.durationEnd.intValue()) {
                                apObj.amount = apObj.amount.add(pInv.outstanding);
                            }
                        }
                    }
                    perCustomer.totalDocument = perCustomer.totalDocument.add(pInv.amount);
                    perCustomer.totalOutstanding = perCustomer.totalOutstanding.add(pInv.outstanding);
                    perSalesman.totalDocument = perSalesman.totalDocument.add(pInv.amount);
                    perSalesman.totalOutstanding = perSalesman.totalOutstanding.add(pInv.outstanding);
                    this.totalDocument = this.totalDocument.add(pInv.amount);
                    this.totalOutstanding = this.totalOutstanding.add(pInv.outstanding);
                }
            }
        }
    }

    public BigDecimal getAgingAmount(CustAccountObject custObj) {
        BigDecimal total = new BigDecimal(0);
        Vector vecAgingPeriod = new Vector(treeAgingPeriod.values());
        for (int cnt1 = 0; cnt1 < vecAgingPeriod.size(); cnt1++) {
            AgingPeriodObject apObj = (AgingPeriodObject) vecAgingPeriod.get(cnt1);
            total = total.add(getAgingAmount(custObj, apObj));
        }
        return total;
    }

    public BigDecimal getAgingAmount(CustAccountObject custObj, AgingPeriodObject apObj) {
        BigDecimal total = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.vecSalesman.size(); cnt1++) {
            PerSalesman perSalesman = (PerSalesman) this.vecSalesman.get(cnt1);
            for (int cnt2 = 0; cnt2 < perSalesman.vecPerCustomer.size(); cnt2++) {
                PerSalesman.PerCustomer perCustomer = (PerSalesman.PerCustomer) perSalesman.vecPerCustomer.get(cnt2);
                if (perCustomer.custObj.pkid.intValue() == custObj.pkid.intValue()) {
                    total = total.add(perCustomer.getAgingAmount(apObj));
                }
            }
        }
        return total;
    }

    public BigDecimal getDocAmount(CustAccountObject custObj) {
        BigDecimal total = new BigDecimal(0);
        for (int cnt1 = 0; cnt1 < this.vecSalesman.size(); cnt1++) {
            PerSalesman perSalesman = (PerSalesman) this.vecSalesman.get(cnt1);
            for (int cnt2 = 0; cnt2 < perSalesman.vecPerCustomer.size(); cnt2++) {
                PerSalesman.PerCustomer perCustomer = (PerSalesman.PerCustomer) perSalesman.vecPerCustomer.get(cnt2);
                if (perCustomer.custObj.pkid.intValue() == custObj.pkid.intValue()) {
                    total = total.add(perCustomer.getDocAmount());
                }
            }
        }
        return total;
    }

    public static class RptRow {

        public Integer salesmanId;

        public Integer custId;

        public Long docId;

        public Timestamp docCreateDate;

        public BigDecimal totalAmt;

        public BigDecimal bal;

        public Integer docUserIdCrate;

        public RptRow() {
            this.salesmanId = new Integer(0);
            this.custId = new Integer(0);
            this.docId = new Long(0);
            this.docCreateDate = TimeFormat.getTimestamp();
            this.totalAmt = new BigDecimal(0);
            this.bal = new BigDecimal(0);
            this.docUserIdCrate = new Integer(0);
        }
    }

    public static class PerSalesman {

        public Timestamp date;

        public BigDecimal totalDocument;

        public BigDecimal totalOutstanding;

        public Integer salesmanId;

        public String salesmanName;

        public Vector vecPerCustomer = null;

        public PerSalesman() {
            this.date = TimeFormat.getTimestamp();
            this.totalDocument = new BigDecimal(0);
            this.totalOutstanding = new BigDecimal(0);
            this.salesmanId = new Integer(0);
            this.salesmanName = "";
            this.vecPerCustomer = new Vector();
        }

        public BigDecimal getAgingAmount(AgingPeriodObject apObj) {
            BigDecimal totalAging = new BigDecimal(0);
            for (int cnt1 = 0; cnt1 < this.vecPerCustomer.size(); cnt1++) {
                PerCustomer perCustomer = (PerCustomer) this.vecPerCustomer.get(cnt1);
                totalAging = totalAging.add(perCustomer.getAgingAmount(apObj));
            }
            return totalAging;
        }

        public void sortCustomer() {
            TreeMap treeCustomer = new TreeMap();
            for (int cnt1 = 0; cnt1 < vecPerCustomer.size(); cnt1++) {
                PerCustomer perCustomer = (PerCustomer) this.vecPerCustomer.get(cnt1);
                treeCustomer.put(perCustomer.custObj.name + perCustomer.custObj.pkid, perCustomer);
            }
            this.vecPerCustomer = new Vector(treeCustomer.values());
        }

        public static class PerCustomer {

            public int customer;

            public CustAccountObject custObj;

            public BigDecimal totalDocument;

            public BigDecimal totalOutstanding;

            public Vector vecDocument;

            public PerCustomer() {
                this.customer = 0;
                this.custObj = null;
                this.totalDocument = new BigDecimal(0);
                this.totalOutstanding = new BigDecimal(0);
                this.vecDocument = new Vector();
            }

            public BigDecimal getAgingAmount(AgingPeriodObject apObj) {
                BigDecimal totalAging = new BigDecimal(0);
                for (int cnt1 = 0; cnt1 < this.vecDocument.size(); cnt1++) {
                    PerDocument perDoc = (PerDocument) this.vecDocument.get(cnt1);
                    totalAging = totalAging.add(perDoc.getAgingAmount(apObj));
                }
                return totalAging;
            }

            public BigDecimal getDocAmount() {
                BigDecimal total = new BigDecimal(0);
                for (int cnt1 = 0; cnt1 < this.vecDocument.size(); cnt1++) {
                    PerDocument perDoc = (PerDocument) this.vecDocument.get(cnt1);
                    total = total.add(perDoc.amount);
                }
                return total;
            }

            public static class PerDocument {

                public String docRef;

                public long pkid;

                public long stmtNo;

                public Timestamp documentDate;

                public Integer nDays;

                public BigDecimal amount;

                public BigDecimal outstanding;

                public BigDecimal postDatedChequeAmt;

                public Integer pic;

                public Vector vecDocLink;

                public Timestamp dateTo;

                public PerDocument() {
                    this.docRef = "";
                    this.pkid = 0;
                    this.stmtNo = 0;
                    this.documentDate = TimeFormat.getTimestamp();
                    this.nDays = new Integer(0);
                    this.amount = new BigDecimal(0);
                    this.outstanding = new BigDecimal(0);
                    this.postDatedChequeAmt = new BigDecimal(0);
                    this.pic = new Integer(0);
                    this.vecDocLink = new Vector();
                    this.dateTo = TimeFormat.getTimestamp();
                }

                public BigDecimal getAgingAmount(AgingPeriodObject apObj) {
                    BigDecimal totalAging = new BigDecimal(0);
                    String strAgingPeriodType = AppConfigManager.getProperty("CUSTOMER-STATEMENT-AGING-PERIOD-TYPE-OPTION");
                    if (strAgingPeriodType.equals("MONTH")) {
                        SimpleDateFormat formatterMonth = new SimpleDateFormat("MMM");
                        long lMonth = this.documentDate.getTime();
                        java.util.Date bufDateMonth = new java.util.Date(lMonth);
                        String strMonthName = formatterMonth.format(bufDateMonth);
                        int intTodayYear = dateTo.getYear();
                        int intTodayMonth = dateTo.getMonth();
                        int intTodayTotalMonth = (intTodayYear * 12) + intTodayMonth;
                        int intDocYear = this.documentDate.getYear();
                        int intDocMonth = this.documentDate.getMonth();
                        int intDocTotalMonth = (intDocYear * 12) + intDocMonth;
                        int intDifference = intTodayTotalMonth - intDocTotalMonth;
                        if ("> 6 months".equals(apObj.monthName)) {
                            if (intDifference >= 6) {
                                totalAging = totalAging.add(this.outstanding);
                            }
                        } else if (strMonthName.equals(apObj.monthName)) {
                            if (intDifference < 6) {
                                totalAging = totalAging.add(this.outstanding);
                            }
                        }
                    } else {
                        if (this.nDays.intValue() >= apObj.durationStart.intValue() && this.nDays.intValue() <= apObj.durationEnd.intValue()) {
                            totalAging = totalAging.add(this.outstanding);
                        }
                    }
                    return totalAging;
                }
            }
        }
    }
}

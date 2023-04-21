package com.vlee.bean.reports;

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

public class MultiBranchDailySalesPurchasesCollectionReport extends java.lang.Object implements Serializable {

    public Integer userId;

    private Timestamp dateFrom;

    private Timestamp dateTo;

    private Timestamp generateTime = TimeFormat.getTimestamp();

    public Result result;

    public TreeMap treeSelectedBranches;

    public MultiBranchDailySalesPurchasesCollectionReport(Integer userId) throws Exception {
        this.dateTo = TimeFormat.getTimestamp();
        this.dateFrom = TimeFormat.set(this.dateTo, Calendar.DATE, 1);
        this.treeSelectedBranches = new TreeMap();
        this.result = null;
        this.userId = userId;
    }

    public void setSelectedBranches(Vector vecBuffer) {
        this.treeSelectedBranches.clear();
        for (int cnt1 = 0; cnt1 < vecBuffer.size(); cnt1++) {
            BranchObject branchObj = (BranchObject) vecBuffer.get(cnt1);
            this.treeSelectedBranches.put(branchObj.code, branchObj);
            Log.printVerbose("check point 2: " + cnt1);
            Log.printVerbose("the branch code: " + branchObj.code);
            Log.printVerbose("the branch description: " + branchObj.description);
        }
    }

    public String getSelectedBranchesCSV() {
        String csvString = "";
        Vector vecSelectedBranches = new Vector(this.treeSelectedBranches.values());
        for (int cnt1 = 0; cnt1 < vecSelectedBranches.size(); cnt1++) {
            BranchObject branchObj = (BranchObject) vecSelectedBranches.get(cnt1);
            if (cnt1 > 0) {
                csvString += ",";
            }
            csvString += branchObj.pkid.toString();
        }
        return csvString;
    }

    public TreeMap getSelectedBranches() {
        return this.treeSelectedBranches;
    }

    public void setDateRange(String dateFrom, String dateTo) {
        this.dateFrom = TimeFormat.createTimestamp(dateFrom);
        this.dateTo = TimeFormat.createTimestamp(dateTo);
    }

    public Timestamp getDateFrom() {
        return this.dateFrom;
    }

    public Timestamp getDateTo() {
        return this.dateTo;
    }

    public String getDateFrom(String str) {
        return TimeFormat.strDisplayDate(this.dateFrom);
    }

    public String getDateTo(String str) {
        return TimeFormat.strDisplayDate(this.dateTo);
    }

    public void generateReport(String buf) {
        Vector vecSelectedBranches = new Vector(this.treeSelectedBranches.values());
        if (vecSelectedBranches.size() == 0) {
            return;
        }
        this.result = StockDeltaNut.getDailySalesPurchasesCollection(this.treeSelectedBranches, this.dateFrom, this.dateTo);
    }

    public void setDate(Timestamp dateFrom, Timestamp dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public void setDate(Timestamp newDate) {
        this.dateFrom = newDate;
        this.dateTo = newDate;
    }

    public Result getResult() {
        return this.result;
    }

    public static class Result {

        public TreeMap treeDay;

        public TreeMap listBranch;

        public Result() {
            this.treeDay = new TreeMap();
            this.listBranch = new TreeMap();
        }

        public BigDecimal getNetSales(Integer iBranch) {
            BigDecimal total = new BigDecimal(0);
            Vector vecDay = new Vector(this.treeDay.values());
            for (int cnt1 = 0; cnt1 < vecDay.size(); cnt1++) {
                PerDay perDay = (PerDay) vecDay.get(cnt1);
                if (perDay != null) {
                    total = total.add(perDay.getNetSales(iBranch));
                }
            }
            return total;
        }

        public BigDecimal getGrossProfit(Integer iBranch) {
            BigDecimal total = new BigDecimal(0);
            Vector vecDay = new Vector(this.treeDay.values());
            for (int cnt1 = 0; cnt1 < vecDay.size(); cnt1++) {
                PerDay perDay = (PerDay) vecDay.get(cnt1);
                if (perDay != null) {
                    total = total.add(perDay.getGrossProfit(iBranch));
                }
            }
            return total;
        }

        public BigDecimal getNetPurchases(Integer iBranch) {
            BigDecimal total = new BigDecimal(0);
            Vector vecDay = new Vector(this.treeDay.values());
            for (int cnt1 = 0; cnt1 < vecDay.size(); cnt1++) {
                PerDay perDay = (PerDay) vecDay.get(cnt1);
                if (perDay != null) {
                    total = total.add(perDay.getNetPurchases(iBranch));
                }
            }
            return total;
        }

        public BigDecimal getCollection(Integer iBranch) {
            BigDecimal total = new BigDecimal(0);
            Vector vecDay = new Vector(this.treeDay.values());
            for (int cnt1 = 0; cnt1 < vecDay.size(); cnt1++) {
                PerDay perDay = (PerDay) vecDay.get(cnt1);
                if (perDay != null) {
                    total = total.add(perDay.getCollection(iBranch));
                }
            }
            return total;
        }

        public BigDecimal getPayment(Integer iBranch) {
            BigDecimal total = new BigDecimal(0);
            Vector vecDay = new Vector(this.treeDay.values());
            for (int cnt1 = 0; cnt1 < vecDay.size(); cnt1++) {
                PerDay perDay = (PerDay) vecDay.get(cnt1);
                if (perDay != null) {
                    total = total.add(perDay.getPayment(iBranch));
                }
            }
            return total;
        }

        public BigDecimal getTradeIn(Integer iBranch) {
            BigDecimal total = new BigDecimal(0);
            Vector vecDay = new Vector(this.treeDay.values());
            for (int cnt1 = 0; cnt1 < vecDay.size(); cnt1++) {
                PerDay perDay = (PerDay) vecDay.get(cnt1);
                if (perDay != null) {
                    total = total.add(perDay.getTradeIn(iBranch));
                }
            }
            return total;
        }
    }

    public static class PerDay {

        public Timestamp txnDate;

        public TreeMap treeBranch;

        public PerDay() {
            this.txnDate = TimeFormat.createTimestamp("0001-01-01");
            this.treeBranch = new TreeMap();
        }

        public String getKey() {
            return TimeFormat.strDisplayDate(this.txnDate);
        }

        public BigDecimal getNetSales(Integer iBranch) {
            BigDecimal total = new BigDecimal(0);
            Vector vecBranch = new Vector(this.treeBranch.values());
            for (int cnt1 = 0; cnt1 < vecBranch.size(); cnt1++) {
                PerBranch perBranch = (PerBranch) vecBranch.get(cnt1);
                if (perBranch != null && iBranch == null) {
                    total = total.add(perBranch.getNetSales());
                }
                if (perBranch != null && iBranch != null && perBranch.branchId.equals(iBranch)) {
                    total = total.add(perBranch.getNetSales());
                }
            }
            return total;
        }

        public BigDecimal getGrossProfit(Integer iBranch) {
            BigDecimal total = new BigDecimal(0);
            Vector vecBranch = new Vector(this.treeBranch.values());
            for (int cnt1 = 0; cnt1 < vecBranch.size(); cnt1++) {
                PerBranch perBranch = (PerBranch) vecBranch.get(cnt1);
                if (perBranch != null && iBranch == null) {
                    total = total.add(perBranch.getGrossProfit());
                }
                if (perBranch != null && iBranch != null && perBranch.branchId.equals(iBranch)) {
                    total = total.add(perBranch.getGrossProfit());
                }
            }
            return total;
        }

        public BigDecimal getNetPurchases(Integer iBranch) {
            BigDecimal total = new BigDecimal(0);
            Vector vecBranch = new Vector(this.treeBranch.values());
            for (int cnt1 = 0; cnt1 < vecBranch.size(); cnt1++) {
                PerBranch perBranch = (PerBranch) vecBranch.get(cnt1);
                if (perBranch != null && iBranch == null) {
                    total = total.add(perBranch.getNetPurchases());
                }
                if (perBranch != null && iBranch != null && perBranch.branchId.equals(iBranch)) {
                    total = total.add(perBranch.getNetPurchases());
                }
            }
            return total;
        }

        public BigDecimal getCollection(Integer iBranch) {
            BigDecimal total = new BigDecimal(0);
            Vector vecBranch = new Vector(this.treeBranch.values());
            for (int cnt1 = 0; cnt1 < vecBranch.size(); cnt1++) {
                PerBranch perBranch = (PerBranch) vecBranch.get(cnt1);
                if (perBranch != null && iBranch == null) {
                    total = total.add(perBranch.amountCollection);
                }
                if (perBranch != null && iBranch != null && perBranch.branchId.equals(iBranch)) {
                    total = total.add(perBranch.amountCollection);
                }
            }
            return total;
        }

        public BigDecimal getPayment(Integer iBranch) {
            BigDecimal total = new BigDecimal(0);
            Vector vecBranch = new Vector(this.treeBranch.values());
            for (int cnt1 = 0; cnt1 < vecBranch.size(); cnt1++) {
                PerBranch perBranch = (PerBranch) vecBranch.get(cnt1);
                if (perBranch != null && iBranch == null) {
                    total = total.add(perBranch.amountPayment);
                }
                if (perBranch != null && iBranch != null && perBranch.branchId.equals(iBranch)) {
                    total = total.add(perBranch.amountPayment);
                }
            }
            return total;
        }

        public BigDecimal getTradeIn(Integer iBranch) {
            BigDecimal total = new BigDecimal(0);
            Vector vecBranch = new Vector(this.treeBranch.values());
            for (int cnt1 = 0; cnt1 < vecBranch.size(); cnt1++) {
                PerBranch perBranch = (PerBranch) vecBranch.get(cnt1);
                if (perBranch != null && iBranch == null) {
                    total = total.add(perBranch.amountTradeIn);
                }
                if (perBranch != null && iBranch != null && perBranch.branchId.equals(iBranch)) {
                    total = total.add(perBranch.amountTradeIn);
                }
            }
            return total;
        }

        public static class PerBranch {

            public Integer branchId;

            public BigDecimal amountSales;

            public BigDecimal amountCOGS;

            public BigDecimal amountSalesReturn;

            public BigDecimal amountCOGR;

            public BigDecimal amountPurchase;

            public BigDecimal amountPurchaseReturn;

            public BigDecimal amountTradeIn;

            public BigDecimal amountTransfer;

            public BigDecimal amountAdjust;

            public BigDecimal amountCollection;

            public BigDecimal amountPayment;

            public PerBranch() {
                this.branchId = new Integer(0);
                this.amountSales = new BigDecimal(0);
                this.amountCOGS = new BigDecimal(0);
                this.amountSalesReturn = new BigDecimal(0);
                this.amountCOGR = new BigDecimal(0);
                this.amountPurchase = new BigDecimal(0);
                this.amountPurchaseReturn = new BigDecimal(0);
                this.amountTradeIn = new BigDecimal(0);
                this.amountTransfer = new BigDecimal(0);
                this.amountAdjust = new BigDecimal(0);
                this.amountCollection = new BigDecimal(0);
                this.amountPayment = new BigDecimal(0);
            }

            public BigDecimal getNetSales() {
                return this.amountSales.subtract(this.amountSalesReturn);
            }

            public BigDecimal getNetCost() {
                return this.amountCOGS.subtract(this.amountCOGR);
            }

            public BigDecimal getGrossProfit() {
                return getNetSales().subtract(getNetCost());
            }

            public BigDecimal getNetPurchases() {
                return this.amountPurchase.add(this.amountPurchaseReturn);
            }

            public BigDecimal getTradeIn() {
                return this.amountTradeIn;
            }
        }
    }
}

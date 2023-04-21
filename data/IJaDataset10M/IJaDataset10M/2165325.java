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
import com.vlee.util.*;

public class ReceiptAnalysisReport extends java.lang.Object implements Serializable {

    private Timestamp dateFrom;

    private Timestamp dateTo;

    private BranchObject branchObj;

    private Vector vecReceipt;

    private Report theReport;

    public ReceiptAnalysisReport() {
        this.dateFrom = TimeFormat.getTimestamp();
        this.dateTo = this.dateFrom;
        this.branchObj = null;
        this.vecReceipt = new Vector();
        this.theReport = new Report(this.dateFrom, this.dateTo, this.branchObj);
    }

    public void setBranch(Integer branchId) {
        if (branchId == null) {
            this.branchObj = null;
        } else {
            this.branchObj = BranchNut.getObject(branchId);
        }
    }

    public BranchObject getBranch() {
        return this.branchObj;
    }

    public String getBranch(String buf) {
        if (this.branchObj != null) {
            return this.branchObj.pkid.toString();
        }
        return buf;
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

    public void generateReport() {
        retrieveAllReceipts();
        populateReport();
    }

    public Report getReport() {
        return this.theReport;
    }

    private void retrieveAllReceipts() {
        Timestamp dateToNextDay = TimeFormat.add(this.dateTo, 0, 0, 1);
        QueryObject query = null;
        if (this.branchObj == null) {
            query = new QueryObject(new String[] { OfficialReceiptBean.PAYMENT_TIME + " >='" + TimeFormat.strDisplayDate(this.dateFrom) + "' ", OfficialReceiptBean.PAYMENT_TIME + " <'" + TimeFormat.strDisplayDate(dateToNextDay) + "' ", OfficialReceiptBean.STATE + " = '" + OfficialReceiptBean.ST_CREATED + "' " });
        } else {
            query = new QueryObject(new String[] { OfficialReceiptBean.BRANCH + " = '" + this.branchObj.pkid.toString() + "' ", OfficialReceiptBean.PAYMENT_TIME + " >='" + TimeFormat.strDisplayDate(this.dateFrom) + "' ", OfficialReceiptBean.PAYMENT_TIME + " <'" + TimeFormat.strDisplayDate(dateToNextDay) + "' ", OfficialReceiptBean.STATE + " = '" + OfficialReceiptBean.ST_CREATED + "' " });
        }
        this.vecReceipt = new Vector(OfficialReceiptNut.getObjects(query));
    }

    private void populateReport() {
        this.theReport = new Report(this.dateFrom, this.dateTo, this.branchObj);
        Vector vecBranchList = new Vector();
        if (this.branchObj == null) {
            vecBranchList = BranchNut.getValueObjectsGiven(BranchBean.STATUS, BranchBean.STATUS_ACTIVE, (String) null, (String) null);
        } else {
            vecBranchList.add(this.branchObj);
        }
        for (int cnt1 = 0; cnt1 < vecBranchList.size(); cnt1++) {
            BranchObject brhObj = (BranchObject) vecBranchList.get(cnt1);
            Report.PerBranch perBranchObj = new Report.PerBranch(brhObj);
            for (int cnt2 = 0; cnt2 < this.vecReceipt.size(); cnt2++) {
                OfficialReceiptObject orObj = (OfficialReceiptObject) this.vecReceipt.get(cnt2);
                if (orObj.branch.equals(perBranchObj.branchObj.pkid)) {
                    perBranchObj.vecReceipt.add(orObj);
                }
            }
            this.theReport.vecBranch.add(perBranchObj);
        }
        for (int cnt1 = 0; cnt1 < this.theReport.vecBranch.size(); cnt1++) {
            Report.PerBranch perBranchObj = (Report.PerBranch) this.theReport.vecBranch.get(cnt1);
            perBranchObj.populateSections();
        }
    }

    public static class Report {

        public Timestamp dateFrom;

        public Timestamp dateTo;

        public BranchObject branchObj;

        public Vector vecBranch;

        public Report(Timestamp dFrom, Timestamp dTo, BranchObject branch) {
            this.dateFrom = dFrom;
            this.dateTo = dTo;
            this.branchObj = branch;
            this.vecBranch = new Vector();
        }

        public BigDecimal getGrandTotal() {
            BigDecimal grandTotal = new BigDecimal(0);
            for (int cnt1 = 0; cnt1 < this.vecBranch.size(); cnt1++) {
                PerBranch perBranch = (PerBranch) this.vecBranch.get(cnt1);
                grandTotal = grandTotal.add(perBranch.getSubTotal());
            }
            return grandTotal;
        }

        public static class PerBranch {

            public BranchObject branchObj;

            public Vector vecSection;

            public Vector vecReceipt;

            public PerBranch(BranchObject branchObj) {
                this.branchObj = branchObj;
                this.vecSection = new Vector();
                this.vecReceipt = new Vector();
            }

            public BigDecimal getSubTotal() {
                BigDecimal subTotal = new BigDecimal(0);
                for (int cnt1 = 0; cnt1 < this.vecSection.size(); cnt1++) {
                    Section oneSection = (Section) this.vecSection.get(cnt1);
                    subTotal = subTotal.add(oneSection.getSubTotal());
                }
                return subTotal;
            }

            public void populateSections() {
                Section cashSection = new Section();
                cashSection.paymentType = "CASH";
                for (int cnt1 = 0; cnt1 < this.vecReceipt.size(); cnt1++) {
                    OfficialReceiptObject orObj = (OfficialReceiptObject) this.vecReceipt.get(cnt1);
                    if (orObj.amountCash.signum() > 0) {
                        Report.PerBranch.Section.Row oneRow = new Report.PerBranch.Section.Row();
                        oneRow.receipt = orObj;
                        oneRow.theDate = orObj.paymentTime;
                        oneRow.cashier = orObj.userIdUpdate;
                        oneRow.vecReferenceDoc = new Vector(DocLinkNut.getBySourceDoc(OfficialReceiptBean.TABLENAME, orObj.pkid));
                        oneRow.cardNo = "";
                        oneRow.approvalCode = "";
                        oneRow.amount = orObj.amountCash;
                        cashSection.vecRow.add(oneRow);
                    }
                }
                this.vecSection.add(cashSection);
                TreeMap treeMap = new TreeMap();
                for (int cnt1 = 0; cnt1 < this.vecReceipt.size(); cnt1++) {
                    OfficialReceiptObject orObj = (OfficialReceiptObject) this.vecReceipt.get(cnt1);
                    treeMap.put(orObj.cardType + orObj.pkid.toString(), orObj);
                }
                this.vecReceipt = new Vector(treeMap.values());
                String cardTypeBuffer = "";
                boolean createNewSection = true;
                Section oneCardSection = new Section();
                for (int cnt1 = 0; cnt1 < this.vecReceipt.size(); cnt1++) {
                    OfficialReceiptObject orObj = (OfficialReceiptObject) this.vecReceipt.get(cnt1);
                    if (orObj.amountCard.signum() > 0) {
                        if (!cardTypeBuffer.equals(orObj.cardType)) {
                            createNewSection = true;
                        }
                        if (createNewSection) {
                            oneCardSection = new Section();
                            this.vecSection.add(oneCardSection);
                            oneCardSection.paymentType = orObj.cardType;
                            createNewSection = false;
                            cardTypeBuffer = orObj.cardType;
                        }
                        Report.PerBranch.Section.Row oneRow = new Report.PerBranch.Section.Row();
                        oneRow.receipt = orObj;
                        oneRow.theDate = orObj.paymentTime;
                        oneRow.cashier = orObj.userIdUpdate;
                        oneRow.vecReferenceDoc = new Vector(DocLinkNut.getBySourceDoc(OfficialReceiptBean.TABLENAME, orObj.pkid));
                        oneRow.cardNo = orObj.cardNumber;
                        oneRow.approvalCode = orObj.cardApprovalCode;
                        oneRow.amount = orObj.amountCard;
                        oneCardSection.vecRow.add(oneRow);
                    }
                }
                Section chequeSection = new Section();
                chequeSection.paymentType = "CHEQUE";
                for (int cnt1 = 0; cnt1 < this.vecReceipt.size(); cnt1++) {
                    OfficialReceiptObject orObj = (OfficialReceiptObject) this.vecReceipt.get(cnt1);
                    if (orObj.amountCheque.signum() > 0) {
                        Report.PerBranch.Section.Row oneRow = new Report.PerBranch.Section.Row();
                        oneRow.receipt = orObj;
                        oneRow.theDate = orObj.paymentTime;
                        oneRow.cashier = orObj.userIdUpdate;
                        oneRow.vecReferenceDoc = new Vector(DocLinkNut.getBySourceDoc(OfficialReceiptBean.TABLENAME, orObj.pkid));
                        oneRow.cardNo = "";
                        oneRow.approvalCode = "";
                        oneRow.amount = orObj.amountCheque;
                        chequeSection.vecRow.add(oneRow);
                    }
                }
                this.vecSection.add(chequeSection);
                Section PDchequeSection = new Section();
                PDchequeSection.paymentType = "PDCHEQUE";
                for (int cnt1 = 0; cnt1 < this.vecReceipt.size(); cnt1++) {
                    OfficialReceiptObject orObj = (OfficialReceiptObject) this.vecReceipt.get(cnt1);
                    if (orObj.amountPDCheque.signum() > 0) {
                        Report.PerBranch.Section.Row oneRow = new Report.PerBranch.Section.Row();
                        oneRow.receipt = orObj;
                        oneRow.theDate = orObj.paymentTime;
                        oneRow.cashier = orObj.userIdUpdate;
                        oneRow.vecReferenceDoc = new Vector(DocLinkNut.getBySourceDoc(OfficialReceiptBean.TABLENAME, orObj.pkid));
                        oneRow.cardNo = "";
                        oneRow.approvalCode = "";
                        oneRow.amount = orObj.amountCheque;
                        PDchequeSection.vecRow.add(oneRow);
                    }
                }
                this.vecSection.add(PDchequeSection);
                Section cashVoucherSection = new Section();
                cashVoucherSection.paymentType = "CRV";
                for (int cnt1 = 0; cnt1 < this.vecReceipt.size(); cnt1++) {
                    OfficialReceiptObject orObj = (OfficialReceiptObject) this.vecReceipt.get(cnt1);
                    if (orObj.amountCoupon.signum() > 0) {
                        Report.PerBranch.Section.Row oneRow = new Report.PerBranch.Section.Row();
                        oneRow.receipt = orObj;
                        oneRow.theDate = orObj.paymentTime;
                        oneRow.cashier = orObj.userIdUpdate;
                        oneRow.vecReferenceDoc = new Vector(DocLinkNut.getBySourceDoc(OfficialReceiptBean.TABLENAME, orObj.pkid));
                        oneRow.cardNo = "";
                        oneRow.approvalCode = "";
                        oneRow.amount = orObj.amountCoupon;
                        cashVoucherSection.vecRow.add(oneRow);
                    }
                }
                this.vecSection.add(cashVoucherSection);
                Section otherSection = new Section();
                otherSection.paymentType = "OTHER (DISCOUNT VOUCHER)";
                for (int cnt1 = 0; cnt1 < this.vecReceipt.size(); cnt1++) {
                    OfficialReceiptObject orObj = (OfficialReceiptObject) this.vecReceipt.get(cnt1);
                    if (orObj.amountOther.signum() > 0) {
                        Report.PerBranch.Section.Row oneRow = new Report.PerBranch.Section.Row();
                        oneRow.receipt = orObj;
                        oneRow.theDate = orObj.paymentTime;
                        oneRow.cashier = orObj.userIdUpdate;
                        oneRow.vecReferenceDoc = new Vector(DocLinkNut.getBySourceDoc(OfficialReceiptBean.TABLENAME, orObj.pkid));
                        oneRow.cardNo = "";
                        oneRow.approvalCode = "";
                        oneRow.amount = orObj.amountOther;
                        otherSection.vecRow.add(oneRow);
                    }
                }
                this.vecSection.add(otherSection);
            }

            public static class Section {

                public String paymentType;

                public Vector vecRow;

                public Section() {
                    this.paymentType = "";
                    this.vecRow = new Vector();
                }

                public BigDecimal getSubTotal() {
                    BigDecimal subTotal = new BigDecimal(0);
                    for (int cnt1 = 0; cnt1 < this.vecRow.size(); cnt1++) {
                        Row oneRow = (Row) this.vecRow.get(cnt1);
                        subTotal = subTotal.add(oneRow.amount);
                    }
                    return subTotal;
                }

                public static class Row {

                    public OfficialReceiptObject receipt;

                    public Timestamp theDate;

                    public Integer cashier;

                    public Vector vecReferenceDoc;

                    public String cardNo;

                    public String approvalCode;

                    public BigDecimal amount;

                    public Row() {
                        this.receipt = null;
                        this.theDate = TimeFormat.getTimestamp();
                        this.cashier = new Integer(0);
                        this.vecReferenceDoc = new Vector();
                        this.cardNo = "";
                        this.approvalCode = "";
                        this.amount = new BigDecimal(0);
                    }
                }
            }
        }
    }
}

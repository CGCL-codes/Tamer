package com.vlee.bean.supplier;

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

public class PurchaseRecordsBySupplierReport extends java.lang.Object implements Serializable {

    public static final String STATE_DRAFT = "draft";

    public static final String SAVED = "saved";

    private String state = null;

    public String sortBy;

    private SuppAccountObject supplier = null;

    private Timestamp dateFrom;

    private Timestamp dateTo;

    public boolean useCodeRange;

    private String codeStart;

    private String codeEnd;

    private PurchaseRecordsBySupplierReport.Report theReport;

    private boolean processing = false;

    public boolean filterCategory0 = false;

    public Integer idCategory0 = new Integer(0);

    public boolean filterCategory1 = false;

    public String category1 = "";

    public boolean filterCategory2 = false;

    public String category2 = "";

    public boolean filterCategory3 = false;

    public String category3 = "";

    public boolean filterCategory4 = false;

    public String category4 = "";

    public boolean filterCategory5 = false;

    public String category5 = "";

    public PurchaseRecordsBySupplierReport() throws Exception {
        this.sortBy = "";
        this.supplier = null;
        this.dateFrom = TimeFormat.getTimestamp();
        this.dateFrom = TimeFormat.add(this.dateFrom, -1, 0, 0);
        this.dateTo = TimeFormat.getTimestamp();
        this.codeStart = "";
        this.codeEnd = "";
        this.theReport = null;
        this.useCodeRange = false;
    }

    public boolean getFilterCategory0() {
        return this.filterCategory0;
    }

    public boolean getFilterCategory1() {
        return this.filterCategory1;
    }

    public boolean getFilterCategory2() {
        return this.filterCategory2;
    }

    public boolean getFilterCategory3() {
        return this.filterCategory3;
    }

    public boolean getFilterCategory4() {
        return this.filterCategory4;
    }

    public boolean getFilterCategory5() {
        return this.filterCategory5;
    }

    public String getIdCategory0(String buf) {
        if (this.idCategory0 == null) {
            return buf;
        }
        return this.idCategory0.toString();
    }

    public String getCategory1() {
        return this.category1;
    }

    public String getCategory2() {
        return this.category2;
    }

    public String getCategory3() {
        return this.category3;
    }

    public String getCategory4() {
        return this.category4;
    }

    public String getCategory5() {
        return this.category5;
    }

    public void setParams(boolean filterCategory0, Integer idCategory0, boolean filterCategory1, String category1, boolean filterCategory2, String category2, boolean filterCategory3, String category3, boolean filterCategory4, String category4, boolean filterCategory5, String category5) {
        this.filterCategory0 = filterCategory0;
        this.idCategory0 = idCategory0;
        this.filterCategory1 = filterCategory1;
        this.category1 = category1;
        this.filterCategory2 = filterCategory2;
        this.category2 = category2;
        this.filterCategory3 = filterCategory3;
        this.category3 = category3;
        this.filterCategory4 = filterCategory4;
        this.category4 = category4;
        this.filterCategory5 = filterCategory5;
        this.category5 = category5;
        System.out.println("filterCategory0 : " + this.filterCategory0);
        System.out.println("filterCategory1 : " + this.filterCategory1);
        System.out.println("filterCategory2 : " + this.filterCategory2);
        System.out.println("filterCategory3 : " + this.filterCategory3);
        System.out.println("filterCategory4 : " + this.filterCategory4);
        System.out.println("filterCategory5 : " + this.filterCategory5);
    }

    public void reset() {
        this.theReport = null;
    }

    public boolean setSupplier(Integer iSupplier) {
        this.supplier = SuppAccountNut.getObject(iSupplier);
        return (this.supplier != null);
    }

    public SuppAccountObject getSupplier() {
        return this.supplier;
    }

    public void setSort(String buf) {
        this.sortBy = buf;
    }

    public String getSort() {
        return this.sortBy;
    }

    public void setDateRange(Timestamp dateFrom, Timestamp dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
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

    public void setCodeRange(boolean useCodeRange, String codeStart, String codeEnd) {
        this.useCodeRange = useCodeRange;
        this.codeStart = codeStart;
        this.codeEnd = codeEnd;
    }

    public String getCodeStart() {
        return this.codeStart.trim();
    }

    public String getCodeEnd() {
        return this.codeEnd.trim();
    }

    public boolean getUseCodeRange() {
        return this.useCodeRange;
    }

    public Report getReport() {
        return this.theReport;
    }

    public void generateReport(String dummy) {
        if (this.processing == true) return;
        this.processing = true;
        this.theReport = new PurchaseRecordsBySupplierReport.Report(this.supplier, this.dateFrom, TimeFormat.add(this.dateTo, 0, 0, 1), this.useCodeRange, this.codeStart, this.codeEnd, this.sortBy, this.filterCategory0, this.idCategory0, this.filterCategory1, this.category1, this.filterCategory2, this.category2, this.filterCategory3, this.category3, this.filterCategory4, this.category4, this.filterCategory5, this.category5);
        this.theReport = GoodsReceivedNoteItemNut.getPurchaseRecordsBySupplierReport(this.theReport);
        this.theReport = PurchaseReturnItemNut.getPurchaseRecordsBySupplierReport(this.theReport);
        this.processing = false;
    }

    public static class Report {

        boolean resultReady = false;

        public Vector vecRows = null;

        public Vector vecPrRows = null;

        public SuppAccountObject supplier = null;

        public Integer custId = null;

        public boolean useCodeRange;

        public Timestamp dateFrom;

        public Timestamp dateTo;

        public String codeStart;

        public String codeEnd;

        public String sortBy;

        public boolean filterCategory0 = false;

        public Integer idCategory0 = new Integer(0);

        public boolean filterCategory1 = false;

        public String category1 = "";

        public boolean filterCategory2 = false;

        public String category2 = "";

        public boolean filterCategory3 = false;

        public String category3 = "";

        public boolean filterCategory4 = false;

        public String category4 = "";

        public boolean filterCategory5 = false;

        public String category5 = "";

        public Report(SuppAccountObject suppObj, Timestamp dateFrom, Timestamp dateTo, boolean useCodeRange, String codeStart, String codeEnd, String sortBy, boolean filterCategory0, Integer idCategory0, boolean filterCategory1, String category1, boolean filterCategory2, String category2, boolean filterCategory3, String category3, boolean filterCategory4, String category4, boolean filterCategory5, String category5) {
            this.supplier = suppObj;
            this.resultReady = false;
            this.vecRows = new Vector();
            this.vecPrRows = new Vector();
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
            this.useCodeRange = useCodeRange;
            this.codeStart = codeStart;
            this.codeEnd = codeEnd;
            this.sortBy = sortBy;
            this.filterCategory0 = filterCategory0;
            this.idCategory0 = idCategory0;
            this.filterCategory1 = filterCategory1;
            this.category1 = category1;
            this.filterCategory2 = filterCategory2;
            this.category2 = category2;
            this.filterCategory3 = filterCategory3;
            this.category3 = category3;
            this.filterCategory4 = filterCategory4;
            this.category4 = category4;
            this.filterCategory5 = filterCategory5;
            this.category5 = category5;
        }

        public boolean getUseCodeRange() {
            return this.useCodeRange;
        }

        public String getCodeStart() {
            return this.codeStart;
        }

        public String getCodeEnd() {
            return this.codeEnd;
        }

        public static class ItemRow {

            public String code;

            public int invoiceId;

            public String reference;

            public int branch;

            public int location;

            public Timestamp date;

            public int posItemId;

            public String itemType;

            public int itemId;

            public String itemCode;

            public String name;

            public BigDecimal qty;

            public BigDecimal price;

            public ItemRow() {
                this.code = "";
                this.invoiceId = 0;
                this.reference = "";
                this.branch = 0;
                this.location = 0;
                this.date = TimeFormat.getTimestamp();
                this.posItemId = 0;
                this.itemType = "";
                this.itemId = 0;
                this.itemCode = "";
                this.name = "";
                this.qty = new BigDecimal(0);
                this.price = new BigDecimal(0);
            }
        }
    }
}

package it.allerj.bean;

import java.util.Date;

/**
 *
 * @author Alessandro Veracchi
 */
public class AllergicTestFilter {

    private String barcode;

    private String toRange = "";

    private Date toDate;

    private String fromRange = "";

    private Date fromDate;

    private boolean singleFrame = true;

    private boolean doubleFrame = true;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getToRange() {
        return toRange;
    }

    public void setToRange(String toRange) {
        this.toRange = toRange;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getFromRange() {
        return fromRange;
    }

    public void setFromRange(String fromRange) {
        this.fromRange = fromRange;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public boolean isSingleFrame() {
        return singleFrame;
    }

    public void setSingleFrame(boolean singleFrame) {
        this.singleFrame = singleFrame;
    }

    public boolean isDoubleFrame() {
        return doubleFrame;
    }

    public void setDoubleFrame(boolean doubleFrame) {
        this.doubleFrame = doubleFrame;
    }
}

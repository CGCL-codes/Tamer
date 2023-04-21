package com.entelience.metrics.events;

import com.entelience.util.DateHelper;

public class ResultPair {

    protected String reportDate;

    protected Object x;

    protected double y;

    public ResultPair() {
    }

    public ResultPair(String reportDate, java.util.Date x, double y) {
        if (reportDate == null) throw new IllegalArgumentException("reportDate null");
        if (x == null) throw new IllegalArgumentException("x null");
        this.reportDate = reportDate;
        this.x = (Object) x;
        this.y = y;
    }

    public ResultPair(String reportDate, double x, double y) {
        if (reportDate == null) throw new IllegalArgumentException("reportDate null");
        this.x = (Object) new Double(x);
        this.y = y;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        if (reportDate == null) throw new IllegalArgumentException("reportDate null");
        this.reportDate = reportDate;
    }

    public java.util.Date getReportDateAsDate() throws Exception {
        return DateHelper.isoString(reportDate);
    }

    public java.util.Date getXAsDate() {
        return ((java.util.Date) this.x);
    }

    public void setXAsDate(java.util.Date x) {
        if (x == null) throw new IllegalArgumentException("x null");
        this.x = (Object) x;
    }

    public double getXAsDouble() {
        return ((Double) this.x).doubleValue();
    }

    public void setXAsDouble(double x) {
        this.x = (Object) new Double(x);
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }
}

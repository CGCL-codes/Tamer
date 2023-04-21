package com.koossery.adempiere.fe.beans.accounting.calendar.year;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @version 1.0
 * @created 25-ao�t-2008 15:12:08
 */
public class YearBean {

    private int c_Calendar_ID;

    private int c_Year_ID;

    private String description;

    private String fiscalYear;

    private String isProcessing;

    private String isActive;

    public int getC_Calendar_ID() {
        return c_Calendar_ID;
    }

    public void setC_Calendar_ID(int c_Calendar_ID) {
        this.c_Calendar_ID = c_Calendar_ID;
    }

    public int getC_Year_ID() {
        return c_Year_ID;
    }

    public void setC_Year_ID(int c_Year_ID) {
        this.c_Year_ID = c_Year_ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getIsProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(String isProcessing) {
        this.isProcessing = isProcessing;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}

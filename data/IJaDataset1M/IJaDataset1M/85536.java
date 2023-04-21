package org.koossery.adempiere.core.contract.criteria.calendar;

import java.sql.Timestamp;
import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;

public class C_NonBusinessDayCriteria extends KTADempiereBaseCriteria {

    private static final long serialVersionUID = 1L;

    private int c_Calendar_ID;

    private int c_NonBusinessDay_ID;

    private Timestamp date1;

    private String name;

    private String isActive;

    public int getC_Calendar_ID() {
        return c_Calendar_ID;
    }

    public void setC_Calendar_ID(int c_Calendar_ID) {
        this.c_Calendar_ID = c_Calendar_ID;
    }

    public int getC_NonBusinessDay_ID() {
        return c_NonBusinessDay_ID;
    }

    public void setC_NonBusinessDay_ID(int c_NonBusinessDay_ID) {
        this.c_NonBusinessDay_ID = c_NonBusinessDay_ID;
    }

    public Timestamp getDate1() {
        return date1;
    }

    public void setDate1(Timestamp date1) {
        this.date1 = date1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}

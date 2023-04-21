package org.koossery.adempiere.core.contract.criteria.pa;

import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;

public class PA_ReportCriteria extends KTADempiereBaseCriteria {

    private static final long serialVersionUID = 1L;

    private int ad_PrintFormat_ID;

    private int c_AcctSchema_ID;

    private int c_Calendar_ID;

    private String description;

    private int jasperProcess_ID;

    private String jasperProcessing;

    private String name;

    private int pa_Report_ID;

    private int pa_ReportColumnSet_ID;

    private int pa_ReportLineSet_ID;

    private String isListSources;

    private String isListTrx;

    private String isProcessing;

    private String isActive;

    public int getAd_PrintFormat_ID() {
        return ad_PrintFormat_ID;
    }

    public void setAd_PrintFormat_ID(int ad_PrintFormat_ID) {
        this.ad_PrintFormat_ID = ad_PrintFormat_ID;
    }

    public int getC_AcctSchema_ID() {
        return c_AcctSchema_ID;
    }

    public void setC_AcctSchema_ID(int c_AcctSchema_ID) {
        this.c_AcctSchema_ID = c_AcctSchema_ID;
    }

    public int getC_Calendar_ID() {
        return c_Calendar_ID;
    }

    public void setC_Calendar_ID(int c_Calendar_ID) {
        this.c_Calendar_ID = c_Calendar_ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getJasperProcess_ID() {
        return jasperProcess_ID;
    }

    public void setJasperProcess_ID(int jasperProcess_ID) {
        this.jasperProcess_ID = jasperProcess_ID;
    }

    public String getJasperProcessing() {
        return jasperProcessing;
    }

    public void setJasperProcessing(String jasperProcessing) {
        this.jasperProcessing = jasperProcessing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPa_Report_ID() {
        return pa_Report_ID;
    }

    public void setPa_Report_ID(int pa_Report_ID) {
        this.pa_Report_ID = pa_Report_ID;
    }

    public int getPa_ReportColumnSet_ID() {
        return pa_ReportColumnSet_ID;
    }

    public void setPa_ReportColumnSet_ID(int pa_ReportColumnSet_ID) {
        this.pa_ReportColumnSet_ID = pa_ReportColumnSet_ID;
    }

    public int getPa_ReportLineSet_ID() {
        return pa_ReportLineSet_ID;
    }

    public void setPa_ReportLineSet_ID(int pa_ReportLineSet_ID) {
        this.pa_ReportLineSet_ID = pa_ReportLineSet_ID;
    }

    public String getIsListSources() {
        return isListSources;
    }

    public void setIsListSources(String isListSources) {
        this.isListSources = isListSources;
    }

    public String getIsListTrx() {
        return isListTrx;
    }

    public void setIsListTrx(String isListTrx) {
        this.isListTrx = isListTrx;
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

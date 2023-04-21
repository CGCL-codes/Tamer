package com.brekeke.hiway.ticket.dto;

/**
 * 报表落款信息管理DTO
 * @author LEPING.LI
 * @version 1.0.0
 */
public class ReportMasterDto extends BaseDto {

    private static final long serialVersionUID = -5053574217935632973L;

    private String reportname;

    private String timeFlag;

    public String getReportname() {
        return reportname;
    }

    public void setReportname(String reportname) {
        this.reportname = reportname;
    }

    public String getTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(String timeFlag) {
        this.timeFlag = timeFlag;
    }
}

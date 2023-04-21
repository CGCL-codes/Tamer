package com.antrou.domain;

import java.util.Date;

public class ArmmAlarmRecord implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     */
    private String alarmReportId;

    /**
     */
    private String receId;

    /**
     */
    private String receAlarmCode;

    /**
     */
    private String alarmNam;

    /**
     */
    private Date firstAlarmTime;

    /**
     */
    private Date lastAlarmTime;

    /**
     */
    private Integer alarmCount;

    /**
     * ʵ��ֵ.
     */
    private Integer alarmType;

    /**
     */
    private String alarmStatus;

    /**
     */
    private String alarmOperNam;

    /**
     */
    private Date alarmOperTime;

    /**
     */
    private String handleResult;

    public ArmmAlarmRecord() {
    }

    public String getAlarmReportId() {
        return alarmReportId;
    }

    public void setAlarmReportId(String alarmReportId) {
        this.alarmReportId = alarmReportId;
    }

    public String getReceId() {
        return receId;
    }

    public void setReceId(String receId) {
        this.receId = receId;
    }

    public String getReceAlarmCode() {
        return receAlarmCode;
    }

    public void setReceAlarmCode(String receAlarmCode) {
        this.receAlarmCode = receAlarmCode;
    }

    public String getAlarmNam() {
        return alarmNam;
    }

    public void setAlarmNam(String alarmNam) {
        this.alarmNam = alarmNam;
    }

    public Date getFirstAlarmTime() {
        return firstAlarmTime;
    }

    public void setFirstAlarmTime(Date firstAlarmTime) {
        this.firstAlarmTime = firstAlarmTime;
    }

    public Date getLastAlarmTime() {
        return lastAlarmTime;
    }

    public void setLastAlarmTime(Date lastAlarmTime) {
        this.lastAlarmTime = lastAlarmTime;
    }

    public Integer getAlarmCount() {
        return alarmCount;
    }

    public void setAlarmCount(Integer alarmCount) {
        this.alarmCount = alarmCount;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(String alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public String getAlarmOperNam() {
        return alarmOperNam;
    }

    public void setAlarmOperNam(String alarmOperNam) {
        this.alarmOperNam = alarmOperNam;
    }

    public Date getAlarmOperTime() {
        return alarmOperTime;
    }

    public void setAlarmOperTime(Date alarmOperTime) {
        this.alarmOperTime = alarmOperTime;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }
}

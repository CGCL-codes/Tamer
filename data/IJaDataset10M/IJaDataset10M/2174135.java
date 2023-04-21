package cn.scu.sa.admin.model;

import java.util.Date;

/**
 * Log entity. @author MyEclipse Persistence Tools
 */
public class Log implements java.io.Serializable {

    private Long id;

    private String userName;

    private String operationContent;

    private Date operationTime;

    private String notes;

    /** default constructor */
    public Log() {
    }

    /** full constructor */
    public Log(String userName, String operationContent, Date operationTime, String notes) {
        this.userName = userName;
        this.operationContent = operationContent;
        this.operationTime = operationTime;
        this.notes = notes;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOperationContent() {
        return this.operationContent;
    }

    public void setOperationContent(String operationContent) {
        this.operationContent = operationContent;
    }

    public Date getOperationTime() {
        return this.operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

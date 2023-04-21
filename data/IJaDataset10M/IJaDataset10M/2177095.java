package com.tcs.hrr.domain;

import java.util.Date;

/**
 * UploadLog entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class UploadLog implements java.io.Serializable {

    private Integer uploadLogId;

    private Candidate candidate;

    private String fileType;

    private String filePath;

    private Date createDate;

    private String createBy;

    /** default constructor */
    public UploadLog() {
    }

    /** full constructor */
    public UploadLog(Candidate candidate, String fileType, String filePath, Date createDate, String createBy) {
        this.candidate = candidate;
        this.fileType = fileType;
        this.filePath = filePath;
        this.createDate = createDate;
        this.createBy = createBy;
    }

    public Integer getUploadLogId() {
        return this.uploadLogId;
    }

    public void setUploadLogId(Integer uploadLogId) {
        this.uploadLogId = uploadLogId;
    }

    public Candidate getCandidate() {
        return this.candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}

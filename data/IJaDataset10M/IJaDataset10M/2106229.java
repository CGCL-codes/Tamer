package com.gdpu.project.vo;

import java.util.Date;

/**
 * ProEnd entity. @author MyEclipse Persistence Tools
 */
public class ProEnd implements java.io.Serializable {

    private Integer id;

    private String projectId;

    private String teacherId;

    private String fileName;

    private String filePath;

    private Date uploadDate;

    private Date endDate;

    private Integer state;

    private String comment;

    /** default constructor */
    public ProEnd() {
    }

    /** full constructor */
    public ProEnd(String projectId, String teacherId, String fileName, String filePath, Date uploadDate, Date endDate, Integer state, String comment) {
        this.projectId = projectId;
        this.teacherId = teacherId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadDate = uploadDate;
        this.endDate = endDate;
        this.state = state;
        this.comment = comment;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTeacherId() {
        return this.teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getUploadDate() {
        return this.uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getState() {
        return this.state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

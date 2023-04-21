package com.ems.common.model.vo;

public class ProjectVO {

    private Integer id;

    private String projectName;

    private String projectNameEn;

    private String projectComment;

    public ProjectVO(Integer id) {
        super();
        this.id = id;
    }

    public ProjectVO(Integer id, String projectName) {
        super();
        this.id = id;
        this.projectName = projectName;
    }

    public ProjectVO(Integer id, String projectName, String projectComment) {
        super();
        this.id = id;
        this.projectName = projectName;
        this.projectComment = projectComment;
    }

    public ProjectVO() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectNameEn() {
        return projectNameEn;
    }

    public void setProjectNameEn(String projectNameEn) {
        this.projectNameEn = projectNameEn;
    }

    public String getProjectComment() {
        return projectComment;
    }

    public void setProjectComment(String projectComment) {
        this.projectComment = projectComment;
    }
}

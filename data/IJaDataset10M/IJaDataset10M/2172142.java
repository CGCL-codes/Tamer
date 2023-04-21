package com.tcs.hrr.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Candidate entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class Candidate implements java.io.Serializable {

    private Integer candidateId;

    private ChannelSource channelSource;

    private String name;

    private String pinyin;

    private String email;

    private String candidateType;

    private String twe;

    private String rwe;

    private String grade;

    private String englishLevel;

    private String contactNo;

    private String curLocation;

    private String empStatus;

    private String workLocation;

    private Integer tagOwnerId;

    private String curSalary;

    private String expSalary;

    private String remark;

    private String proposeStatus;

    private String caller;

    private Date cvReceivedDate;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private String status;

    private Set uploadLogs = new HashSet(0);

    private Set proposes = new HashSet(0);

    private Set candidateCompetencies = new HashSet(0);

    private String gender;

    private String empno;

    private String experienceDesc;

    private String cv_path;

    public String getCv_path() {
        return cv_path;
    }

    public void setCv_path(String cv_path) {
        this.cv_path = cv_path;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmpno() {
        return empno;
    }

    public void setEmpno(String empno) {
        this.empno = empno;
    }

    public String getExperienceDesc() {
        return experienceDesc;
    }

    public void setExperienceDesc(String experienceDesc) {
        this.experienceDesc = experienceDesc;
    }

    /** default constructor */
    public Candidate() {
    }

    /** minimal constructor */
    public Candidate(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /** full constructor */
    public Candidate(ChannelSource channelSource, String name, String pinyin, String email, String candidateType, String twe, String rwe, String grade, String englishLevel, String contactNo, String curLocation, String empStatus, String workLocation, Integer tagOwnerId, String curSalary, String expSalary, String remark, String proposeStatus, String caller, Date cvReceivedDate, String createBy, Date createDate, String updateBy, Date updateDate, String status, Set uploadLogs, Set proposes, Set candidateCompetencies, String gender, String empno, String experienceDesc, String cv_path) {
        this.channelSource = channelSource;
        this.name = name;
        this.pinyin = pinyin;
        this.email = email;
        this.candidateType = candidateType;
        this.twe = twe;
        this.rwe = rwe;
        this.grade = grade;
        this.englishLevel = englishLevel;
        this.contactNo = contactNo;
        this.curLocation = curLocation;
        this.empStatus = empStatus;
        this.workLocation = workLocation;
        this.tagOwnerId = tagOwnerId;
        this.curSalary = curSalary;
        this.expSalary = expSalary;
        this.remark = remark;
        this.proposeStatus = proposeStatus;
        this.caller = caller;
        this.cvReceivedDate = cvReceivedDate;
        this.createBy = createBy;
        this.createDate = createDate;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
        this.status = status;
        this.uploadLogs = uploadLogs;
        this.proposes = proposes;
        this.candidateCompetencies = candidateCompetencies;
        this.gender = gender;
        this.empno = empno;
        this.experienceDesc = experienceDesc;
        this.cv_path = cv_path;
    }

    public Integer getCandidateId() {
        return this.candidateId;
    }

    public void setCandidateId(Integer candidateId) {
        this.candidateId = candidateId;
    }

    public ChannelSource getChannelSource() {
        return this.channelSource;
    }

    public void setChannelSource(ChannelSource channelSource) {
        this.channelSource = channelSource;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return this.pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCandidateType() {
        return this.candidateType;
    }

    public void setCandidateType(String candidateType) {
        this.candidateType = candidateType;
    }

    public String getTwe() {
        return this.twe;
    }

    public void setTwe(String twe) {
        this.twe = twe;
    }

    public String getRwe() {
        return this.rwe;
    }

    public void setRwe(String rwe) {
        this.rwe = rwe;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEnglishLevel() {
        return this.englishLevel;
    }

    public void setEnglishLevel(String englishLevel) {
        this.englishLevel = englishLevel;
    }

    public String getContactNo() {
        return this.contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCurLocation() {
        return this.curLocation;
    }

    public void setCurLocation(String curLocation) {
        this.curLocation = curLocation;
    }

    public String getEmpStatus() {
        return this.empStatus;
    }

    public void setEmpStatus(String empStatus) {
        this.empStatus = empStatus;
    }

    public String getWorkLocation() {
        return this.workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public Integer getTagOwnerId() {
        return this.tagOwnerId;
    }

    public void setTagOwnerId(Integer tagOwnerId) {
        this.tagOwnerId = tagOwnerId;
    }

    public String getCurSalary() {
        return this.curSalary;
    }

    public void setCurSalary(String curSalary) {
        this.curSalary = curSalary;
    }

    public String getExpSalary() {
        return this.expSalary;
    }

    public void setExpSalary(String expSalary) {
        this.expSalary = expSalary;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProposeStatus() {
        return this.proposeStatus;
    }

    public void setProposeStatus(String proposeStatus) {
        this.proposeStatus = proposeStatus;
    }

    public String getCaller() {
        return this.caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public Date getCvReceivedDate() {
        return this.cvReceivedDate;
    }

    public void setCvReceivedDate(Date cvReceivedDate) {
        this.cvReceivedDate = cvReceivedDate;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return this.updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set getUploadLogs() {
        return this.uploadLogs;
    }

    public void setUploadLogs(Set uploadLogs) {
        this.uploadLogs = uploadLogs;
    }

    public Set getProposes() {
        return this.proposes;
    }

    public void setProposes(Set proposes) {
        this.proposes = proposes;
    }

    public Set getcandidateCompetencies() {
        return this.candidateCompetencies;
    }

    public void setcandidateCompetencies(Set candidateCompetencies) {
        this.candidateCompetencies = candidateCompetencies;
    }
}

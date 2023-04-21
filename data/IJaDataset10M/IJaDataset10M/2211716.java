package cn.edu.zju.acm.onlinejudge.bean;

import java.util.Date;

public class QQ {

    public static final String QQ_NEW = "New";

    public static final String QQ_DELIVERING = "Delivering";

    public static final String QQ_FINISHED = "Finished";

    private long submissionId = -1;

    private Date submissionDate = null;

    private long userProfileId = -1;

    private String handle = null;

    private String nickName = null;

    private long problemId = -1;

    private String code = null;

    private String color = null;

    private String status = null;

    public QQ() {
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHandle() {
        return this.handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public long getProblemId() {
        return this.problemId;
    }

    public void setProblemId(long problemId) {
        this.problemId = problemId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getSubmissionId() {
        return this.submissionId;
    }

    public void setSubmissionId(long submissionId) {
        this.submissionId = submissionId;
    }

    public Date getSubmissionDate() {
        return this.submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public long getUserProfileId() {
        return this.userProfileId;
    }

    public void setUserProfileId(long userProfileId) {
        this.userProfileId = userProfileId;
    }
}

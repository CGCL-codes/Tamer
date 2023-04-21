package mhk.dto;

import java.sql.Timestamp;
import org.seasar.dao.annotation.tiger.Column;

public class RemarkBean {

    private Integer id;

    private Integer threadId;

    private String userName;

    private String iconFilename;

    private String color;

    private Timestamp remarkTime;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column("thread_id")
    public Integer getThreadId() {
        return threadId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }

    @Column("user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column("icon_filename")
    public String getIconFilename() {
        return iconFilename;
    }

    public void setIconFilename(String iconFilename) {
        this.iconFilename = iconFilename;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Column("remark_time")
    public Timestamp getRemarkTime() {
        return remarkTime;
    }

    public void setRemarkTime(Timestamp remarkTime) {
        this.remarkTime = remarkTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

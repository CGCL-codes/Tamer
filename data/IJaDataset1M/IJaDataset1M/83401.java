package org.monet.kernel.model;

import java.util.Date;
import org.monet.kernel.constants.Strings;
import org.monet.kernel.library.LibraryDate;

public class TaskFact implements Fact {

    private long id;

    private Date createDate;

    private String workLineCode;

    private String workStopCode;

    private String type;

    private String userId;

    private String taskId;

    private Object extraData;

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getInternalCreateDate() {
        return createDate;
    }

    public String getCreateDate() {
        return LibraryDate.getDateAndTimeString(this.createDate, Language.getCurrent(), LibraryDate.Format.DEFAULT, true, Strings.BAR45);
    }

    public void setWorklineCode(String workLineCode) {
        this.workLineCode = workLineCode;
    }

    public String getWorklineCode() {
        return workLineCode;
    }

    public void setWorkstopCode(String workStopCode) {
        this.workStopCode = workStopCode;
    }

    public String getWorkstopCode() {
        return workStopCode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }

    public Object getExtraData() {
        return this.extraData;
    }
}

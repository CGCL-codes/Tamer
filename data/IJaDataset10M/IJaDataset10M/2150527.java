package cowsultants.itracker.ejb.beans.entity;

import java.util.*;
import java.sql.Timestamp;
import cowsultants.itracker.ejb.client.models.IssueAttachmentModel;

public abstract class IssueAttachmentBean extends GenericBean {

    public abstract String getOriginalFileName();

    public abstract void setOriginalFileName(String value);

    public abstract String getType();

    public abstract void setType(String value);

    public abstract String getFileName();

    public abstract void setFileName(String value);

    public abstract byte[] getFileData();

    public abstract void setFileData(byte[] value);

    public abstract String getDescription();

    public abstract void setDescription(String value);

    public abstract long getSize();

    public abstract void setSize(long value);

    public abstract IssueLocal getIssue();

    public abstract void setIssue(IssueLocal value);

    public abstract UserLocal getUser();

    public abstract void setUser(UserLocal value);

    public IssueAttachmentModel getModel() {
        IssueAttachmentModel model = new IssueAttachmentModel();
        model.setId(this.getId());
        model.setOriginalFileName(this.getOriginalFileName());
        model.setType(this.getType());
        model.setFileName(this.getFileName());
        model.setDescription(this.getDescription());
        model.setSize(this.getSize());
        model.setLastModifiedDate(this.getLastModifiedDate());
        model.setCreateDate(this.getCreateDate());
        model.setIssueId(this.getIssue().getId());
        model.setUser(this.getUser().getModel());
        return model;
    }

    public void setModel(IssueAttachmentModel model) {
        this.setOriginalFileName(model.getOriginalFileName());
        this.setType(model.getType());
        this.setFileName(model.getFileName());
        this.setSize(model.getSize());
        this.setDescription(model.getDescription());
        this.setLastModifiedDate(new Timestamp(new Date().getTime()));
    }
}

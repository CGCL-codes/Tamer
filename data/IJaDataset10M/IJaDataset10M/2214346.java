package cowsultants.itracker.ejb.beans.entity;

import java.util.*;
import java.sql.Timestamp;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.CreateException;
import cowsultants.itracker.ejb.client.models.IssueRelationModel;

public abstract class IssueRelationBean implements EntityBean {

    public abstract Integer getId();

    public abstract void setId(Integer value);

    public abstract Integer getMatchingRelationId();

    public abstract void setMatchingRelationId(Integer value);

    public abstract int getRelationType();

    public abstract void setRelationType(int value);

    public abstract IssueLocal getIssue();

    public abstract void setIssue(IssueLocal value);

    public abstract IssueLocal getRelatedIssue();

    public abstract void setRelatedIssue(IssueLocal value);

    public abstract Timestamp getCreateDate();

    public abstract void setCreateDate(Timestamp value);

    public abstract Timestamp getLastModifiedDate();

    public abstract void setLastModifiedDate(Timestamp value);

    public IssueRelationModel getModel() {
        IssueRelationModel model = new IssueRelationModel();
        model.setId(this.getId());
        model.setRelationType(this.getRelationType());
        model.setMatchingRelationId(this.getMatchingRelationId());
        model.setLastModifiedDate(this.getLastModifiedDate());
        model.setCreateDate(this.getCreateDate());
        if (this.getIssue() != null) {
            model.setIssueId(this.getIssue().getId());
        }
        IssueLocal relatedIssue = getRelatedIssue();
        if (relatedIssue != null) {
            model.setRelatedIssueId(relatedIssue.getId());
            model.setRelatedIssueDescription(relatedIssue.getDescription());
            model.setRelatedIssueStatus(relatedIssue.getStatus());
            model.setRelatedIssueSeverity(relatedIssue.getSeverity());
        }
        return model;
    }

    public void setModel(IssueRelationModel model) {
        this.setRelationType(model.getRelationType());
        this.setLastModifiedDate(new Timestamp(new Date().getTime()));
    }

    public void setEntityContext(EntityContext value) {
    }

    public void unsetEntityContext() {
    }

    public Integer ejbCreate(Integer value) throws CreateException {
        this.setId(value);
        return null;
    }

    public void ejbPostCreate(Integer value) throws CreateException {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbLoad() {
    }

    public void ejbStore() {
    }

    public void ejbRemove() {
    }
}

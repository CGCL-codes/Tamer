package com.entelience.objects.audit;

import com.entelience.objects.raci.RaciableBean;
import com.entelience.objects.raci.Targettable;
import com.entelience.util.DateHelper;
import java.util.Date;

/**
 * BEAN - Audit data
 */
public class Audit implements java.io.Serializable, RaciableBean, Targettable {

    public Audit() {
    }

    private AuditId auditId;

    private Integer e_raci_obj;

    private int originId;

    private String origin;

    private Integer topicId;

    private String topic;

    private Date creationDate;

    private Date startDate;

    private Date endDate;

    private String reference;

    private String owner;

    private Integer ownerId;

    private String objectives;

    private String context;

    private String deliverables;

    private String scope;

    private int statusId;

    private String status;

    private int confidentialityId;

    private String confidentiality;

    private boolean compliant;

    private Integer standardId;

    private String standardName;

    public String toString() {
        StringBuffer s = new StringBuffer(super.toString());
        s.append(" [auditId=").append(auditId).append("],");
        s.append(" [e_raci_obj=").append(e_raci_obj).append("],");
        s.append(" [originId=").append(originId).append("],");
        s.append(" [topicId=").append(topicId).append("],");
        s.append(" [startDate=").append(startDate).append("],");
        s.append(" [endDate=").append(endDate).append("],");
        s.append(" [reference=").append(reference).append("],");
        s.append(" [objectives=").append(objectives).append("],");
        s.append(" [context=").append(context).append("],");
        s.append(" [deliverables=").append(deliverables).append("],");
        s.append(" [scope=").append(scope).append("],");
        s.append(" [statusId=").append(statusId).append("],");
        s.append(" [confidentialityId=").append(confidentialityId).append("],");
        return s.toString();
    }

    public void setAuditId(AuditId auditId) {
        this.auditId = auditId;
    }

    public AuditId getAuditId() {
        return auditId;
    }

    public void setE_raci_obj(Integer e_raci_obj) {
        this.e_raci_obj = e_raci_obj;
    }

    public Integer getE_raci_obj() {
        return e_raci_obj;
    }

    public void setOriginId(int originId) {
        this.originId = originId;
    }

    public int getOriginId() {
        return originId;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOrigin() {
        return origin;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public void setDeliverables(String deliverables) {
        this.deliverables = deliverables;
    }

    public String getDeliverables() {
        return deliverables;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setConfidentialityId(int confidentialityId) {
        this.confidentialityId = confidentialityId;
    }

    public int getConfidentialityId() {
        return confidentialityId;
    }

    public void setConfidentiality(String confidentiality) {
        this.confidentiality = confidentiality;
    }

    public String getConfidentiality() {
        return confidentiality;
    }

    public String asText() throws Exception {
        StringBuffer message = new StringBuffer("\nAudit\n");
        message.append("Reference: ").append(getReference()).append('\n');
        message.append("Origin: ").append(getOrigin()).append('\n');
        if (getTopic() != null) {
            message.append("Topic: ").append(getTopic()).append('\n');
        } else if (getStandardName() != null) {
            if (isCompliant()) {
                message.append("Compliant to a standard: ").append(isCompliant()).append('\n');
            }
            message.append("Standard: ").append(getStandardName()).append('\n');
        }
        message.append("Start date: ").append(DateHelper.HTMLDateOrNull(getStartDate())).append('\n');
        message.append("End date: ").append(DateHelper.HTMLDateOrNull(getEndDate())).append('\n');
        message.append("Status: ").append(getStatus()).append('\n');
        message.append("Confidentiality: ").append(getConfidentiality()).append('\n');
        message.append("Objectives: ").append(getObjectives()).append('\n');
        message.append("Context: ").append(getContext()).append('\n');
        message.append("Deliverables: ").append(getDeliverables()).append('\n');
        message.append("Scope: ").append(getScope()).append('\n');
        message.append("Responsible: ").append(getOwner()).append('\n');
        message.append('\n');
        return message.toString();
    }

    public String constructUrl(String urlLocal, String sessionId) throws Exception {
        return urlLocal + "html/audit/audit.jsp?auditId=" + auditId.getId();
    }

    public boolean isCompliant() {
        return compliant;
    }

    public void setCompliant(boolean compliant) {
        this.compliant = compliant;
    }

    public Integer getStandardId() {
        return standardId;
    }

    public void setStandardId(Integer standardId) {
        this.standardId = standardId;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public Date getTargetDate() {
        return getEndDate();
    }
}

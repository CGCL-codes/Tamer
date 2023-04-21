package com.entelience.objects.audit;

import com.entelience.objects.raci.RaciableBean;
import com.entelience.objects.raci.Targettable;
import com.entelience.util.DateHelper;
import java.util.Date;
import java.io.Serializable;

/**
 * BEAN - Action data
 */
public class Action implements Serializable, RaciableBean, Targettable {

    public Action() {
    }

    private AuditActionId action_id;

    private int action_status_id;

    private String action_status;

    private Integer e_raci_obj;

    private String owner_username;

    private Integer owner_id;

    private Date due_date;

    private Date closed_date;

    private Date creation_date;

    private String title;

    private String description;

    private String recom_title;

    private String recom_owner_username;

    private Integer recomStatusId;

    private AuditRecId rec_id;

    private String externalRef;

    private Boolean escalade;

    private String recomReference;

    public String toString() {
        StringBuffer s = new StringBuffer(super.toString());
        s.append(" [action_id=").append(action_id).append("],");
        s.append(" [e_raci_obj=").append(e_raci_obj).append("],");
        s.append(" [status=").append(action_status_id).append("],");
        s.append(" [due_date=").append(due_date).append("],");
        s.append(" [title=").append(title).append("],");
        s.append(" [description=").append(description).append(']');
        return s.toString();
    }

    public void setAction_id(AuditActionId action_id) {
        this.action_id = action_id;
    }

    public AuditActionId getAction_id() {
        return action_id;
    }

    public void setAction_status_id(int action_status_id) {
        this.action_status_id = action_status_id;
    }

    public int getAction_status_id() {
        return action_status_id;
    }

    public void setAction_status(String action_status) {
        this.action_status = action_status;
    }

    public String getAction_status() {
        return action_status;
    }

    public void setE_raci_obj(Integer e_raci_obj) {
        this.e_raci_obj = e_raci_obj;
    }

    public Integer getE_raci_obj() {
        return e_raci_obj;
    }

    public void setOwner_username(String owner_username) {
        this.owner_username = owner_username;
    }

    public String getOwner_username() {
        return owner_username;
    }

    public void setOwner_id(Integer owner_id) {
        this.owner_id = owner_id;
    }

    public Integer getOwner_id() {
        return owner_id;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setClosed_date(Date closed_date) {
        this.closed_date = closed_date;
    }

    public Date getClosed_date() {
        return closed_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setRecom_title(String recom_title) {
        this.recom_title = recom_title;
    }

    public String getRecom_title() {
        return recom_title;
    }

    public void setRecom_owner_username(String recom_owner_username) {
        this.recom_owner_username = recom_owner_username;
    }

    public String getRecom_owner_username() {
        return recom_owner_username;
    }

    public void setRecomStatusId(Integer recomStatusId) {
        this.recomStatusId = recomStatusId;
    }

    public Integer getRecomStatusId() {
        return recomStatusId;
    }

    public void setRec_id(AuditRecId rec_id) {
        this.rec_id = rec_id;
    }

    public AuditRecId getRec_id() {
        return rec_id;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public String asText() throws Exception {
        StringBuffer message = new StringBuffer("\nAction\n");
        message.append("From recommendation: ").append(getRecom_title());
        message.append(" (ref:").append(getRecomReference());
        message.append(')').append('\n');
        message.append('\n');
        message.append("Title: ").append(getTitle()).append('\n');
        message.append("Description: ").append(getDescription()).append('\n');
        message.append("Status: ").append(getAction_status()).append('\n');
        message.append("Due date: ").append(DateHelper.HTMLDateOrNull(getDue_date())).append('\n');
        message.append("Closed date: ").append(DateHelper.HTMLDateOrNull(getClosed_date())).append('\n');
        message.append("External ref: ").append(getExternalRef()).append('\n');
        message.append("Responsible: ").append(getOwner_username()).append('\n');
        message.append('\n');
        return message.toString();
    }

    public String constructUrl(String urlLocal, String sessionId) throws Exception {
        return urlLocal + "html/audit/action.jsp?actionId=" + action_id.getId();
    }

    public Boolean getEscalade() {
        return escalade;
    }

    public void setEscalade(Boolean escalade) {
        this.escalade = escalade;
    }

    public String getRecomReference() {
        return recomReference;
    }

    public void setRecomReference(String recomReference) {
        this.recomReference = recomReference;
    }

    public Date getTargetDate() {
        return getDue_date();
    }
}

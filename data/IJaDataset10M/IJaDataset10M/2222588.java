package com.yubuild.coreman.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SubDocumentHistory extends BaseObject implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Long id;

    protected Long userId;

    protected Date date;

    protected Long documentId;

    protected Long subdocumentId;

    protected Integer action;

    protected User user;

    protected String referenceNo;

    protected String keywordRefs;

    private String changesAsString;

    protected Integer statusId;

    protected String receivedFrom;

    protected String sentTo;

    protected String sentVia;

    protected Date receivedFromDate;

    protected Date sentToDate;

    protected Date sentViaDate;

    protected String receivedFromRef;

    protected String sentToRef;

    protected String sentViaRef;

    protected String subject;

    protected String body;

    protected String pdfFilePath;

    protected String docFilePath;

    protected Status status;

    protected byte pdfBytes[];

    protected byte docBytes[];

    private ArrayList orderBy;

    public SubDocumentHistory() {
        changesAsString = "";
    }

    public Date getDate() {
        return date;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public Long getSubdocumentId() {
        return subdocumentId;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public void setSubdocumentId(Long subdocumentId) {
        this.subdocumentId = subdocumentId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getChangesAsString() {
        return "<b>body</b>: " + body + "<br><b>date</b>: " + date + "<br><b>docFilePath</b>: " + docFilePath + "<br><b>pdfFilePath</b>: " + pdfFilePath + "<br><b>receivedFrom</b>: " + receivedFrom + "<br><b>receivedFromDate</b>: " + (receivedFromDate != null ? receivedFromDate.toString() : "") + "<br><b>receivedFromRef</b>: " + receivedFromRef + "<br><b>referenceNo</b>: " + "<br><b>sentTo</b>: " + sentTo + "<br><b>sentToDate</b>: " + (sentToDate != null ? sentToDate.toString() : "") + "<br><b>sentToRef</b>: " + sentToRef + "<br><b>sentVia</b>: " + sentVia + "<br><b>sentViaDate</b>: " + (sentViaDate != null ? sentViaDate.toString() : "") + "<br><b>sentViaRef</b>: " + sentViaRef + "<br><b>status</b>: " + statusId + "<br><b>subject</b>: " + subject;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getKeywordRefs() {
        return keywordRefs;
    }

    public void setKeywordRefs(String keywordRefs) {
        this.keywordRefs = keywordRefs;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public byte[] getDocBytes() {
        return docBytes;
    }

    public void setDocBytes(byte docBytes[]) {
        this.docBytes = docBytes;
    }

    public byte[] getPdfBytes() {
        return pdfBytes;
    }

    public void setPdfBytes(byte pdfBytes[]) {
        this.pdfBytes = pdfBytes;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer status) {
        statusId = status;
    }

    public Class getBaseSearchClass() {
        return getClass();
    }

    public ArrayList getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(ArrayList al) {
        orderBy = al;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDocFilePath() {
        return docFilePath;
    }

    public void setDocFilePath(String docFilePath) {
        this.docFilePath = docFilePath;
    }

    public String getPdfFilePath() {
        return pdfFilePath;
    }

    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public Date getReceivedFromDate() {
        return receivedFromDate;
    }

    public void setReceivedFromDate(Date receivedFromDate) {
        this.receivedFromDate = receivedFromDate;
    }

    public String getReceivedFromRef() {
        return receivedFromRef;
    }

    public void setReceivedFromRef(String receivedFromRef) {
        this.receivedFromRef = receivedFromRef;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public Date getSentToDate() {
        return sentToDate;
    }

    public void setSentToDate(Date sentToDate) {
        this.sentToDate = sentToDate;
    }

    public String getSentToRef() {
        return sentToRef;
    }

    public void setSentToRef(String sentToRef) {
        this.sentToRef = sentToRef;
    }

    public String getSentVia() {
        return sentVia;
    }

    public void setSentVia(String sentVia) {
        this.sentVia = sentVia;
    }

    public Date getSentViaDate() {
        return sentViaDate;
    }

    public void setSentViaDate(Date sentViaDate) {
        this.sentViaDate = sentViaDate;
    }

    public String getSentViaRef() {
        return sentViaRef;
    }

    public void setSentViaRef(String sentViaRef) {
        this.sentViaRef = sentViaRef;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean equals(Object object) {
        if (!(object instanceof SubDocumentHistory)) {
            return false;
        } else {
            SubDocumentHistory rhs = (SubDocumentHistory) object;
            return (new EqualsBuilder()).append(userId, rhs.userId).append(sentToDate, rhs.sentToDate).append(pdfFilePath, rhs.pdfFilePath).append(receivedFromRef, rhs.receivedFromRef).append(orderBy, rhs.orderBy).append(pdfBytes, rhs.pdfBytes).append(date, rhs.date).append(id, rhs.id).append(changesAsString, rhs.changesAsString).append(docFilePath, rhs.docFilePath).append(keywordRefs, rhs.keywordRefs).append(sentToRef, rhs.sentToRef).append(body, rhs.body).append(referenceNo, rhs.referenceNo).append(docBytes, rhs.docBytes).append(sentTo, rhs.sentTo).append(action, rhs.action).append(documentId, rhs.documentId).append(sentVia, rhs.sentVia).append(receivedFromDate, rhs.receivedFromDate).append(statusId, rhs.statusId).append(receivedFrom, rhs.receivedFrom).append(sentViaRef, rhs.sentViaRef).append(subject, rhs.subject).append(sentViaDate, rhs.sentViaDate).isEquals();
        }
    }

    public String toString() {
        return (new ToStringBuilder(this)).append("sentVia", sentVia).append("action", action).append("id", id).append("statusId", statusId).append("referenceNo", referenceNo).append("body", body).append("pdfBytes", pdfBytes).append("baseSearchClass", getBaseSearchClass()).append("sentViaRef", sentViaRef).append("sentToRef", sentToRef).append("date", date).append("orderBy", orderBy).append("documentId", documentId).append("userId", userId).append("changesAsString", changesAsString).append("sentToDate", sentToDate).append("keywordRefs", keywordRefs).append("receivedFromRef", receivedFromRef).append("user", user).append("receivedFrom", receivedFrom).append("sentTo", sentTo).append("docBytes", docBytes).append("docFilePath", docFilePath).append("subject", subject).append("sentViaDate", sentViaDate).append("status", status).append("receivedFromDate", receivedFromDate).append("pdfFilePath", pdfFilePath).toString();
    }

    public int hashCode() {
        return (new HashCodeBuilder(0x75b41561, 0x6286fed5)).append(userId).append(sentToDate).append(pdfFilePath).append(receivedFromRef).append(orderBy).append(pdfBytes).append(date).append(id).append(changesAsString).append(docFilePath).append(keywordRefs).append(sentToRef).append(body).append(referenceNo).append(docBytes).append(sentTo).append(user).append(action).append(documentId).append(sentVia).append(receivedFromDate).append(statusId).append(status).append(receivedFrom).append(sentViaRef).append(subject).append(sentViaDate).toHashCode();
    }
}

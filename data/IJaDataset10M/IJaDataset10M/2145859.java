package org.monet.backmobile.model.task;

import java.util.Date;
import org.monet.backmobile.model.BaseModel;

public class Fact extends BaseModel {

    private Date createDate;

    private String workplaceLabel;

    private String worklineResultLabel;

    private String workstopLabel;

    private String type;

    private String formContent;

    private String requestDocumentId;

    private String requestDocumentLabel;

    private Date requestDocumentCreateDate;

    private String responseDocumentId;

    private String responseDocumentLabel;

    private Date responseDocumentCreateDate;

    private String providerLabel;

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setWorkplaceLabel(String workplaceLabel) {
        this.workplaceLabel = workplaceLabel;
    }

    public String getWorkplaceLabel() {
        return workplaceLabel;
    }

    public void setWorklineResultLabel(String worklineResultLabel) {
        this.worklineResultLabel = worklineResultLabel;
    }

    public String getWorklineResultLabel() {
        return worklineResultLabel;
    }

    public void setWorkstopLabel(String workstopLabel) {
        this.workstopLabel = workstopLabel;
    }

    public String getWorkstopLabel() {
        return workstopLabel;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setFormContent(String formContent) {
        this.formContent = formContent;
    }

    public String getFormContent() {
        return formContent;
    }

    public void setRequestDocumentId(String requestDocumentId) {
        this.requestDocumentId = requestDocumentId;
    }

    public String getRequestDocumentId() {
        return requestDocumentId;
    }

    public void setRequestDocumentLabel(String requestDocumentLabel) {
        this.requestDocumentLabel = requestDocumentLabel;
    }

    public String getRequestDocumentLabel() {
        return requestDocumentLabel;
    }

    public void setRequestDocumentCreateDate(Date requestDocumentCreateDate) {
        this.requestDocumentCreateDate = requestDocumentCreateDate;
    }

    public Date getRequestDocumentCreateDate() {
        return requestDocumentCreateDate;
    }

    public void setResponseDocumentId(String responseDocumentId) {
        this.responseDocumentId = responseDocumentId;
    }

    public String getResponseDocumentId() {
        return responseDocumentId;
    }

    public void setResponseDocumentLabel(String responseDocumentLabel) {
        this.responseDocumentLabel = responseDocumentLabel;
    }

    public String getResponseDocumentLabel() {
        return responseDocumentLabel;
    }

    public void setResponseDocumentCreateDate(Date responseDocumentCreateDate) {
        this.responseDocumentCreateDate = responseDocumentCreateDate;
    }

    public Date getResponseDocumentCreateDate() {
        return responseDocumentCreateDate;
    }

    public void setProviderLabel(String providerLabel) {
        this.providerLabel = providerLabel;
    }

    public String getProviderLabel() {
        return providerLabel;
    }
}

package com.requestApplication.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

@PersistenceCapable
public class Request implements IsSerializable {

    public static String ASSIGNED = "ASSIGNED";

    public static String PRINTED = "PRINTED";

    public static String DELIVERED = "DELIVERED";

    @Persistent
    private String comment;

    @Persistent
    private String clientName;

    @Persistent
    private String clientLastName;

    @Persistent
    private String assignedUserId;

    @Persistent
    private int amount;

    @Persistent
    private String productCode;

    @Persistent
    private String status = ASSIGNED;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long dbId;

    @Persistent
    private Date requestDate = new Date();

    @Persistent
    private String targetName;

    @Persistent
    private String targetLastName;

    public Request() {
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetLastName() {
        return targetLastName;
    }

    public void setTargetLastName(String targetLastName) {
        this.targetLastName = targetLastName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

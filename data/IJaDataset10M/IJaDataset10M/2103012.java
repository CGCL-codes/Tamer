package com.taobao.api.model;

import java.util.Date;

/**
 * B商家保证金使用记录。
 * 
 * @author fengsheng
 * @since 1.0, Jun 30, 2009
 */
public class GuarantyFund extends TaobaoModel {

    private static final long serialVersionUID = 9012197994329277997L;

    private Long id;

    private Integer type;

    private Long amount;

    private Long balance;

    private Long sellerId;

    private String sellerNick;

    private Long buyerId;

    private String buyerNick;

    private Long orderId;

    private Long accuseId;

    private Integer operationType;

    private String operationAction;

    private String memo;

    private String creator;

    private String operator;

    private Date created;

    private Date modified;

    private Integer status;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBalance() {
        return this.balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerNick() {
        return this.sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public Long getBuyerId() {
        return this.buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerNick() {
        return this.buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getAccuseId() {
        return this.accuseId;
    }

    public void setAccuseId(Long accuseId) {
        this.accuseId = accuseId;
    }

    public Integer getOperationType() {
        return this.operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public String getOperationAction() {
        return this.operationAction;
    }

    public void setOperationAction(String operationAction) {
        this.operationAction = operationAction;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return this.modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

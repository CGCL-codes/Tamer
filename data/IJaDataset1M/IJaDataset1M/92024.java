package com.completex.objective.components.persistency.mapper.impl.domain;

import java.util.Date;

/**
 * @author Gennady Krizhevsky
 */
public class TestOrderItem {

    private Long orderItemId;

    private TestOrder order;

    private Long productId;

    private Long quantity;

    private String state;

    private Date lastUpdated;

    private Date creationDate;

    public TestOrderItem() {
    }

    public TestOrderItem(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public TestOrder getOrder() {
        return order;
    }

    public void setOrder(TestOrder order) {
        this.order = order;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}

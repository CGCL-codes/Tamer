package com.anasoft.os.daofusion.test.example.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import com.anasoft.os.daofusion.test.example.embed.Address;

@Entity
@Table(name = "orders")
public class Order extends OidBasedMutablePersistentEntity {

    private static final long serialVersionUID = 6587315771391217449L;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "street", column = @Column(name = "shipping_street")), @AttributeOverride(name = "city", column = @Column(name = "shipping_city")), @AttributeOverride(name = "zip", column = @Column(name = "shipping_zip")) })
    @AssociationOverrides({ @AssociationOverride(name = "country", joinColumns = @JoinColumn(name = "shipping_country")) })
    private Address shippingAddress;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "street", column = @Column(name = "billing_street")), @AttributeOverride(name = "city", column = @Column(name = "billing_city")), @AttributeOverride(name = "zip", column = @Column(name = "billing_zip")) })
    @AssociationOverrides({ @AssociationOverride(name = "country", joinColumns = @JoinColumn(name = "billing_country")) })
    private Address billingAddress;

    @OneToMany(mappedBy = "order")
    @Cascade(value = { CascadeType.SAVE_UPDATE, CascadeType.DELETE })
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    @Column(nullable = false)
    private Boolean complete;

    @ManyToOne
    @JoinColumn(nullable = false, updatable = false)
    private Customer customer;

    @Column
    private String description;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    protected List<OrderItem> getOrderItems() {
        return orderItems;
    }

    protected void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

    public List<OrderItem> getUnmodifiableOrderItemList() {
        return Collections.unmodifiableList(orderItems);
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalPrice() {
        int result = 0;
        for (OrderItem orderItem : orderItems) {
            result += orderItem.getTotalPrice();
        }
        return result;
    }
}

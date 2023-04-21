package com.jalarbee.core.bootik.order.business;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import com.jalarbee.core.bootik.bon.business.Bon;
import com.jalarbee.core.bootik.staff.business.Member;
import com.jalarbee.core.business.BaseEntity;
import com.jalarbee.core.money.MonetaryAmount;

@Entity
@Table(name = "Orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Bon bon;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Temporal(value = TemporalType.DATE)
    private Date creationDate;

    @Temporal(value = TemporalType.DATE)
    private Date lastModified;

    @Temporal(value = TemporalType.DATE)
    private Date approvalDate;

    @OneToOne(fetch = FetchType.LAZY)
    private Member approvedBy;

    @Type(type = "com.jalarbee.core.money.MonetaryAmountType")
    @Columns(columns = { @Column(name = "P_CURRENCY"), @Column(name = "P_AMOUNT") })
    private MonetaryAmount price;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> products;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bon getBon() {
        return bon;
    }

    public void setBon(Bon bon) {
        this.bon = bon;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Member getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Member approvedBy) {
        this.approvedBy = approvedBy;
    }

    public MonetaryAmount getPrice() {
        return price;
    }

    public void setPrice(MonetaryAmount price) {
        this.price = price;
    }

    public List<OrderItem> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Order)) {
            return false;
        }
        Order tmp = (Order) obj;
        if (id == tmp.getId()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id * 32 + approvedBy.hashCode();
    }

    @Override
    public String toString() {
        return "" + products.size() + " items for " + price;
    }
}

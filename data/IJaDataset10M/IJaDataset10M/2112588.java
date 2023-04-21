package org.jawa.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Entity
@Table(name = "orders")
public class Order extends BaseObject implements Serializable {

    private static final long serialVersionUID = -558470921618587954L;

    private Long id;

    private Shop shop;

    private Product product;

    private User user;

    private Long quantity;

    private Character status;

    private Date orderdate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOPID")
    public Shop getShop() {
        return this.shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCTID")
    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERID")
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "QUANTITY")
    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Column(name = "STATUS", length = 1)
    public Character getStatus() {
        return this.status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "ORDERDATE", length = 0)
    public Date getOrderdate() {
        return this.orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order pojo = (Order) o;
        if (shop != null ? !shop.equals(pojo.shop) : pojo.shop != null) return false;
        if (product != null ? !product.equals(pojo.product) : pojo.product != null) return false;
        if (user != null ? !user.equals(pojo.user) : pojo.user != null) return false;
        if (quantity != null ? !quantity.equals(pojo.quantity) : pojo.quantity != null) return false;
        if (status != null ? !status.equals(pojo.status) : pojo.status != null) return false;
        if (orderdate != null ? !orderdate.equals(pojo.orderdate) : pojo.orderdate != null) return false;
        return true;
    }

    public int hashCode() {
        int result = 0;
        result = (shop != null ? shop.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (orderdate != null ? orderdate.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb.append(" [");
        sb.append("id").append("='").append(getId()).append("', ");
        sb.append("shop").append("='").append(getShop()).append("', ");
        sb.append("product").append("='").append(getProduct()).append("', ");
        sb.append("user").append("='").append(getUser()).append("', ");
        sb.append("quantity").append("='").append(getQuantity()).append("', ");
        sb.append("status").append("='").append(getStatus()).append("', ");
        sb.append("orderdate").append("='").append(getOrderdate()).append("'");
        sb.append("]");
        return sb.toString();
    }
}

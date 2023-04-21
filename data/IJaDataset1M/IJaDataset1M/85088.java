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
@Table(name = "auction")
public class Auction extends BaseObject implements Serializable {

    private static final long serialVersionUID = 5951769325375208931L;

    private Long id;

    private Product product;

    private User user;

    private Double auctionprice;

    private Date auctiondate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Column(name = "AUCTIONPRICE", precision = 22, scale = 0)
    public Double getAuctionprice() {
        return this.auctionprice;
    }

    public void setAuctionprice(Double auctionprice) {
        this.auctionprice = auctionprice;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "AUCTIONDATE", length = 0)
    public Date getAuctiondate() {
        return this.auctiondate;
    }

    public void setAuctiondate(Date auctiondate) {
        this.auctiondate = auctiondate;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auction pojo = (Auction) o;
        if (product != null ? !product.equals(pojo.product) : pojo.product != null) return false;
        if (user != null ? !user.equals(pojo.user) : pojo.user != null) return false;
        if (auctionprice != null ? !auctionprice.equals(pojo.auctionprice) : pojo.auctionprice != null) return false;
        if (auctiondate != null ? !auctiondate.equals(pojo.auctiondate) : pojo.auctiondate != null) return false;
        return true;
    }

    public int hashCode() {
        int result = 0;
        result = (product != null ? product.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (auctionprice != null ? auctionprice.hashCode() : 0);
        result = 31 * result + (auctiondate != null ? auctiondate.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(getClass().getSimpleName());
        sb.append(" [");
        sb.append("id").append("='").append(getId()).append("', ");
        sb.append("product").append("='").append(getProduct()).append("', ");
        sb.append("user").append("='").append(getUser()).append("', ");
        sb.append("auctionprice").append("='").append(getAuctionprice()).append("', ");
        sb.append("auctiondate").append("='").append(getAuctiondate()).append("'");
        sb.append("]");
        return sb.toString();
    }
}

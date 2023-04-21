package com.ateam.webstore.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.jdo.annotations.PersistenceCapable;
import com.ateam.webstore.dao.common.BaseModel;

/**
 * @author Hendrix Tavarez
 *
 */
@PersistenceCapable
public class WishList extends BaseModel implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -9044899967230243877L;

    private Long id;

    private String name;

    private Date createdDate;

    private Date lastUpdated;

    private Customer customer;

    private Collection<ProductsInWishList> products;

    @SuppressWarnings("unused")
    private WishList() {
    }

    public WishList(Customer customer) {
        super();
        this.customer = customer;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addProduct(ProductsInWishList product) {
        this.products.add(product);
    }

    public void removeProduct(ProductsInWishList product) {
        this.products.remove(product);
    }

    public Collection<ProductsInWishList> getProducts() {
        return this.products;
    }
}

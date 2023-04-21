package org.nakedobjects.headlessviewer.viewer.sample.domain;

import java.util.Date;
import org.apache.log4j.Logger;
import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.util.TitleBuffer;

public class Order extends AbstractDomainObject {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(Order.class);

    /**
     * Defines the title that will be displayed on the user interface in order to identity this object.
     */
    public String title() {
        final TitleBuffer t = new TitleBuffer();
        final Product product = getProduct();
        if (product != null) {
            t.append(product.getCode());
        } else {
            t.append("???");
        }
        t.append("x", getQuantity());
        return t.toString();
    }

    private Date orderDate;

    @Disabled
    public Date getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(final Date orderDate) {
        this.orderDate = orderDate;
    }

    private Integer quantity;

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public String validateQuantity(final Integer quantity) {
        return quantity.intValue() <= 0 ? "Quantity must be a positive value" : null;
    }

    public String disableQuantity() {
        return isPersistent() ? "Already saved" : null;
    }

    public Integer defaultQuantity() {
        return new Integer(1);
    }

    private Customer customer;

    @Disabled
    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    public void modifyCustomer(final Customer customer) {
        setCustomer(customer);
    }

    public void clearCustomer() {
        setCustomer(null);
    }

    private Product product;

    @Disabled
    public Product getProduct() {
        return this.product;
    }

    public void setProduct(final Product product) {
        this.product = product;
    }

    /**
     * Capture price from product at time the order is taken.
     * 
     * @param product
     */
    public void modifyProduct(final Product product) {
        setProduct(product);
        setPrice(product.getPrice());
    }

    /**
     * Never called.
     * 
     * @param product
     */
    public void clearProduct() {
        setProduct(null);
    }

    private Double price;

    @Disabled
    public Double getPrice() {
        return this.price;
    }

    public void setPrice(final Double price) {
        this.price = price;
    }

    /**
     * Raise visibility so can be invoked by other classes.
     */
    @Override
    public void makePersistent() {
        persist(this);
    }
}

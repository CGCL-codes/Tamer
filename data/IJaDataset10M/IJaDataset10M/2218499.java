package org.xmdl.taslak.model;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;
import javax.persistence.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.xmdl.ida.lib.model.BaseObject;
import org.xmdl.mesken.model.User;

/**
 *
 * Order entity bean
 *  
 * $Id$
 *
 * @generated
 */
@Entity(name = ("TBL_ORDER"))
public class Order extends BaseObject implements Serializable, Cloneable {

    /** 
     * Unique identifier
     * 
     * @generated
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = ("ID"))
    private Long id;

    /** 
     * @generated
     */
    @Column(name = ("F_NAME"), length = 15)
    private String name;

    /** 
     * @generated
     */
    @Column(name = ("F_PRICETOTALS"), length = 15)
    private Double priceTotals;

    /** 
     * @generated
     */
    @Column(name = ("F_CREATEDATE"), length = 15)
    private Date createDate;

    /** 
     * @generated
     */
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = ("order"))
    private Set<OrderElement> orderElements;

    /** 
     * @generated
     */
    public Order() {
    }

    /** 
     * @generated
     */
    public Long getId() {
        return id;
    }

    /** 
     * @generated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /** 
     * @generated
     */
    public String getName() {
        return name;
    }

    /** 
     * @generated
     */
    public void setName(String name) {
        this.name = name;
    }

    /** 
     * @generated
     */
    public Double getPriceTotals() {
        return priceTotals;
    }

    /** 
     * @generated
     */
    public void setPriceTotals(Double priceTotals) {
        this.priceTotals = priceTotals;
    }

    /** 
     * @generated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /** 
     * @generated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /** 
     * @generated
     */
    public Set<OrderElement> getOrderElements() {
        return orderElements;
    }

    /** 
     * @generated
     */
    public void setOrderElements(Set<OrderElement> orderElements) {
        this.orderElements = orderElements;
    }

    /** 
     * @generated
     */
    public String toString() {
        return MessageFormat.format("Order [id={0}][name={1}][priceTotals={2}][createDate={3}]", id, name, priceTotals, createDate);
    }

    /** 
     * @generated
     */
    public boolean equals(Object o) {
        return o instanceof Order && ((Order) o).getId() == this.getId();
    }

    /** 
     * @generated
     */
    public int hashCode() {
        int result = 1;
        if (id != null) result = 31 * result + id.hashCode();
        if (name != null) result = 31 * result + name.hashCode();
        if (priceTotals != null) result = 31 * result + priceTotals.hashCode();
        if (createDate != null) result = 31 * result + createDate.hashCode();
        return result;
    }

    /** 
     * @generated
     */
    public Order createClone() {
        try {
            return (Order) clone();
        } catch (CloneNotSupportedException e) {
            Order copy = new Order();
            copy.name = this.name;
            copy.priceTotals = this.priceTotals;
            copy.createDate = this.createDate;
            return copy;
        }
    }
}

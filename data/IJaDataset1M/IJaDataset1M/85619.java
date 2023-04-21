package info.gdeDengi.list;

import info.gdeDengi.common.Item;
import info.gdeDengi.expense.ExpRecepient;
import java.io.Serializable;
import javax.persistence.*;
import java.math.BigInteger;

/**
 * The persistent class for the alternateitems database table.
 * 
 */
@Entity
@Table(name = "alternateitems")
public class Alternateitem implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private AlternateitemPK id;

    @Column(nullable = false, length = 50)
    private String itemname;

    @Column(name = "price_plan")
    private double pricePlan;

    @Column(name = "qty_plan")
    private Integer qtyPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "listid", referencedColumnName = "listid", nullable = false, insertable = false, updatable = false), @JoinColumn(name = "listlineid", referencedColumnName = "listlineid", nullable = false, insertable = false, updatable = false) })
    private Simplelistline simplelistline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "recepientid", referencedColumnName = "recepientid", nullable = false, insertable = false, updatable = false) })
    private ExpRecepient expRecepient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemid", nullable = false, insertable = false, updatable = false)
    private Item item;

    public Alternateitem() {
    }

    public AlternateitemPK getId() {
        return this.id;
    }

    public void setId(AlternateitemPK id) {
        this.id = id;
    }

    public String getItemname() {
        return this.itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public double getPricePlan() {
        return this.pricePlan;
    }

    public void setPricePlan(double pricePlan) {
        this.pricePlan = pricePlan;
    }

    public Integer getQtyPlan() {
        return this.qtyPlan;
    }

    public void setQtyPlan(Integer qtyPlan) {
        this.qtyPlan = qtyPlan;
    }

    public Simplelistline getSimplelistline() {
        return this.simplelistline;
    }

    public void setSimplelistline(Simplelistline simplelistline) {
        this.simplelistline = simplelistline;
    }

    public ExpRecepient getExpRecepient() {
        return this.expRecepient;
    }

    public void setExpRecepient(ExpRecepient expRecepient) {
        this.expRecepient = expRecepient;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

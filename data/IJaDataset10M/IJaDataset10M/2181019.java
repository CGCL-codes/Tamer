package org.cmsuite2.model.receipt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.cmsuite2.business.interfaces.Rate;
import org.cmsuite2.model.AbstractEntity;

@Entity
@Table(name = "cmsuite_receipt_rate")
public class ReceiptRate extends AbstractEntity<ReceiptRate> implements Rate {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "i_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "f_shippingCosts", nullable = true)
    private double shippingCosts;

    @Column(name = "f_customsCosts", nullable = true)
    private double customsCosts;

    @Column(name = "f_packCosts", nullable = true)
    private double packCosts;

    @Column(name = "f_adminCosts", nullable = true)
    private double adminCosts;

    @Column(name = "f_otherCosts", nullable = true)
    private double otherCosts;

    @Column(name = "f_duties", nullable = true)
    private double duties;

    @Column(name = "f_discountedPrice", nullable = true)
    private double discountedPrice;

    @Column(name = "f_totalPrice", nullable = false)
    private double totalPrice;

    @Column(name = "f_taxPrice", nullable = true)
    private double taxPrice;

    @Column(name = "f_finalPrice", nullable = false)
    private double finalPrice;

    @OneToOne
    private Receipt receipt;

    @Override
    public long getId() {
        return id;
    }

    public double getShippingCosts() {
        return shippingCosts;
    }

    public void setShippingCosts(double shippingCosts) {
        this.shippingCosts = shippingCosts;
    }

    public double getCustomsCosts() {
        return customsCosts;
    }

    public void setCustomsCosts(double customsCosts) {
        this.customsCosts = customsCosts;
    }

    public double getPackCosts() {
        return packCosts;
    }

    public void setPackCosts(double packCosts) {
        this.packCosts = packCosts;
    }

    public double getAdminCosts() {
        return adminCosts;
    }

    public void setAdminCosts(double adminCosts) {
        this.adminCosts = adminCosts;
    }

    public double getOtherCosts() {
        return otherCosts;
    }

    public void setOtherCosts(double otherCosts) {
        this.otherCosts = otherCosts;
    }

    public double getDuties() {
        return duties;
    }

    public void setDuties(double duties) {
        this.duties = duties;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(double taxPrice) {
        this.taxPrice = taxPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ReceiptRate other = (ReceiptRate) obj;
        if (id != other.id) return false;
        return true;
    }

    @Override
    public String toString() {
        return "ReceiptRate [id=" + id + ", shippingCosts=" + shippingCosts + ", customsCosts=" + customsCosts + ", packCosts=" + packCosts + ", adminCosts=" + adminCosts + ", otherCosts=" + otherCosts + ", duties=" + duties + ", discountedPrice=" + discountedPrice + ", totalPrice=" + totalPrice + ", taxPrice=" + taxPrice + ", finalPrice=" + finalPrice + "]";
    }
}

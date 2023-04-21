package com.etc.bin.beans;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author MaGicBank
 */
public class POSListBeans implements Serializable {

    private static final long serialVersionUID = 2L;

    private String id;

    private String bId;

    private String proCode;

    private String proName;

    private String lot;

    private String displot;

    private String unit;

    private String qty;

    private String price;

    private String vat;

    private String discount;

    private String amount;

    private BigDecimal key;

    public POSListBeans() {
    }

    public POSListBeans(String id, String bId, String proCode, String proName, String lot, String displot, String unit, String qty, String price, String vat, String discount, String amount, BigDecimal key) {
        this.id = id;
        this.bId = bId;
        this.proCode = proCode;
        this.proName = proName;
        this.lot = lot;
        this.displot = displot;
        this.unit = unit;
        this.qty = qty;
        this.price = price;
        this.vat = vat;
        this.discount = discount;
        this.amount = amount;
        this.key = key;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBId() {
        return bId;
    }

    public void setBId(String bId) {
        this.bId = bId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDisplot() {
        return displot;
    }

    public void setDisplot(String displot) {
        this.displot = displot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getKey() {
        return key;
    }

    public void setKey(BigDecimal key) {
        this.key = key;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }
}

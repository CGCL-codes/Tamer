package com.isdinvestments.cam.domain.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the ASSET database table.
 * 
 */
@Entity
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ASSET_ID_GENERATOR", sequenceName = "ASSET_ID_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ASSET_ID_GENERATOR")
    private long id;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "PRICE_DATE")
    private Date priceDate;

    private String symbol;

    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @ManyToOne
    @JoinColumn(name = "ASSET_CLASSFICATION")
    private AssetSubClassification assetSubClassification;

    @ManyToOne
    @JoinColumn(name = "STATUS")
    private AssetStatus status;

    public Asset() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPriceDate() {
        return this.priceDate;
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public AssetSubClassification getAssetSubClassification() {
        return this.assetSubClassification;
    }

    public void setAssetSubClassification(AssetSubClassification assetSubClassification) {
        this.assetSubClassification = assetSubClassification;
    }

    public AssetStatus getStatus() {
        return this.status;
    }

    public void seStatus(AssetStatus status) {
        this.status = status;
    }
}

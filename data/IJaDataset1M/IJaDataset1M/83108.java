package com.windrift.springhibernate.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Cost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "cost_date")
    private Date costDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_type_id", insertable = false, updatable = false)
    private AssetType assetType;

    @Column(name = "asset_type_id")
    private Integer assetTypeId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cost_type_id", insertable = false, updatable = false)
    private CostType costType;

    @Column(name = "cost_type_id")
    private Integer costTypeId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "comment")
    private String comment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCostDate() {
        return costDate;
    }

    public void setCostDate(Date costDate) {
        this.costDate = costDate;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public Integer getAssetTypeId() {
        return assetTypeId;
    }

    public void setAssetTypeId(Integer assetTypeId) {
        this.assetTypeId = assetTypeId;
    }

    public CostType getCostType() {
        return costType;
    }

    public void setCostType(CostType costType) {
        this.costType = costType;
    }

    public Integer getCostTypeId() {
        return costTypeId;
    }

    public void setCostTypeId(Integer costTypeId) {
        this.costTypeId = costTypeId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

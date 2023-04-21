package com.sobek.shop.dao;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Alexandra Sobek
 */
@Entity
public class Product {

    /** Creates a new instance of Product */
    public Product() {
    }

    /**
     * Holds value of property id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Getter for property id.
     * @return Value of property id.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for property id.
     * @param id New value of property id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Holds value of property shortdescription.
     */
    private String shortdescription;

    /**
     * Getter for property shortdescription.
     * @return Value of property shortdescription.
     */
    public String getShortdescription() {
        return this.shortdescription;
    }

    /**
     * Setter for property shortdescription.
     * @param shortdescription New value of property shortdescription.
     */
    public void setShortdescription(String shortdescription) {
        this.shortdescription = shortdescription;
    }

    /**
     * Holds value of property description.
     */
    private String description;

    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Holds value of property price.
     */
    private Double price;

    /**
     * Getter for property price.
     * @return Value of property price.
     */
    public Double getPrice() {
        return this.price;
    }

    /**
     * Setter for property price.
     * @param price New value of property price.
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Holds value of property creationDate.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    /**
     * Getter for property createDate.
     * @return Value of property createDate.
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * Setter for property createDate.
     * @param createDate New value of property createDate.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Holds value of property onlineStatus.
     */
    private Boolean onlineStatus;

    /**
     * Getter for property onlineStatus.
     * @return Value of property onlineStatus.
     */
    public Boolean getOnlineStatus() {
        return this.onlineStatus;
    }

    /**
     * Setter for property onlineStatus.
     * @param onlineStatus New value of property onlineStatus.
     */
    public void setOnlineStatus(Boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    /**
     * Holds value of property productGroupId.
     */
    private Long productGroupId;

    /**
     * Getter for property productGroupId.
     * @return Value of property productGroupId.
     */
    public Long getProductGroupId() {
        return this.productGroupId;
    }

    /**
     * Setter for property productGroupId.
     * @param productGroupId New value of property productGroupId.
     */
    public void setProductGroupId(Long productGroupId) {
        this.productGroupId = productGroupId;
    }

    /**
     * Holds value of property features.
     */
    @OneToMany(fetch = FetchType.EAGER)
    private List<Feature> features;

    /**
     * Getter for property features.
     * @return Value of property features.
     */
    public List<Feature> getFeatures() {
        return this.features;
    }

    /**
     * Setter for property features.
     * @param features New value of property features.
     */
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    /**
     * Holds value of property rank.
     */
    private Integer rank;

    /**
     * Getter for property rank.
     * @return Value of property rank.
     */
    public Integer getRank() {
        return this.rank;
    }

    /**
     * Setter for property rank.
     * @param rank New value of property rank.
     */
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    /**
     * Enthält den Wert der Eigenschaft  seoTags.
     */
    @OneToOne(fetch = FetchType.EAGER)
    private SEOTags seoTags;

    /**
     * Abfrage für Eigenschaft seoTags.
     * @return Wert der Eigenschaft seoTags.
     */
    public SEOTags getSeoTags() {
        return this.seoTags;
    }

    /**
     * Zuweisung für Eigenschaft seoTags.
     * @param seoTags Neuer Wert der Eigenschaft seoTags.
     */
    public void setSeoTags(SEOTags seoTags) {
        this.seoTags = seoTags;
    }

    private Boolean available;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private Manufacturer manufacturer;

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    private Long imageID;

    private Long previewImageID;

    public Long getImageID() {
        return imageID;
    }

    public void setImageID(Long imageID) {
        this.imageID = imageID;
    }

    public Long getPreviewImageID() {
        return previewImageID;
    }

    public void setPreviewImageID(Long previewImageID) {
        this.previewImageID = previewImageID;
    }
}

package com.uberdose.publisher.model.wordpress;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * WpLinkcategories generated by hbm2java
 */
@Entity
@Table(name = "wp_linkcategories", catalog = "uberdose_wordpress", uniqueConstraints = {  })
public class WpLinkcategories implements java.io.Serializable {

    private long catId;

    private String catName;

    private String autoToggle;

    private String showImages;

    private String showDescription;

    private String showRating;

    private String showUpdated;

    private String sortOrder;

    private String sortDesc;

    private String textBeforeLink;

    private String textAfterLink;

    private String textAfterAll;

    private int listLimit;

    /** default constructor */
    public WpLinkcategories() {
    }

    /** full constructor */
    public WpLinkcategories(long catId, String catName, String autoToggle, String showImages, String showDescription, String showRating, String showUpdated, String sortOrder, String sortDesc, String textBeforeLink, String textAfterLink, String textAfterAll, int listLimit) {
        this.catId = catId;
        this.catName = catName;
        this.autoToggle = autoToggle;
        this.showImages = showImages;
        this.showDescription = showDescription;
        this.showRating = showRating;
        this.showUpdated = showUpdated;
        this.sortOrder = sortOrder;
        this.sortDesc = sortDesc;
        this.textBeforeLink = textBeforeLink;
        this.textAfterLink = textAfterLink;
        this.textAfterAll = textAfterAll;
        this.listLimit = listLimit;
    }

    @Id
    @Column(name = "cat_id", unique = true, nullable = false, insertable = true, updatable = true)
    public long getCatId() {
        return this.catId;
    }

    public void setCatId(long catId) {
        this.catId = catId;
    }

    @Column(name = "cat_name", unique = false, nullable = false, insertable = true, updatable = true)
    public String getCatName() {
        return this.catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    @Column(name = "auto_toggle", unique = false, nullable = false, insertable = true, updatable = true, length = 2)
    public String getAutoToggle() {
        return this.autoToggle;
    }

    public void setAutoToggle(String autoToggle) {
        this.autoToggle = autoToggle;
    }

    @Column(name = "show_images", unique = false, nullable = false, insertable = true, updatable = true, length = 2)
    public String getShowImages() {
        return this.showImages;
    }

    public void setShowImages(String showImages) {
        this.showImages = showImages;
    }

    @Column(name = "show_description", unique = false, nullable = false, insertable = true, updatable = true, length = 2)
    public String getShowDescription() {
        return this.showDescription;
    }

    public void setShowDescription(String showDescription) {
        this.showDescription = showDescription;
    }

    @Column(name = "show_rating", unique = false, nullable = false, insertable = true, updatable = true, length = 2)
    public String getShowRating() {
        return this.showRating;
    }

    public void setShowRating(String showRating) {
        this.showRating = showRating;
    }

    @Column(name = "show_updated", unique = false, nullable = false, insertable = true, updatable = true, length = 2)
    public String getShowUpdated() {
        return this.showUpdated;
    }

    public void setShowUpdated(String showUpdated) {
        this.showUpdated = showUpdated;
    }

    @Column(name = "sort_order", unique = false, nullable = false, insertable = true, updatable = true, length = 64)
    public String getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Column(name = "sort_desc", unique = false, nullable = false, insertable = true, updatable = true, length = 2)
    public String getSortDesc() {
        return this.sortDesc;
    }

    public void setSortDesc(String sortDesc) {
        this.sortDesc = sortDesc;
    }

    @Column(name = "text_before_link", unique = false, nullable = false, insertable = true, updatable = true, length = 128)
    public String getTextBeforeLink() {
        return this.textBeforeLink;
    }

    public void setTextBeforeLink(String textBeforeLink) {
        this.textBeforeLink = textBeforeLink;
    }

    @Column(name = "text_after_link", unique = false, nullable = false, insertable = true, updatable = true, length = 128)
    public String getTextAfterLink() {
        return this.textAfterLink;
    }

    public void setTextAfterLink(String textAfterLink) {
        this.textAfterLink = textAfterLink;
    }

    @Column(name = "text_after_all", unique = false, nullable = false, insertable = true, updatable = true, length = 128)
    public String getTextAfterAll() {
        return this.textAfterAll;
    }

    public void setTextAfterAll(String textAfterAll) {
        this.textAfterAll = textAfterAll;
    }

    @Column(name = "list_limit", unique = false, nullable = false, insertable = true, updatable = true)
    public int getListLimit() {
        return this.listLimit;
    }

    public void setListLimit(int listLimit) {
        this.listLimit = listLimit;
    }
}
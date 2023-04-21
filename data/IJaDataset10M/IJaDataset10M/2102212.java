package com.codebitches.spruce.module.bb.domain.hibernate;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="sprucebb_categories"
 *     
*/
public class SprucebbCategory implements Serializable {

    /** identifier field */
    private Long catId;

    /** nullable persistent field */
    private String catTitle;

    /** persistent field */
    private int catOrder;

    /** persistent field */
    private Set sprucebbForums;

    /** full constructor */
    public SprucebbCategory(Long catId, String catTitle, int catOrder, Set sprucebbForums) {
        this.catId = catId;
        this.catTitle = catTitle;
        this.catOrder = catOrder;
        this.sprucebbForums = sprucebbForums;
    }

    /** default constructor */
    public SprucebbCategory() {
    }

    /** minimal constructor */
    public SprucebbCategory(Long catId, int catOrder, Set sprucebbForums) {
        this.catId = catId;
        this.catOrder = catOrder;
        this.sprucebbForums = sprucebbForums;
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Long"
     *             column="cat_id"
     *         
     */
    public Long getCatId() {
        return this.catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    /** 
     *            @hibernate.property
     *             column="cat_title"
     *             length="100"
     *         
     */
    public String getCatTitle() {
        return this.catTitle;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    /** 
     *            @hibernate.property
     *             column="cat_order"
     *             length="10"
     *             not-null="true"
     *         
     */
    public int getCatOrder() {
        return this.catOrder;
    }

    public void setCatOrder(int catOrder) {
        this.catOrder = catOrder;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     * 	       @hibernate.collection-key
     * 	        column="cat_id"
     *            @hibernate.collection-one-to-many
     *             class="com.codebitches.spruce.module.bb.domain.hibernate.SprucebbForum"
     *         
     */
    public Set getSprucebbForums() {
        return this.sprucebbForums;
    }

    public void setSprucebbForums(Set sprucebbForums) {
        this.sprucebbForums = sprucebbForums;
    }

    public String toString() {
        return new ToStringBuilder(this).append("catId", getCatId()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof SprucebbCategory)) return false;
        SprucebbCategory castOther = (SprucebbCategory) other;
        return new EqualsBuilder().append(this.getCatId(), castOther.getCatId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getCatId()).toHashCode();
    }
}

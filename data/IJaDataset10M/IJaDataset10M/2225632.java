package net.caece.fm.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="building"
 *     
*/
public class Building implements Serializable {

    /** identifier field */
    private Long buildingId;

    /** nullable persistent field */
    private String buildingName;

    /** nullable persistent field */
    private String buildingAddress;

    /** nullable persistent field */
    private Date operateAt;

    /** persistent field */
    private List applyOns;

    /** persistent field */
    private List buildingDocs;

    /** full constructor */
    public Building(String buildingName, String buildingAddress, Date operateAt, List applyOns, List buildingDocs) {
        this.buildingName = buildingName;
        this.buildingAddress = buildingAddress;
        this.operateAt = operateAt;
        this.applyOns = applyOns;
        this.buildingDocs = buildingDocs;
    }

    /** default constructor */
    public Building() {
    }

    /** minimal constructor */
    public Building(List applyOns, List buildingDocs) {
        this.applyOns = applyOns;
        this.buildingDocs = buildingDocs;
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Long"
     *             column="buildingid"
     *         
     */
    public Long getBuildingId() {
        return this.buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    /** 
     *            @hibernate.property
     *             column="buildingname"
     *             length="120"
     *         
     */
    public String getBuildingName() {
        return this.buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    /** 
     *            @hibernate.property
     *             column="buildingaddress"
     *             length="180"
     *         
     */
    public String getBuildingAddress() {
        return this.buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public Date getOperateAt() {
        return this.operateAt;
    }

    public void setOperateAt(Date operateAt) {
        this.operateAt = operateAt;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="buildingid"
     *            @hibernate.collection-one-to-many
     *             class="net.caece.fm.hibernate.ApplyOn"
     *         
     */
    public List getApplyOns() {
        return this.applyOns;
    }

    public void setApplyOns(List applyOns) {
        this.applyOns = applyOns;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="buildingid"
     *            @hibernate.collection-one-to-many
     *             class="net.caece.fm.hibernate.BuildingDoc"
     *         
     */
    public List getBuildingDocs() {
        return this.buildingDocs;
    }

    public void setBuildingDocs(List buildingDocs) {
        this.buildingDocs = buildingDocs;
    }

    public String toString() {
        return new ToStringBuilder(this).append("buildingId", getBuildingId()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Building)) return false;
        Building castOther = (Building) other;
        return new EqualsBuilder().append(this.getBuildingId(), castOther.getBuildingId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getBuildingId()).toHashCode();
    }
}

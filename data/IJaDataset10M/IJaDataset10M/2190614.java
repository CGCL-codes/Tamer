package com.bio.jpa.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Diego
 */
@Entity
@Table(name = "status", catalog = "biodb", schema = "")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Status.findAll", query = "SELECT s FROM Status s"), @NamedQuery(name = "Status.findByStsId", query = "SELECT s FROM Status s WHERE s.stsId = :stsId"), @NamedQuery(name = "Status.findByStsColor", query = "SELECT s FROM Status s WHERE s.stsColor = :stsColor"), @NamedQuery(name = "Status.findByStsStatus", query = "SELECT s FROM Status s WHERE s.stsStatus = :stsStatus") })
public class Status implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statusStsId")
    private List<FeatureKey> featureKeyList;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "sts_id")
    private Integer stsId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "sts_color")
    private String stsColor;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "sts_status")
    private String stsStatus;

    public Status() {
    }

    public Status(Integer stsId) {
        this.stsId = stsId;
    }

    public Status(Integer stsId, String stsColor, String stsStatus) {
        this.stsId = stsId;
        this.stsColor = stsColor;
        this.stsStatus = stsStatus;
    }

    public Integer getStsId() {
        return stsId;
    }

    public void setStsId(Integer stsId) {
        this.stsId = stsId;
    }

    public String getStsColor() {
        return stsColor;
    }

    public void setStsColor(String stsColor) {
        this.stsColor = stsColor;
    }

    public String getStsStatus() {
        return stsStatus;
    }

    public void setStsStatus(String stsStatus) {
        this.stsStatus = stsStatus;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (stsId != null ? stsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Status)) {
            return false;
        }
        Status other = (Status) object;
        if ((this.stsId == null && other.stsId != null) || (this.stsId != null && !this.stsId.equals(other.stsId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Status[ stsId=" + stsId + " ]";
    }

    @XmlTransient
    public List<FeatureKey> getFeatureKeyList() {
        return featureKeyList;
    }

    public void setFeatureKeyList(List<FeatureKey> featureKeyList) {
        this.featureKeyList = featureKeyList;
    }
}

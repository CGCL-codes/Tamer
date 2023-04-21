package com.diancai.orm;

import com.diancai.custom.TDiancai;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Misu
 */
@Entity
@Table(name = "t_diancai_zhuo_category")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "TDiancaiZhuoCategory.findAll", query = "SELECT t FROM TDiancaiZhuoCategory t"), @NamedQuery(name = "TDiancaiZhuoCategory.findByFDiancaiZhuoCategoryId", query = "SELECT t FROM TDiancaiZhuoCategory t WHERE t.fDiancaiZhuoCategoryId = :fDiancaiZhuoCategoryId"), @NamedQuery(name = "TDiancaiZhuoCategory.findByFDatetime", query = "SELECT t FROM TDiancaiZhuoCategory t WHERE t.fDatetime = :fDatetime"), @NamedQuery(name = "TDiancaiZhuoCategory.findByFTitle", query = "SELECT t FROM TDiancaiZhuoCategory t WHERE t.fTitle = :fTitle") })
public class TDiancaiZhuoCategory extends TDiancai implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 38)
    @Column(name = "F_DIANCAI_ZHUO_CATEGORY_ID", nullable = false, length = 38)
    private String fDiancaiZhuoCategoryId;

    @Column(name = "F_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fDatetime;

    @Size(max = 50)
    @Column(name = "F_TITLE", length = 50)
    private String fTitle;

    @OneToMany(mappedBy = "fDiancaiZhuoCategoryId")
    private Collection<TDiancaiZhuo> tDiancaiZhuoCollection;

    public TDiancaiZhuoCategory() {
    }

    public TDiancaiZhuoCategory(String fDiancaiZhuoCategoryId) {
        this.fDiancaiZhuoCategoryId = fDiancaiZhuoCategoryId;
    }

    public String getFDiancaiZhuoCategoryId() {
        return fDiancaiZhuoCategoryId;
    }

    public void setFDiancaiZhuoCategoryId(String fDiancaiZhuoCategoryId) {
        this.fDiancaiZhuoCategoryId = fDiancaiZhuoCategoryId;
    }

    public Date getFDatetime() {
        return fDatetime;
    }

    public void setFDatetime(Date fDatetime) {
        this.fDatetime = fDatetime;
    }

    public String getFTitle() {
        return fTitle;
    }

    public void setFTitle(String fTitle) {
        this.fTitle = fTitle;
    }

    @XmlTransient
    public Collection<TDiancaiZhuo> getTDiancaiZhuoCollection() {
        return tDiancaiZhuoCollection;
    }

    public void setTDiancaiZhuoCollection(Collection<TDiancaiZhuo> tDiancaiZhuoCollection) {
        this.tDiancaiZhuoCollection = tDiancaiZhuoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fDiancaiZhuoCategoryId != null ? fDiancaiZhuoCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TDiancaiZhuoCategory)) {
            return false;
        }
        TDiancaiZhuoCategory other = (TDiancaiZhuoCategory) object;
        if ((this.fDiancaiZhuoCategoryId == null && other.fDiancaiZhuoCategoryId != null) || (this.fDiancaiZhuoCategoryId != null && !this.fDiancaiZhuoCategoryId.equals(other.fDiancaiZhuoCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.diancai.orm.TDiancaiZhuoCategory[ fDiancaiZhuoCategoryId=" + fDiancaiZhuoCategoryId + " ]";
    }
}

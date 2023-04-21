package com.fisoft.phucsinh.phucsinhsrv.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author vantinh
 */
@Entity
@Table(name = "cm_account_type", schema = "")
@NamedQueries({ @NamedQuery(name = "CmAccountType.findAll", query = "SELECT e FROM CmAccountType e") })
public class CmAccountType implements Serializable, ICommonEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "AcctTypeID", nullable = false, length = 60)
    private String acctTypeID;

    @Column(name = "AcctTypeName", length = 60)
    private String acctTypeName;

    @JoinColumn(name = "ScreenID", referencedColumnName = "ScreenID", nullable = false)
    @ManyToOne(optional = false)
    private CmScreen screenID;

    @OneToMany(mappedBy = "acctTypeID")
    private Collection<CmAutoAcctConfig> cmAutoAcctConfigCollection;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cm_account_type_element", joinColumns = @JoinColumn(name = "AcctTypeID", referencedColumnName = "AcctTypeID"), inverseJoinColumns = @JoinColumn(name = "ElementID", referencedColumnName = "ElementID"))
    private Collection<CmElement> cmElementCollection;

    public Collection<CmElement> getCmElementCollection() {
        return cmElementCollection;
    }

    public void setCmElementCollection(Collection<CmElement> cmElementCollection) {
        this.cmElementCollection = cmElementCollection;
    }

    public CmAccountType() {
    }

    public CmAccountType(String acctTypeID) {
        this.acctTypeID = acctTypeID;
    }

    public Object getID() {
        return acctTypeID;
    }

    public void setID(Object acctTypeID) {
        this.acctTypeID = (String) acctTypeID;
    }

    public String getAcctTypeName() {
        return acctTypeName;
    }

    public void setAcctTypeName(String acctTypeName) {
        this.acctTypeName = acctTypeName;
    }

    public CmScreen getScreenID() {
        return screenID;
    }

    public void setScreenID(CmScreen screenID) {
        this.screenID = screenID;
    }

    public Collection<CmAutoAcctConfig> getCmAutoAcctConfigCollection() {
        return cmAutoAcctConfigCollection;
    }

    public void setCmAutoAcctConfigCollection(Collection<CmAutoAcctConfig> cmAutoAcctConfigCollection) {
        this.cmAutoAcctConfigCollection = cmAutoAcctConfigCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (acctTypeID != null ? acctTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CmAccountType)) {
            return false;
        }
        CmAccountType other = (CmAccountType) object;
        if ((this.acctTypeID == null && other.acctTypeID != null) || (this.acctTypeID != null && !this.acctTypeID.equals(other.acctTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fisoft.phucsinh.phucsinhsrv.entity.CmAccountType[acctTypeID=" + acctTypeID + "]";
    }

    public Integer getActiveStatus() {
        return EntityStatus.ACTIVE.getValue();
    }

    public void setActiveStatus(Integer activeStatus) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

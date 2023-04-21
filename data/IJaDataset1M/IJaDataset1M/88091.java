package com.fisoft.phucsinh.phucsinhsrv.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

/**
 *
 * @author Truong Ha
 */
@Entity
@Table(name = "iv_uomgroup_converter", uniqueConstraints = @UniqueConstraint(columnNames = { "SrcGroup", "DestGroup" }))
@NamedQueries({ @NamedQuery(name = "IvUOMGroupConverter.findAll", query = "SELECT a FROM IvUOMGroupConverter a") })
public class IvUOMGroupConverter implements Serializable, ICommonEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "GroupConverterID", nullable = false)
    private Integer groupConverterID;

    @JoinColumn(name = "SrcGroup", nullable = false)
    @ManyToOne
    private IvUnitOfMeasurementGroup srcGroup;

    @JoinColumn(name = "DestGroup", nullable = false)
    @ManyToOne
    private IvUnitOfMeasurementGroup destGroup;

    @JoinColumn(name = "SrcUnit", nullable = false)
    @ManyToOne
    private IvUnitOfMeasurement srcUnit;

    @JoinColumn(name = "DestUnit", nullable = false)
    @ManyToOne
    private IvUnitOfMeasurement destUnit;

    @Basic(optional = false)
    @Column(name = "UserRatio")
    private Double userRatio = 1d;

    @Basic(optional = false)
    @Column(name = "Ratio")
    private Double ratio = 1d;

    @Basic(optional = false)
    @Column(name = "Reversed")
    private Boolean reversed = false;

    @Basic(optional = false)
    @Column(name = "ActiveStatus", nullable = false)
    private Integer activeStatus = EntityStatus.ACTIVE.getValue();

    @Column(name = "Inputter", length = 30)
    private String inputter;

    @Column(name = "CreateDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "Authoriser", length = 30)
    private String authoriser;

    @Column(name = "AuthoriseDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date authoriseDate;

    @Version
    @Column(name = "Version")
    private int version;

    public IvUOMGroupConverter() {
    }

    public IvUOMGroupConverter(IvUnitOfMeasurementGroup srcGroup, IvUnitOfMeasurementGroup destGroup, IvUnitOfMeasurement srcUnit, IvUnitOfMeasurement destUnit) {
        this.srcGroup = srcGroup;
        this.destGroup = destGroup;
        this.srcUnit = srcUnit;
        this.destUnit = destUnit;
    }

    public Date getAuthoriseDate() {
        return authoriseDate;
    }

    public void setAuthoriseDate(Date authoriseDate) {
        this.authoriseDate = authoriseDate;
    }

    public String getAuthoriser() {
        return authoriser;
    }

    public void setAuthoriser(String authoriser) {
        this.authoriser = authoriser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getInputter() {
        return inputter;
    }

    public void setInputter(String inputter) {
        this.inputter = inputter;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Object getID() {
        return groupConverterID;
    }

    public void setID(Object ID) {
        this.groupConverterID = (Integer) ID;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public IvUnitOfMeasurementGroup getDestGroup() {
        return destGroup;
    }

    public void setDestGroup(IvUnitOfMeasurementGroup destGroup) {
        this.destGroup = destGroup;
    }

    public IvUnitOfMeasurement getDestUnit() {
        return destUnit;
    }

    public void setDestUnit(IvUnitOfMeasurement destUnit) {
        this.destUnit = destUnit;
    }

    public Integer getGroupConverterID() {
        return groupConverterID;
    }

    public void setGroupConverterID(Integer groupConverterID) {
        this.groupConverterID = groupConverterID;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public IvUnitOfMeasurementGroup getSrcGroup() {
        return srcGroup;
    }

    public void setSrcGroup(IvUnitOfMeasurementGroup srcGroup) {
        this.srcGroup = srcGroup;
    }

    public IvUnitOfMeasurement getSrcUnit() {
        return srcUnit;
    }

    public void setSrcUnit(IvUnitOfMeasurement srcUnit) {
        this.srcUnit = srcUnit;
    }

    public Double getUserRatio() {
        return userRatio;
    }

    public void setUserRatio(Double userRatio) {
        this.userRatio = userRatio;
    }

    public Boolean getReversed() {
        return reversed;
    }

    public void setReversed(Boolean reversed) {
        this.reversed = reversed;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupConverterID != null ? groupConverterID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        boolean res = false;
        if (object instanceof IvUOMGroupConverter) {
            IvUOMGroupConverter gc = (IvUOMGroupConverter) object;
            if (gc.getGroupConverterID().equals(this.getGroupConverterID())) {
                res = true;
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return "com.fisoft.phucsinh.phucsinhsrv.entity.IvUOMGroupConverter[groupConverterID=" + groupConverterID + "]";
    }
}

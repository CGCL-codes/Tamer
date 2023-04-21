package jkook.vetan.model.hrm;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity class HsHrDbVersion
 * 
 * @author kirank
 */
@Entity
@Table(name = "hs_hr_db_version")
@NamedQueries({ @NamedQuery(name = "HsHrDbVersion.findById", query = "SELECT h FROM HsHrDbVersion h WHERE h.id = :id"), @NamedQuery(name = "HsHrDbVersion.findByName", query = "SELECT h FROM HsHrDbVersion h WHERE h.name = :name"), @NamedQuery(name = "HsHrDbVersion.findByDescription", query = "SELECT h FROM HsHrDbVersion h WHERE h.description = :description"), @NamedQuery(name = "HsHrDbVersion.findByEnteredDate", query = "SELECT h FROM HsHrDbVersion h WHERE h.enteredDate = :enteredDate"), @NamedQuery(name = "HsHrDbVersion.findByModifiedDate", query = "SELECT h FROM HsHrDbVersion h WHERE h.modifiedDate = :modifiedDate") })
public class HsHrDbVersion implements Serializable {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "entered_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enteredDate;

    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @OneToMany(mappedBy = "dbVersion")
    private Collection<HsHrVersions> hsHrVersionsCollection;

    @JoinColumn(name = "entered_by", referencedColumnName = "id")
    @ManyToOne
    private HsHrUsers enteredBy;

    @JoinColumn(name = "modified_by", referencedColumnName = "id")
    @ManyToOne
    private HsHrUsers modifiedBy;

    /** Creates a new instance of HsHrDbVersion */
    public HsHrDbVersion() {
    }

    /**
     * Creates a new instance of HsHrDbVersion with the specified values.
     * @param id the id of the HsHrDbVersion
     */
    public HsHrDbVersion(String id) {
        this.id = id;
    }

    /**
     * Gets the id of this HsHrDbVersion.
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the id of this HsHrDbVersion to the specified value.
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of this HsHrDbVersion.
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of this HsHrDbVersion to the specified value.
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of this HsHrDbVersion.
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of this HsHrDbVersion to the specified value.
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the enteredDate of this HsHrDbVersion.
     * @return the enteredDate
     */
    public Date getEnteredDate() {
        return this.enteredDate;
    }

    /**
     * Sets the enteredDate of this HsHrDbVersion to the specified value.
     * @param enteredDate the new enteredDate
     */
    public void setEnteredDate(Date enteredDate) {
        this.enteredDate = enteredDate;
    }

    /**
     * Gets the modifiedDate of this HsHrDbVersion.
     * @return the modifiedDate
     */
    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    /**
     * Sets the modifiedDate of this HsHrDbVersion to the specified value.
     * @param modifiedDate the new modifiedDate
     */
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    /**
     * Gets the hsHrVersionsCollection of this HsHrDbVersion.
     * @return the hsHrVersionsCollection
     */
    public Collection<HsHrVersions> getHsHrVersionsCollection() {
        return this.hsHrVersionsCollection;
    }

    /**
     * Sets the hsHrVersionsCollection of this HsHrDbVersion to the specified value.
     * @param hsHrVersionsCollection the new hsHrVersionsCollection
     */
    public void setHsHrVersionsCollection(Collection<HsHrVersions> hsHrVersionsCollection) {
        this.hsHrVersionsCollection = hsHrVersionsCollection;
    }

    /**
     * Gets the enteredBy of this HsHrDbVersion.
     * @return the enteredBy
     */
    public HsHrUsers getEnteredBy() {
        return this.enteredBy;
    }

    /**
     * Sets the enteredBy of this HsHrDbVersion to the specified value.
     * @param enteredBy the new enteredBy
     */
    public void setEnteredBy(HsHrUsers enteredBy) {
        this.enteredBy = enteredBy;
    }

    /**
     * Gets the modifiedBy of this HsHrDbVersion.
     * @return the modifiedBy
     */
    public HsHrUsers getModifiedBy() {
        return this.modifiedBy;
    }

    /**
     * Sets the modifiedBy of this HsHrDbVersion to the specified value.
     * @param modifiedBy the new modifiedBy
     */
    public void setModifiedBy(HsHrUsers modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this HsHrDbVersion.  The result is 
     * <code>true</code> if and only if the argument is not null and is a HsHrDbVersion object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HsHrDbVersion)) {
            return false;
        }
        HsHrDbVersion other = (HsHrDbVersion) object;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "jkook.vetan.model.hrm.HsHrDbVersion[id=" + id + "]";
    }
}

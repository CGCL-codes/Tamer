package jkook.vetan.model.hrm;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity class HsHrModule
 * 
 * @author kirank
 */
@Entity
@Table(name = "hs_hr_module")
@NamedQueries({ @NamedQuery(name = "HsHrModule.findByModId", query = "SELECT h FROM HsHrModule h WHERE h.modId = :modId"), @NamedQuery(name = "HsHrModule.findByName", query = "SELECT h FROM HsHrModule h WHERE h.name = :name"), @NamedQuery(name = "HsHrModule.findByOwner", query = "SELECT h FROM HsHrModule h WHERE h.owner = :owner"), @NamedQuery(name = "HsHrModule.findByOwnerEmail", query = "SELECT h FROM HsHrModule h WHERE h.ownerEmail = :ownerEmail") })
public class HsHrModule implements Serializable {

    @Id
    @Column(name = "mod_id", nullable = false)
    private String modId;

    @Column(name = "name")
    private String name;

    @Column(name = "owner")
    private String owner;

    @Column(name = "owner_email")
    private String ownerEmail;

    @Lob
    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hsHrModule")
    private Collection<HsHrRights> hsHrRightsCollection;

    @OneToMany(mappedBy = "alteredModule")
    private Collection<HsHrFileVersion> hsHrFileVersionCollection;

    @JoinColumn(name = "version", referencedColumnName = "id")
    @ManyToOne
    private HsHrVersions version;

    /** Creates a new instance of HsHrModule */
    public HsHrModule() {
    }

    /**
     * Creates a new instance of HsHrModule with the specified values.
     * @param modId the modId of the HsHrModule
     */
    public HsHrModule(String modId) {
        this.modId = modId;
    }

    /**
     * Gets the modId of this HsHrModule.
     * @return the modId
     */
    public String getModId() {
        return this.modId;
    }

    /**
     * Sets the modId of this HsHrModule to the specified value.
     * @param modId the new modId
     */
    public void setModId(String modId) {
        this.modId = modId;
    }

    /**
     * Gets the name of this HsHrModule.
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of this HsHrModule to the specified value.
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the owner of this HsHrModule.
     * @return the owner
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * Sets the owner of this HsHrModule to the specified value.
     * @param owner the new owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * Gets the ownerEmail of this HsHrModule.
     * @return the ownerEmail
     */
    public String getOwnerEmail() {
        return this.ownerEmail;
    }

    /**
     * Sets the ownerEmail of this HsHrModule to the specified value.
     * @param ownerEmail the new ownerEmail
     */
    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    /**
     * Gets the description of this HsHrModule.
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of this HsHrModule to the specified value.
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the hsHrRightsCollection of this HsHrModule.
     * @return the hsHrRightsCollection
     */
    public Collection<HsHrRights> getHsHrRightsCollection() {
        return this.hsHrRightsCollection;
    }

    /**
     * Sets the hsHrRightsCollection of this HsHrModule to the specified value.
     * @param hsHrRightsCollection the new hsHrRightsCollection
     */
    public void setHsHrRightsCollection(Collection<HsHrRights> hsHrRightsCollection) {
        this.hsHrRightsCollection = hsHrRightsCollection;
    }

    /**
     * Gets the hsHrFileVersionCollection of this HsHrModule.
     * @return the hsHrFileVersionCollection
     */
    public Collection<HsHrFileVersion> getHsHrFileVersionCollection() {
        return this.hsHrFileVersionCollection;
    }

    /**
     * Sets the hsHrFileVersionCollection of this HsHrModule to the specified value.
     * @param hsHrFileVersionCollection the new hsHrFileVersionCollection
     */
    public void setHsHrFileVersionCollection(Collection<HsHrFileVersion> hsHrFileVersionCollection) {
        this.hsHrFileVersionCollection = hsHrFileVersionCollection;
    }

    /**
     * Gets the version of this HsHrModule.
     * @return the version
     */
    public HsHrVersions getVersion() {
        return this.version;
    }

    /**
     * Sets the version of this HsHrModule to the specified value.
     * @param version the new version
     */
    public void setVersion(HsHrVersions version) {
        this.version = version;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.modId != null ? this.modId.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this HsHrModule.  The result is 
     * <code>true</code> if and only if the argument is not null and is a HsHrModule object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HsHrModule)) {
            return false;
        }
        HsHrModule other = (HsHrModule) object;
        if (this.modId != other.modId && (this.modId == null || !this.modId.equals(other.modId))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "jkook.vetan.model.hrm.HsHrModule[modId=" + modId + "]";
    }
}

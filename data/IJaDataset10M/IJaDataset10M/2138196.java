package edu.upmc.opi.caBIG.caTIES.database.comm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.log4j.Logger;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

/**
 * The Class PatientImpl.
 */
@Entity
@Table(name = "PATIENT")
@org.hibernate.annotations.Entity(selectBeforeUpdate = true, dynamicInsert = true, dynamicUpdate = true)
@org.hibernate.annotations.Proxy(lazy = true)
@BatchSize(size = 5)
public class PatientImpl implements java.io.Serializable {

    /**
	 * Field logger.
	 */
    private static final Logger logger = Logger.getLogger(PatientImpl.class);

    /**
	 * The Constant serialVersionUID.
	 */
    private static final long serialVersionUID = 1234567890L;

    /**
	 * The id.
	 */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "hibseq")
    @GenericGenerator(name = "hibseq", strategy = "edu.upmc.opi.caBIG.caTIES.database.ExistingIDPreservingTableHiLoGenerator", parameters = { @org.hibernate.annotations.Parameter(name = "table", value = "HIBERNATE_UNIQUE_KEY"), @org.hibernate.annotations.Parameter(name = "column", value = "NEXT_HI") })
    protected java.lang.Long id;

    /**
	 * Gets the id.
	 * 
	 * @return the id
	 */
    public java.lang.Long getId() {
        return id;
    }

    /**
	 * Sets the id.
	 * 
	 * @param id
	 *            the id
	 */
    public void setId(java.lang.Long id) {
        this.id = id;
    }

    /**
	 * The uuid.
	 */
    @Column(name = "UUID", length = 50)
    protected java.lang.String uuid;

    /**
	 * Gets the uuid.
	 * 
	 * @return the uuid
	 */
    public java.lang.String getUuid() {
        return uuid;
    }

    /**
	 * Sets the uuid.
	 * 
	 * @param uuid
	 *            the uuid
	 */
    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }

    /**
	 * The race.
	 */
    @Column(name = "RACE", length = 50)
    protected java.lang.String race;

    /**
	 * Gets the race.
	 * 
	 * @return the race
	 */
    public java.lang.String getRace() {
        return race;
    }

    /**
	 * Sets the race.
	 * 
	 * @param race
	 *            the race
	 */
    public void setRace(java.lang.String race) {
        this.race = race;
    }

    /**
	 * The ethnicity.
	 */
    @Column(name = "ETHNICITY", length = 50)
    protected java.lang.String ethnicity;

    /**
	 * Gets the ethnicity.
	 * 
	 * @return the ethnicity
	 */
    public java.lang.String getEthnicity() {
        return ethnicity;
    }

    /**
	 * Sets the ethnicity.
	 * 
	 * @param ethnicity
	 *            the ethnicity
	 */
    public void setEthnicity(java.lang.String ethnicity) {
        this.ethnicity = ethnicity;
    }

    /**
	 * The birth date.
	 */
    @Column(name = "BIRTH_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    protected java.util.Date birthDate;

    /**
	 * Gets the birth date.
	 * 
	 * @return the birth date
	 */
    public java.util.Date getBirthDate() {
        return birthDate;
    }

    /**
	 * Sets the birth date.
	 * 
	 * @param birthDate
	 *            the birth date
	 */
    public void setBirthDate(java.util.Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
	 * The gender.
	 */
    @Column(name = "GENDER", length = 20)
    protected java.lang.String gender;

    /**
	 * Gets the gender.
	 * 
	 * @return the gender
	 */
    public java.lang.String getGender() {
        return gender;
    }

    /**
	 * Sets the gender.
	 * 
	 * @param gender
	 *            the gender
	 */
    public void setGender(java.lang.String gender) {
        this.gender = gender;
    }

    /**
	 * The pathology report collection.
	 */
    @OneToMany(targetEntity = DocumentImpl.class)
    @JoinColumn(name = "PATIENT_ID")
    private java.util.Collection documentCollection = new java.util.HashSet();

    /**
	 * Gets the pathology report collection.
	 * 
	 * @return the pathology report collection
	 */
    public java.util.Collection<DocumentImpl> getDocumentCollection() {
        return documentCollection;
    }

    /**
	 * Sets the pathology report collection.
	 * 
	 * @param documentCollection
	 *            the pathology report collection
	 */
    public void setDocumentCollection(java.util.Collection<DocumentImpl> documentCollection) {
        this.documentCollection = documentCollection;
    }

    /**
	 * Adds the pathology report.
	 * 
	 * @param document
	 *            the pathology report
	 */
    public void addDocument(DocumentImpl document) {
        this.documentCollection.add(document);
        document.setPatient(this);
    }

    /**
	 * The organization.
	 */
    @ManyToOne(targetEntity = OrganizationImpl.class)
    @JoinColumn(name = "ORG_ID")
    private OrganizationImpl organization;

    /**
	 * Gets the organization.
	 * 
	 * @return the organization
	 */
    public OrganizationImpl getOrganization() {
        return organization;
    }

    /**
	 * Sets the organization.
	 * 
	 * @param organization
	 *            the organization
	 */
    public void setOrganization(OrganizationImpl organization) {
        this.organization = organization;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName() + "\n");
        sb.append("ID ==> " + this.id + "\n");
        sb.append("UUID ==> " + this.uuid + "\n");
        sb.append("RACE ==> " + this.race + "\n");
        sb.append("ETHNICITY ==> " + this.ethnicity + "\n");
        sb.append("BIRTH_DATE ==> " + this.birthDate + "\n");
        sb.append("GENDER ==> " + this.gender + "\n");
        return sb.toString();
    }
}

package edu.upmc.opi.caBIG.caTIES.database.comm;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

/**
 * The Class DocumentImpl.
 */
@Entity
@Table(name = "DOCUMENT")
@org.hibernate.annotations.Entity(selectBeforeUpdate = true, dynamicInsert = true, dynamicUpdate = true)
@org.hibernate.annotations.Proxy(lazy = true)
@BatchSize(size = 5)
public class DocumentImpl implements java.io.Serializable {

    /**
	 * The id.
	 */
    @Id
    @Column(name = "ID")
    @GenericGenerator(name = "hibseq", strategy = "edu.upmc.opi.caBIG.caTIES.database.ExistingIDPreservingTableHiLoGenerator", parameters = { @org.hibernate.annotations.Parameter(name = "table", value = "HIBERNATE_UNIQUE_KEY"), @org.hibernate.annotations.Parameter(name = "column", value = "NEXT_HI") })
    @GeneratedValue(generator = "hibseq")
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
    @Column(name = "UUID", length = 36)
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

    @Column(name = "LAST_DEIDENTIFIED")
    @Temporal(TemporalType.TIMESTAMP)
    protected java.util.Date lastDeidentified;

    @Column(name = "LAST_CODED")
    @Temporal(TemporalType.TIMESTAMP)
    protected java.util.Date lastCoded;

    @Column(name = "LAST_INDEXED")
    @Temporal(TemporalType.TIMESTAMP)
    protected java.util.Date lastIndexed;

    public java.util.Date getLastDeidentified() {
        return lastDeidentified;
    }

    public void setLastDeidentified(java.util.Date lastDeidentified) {
        this.lastDeidentified = lastDeidentified;
    }

    public java.util.Date getLastCoded() {
        return lastCoded;
    }

    public void setLastCoded(java.util.Date lastCoded) {
        this.lastCoded = lastCoded;
    }

    public java.util.Date getLastIndexed() {
        return lastIndexed;
    }

    public void setLastIndexed(java.util.Date lastIndexed) {
        this.lastIndexed = lastIndexed;
    }

    /**
	 * The collection date time.
	 */
    @Column(name = "COLLECTION_DATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    protected java.util.Date collectionDateTime;

    /**
	 * Gets the collection date time.
	 * 
	 * @return the collection date time
	 */
    public java.util.Date getCollectionDateTime() {
        return collectionDateTime;
    }

    @Column(name = "COLLECTION_YEAR")
    protected java.lang.Integer collectionYear;

    public Integer getCollectionYear() {
        return this.collectionYear;
    }

    public void setCollectionYear(Integer collectionYear) {
        this.collectionYear = collectionYear;
    }

    /**
	 * Sets the collection date time.
	 * 
	 * @param collectionDateTime
	 *            the collection date time
	 */
    public void setCollectionDateTime(java.util.Date collectionDateTime) {
        this.collectionDateTime = collectionDateTime;
    }

    /**
	 * The patient age at collection.
	 */
    @Column(name = "PATIENT_AGE_AT_COLLECTION")
    protected java.lang.Integer patientAgeAtCollection;

    /**
	 * Gets the patient age at collection.
	 * 
	 * @return the patient age at collection
	 */
    public java.lang.Integer getPatientAgeAtCollection() {
        return patientAgeAtCollection;
    }

    /**
	 * Sets the patient age at collection.
	 * 
	 * @param patientAgeAtCollection
	 *            the patient age at collection
	 */
    public void setPatientAgeAtCollection(java.lang.Integer patientAgeAtCollection) {
        this.patientAgeAtCollection = patientAgeAtCollection;
    }

    @Column(name = "IS_FLAGGED_FOR_REVIEW")
    protected java.lang.Boolean isFlaggedForReview;

    /**
	 * Gets the is flagged for review.
	 * 
	 * @return the is flagged for review
	 */
    public java.lang.Boolean getIsFlaggedForReview() {
        return isFlaggedForReview;
    }

    /**
	 * Sets the is flagged for review.
	 * 
	 * @param isFlaggedForReview
	 *            the is flagged for review
	 */
    public void setIsFlaggedForReview(java.lang.Boolean isFlaggedForReview) {
        this.isFlaggedForReview = isFlaggedForReview;
    }

    @Column(name = "IS_PROXY")
    protected java.lang.Boolean isProxy;

    public java.lang.Boolean getIsProxy() {
        return isProxy;
    }

    public void setIsProxy(java.lang.Boolean isProxy) {
        this.isProxy = isProxy;
    }

    /**
	 * The is tissue available.
	 */
    @Column(name = "IS_TISSUE_AVAILABLE")
    protected java.lang.Boolean isTissueAvailable;

    /**
	 * Gets the is tissue available.
	 * 
	 * @return the is tissue available
	 */
    public java.lang.Boolean getIsTissueAvailable() {
        return isTissueAvailable;
    }

    /**
	 * Sets the is tissue available.
	 * 
	 * @param isTissueAvailable
	 *            the is tissue available
	 */
    public void setIsTissueAvailable(java.lang.Boolean isTissueAvailable) {
        this.isTissueAvailable = isTissueAvailable;
    }

    /**
	 * The is quarantined.
	 */
    @Column(name = "IS_QUARANTINED")
    protected java.lang.Boolean isQuarantined;

    /**
	 * Gets the is quarantined.
	 * 
	 * @return the is quarantined
	 */
    public java.lang.Boolean getIsQuarantined() {
        return isQuarantined;
    }

    /**
	 * Sets the is quarantined.
	 * 
	 * @param isQuarantined
	 *            the is quarantined
	 */
    public void setIsQuarantined(java.lang.Boolean isQuarantined) {
        this.isQuarantined = isQuarantined;
    }

    /**
	 * The patient.
	 */
    @ManyToOne(targetEntity = PatientImpl.class)
    @JoinColumn(name = "PATIENT_ID")
    private PatientImpl patient;

    /**
	 * Gets the patient.
	 * 
	 * @return the patient
	 */
    public PatientImpl getPatient() {
        return patient;
    }

    /**
	 * Sets the patient.
	 * 
	 * @param patient
	 *            the patient
	 */
    public void setPatient(PatientImpl patient) {
        this.patient = patient;
    }

    /**
	 * The section collection.
	 */
    @OneToMany(targetEntity = SectionImpl.class)
    @JoinColumn(name = "DOCUMENT_ID")
    private java.util.Collection<SectionImpl> sectionCollection = new java.util.HashSet<SectionImpl>();

    /**
	 * Gets the section collection.
	 * 
	 * @return the section collection
	 */
    public java.util.Collection<SectionImpl> getSectionCollection() {
        return sectionCollection;
    }

    /**
	 * Sets the section collection.
	 * 
	 * @param sectionCollection
	 *            the section collection
	 */
    public void setSectionCollection(java.util.Collection<SectionImpl> sectionCollection) {
        this.sectionCollection = sectionCollection;
    }

    /**
	 * Adds the section.
	 * 
	 * @param section
	 *            the section
	 */
    public void addSection(SectionImpl section) {
        this.sectionCollection.add(section);
        section.setDocument(this);
    }

    public void removeSection(SectionImpl section) {
        this.sectionCollection.remove(section);
        section.setDocument(null);
    }

    /**
	 * The OrganizationImpl.
	 */
    @ManyToOne(targetEntity = OrganizationImpl.class)
    @JoinColumn(name = "ORG_ID", updatable = false, insertable = false)
    private OrganizationImpl organization;

    /**
	 * Gets the OrganizationImpl.
	 * 
	 * @return the OrganizationImpl
	 */
    public OrganizationImpl getOrganization() {
        return organization;
    }

    /**
	 * Sets the OrganizationImpl.
	 * 
	 * @param OrganizationImpl
	 *            the OrganizationImpl
	 */
    public void setOrganization(OrganizationImpl organization) {
        this.organization = organization;
    }

    /**
	 * Equals.
	 * 
	 * @param obj
	 *            the obj
	 * 
	 * @return true, if equals
	 */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        boolean eq = false;
        if (obj instanceof DocumentImpl) {
            DocumentImpl c = (DocumentImpl) obj;
            String thisUUID = getUuid();
            if (this.uuid != null && thisUUID.equals(c.getUuid())) {
                eq = true;
            }
        }
        return eq;
    }

    /**
	 * The application status.
	 */
    @Column(name = "APPLICATION_STATUS", length = 50)
    private String applicationStatus;

    public String getApplicationStatus() {
        return this.applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "document")
    @JoinColumn(name = "document_id")
    private DocumentDataImpl documentData;

    public DocumentDataImpl getDocumentData() {
        return documentData;
    }

    public void setDocumentData(DocumentDataImpl documentData) {
        this.documentData = documentData;
        if (this.documentData != null) {
            this.documentData.setDocument(this);
        }
    }

    /**
     * The document type.
     */
    @ManyToOne(targetEntity = DocumentTypeImpl.class)
    @JoinColumn(name = "DOCUMENT_TYPE_ID", updatable = false, insertable = false)
    protected DocumentTypeImpl documentType;

    /**
     * Gets the document type.
     * 
     * @return the document type.
     */
    public DocumentTypeImpl getDocumentType() {
        return this.documentType;
    }

    /**
     * Sets the document type
     * 
     * @param documentType
     *            the document type.
     */
    public void setDocumentType(DocumentTypeImpl documentType) {
        this.documentType = documentType;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName() + "\n");
        sb.append("ID ==> " + this.id + "\n");
        sb.append("UUID ==> " + this.uuid + "\n");
        sb.append("IS_PROXY ==> " + this.isProxy + "\n");
        sb.append("COLLECTION_DATE_TIME ==> " + this.collectionDateTime + "\n");
        sb.append("COLLECTION_YEAR ==> " + this.collectionYear + "\n");
        sb.append("PATIENT_AGE_AT_COLLECTION ==> " + this.patientAgeAtCollection + "\n");
        sb.append("APPLICATION_STATUS ==> " + this.applicationStatus + "\n");
        sb.append("IS_FLAGGED_FOR_REVIEW ==> " + this.isFlaggedForReview + "\n");
        sb.append("IS_TISSUE_AVAILABLE ==> " + this.isTissueAvailable + "\n");
        sb.append("IS_QUARANTINED ==> " + this.isQuarantined + "\n");
        sb.append("LAST_DEIDENTIFIED ==> " + this.lastDeidentified + "\n");
        sb.append("LAST_CODED ==> " + this.lastCoded + "\n");
        sb.append("LAST_INDEXED ==> " + this.lastIndexed + "\n");
        return sb.toString();
    }
}

package uk.icat3.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import uk.icat3.util.ElementType;

/**
 * Entity class StudyInvestigation
 * 
 * @author gjd37
 */
@Entity
@Table(name = "STUDY_INVESTIGATION")
@NamedQueries({ @NamedQuery(name = "StudyInvestigation.findByStudyId", query = "SELECT s FROM StudyInvestigation s WHERE s.studyInvestigationPK.studyId = :studyId"), @NamedQuery(name = "StudyInvestigation.findByInvestigationId", query = "SELECT s FROM StudyInvestigation s WHERE s.studyInvestigationPK.investigationId = :investigationId"), @NamedQuery(name = "StudyInvestigation.findByInvestigationVisitId", query = "SELECT s FROM StudyInvestigation s WHERE s.investigationVisitId = :investigationVisitId"), @NamedQuery(name = "StudyInvestigation.findByModId", query = "SELECT s FROM StudyInvestigation s WHERE s.modId = :modId"), @NamedQuery(name = "StudyInvestigation.findByModTime", query = "SELECT s FROM StudyInvestigation s WHERE s.modTime = :modTime") })
public class StudyInvestigation extends EntityBaseBean implements Serializable {

    /**
     * EmbeddedId primary key field
     */
    @EmbeddedId
    protected StudyInvestigationPK studyInvestigationPK;

    @Column(name = "INVESTIGATION_VISIT_ID", nullable = false)
    private String investigationVisitId;

    @JoinColumn(name = "STUDY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private Investigation investigation;

    @JoinColumn(name = "STUDY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    @ICAT(merge = false)
    private Study study;

    /** Creates a new instance of StudyInvestigation */
    public StudyInvestigation() {
    }

    /**
     * Creates a new instance of StudyInvestigation with the specified values.
     * @param studyInvestigationPK the studyInvestigationPK of the StudyInvestigation
     */
    public StudyInvestigation(StudyInvestigationPK studyInvestigationPK) {
        this.studyInvestigationPK = studyInvestigationPK;
    }

    /**
     * Creates a new instance of StudyInvestigation with the specified values.
     * @param studyInvestigationPK the studyInvestigationPK of the StudyInvestigation
     * @param investigationVisitId the investigationVisitId of the StudyInvestigation
     * @param modId the modId of the StudyInvestigation
     * @param modTime the modTime of the StudyInvestigation
     */
    public StudyInvestigation(StudyInvestigationPK studyInvestigationPK, String investigationVisitId, String modId, Date modTime) {
        this.studyInvestigationPK = studyInvestigationPK;
        this.investigationVisitId = investigationVisitId;
        this.modId = modId;
        this.modTime = modTime;
    }

    /**
     * Creates a new instance of StudyInvestigationPK with the specified values.
     * @param investigationId the investigationId of the StudyInvestigationPK
     * @param studyId the studyId of the StudyInvestigationPK
     */
    public StudyInvestigation(Long investigationId, Long studyId) {
        this.studyInvestigationPK = new StudyInvestigationPK(investigationId, studyId);
    }

    /**
     * Gets the studyInvestigationPK of this StudyInvestigation.
     * @return the studyInvestigationPK
     */
    public StudyInvestigationPK getStudyInvestigationPK() {
        return this.studyInvestigationPK;
    }

    /**
     * Sets the studyInvestigationPK of this StudyInvestigation to the specified value.
     * @param studyInvestigationPK the new studyInvestigationPK
     */
    public void setStudyInvestigationPK(StudyInvestigationPK studyInvestigationPK) {
        this.studyInvestigationPK = studyInvestigationPK;
    }

    /**
     * Gets the investigationVisitId of this StudyInvestigation.
     * @return the investigationVisitId
     */
    public String getInvestigationVisitId() {
        return this.investigationVisitId;
    }

    /**
     * Sets the investigationVisitId of this StudyInvestigation to the specified value.
     * @param investigationVisitId the new investigationVisitId
     */
    public void setInvestigationVisitId(String investigationVisitId) {
        this.investigationVisitId = investigationVisitId;
    }

    /**
     * Gets the investigation of this StudyInvestigation.
     * @return the investigation
     */
    public Investigation getInvestigation() {
        return this.investigation;
    }

    /**
     * Sets the investigation of this StudyInvestigation to the specified value.
     * @param investigation the new investigation
     */
    public void setInvestigation(Investigation investigation) {
        this.investigation = investigation;
    }

    /**
     * Gets the study of this StudyInvestigation.
     * @return the study
     */
    public Study getStudy() {
        return this.study;
    }

    /**
     * Sets the study of this StudyInvestigation to the specified value.
     * @param study the new study
     */
    public void setStudy(Study study) {
        this.study = study;
    }

    /**
     * Gets the element type of the bean
     */
    public ElementType getRootElementType() {
        return ElementType.STUDY;
    }

    /**
     * Returns a hash code value for the object.  This implementation computes 
     * a hash code value based on the id fields in this object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.studyInvestigationPK != null ? this.studyInvestigationPK.hashCode() : 0);
        return hash;
    }

    /**
     * Determines whether another object is equal to this StudyInvestigation.  The result is 
     * <code>true</code> if and only if the argument is not null and is a StudyInvestigation object that 
     * has the same id field values as this object.
     * @param object the reference object with which to compare
     * @return <code>true</code> if this object is the same as the argument;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof StudyInvestigation)) {
            return false;
        }
        StudyInvestigation other = (StudyInvestigation) object;
        if (this.studyInvestigationPK != other.studyInvestigationPK && (this.studyInvestigationPK == null || !this.studyInvestigationPK.equals(other.studyInvestigationPK))) return false;
        return true;
    }

    /**
     * Returns a string representation of the object.  This implementation constructs 
     * that representation based on the id fields.
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return "StudyInvestigation[studyInvestigationPK=" + studyInvestigationPK + "]";
    }
}

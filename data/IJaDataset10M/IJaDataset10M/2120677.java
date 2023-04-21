package edu.upmc.opi.caBIG.caTIES.database.domain;

import java.util.TreeSet;

/**
 * The Interface Patient.<br>
 * <br>
 * Table mapped to: PATIENT<br>
 * <br>
 * Members:<br>
 * <br>
 * id : id of the object<br>
 * uuid : uuid of the object<br>
 * version : version of the object. Used by Hibernate for caching.<br>
 * race :<br>
 * ethnicity :<br>
 * birthDate :<br>
 * gender :<br>
 * conceptCodeSet : aggregation of all meaningful concepts for this patient<br>
 *                  derived across all pathology reports<br>
 * conceptReferentCollection : normalized dual of conceptCodeSet<br>
 * pathologyReportCollection : reports for this patient<br>
 * organization : organization that documents this patient<br>
 * 
 */
public interface Patient {

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public java.lang.Long getId();

    /**
     * Sets the id.
     * 
     * @param id the id
     */
    public void setId(java.lang.Long id);

    /**
     * Gets the version.
     * 
     * @return the version
     */
    public java.lang.Long getVersion();

    /**
     * Sets the version.
     * 
     * @param version the version
     */
    public void setVersion(java.lang.Long version);

    /**
     * Gets the uuid.
     * 
     * @return the uuid
     */
    public java.lang.String getUuid();

    /**
     * Sets the uuid.
     * 
     * @param uuid the uuid
     */
    public void setUuid(java.lang.String uuid);

    /**
     * Gets the race.
     * 
     * @return the race
     */
    public java.lang.String getRace();

    /**
     * Sets the race.
     * 
     * @param race the race
     */
    public void setRace(java.lang.String race);

    /**
     * Gets the ethnicity.
     * 
     * @return the ethnicity
     */
    public java.lang.String getEthnicity();

    /**
     * Sets the ethnicity.
     * 
     * @param ethnicity the ethnicity
     */
    public void setEthnicity(java.lang.String ethnicity);

    /**
     * Gets the birth date.
     * 
     * @return the birth date
     */
    public java.util.Date getBirthDate();

    /**
     * Sets the birth date.
     * 
     * @param birthDate the birth date
     */
    public void setBirthDate(java.util.Date birthDate);

    /**
     * Gets the gender.
     * 
     * @return the gender
     */
    public java.lang.String getGender();

    /**
     * Sets the gender.
     * 
     * @param gender the gender
     */
    public void setGender(java.lang.String gender);

    /**
     * Gets the concept code set.
     * 
     * @return the concept code set
     */
    public java.lang.String getConceptCodeSet();

    /**
     * Sets the concept code set.
     * 
     * @param conceptCodeSet the concept code set
     */
    public void setConceptCodeSet(java.lang.String conceptCodeSet);

    /**
     * Gets the concept referent collection.
     * 
     * @return the concept referent collection
     */
    public java.util.Collection getConceptReferentCollection();

    /**
     * Sets the concept referent collection.
     * 
     * @param conceptReferentCollection the concept referent collection
     */
    public void setConceptReferentCollection(java.util.Collection conceptReferentCollection);

    /**
     * Adds the concept referent.
     * 
     * @param conceptReferent the concept referent
     */
    public void addConceptReferent(edu.upmc.opi.caBIG.caTIES.database.domain.ConceptReferent conceptReferent);

    /**
     * Gets the pathology report collection.
     * 
     * @return the pathology report collection
     */
    public java.util.Collection getPathologyReportCollection();

    /**
     * Sets the pathology report collection.
     * 
     * @param pathologyReportCollection the pathology report collection
     */
    public void setPathologyReportCollection(java.util.Collection pathologyReportCollection);

    /**
     * Adds the pathology report.
     * 
     * @param pathologyReport the pathology report
     */
    public void addPathologyReport(edu.upmc.opi.caBIG.caTIES.database.domain.PathologyReport pathologyReport);

    /**
     * Gets the organization.
     * 
     * @return the organization
     */
    public edu.upmc.opi.caBIG.caTIES.database.domain.Organization getOrganization();

    /**
     * Sets the organization.
     * 
     * @param organization the organization
     */
    public void setOrganization(edu.upmc.opi.caBIG.caTIES.database.domain.Organization organization);

    public TreeSet getSortedPatholgyReports();

    public void setSortedPathologyReports(TreeSet sortedPathologyReports);
}

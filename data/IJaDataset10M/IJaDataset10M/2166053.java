package org.bejug.javacareers.jobs.view.jsf.forms;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.bejug.javacareers.jobs.model.Profile;
import org.bejug.javacareers.jobs.service.AdminService;

/**
 * The JSF form which handles the posting of a job.
 * 
 * @author kva (latest modification by $Author: shally $)
 * @version $Revision: 1.8 $ - $Date: 2006/03/20 20:14:33 $
 */
public class PostJobForm {

    /**
     * Job title String.
     */
    private String jobTitle;

    /**
     * Job description String. 
     */
    private String jobDescription;

    /**
     * Job location region. 
     */
    private String jobRegion;

    /**
     * Job location city
     */
    private String jobCity;

    /**
	 * Job location zipcode
	 */
    private String jobZipCode;

    /**
	 * Job country 
	 */
    private String jobCountry;

    /**
     *  Organisation title String.
     */
    private String organisationTitle;

    /**
     * Organisation description String. 
     */
    private String organisationDescription;

    /**
     * Organisation Region 
     */
    private String organisationRegion;

    /**
     * Organisation city
     */
    private String organisationCity;

    /**
	 * Organisation zipcode
	 */
    private String organisationZipCode;

    /**
	 * Organisation country
	 */
    private String organisationCountry;

    /**
     * RequiredProfile String. 
     */
    private String requiredProfile;

    private String sector;

    private String language;

    /**
	 * String keywords, containing csv
	 */
    private String keywords;

    private String requiredSkills;

    private String contactDetails;

    /**
     * startDate String. 
     */
    private Date startDate = new Date();

    /**
     * externalURL String. 
     */
    private String externalURL;

    /**
     * adminService adminService.
     */
    private AdminService adminService;

    /**
     * @return Returns the requiredProfile.
     */
    public String getRequiredProfile() {
        return requiredProfile;
    }

    /**
     * @param requiredProfile
     *            The requiredProfile to set.
     */
    public void setRequiredProfile(String requiredProfile) {
        this.requiredProfile = requiredProfile;
    }

    /**
     * @return Returns the description.
     */
    public String getJobDescription() {
        return jobDescription;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    /**
     * @return Returns the title.
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * @return Returns the startDate.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate
     *            The startDate to set.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return Returns the locations.
     */
    public String getJobRegion() {
        return jobRegion;
    }

    /**
     * @param location String The location to set.          
     */
    public void setJobRegion(String jobRegion) {
        this.jobRegion = jobRegion;
    }

    /**
     * @return Returns the externalURL.
     */
    public String getExternalURL() {
        return externalURL;
    }

    /**
     * @param externalURL
     *            The externalURL to set. 
     *            
     *            TODO create validator for this field (psong09)            
     */
    public void setExternalURL(String externalURL) {
        this.externalURL = externalURL;
    }

    /**
     * Get the selected profile model object.
     * @return the selected profile
     */
    public Profile getModelProfile() {
        List profiles = adminService.getProfiles();
        Profile foundProfile = null;
        for (Iterator it = profiles.iterator(); it.hasNext(); ) {
            Profile jobProfile = (Profile) it.next();
            if (jobProfile.getName().equals(requiredProfile)) {
                foundProfile = jobProfile;
                break;
            }
        }
        return foundProfile;
    }

    /**
     * @param adminService The adminService to set.
     */
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getLanguage() {
        return language;
    }

    public String getSector() {
        return sector;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getJobCity() {
        return jobCity;
    }

    public String getJobCountry() {
        return jobCountry;
    }

    public String getJobZipCode() {
        return jobZipCode;
    }

    public void setJobCity(String jobCity) {
        this.jobCity = jobCity;
    }

    public void setJobCountry(String jobCountry) {
        this.jobCountry = jobCountry;
    }

    public void setJobZipCode(String jobZipCode) {
        this.jobZipCode = jobZipCode;
    }

    public String getOrganisationCity() {
        return organisationCity;
    }

    public String getOrganisationCountry() {
        return organisationCountry;
    }

    public String getOrganisationDescription() {
        return organisationDescription;
    }

    public String getOrganisationRegion() {
        return organisationRegion;
    }

    public String getOrganisationTitle() {
        return organisationTitle;
    }

    public String getOrganisationZipCode() {
        return organisationZipCode;
    }

    public void setOrganisationCity(String organisationCity) {
        this.organisationCity = organisationCity;
    }

    public void setOrganisationCountry(String organisationCountry) {
        this.organisationCountry = organisationCountry;
    }

    public void setOrganisationDescription(String organisationDescription) {
        this.organisationDescription = organisationDescription;
    }

    public void setOrganisationRegion(String organisationRegion) {
        this.organisationRegion = organisationRegion;
    }

    public void setOrganisationTitle(String organisationTitle) {
        this.organisationTitle = organisationTitle;
    }

    public void setOrganisationZipCode(String organisationZipCode) {
        this.organisationZipCode = organisationZipCode;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String toString() {
        return new ToStringBuilder(this).append("jobRegion", this.jobRegion).append("jobCity", this.jobCity).append("jobZipCode", this.jobZipCode).append("jobCountry", this.jobCountry).append("organisationTitle", this.organisationTitle).append("organisationDescription", this.organisationDescription).append("organisationRegion", this.organisationRegion).append("organisationCity", this.organisationCity).append("organisationZipCode", this.organisationZipCode).append("organisationCountry", this.organisationCountry).append("requiredProfile", this.requiredProfile).append("sector", this.sector).append("language", this.language).append("keywords", this.keywords).append("requiredSkills", this.requiredSkills).append("contactDetails", this.contactDetails).append("startDate", this.startDate).append("externalURL", this.externalURL).append(']').toString();
    }
}

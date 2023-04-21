package de.beas.explicanto.distribution.web.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import de.beas.explicanto.distribution.config.Constants;

/**
 * Form for profile.
 * 
 * @author dorel
 * 
 * @struts.form name="profileForm"
 */
public class ProfileForm extends BaseForm {

    private static final SimpleDateFormat TIMESTAMP_FORMAT = Constants.TIMESTAMP_FORMAT;

    private static final SimpleDateFormat DATE_FORMAT = Constants.DATE_FORMAT;

    public static final ProfileForm DEFAULT = new ProfileForm();

    private java.lang.String id = null;

    private java.lang.String name = null;

    private java.lang.String description = null;

    private java.lang.String type = null;

    private int version = 0;

    private java.lang.String observations = null;

    private java.util.Date createdAt = new java.util.Date(System.currentTimeMillis());

    private java.util.Date lastUpdated = new java.util.Date(System.currentTimeMillis());

    private java.lang.String rssChannelId = null;

    private String language = null;

    private String country = null;

    private boolean useLanguage = false;

    private boolean useCountry = false;

    private boolean useInitials = false;

    private boolean useTitle = false;

    private boolean useOrganization = false;

    private boolean useOrganizationUnit = false;

    private boolean useDepartmentNr = false;

    private boolean useLocation = false;

    private String initials = null;

    private String title = null;

    private String organization = null;

    private String organizationUnit = null;

    private String departmentNr = null;

    private String location = null;

    private String externalLink = null;

    /**
 * Standard constructor.
 */
    public ProfileForm() {
    }

    /**
 * Returns the id
 * 
 * @return the id
 */
    public java.lang.String getId() {
        return id;
    }

    /**
 * Sets the id
 * 
 * @param id
 *            the new id value
 */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
 * Returns the name
 * 
 * @return the name
 */
    public java.lang.String getName() {
        return name;
    }

    /**
 * Sets the name
 * 
 * @param name
 *            the new name value
 */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
 * Returns the description
 * 
 * @return the description
 */
    public java.lang.String getDescription() {
        return description;
    }

    /**
 * Sets the description
 * 
 * @param description
 *            the new description value
 */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
 * Returns the type
 * 
 * @return the type
 */
    public java.lang.String getType() {
        return type;
    }

    /**
 * Sets the type
 * 
 * @param type
 *            the new type value
 */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    /**
 * Returns the version
 * 
 * @return the version
 */
    public int getVersion() {
        return version;
    }

    /**
 * Sets the version
 * 
 * @param version
 *            the new version value
 */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
 * Returns the observations
 * 
 * @return the observations
 */
    public java.lang.String getObservations() {
        return observations;
    }

    /**
 * Sets the observations
 * 
 * @param observations
 *            the new observations value
 */
    public void setObservations(java.lang.String observations) {
        this.observations = observations;
    }

    /**
 * Returns the createdAt
 * 
 * @return the createdAt
 */
    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    /**
 * Returns the createdAt as a String
 * 
 * @return the createdAt as a String
 */
    public String getCreatedAtAsString() {
        if (createdAt != null) {
            return DATE_FORMAT.format(createdAt);
        } else {
            return "";
        }
    }

    /**
 * Sets the createdAt
 * 
 * @param createdAt
 *            the new createdAt value
 */
    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
 * Sets the createdAt as a String.
 * 
 * @param createdAt
 *            the new createdAt value as a String
 */
    public void setCreatedAtAsString(String createdAt) {
        try {
            this.createdAt = new java.util.Date(DATE_FORMAT.parse(createdAt).getTime());
        } catch (ParseException pe) {
            this.createdAt = new java.util.Date();
        }
    }

    /**
 * Returns the lastUpdated
 * 
 * @return the lastUpdated
 */
    public java.util.Date getLastUpdated() {
        return lastUpdated;
    }

    /**
 * Returns the lastUpdated as a String
 * 
 * @return the lastUpdated as a String
 */
    public String getLastUpdatedAsString() {
        if (lastUpdated != null) {
            return DATE_FORMAT.format(lastUpdated);
        } else {
            return "";
        }
    }

    /**
 * Sets the lastUpdated
 * 
 * @param lastUpdated
 *            the new lastUpdated value
 */
    public void setLastUpdated(java.util.Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
 * Sets the lastUpdated as a String.
 * 
 * @param lastUpdated
 *            the new lastUpdated value as a String
 */
    public void setLastUpdatedAsString(String lastUpdated) {
        try {
            this.lastUpdated = new java.util.Date(DATE_FORMAT.parse(lastUpdated).getTime());
        } catch (ParseException pe) {
            this.lastUpdated = new java.util.Date();
        }
    }

    /**
 * Returns the rssChannelId
 * 
 * @return the rssChannelId
 */
    public java.lang.String getRssChannelId() {
        return rssChannelId;
    }

    /**
 * Sets the rssChannelId
 * 
 * @param rssChannelId
 *            the new rssChannelId value
 */
    public void setRssChannelId(java.lang.String rssChannelId) {
        this.rssChannelId = rssChannelId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isUseLanguage() {
        return useLanguage;
    }

    public void setUseLanguage(boolean useLanguage) {
        this.useLanguage = useLanguage;
    }

    public boolean isUseCountry() {
        return useCountry;
    }

    public void setUseCountry(boolean useCountry) {
        this.useCountry = useCountry;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getDepartmentNr() {
        return departmentNr;
    }

    public void setDepartmentNr(String departmentNr) {
        this.departmentNr = departmentNr;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isUseInitials() {
        return useInitials;
    }

    public void setUseInitials(boolean useInitials) {
        this.useInitials = useInitials;
    }

    public boolean isUseTitle() {
        return useTitle;
    }

    public void setUseTitle(boolean useTitle) {
        this.useTitle = useTitle;
    }

    public boolean isUseOrganization() {
        return useOrganization;
    }

    public void setUseOrganization(boolean useOrganization) {
        this.useOrganization = useOrganization;
    }

    public boolean isUseOrganizationUnit() {
        return useOrganizationUnit;
    }

    public void setUseOrganizationUnit(boolean useOrganizationUnit) {
        this.useOrganizationUnit = useOrganizationUnit;
    }

    public boolean isUseDepartmentNr() {
        return useDepartmentNr;
    }

    public void setUseDepartmentNr(boolean useDepartmentNr) {
        this.useDepartmentNr = useDepartmentNr;
    }

    public boolean isUseLocation() {
        return useLocation;
    }

    public void setUseLocation(boolean useLocation) {
        this.useLocation = useLocation;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    /**
 * Validate the properties that have been set from this HTTP request, and
 * return an <code>ActionErrors</code> object that encapsulates any
 * validation errors that have been found. If no errors are found, return
 * <code>null</code> or an <code>ActionErrors</code> object with no
 * recorded error messages.
 * 
 * @param mapping
 *            The mapping used to select this instance
 * @param request
 *            The servlet request we are processing
 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (this.isForRemove()) {
            return errors;
        }
        if (isNullOrEmptyString(getName())) {
            errors.add("name", new ActionMessage("field.required"));
        }
        if (isUseInitials() && isNullOrEmptyString(getInitials())) {
            errors.add("initials", new ActionMessage("field.required"));
        }
        if (isUseTitle() && isNullOrEmptyString(getTitle())) {
            errors.add("title", new ActionMessage("field.required"));
        }
        if (isUseOrganization() && isNullOrEmptyString(getOrganization())) {
            errors.add("organization", new ActionMessage("field.required"));
        }
        if (isUseOrganizationUnit() && isNullOrEmptyString(getOrganizationUnit())) {
            errors.add("organizationUnit", new ActionMessage("field.required"));
        }
        if (isUseDepartmentNr() && isNullOrEmptyString(getDepartmentNr())) {
            errors.add("departmentNr", new ActionMessage("field.required"));
        }
        if (isUseLocation() && isNullOrEmptyString(getLocation())) {
            errors.add("location", new ActionMessage("field.required"));
        }
        if (isUseLanguage() && isNullOrEmptyString(getLanguage())) {
            errors.add("language", new ActionMessage("field.required"));
        }
        if (isUseCountry() && isNullOrEmptyString(getCountry())) {
            errors.add("country", new ActionMessage("field.required"));
        }
        return errors;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        id = null;
        name = null;
        description = null;
        type = null;
        version = 0;
        observations = null;
        createdAt = new java.util.Date(System.currentTimeMillis());
        lastUpdated = new java.util.Date(System.currentTimeMillis());
        rssChannelId = null;
        language = null;
        country = null;
        useLanguage = false;
        useCountry = false;
        useInitials = false;
        useTitle = false;
        useOrganization = false;
        useOrganizationUnit = false;
        useDepartmentNr = false;
        useLocation = false;
        initials = null;
        title = null;
        organization = null;
        organizationUnit = null;
        departmentNr = null;
        location = null;
        externalLink = null;
    }
}

package net.sourceforge.jcodebaseHQ.project;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import net.sourceforge.jcodebaseHQ.Constants;
import net.sourceforge.jcodebaseHQ.Messages;
import net.sourceforge.jcodebaseHQ.exception.InvalidParameterRangeException;
import net.sourceforge.jcodebaseHQ.validator.IntegerValidator;
import net.sourceforge.jcodebaseHQ.validator.StringValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@XmlRootElement(name = Constants.PROJECT_XML_TAG)
public class Project {

    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(Project.class);

    private String name;

    private String permalink;

    private int groupId;

    private String overview;

    private ProjectStartpage startPage;

    private ProjectStatus status;

    private int iconId;

    private ProjectIcon icon;

    private IntegerValidator iconIdValidator;

    private StringValidator notEmptyOrNullValidator;

    /**
     * create a project object with required parameters
     * 
     * @param name
     * @param startPage
     * @param status
     * @throws InvalidParameterRangeException
     */
    public Project(String name, String permalink, ProjectStartpage startPage, ProjectStatus status) {
        this();
        this.setName(name);
        this.setStartPage(startPage);
        this.setStatus(status);
        this.setPermalink(permalink);
    }

    public Project() {
        this.iconIdValidator = new IntegerValidator(1, 9);
        this.notEmptyOrNullValidator = new StringValidator(false, false);
    }

    /**
     * @return the name
     */
    @XmlElement(required = true, name = Constants.PROJECT_NAME_XML_TAG)
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     *        the name to set
     * @throws InvalidParameterRangeException
     */
    public void setName(String name) {
        this.notEmptyOrNullValidator.validate(name, "projectName");
        this.name = name;
    }

    /**
     * @return the groupId
     */
    @XmlElement(required = false, name = Constants.PROJECT_GROUP_ID_XML_TAG, nillable = true)
    public int getGroupId() {
        return this.groupId;
    }

    /**
     * @param groupId
     *        the groupId to set
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the overview
     */
    @XmlElement(required = true, name = Constants.PROJECT_OVERVIEW_XML_TAG)
    public String getOverview() {
        return this.overview;
    }

    /**
     * @param overview
     *        the overview to set
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * @return the startPage
     */
    @XmlElement(required = true, name = Constants.PROJECT_START_PAGE_XML_TAG)
    public ProjectStartpage getStartPage() {
        return this.startPage;
    }

    /**
     * @param startPage
     *        the startPage to set
     * @throws InvalidParameterRangeException
     */
    public void setStartPage(ProjectStartpage startPage) {
        if (startPage == null) {
            throw new InvalidParameterRangeException(String.format(Messages.INVALID_PROJECT_PARAMETER_VALUE, "startPage"));
        } else {
            this.startPage = startPage;
        }
    }

    /**
     * @return the status
     */
    @XmlElement(required = true, name = Constants.PROJECT_STATUS_XML_TAG)
    public ProjectStatus getStatus() {
        return this.status;
    }

    /**
     * @param status
     *        the status to set
     * @throws InvalidParameterRangeException
     */
    public void setStatus(ProjectStatus status) {
        if (status == null) {
            throw new InvalidParameterRangeException(String.format(Messages.INVALID_PROJECT_PARAMETER_VALUE, "status"));
        } else {
            this.status = status;
            if (status == ProjectStatus.active) {
                this.getIcon().setEnabled(true);
            } else {
                this.getIcon().setEnabled(false);
            }
        }
    }

    /**
     * @return the icon
     */
    @SuppressWarnings("unused")
    @XmlElement(required = true, name = Constants.PROJECT_ICON_XML_TAG)
    private int getIconId() {
        return this.iconId;
    }

    /**
     * @param icon
     *        the icon to set
     */
    @SuppressWarnings("unused")
    private void setIconId(int iconId) {
        this.iconIdValidator.validate(iconId, "projectIconId");
        this.iconId = iconId;
        this.getIcon().setIconId(iconId);
    }

    public ProjectIcon getIcon() {
        if (this.icon == null) {
            this.icon = new ProjectIcon();
        }
        return this.icon;
    }

    /**
     * @return the permalink
     */
    @XmlElement(required = false, name = Constants.PROJECT_PERMALINK_XML_TAG)
    public String getPermalink() {
        return this.permalink;
    }

    /**
     * @param permalink
     *        set the permalink
     * @throws InvalidParameterRangeException
     */
    private void setPermalink(String permalink) {
        this.notEmptyOrNullValidator.validate(permalink, "projectPermalink");
        this.permalink = permalink;
    }

    @Override
    public String toString() {
        return this.getName() + "\n Overview: " + this.getOverview() + "\n Icon: " + this.getIcon() + "\n SmallIcon: " + this.getIcon().getSmallIconUri() + "\n ProjectStatus: " + this.getStatus() + "\n StartPage: " + this.getStartPage() + "\n Permalink: " + this.getPermalink() + "\n GroupId: " + this.getGroupId();
    }
}

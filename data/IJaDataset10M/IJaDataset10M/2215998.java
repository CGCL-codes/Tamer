package de.beas.explicanto.distribution.web.forms;

import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import de.beas.explicanto.distribution.config.Constants;

/**
 * Used for 3.2.4
 * 
 * @author Herta Paul
 */
public class RssFeedForm extends BaseForm {

    private static final SimpleDateFormat TIMESTAMP_FORMAT = Constants.TIMESTAMP_FORMAT;

    private static final SimpleDateFormat DATE_FORMAT = Constants.DATE_FORMAT;

    public static final RssFeedForm DEFAULT = new RssFeedForm();

    /**
     * Used for passing the id of the user, form the main action class to the
     * rest
     */
    private String userID;

    /**
     * same as above
     */
    private String userFullName;

    /**
     * Used for passing the id of the group if My Groups will be selected
     */
    private String groupID;

    /**
     * same as above
     */
    private String groupName;

    private String profileID;

    private String profileName;

    private String courseID;

    private String publishedSurveyID;

    /**
     * Used for determining the type of the feed requested (personal, my groups,
     * my profiles... )
     */
    private String feedType;

    /**
     * Used for determining Course Blog or Start Course
     */
    private String feedSelected;

    private String allType;

    private String courseType;

    private String link;

    private String externalLink;

    private String title;

    private String description;

    private java.util.Date createdAt = new java.util.Date(System.currentTimeMillis());

    private java.util.Date lastUpdated = new java.util.Date(System.currentTimeMillis());

    public static final String PERSONAL_OP = "personal";

    public static final String GROUP_OP = "group";

    public static final String PROFILE_OP = "profile";

    public static final String COURSE_COMPLETE_OP = "courseComplete";

    public static final String COURSE_SUMMARY_OP = "courseSummary";

    public static final String COURSE_BLOG = "courseBlog";

    public static final String COURSE_START = "startCourse";

    public static final String ONE_GROUP = "oneGroup";

    public static final String ONE_PROFILE = "oneProfile";

    public static final String ONE_COMPLETE = "oneCourseComplete";

    public static final String ONE_SUMMARY = "oneCourseSummary";

    public boolean isComplete() {
        return ONE_COMPLETE.equals(courseType);
    }

    public boolean isSummary() {
        return ONE_SUMMARY.equals(courseType);
    }

    public boolean isPersonal() {
        return PERSONAL_OP.equals(feedType);
    }

    public boolean isGroup() {
        return GROUP_OP.equals(feedType);
    }

    public boolean isProfile() {
        return PROFILE_OP.equals(feedType);
    }

    public boolean isCourseComplete() {
        return COURSE_COMPLETE_OP.equals(feedType);
    }

    public boolean isCourseSummary() {
        return COURSE_SUMMARY_OP.equals(feedType);
    }

    public boolean isCourseBlog() {
        return COURSE_BLOG.equals(feedSelected);
    }

    public boolean isStartCourse() {
        return COURSE_START.equals(feedSelected);
    }

    public boolean isOneGroup() {
        return ONE_GROUP.equals(allType);
    }

    public boolean isOneProfile() {
        return ONE_PROFILE.equals(allType);
    }

    /**
     * @return Returns the feedSelected.
     */
    public String getFeedSelected() {
        return feedSelected;
    }

    /**
     * @param feedSelected
     *            The feedSelected to set.
     */
    public void setFeedSelected(String feedSelected) {
        this.feedSelected = feedSelected;
    }

    /**
     * @return Returns the feedType.
     */
    public String getFeedType() {
        return feedType;
    }

    /**
     * @param feedType
     *            The feedType to set.
     */
    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    /**
     * @return Returns the userID.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @param userID
     *            The userID to set.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * @return Returns the userName.
     */
    public String getUserFullName() {
        return userFullName;
    }

    /**
     * @param userName
     *            The userName to set.
     */
    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    /**
     * @return Returns the link.
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link
     *            The link to set.
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the cOURSE_BLOG.
     */
    public static String getCOURSE_BLOG() {
        return COURSE_BLOG;
    }

    /**
     * @return Returns the cOURSE_COMPLETE_OP.
     */
    public static String getCOURSE_COMPLETE_OP() {
        return COURSE_COMPLETE_OP;
    }

    /**
     * @return Returns the cOURSE_START.
     */
    public static String getCOURSE_START() {
        return COURSE_START;
    }

    /**
     * @return Returns the cOURSE_SUMMARY_OP.
     */
    public static String getCOURSE_SUMMARY_OP() {
        return COURSE_SUMMARY_OP;
    }

    /**
     * @return Returns the dATE_FORMAT.
     */
    public static SimpleDateFormat getDATE_FORMAT() {
        return DATE_FORMAT;
    }

    /**
     * @return Returns the dEFAULT.
     */
    public static RssFeedForm getDEFAULT() {
        return DEFAULT;
    }

    /**
     * @return Returns the gROUP_OP.
     */
    public static String getGROUP_OP() {
        return GROUP_OP;
    }

    /**
     * @return Returns the pERSONAL_OP.
     */
    public static String getPERSONAL_OP() {
        return PERSONAL_OP;
    }

    /**
     * @return Returns the pROFILE_OP.
     */
    public static String getPROFILE_OP() {
        return PROFILE_OP;
    }

    /**
     * @return Returns the tIMESTAMP_FORMAT.
     */
    public static SimpleDateFormat getTIMESTAMP_FORMAT() {
        return TIMESTAMP_FORMAT;
    }

    /**
     * @return Returns the createdAt.
     */
    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt
     *            The createdAt to set.
     */
    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return Returns the groupID.
     */
    public String getGroupID() {
        return groupID;
    }

    /**
     * @param groupID
     *            The groupID to set.
     */
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    /**
     * @return Returns the groupName.
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName
     *            The groupName to set.
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * @return Returns the lastUpdated.
     */
    public java.util.Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated
     *            The lastUpdated to set.
     */
    public void setLastUpdated(java.util.Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * @return Returns the allType.
     */
    public String getAllType() {
        return allType;
    }

    /**
     * @param allType
     *            The allType to set.
     */
    public void setAllType(String allType) {
        this.allType = allType;
    }

    /**
     * @return Returns the profileID.
     */
    public String getProfileID() {
        return profileID;
    }

    /**
     * @param profileID
     *            The profileID to set.
     */
    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    /**
     * @return Returns the profileName.
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * @param profileName
     *            The profileName to set.
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     * @return Returns the externalLink.
     */
    public String getExternalLink() {
        return externalLink;
    }

    /**
     * @param externalLink
     *            The externalLink to set.
     */
    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    /**
     * @return Returns the courseType.
     */
    public String getCourseType() {
        return courseType;
    }

    /**
     * @param courseType
     *            The courseType to set.
     */
    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    /**
     * @return Returns the courseID.
     */
    public String getCourseID() {
        return courseID;
    }

    /**
     * @param courseID The courseID to set.
     */
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    /**
     * @return Returns the publishedSurveyID.
     */
    public String getPublishedSurveyID() {
        return publishedSurveyID;
    }

    /**
     * @param publishedSurveyID The publishedSurveyID to set.
     */
    public void setPublishedSurveyID(String publishedSurveyID) {
        this.publishedSurveyID = publishedSurveyID;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        this.allType = null;
        this.description = null;
        this.feedSelected = null;
        this.groupID = null;
        this.groupName = null;
        this.link = null;
        this.profileID = null;
        this.profileName = null;
        this.title = null;
        this.userFullName = null;
        this.userID = null;
        this.externalLink = null;
        this.courseType = null;
        this.courseID = null;
        this.publishedSurveyID = null;
    }
}

package org.managersheet.markingManagement.api;

import java.io.Serializable;
import java.util.Date;
import org.managersheet.accessControl.api.User;
import org.managersheet.administration.api.Activity;
import org.managersheet.userProfile.api.Project;
import org.managersheet.util.helpers.DateHelper;

public class Marking implements Serializable {

    private static final long serialVersionUID = -320552109590912862L;

    private int id;

    private Date startDate;

    private Date endDate;

    private Activity activity;

    private Project project;

    private User user;

    private String comment;

    private int duration;

    /**
	 * @return the id
	 */
    public int getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return the startDate
	 */
    public Date getStartDate() {
        return startDate;
    }

    /**
	 * @param startDate the startDate to set
	 */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
	 * @return the endDate
	 */
    public Date getEndDate() {
        return endDate;
    }

    /**
	 * @param endDate the endDate to set
	 */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
	 * @return the activity
	 */
    public Activity getActivity() {
        return activity;
    }

    /**
	 * @param activity the activity to set
	 */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
	 * @return the project
	 */
    public Project getProject() {
        return project;
    }

    /**
	 * @param project the project to set
	 */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
	 * @return the user
	 */
    public User getUser() {
        return user;
    }

    /**
	 * @param user the user to set
	 */
    public void setUser(User user) {
        this.user = user;
    }

    /**
	 * @return the comment
	 */
    public String getComment() {
        return comment;
    }

    /**
	 * @param comment the comment to set
	 */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
	 * @return the duration
	 */
    public int getDuration() {
        return duration;
    }

    /**
	 * @param duration the duration to set
	 */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFormatedDate() {
        return DateHelper.parseDateToString(this.getStartDate(), DateHelper.FRIENDLY_DATE_FORMAT);
    }

    public String getFormatedDuration() {
        String formated = "";
        if (this.getDuration() > 0) {
            long duration = this.getDuration() * 1000 * 60 * 60;
            formated = DateHelper.getFormattedTime(duration);
        } else {
            Date endDate = DateHelper.parseDateToTime(this.getEndDate(), DateHelper.TIME_FORMAT);
            Date startDate = DateHelper.parseDateToTime(this.getStartDate(), DateHelper.TIME_FORMAT);
            long totalTime = (endDate.getTime() - startDate.getTime());
            formated = DateHelper.getFormattedTime(totalTime);
        }
        return formated;
    }
}

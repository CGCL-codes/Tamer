package com.fddtool.ui.faces.component.activity;

import com.fddtool.pd.fddproject.IProgressInfo;

/**
 * A class implements <code>IActivityProgressModel</code> if it can provide the
 * data required for Activity Progress component.
 *
 * @author Serguei Khramtchenko
 */
public interface IActivityProgressModel extends IProgressInfo {

    /**
     * Returns unique identifier of activity.
     *
     * @return String with activity identifier.
     */
    public String getId();

    /**
     * Returns name of the activity.
     *
     * @return String containing activity name.
     */
    public String getName();

    /**
     * Returns percent of activity completion.
     *
     * @return integer value of comletion percent.
     */
    public int getPercentComplete();

    /**
     * Returns the date (usually montg and year ) when activity is supposed
     * to be complete.
     *
     * @return String containg optional completion date.
     */
    public String getPlannedEndDate();

    /**
     * Returns initials of a person who is an owner of the activity.
     *
     * @return String containg initials of an owner.
     */
    public String getOwnerInitials();

    /**
     * returns number of features in the activity.
    *
     * @return String representing number of features in the activity.
     */
    public String getNumberOfFeatures();
}

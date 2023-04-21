package com.liferay.portal.model;

import com.liferay.portal.theme.ThemeDisplay;

/**
 * <a href="ActivityTrackerInterpreter.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface ActivityTrackerInterpreter {

    public String[] getClassNames();

    public ActivityFeedEntry interpret(ActivityTracker activityTracker, ThemeDisplay themeDisplay);
}

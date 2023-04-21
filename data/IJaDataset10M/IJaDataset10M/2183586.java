package com.ivis.xprocess.ui.filters;

import com.ivis.xprocess.ui.datawrappers.project.RequiredResourceRoleWrapper;
import com.ivis.xprocess.ui.personalplanner.PPConstants;
import com.ivis.xprocess.ui.properties.FilterMessages;
import com.ivis.xprocess.util.Day;

public class WeekInViewFilter implements XProcessFilter {

    private boolean enabled;

    public String getName() {
        return FilterMessages.hide_not_week_in_view_title;
    }

    public String getDescription() {
        return FilterMessages.hide_not_week_in_view_description;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean allow(FilterParams params) {
        if (!enabled) {
            return true;
        }
        RequiredResourceRoleWrapper wrapper = params.get(PPConstants.ASSIGNMENT);
        Day startWeek = params.get(PPConstants.DAY_IN_VIEW);
        Day endWeek = startWeek.addDays(6);
        boolean allow = false;
        for (Day day : startWeek.getDays(endWeek)) {
            if (wrapper.getTime(day) > 0) {
                allow = true;
            }
        }
        return allow;
    }
}

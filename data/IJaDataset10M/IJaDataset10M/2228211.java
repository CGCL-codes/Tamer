package com.ivis.xprocess.ui.views.filters;

import java.util.List;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import com.ivis.xprocess.core.Person;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.datawrappers.project.RequiredResourceRoleWrapper;
import com.ivis.xprocess.ui.filters.FilterManager;
import com.ivis.xprocess.ui.filters.FilterParams;
import com.ivis.xprocess.ui.filters.XProcessFilter;
import com.ivis.xprocess.ui.personalplanner.PPConstants;
import com.ivis.xprocess.util.Day;

public class PersonalPlannerViewerFilter extends ViewerFilter {

    private Day currentDay;

    private Person person;

    private Class<?> targetClass;

    private FilterParams params;

    public PersonalPlannerViewerFilter(Person person, Day currentDay, Class<?> targetClass) {
        this.person = person;
        this.currentDay = currentDay;
        this.targetClass = targetClass;
        params = FilterParams.create();
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof IElementWrapper) {
            IElementWrapper wrapper = (IElementWrapper) element;
            if ((wrapper.getElement() == null) || !(wrapper instanceof RequiredResourceRoleWrapper)) {
                return true;
            }
            List<XProcessFilter> filters = FilterManager.getInstance().getFilters(targetClass);
            for (XProcessFilter filter : filters) {
                if (!filter.allow(getParams().put(PPConstants.ASSIGNMENT, wrapper))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private FilterParams getParams() {
        params.put(PPConstants.PERSON, person);
        params.put(PPConstants.DAY_IN_VIEW, currentDay);
        return params;
    }

    public void updateDayInView(Day day) {
        this.currentDay = day;
    }
}

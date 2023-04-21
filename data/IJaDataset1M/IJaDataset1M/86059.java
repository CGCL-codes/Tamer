package uk.co.fortunecookie.timesheet.data.entities.comparators;

import java.util.Comparator;
import uk.co.fortunecookie.timesheet.data.entities.ProjectView;

public class ProjectViewPaidComparator implements Comparator<ProjectView> {

    @Override
    public int compare(ProjectView thisObj, ProjectView thatObj) {
        int cmp = thisObj.getPaid().compareTo(thatObj.getPaid());
        if (cmp != 0) return cmp;
        return thisObj.getProjectId().compareTo(thatObj.getProjectId());
    }
}

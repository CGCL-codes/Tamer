package at.rc.tacos.client.view.sorterAndTooltip;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import at.rc.tacos.model.RosterEntry;

/**
 * Provides sorting functions for the personal view table.
 * 
 * @author Michael
 */
public class PersonalViewSorter extends ViewerSorter {

    public static final String NAME_SORTER = "name";

    public static final String WORKTIME_SORTER = "worktime";

    public static final String CHECKIN_SORTER = "checkin";

    public static final String CHECKOUT_SORTER = "checkout";

    public static final String SERVICE_SORTER = "service";

    public static final String JOB_SORTER = "job";

    public static final String VEHICLE_SORTER = "vehicle";

    private String column = null;

    private int dir = SWT.DOWN;

    /**
	 * Default class constructor providing a columt to sort and a direction
	 * 
	 * @param column
	 *            the column to sort by
	 * @param dir
	 *            the sorting direction
	 */
    public PersonalViewSorter(String column, int dir) {
        super();
        this.column = column;
        this.dir = dir;
    }

    /**
	 * Compares the given object and returns the result of the comparator
	 * 
	 * @param viewer
	 *            the viewer containing the data
	 * @param object1
	 *            the first object to compare
	 * @param object2
	 *            the second object to compare
	 * @return the result of the comparation
	 */
    @Override
    public int compare(Viewer viewer, Object object1, Object object2) {
        int returnValue = 0;
        RosterEntry entry1 = (RosterEntry) object1;
        RosterEntry entry2 = (RosterEntry) object2;
        if (column == NAME_SORTER) {
            String name1 = entry1.getStaffMember().getLastName();
            String name2 = entry2.getStaffMember().getLastName();
            returnValue = name1.compareTo(name2);
        }
        if (column == WORKTIME_SORTER) {
            long start1 = entry1.getPlannedStartOfWork();
            long start2 = entry2.getPlannedStartOfWork();
            if (start1 > start2) returnValue = -1;
            if (start1 < start2) returnValue = 1;
            if (start1 == start2) returnValue = 0;
        }
        if (column == CHECKIN_SORTER) {
            long checkin1 = entry1.getRealStartOfWork();
            long checkin2 = entry2.getRealStartOfWork();
            if (checkin1 > checkin2) returnValue = -1;
            if (checkin1 < checkin2) returnValue = 1;
            if (checkin1 == checkin2) returnValue = 0;
        }
        if (column == CHECKOUT_SORTER) {
            long checkout1 = entry1.getRealEndOfWork();
            long checkout2 = entry2.getRealEndOfWork();
            if (checkout1 > checkout2) returnValue = -1;
            if (checkout1 < checkout2) returnValue = 1;
            if (checkout1 == checkout2) returnValue = 0;
        }
        if (column == SERVICE_SORTER) returnValue = entry1.getServicetype().getServiceName().compareTo(entry2.getServicetype().getServiceName());
        if (column == JOB_SORTER) returnValue = entry1.getJob().getJobName().compareTo(entry2.getJob().getJobName());
        if (this.dir == SWT.DOWN) {
            returnValue = returnValue * -1;
        }
        return returnValue;
    }
}

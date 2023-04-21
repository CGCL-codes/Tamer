package org.eledge.domain.participants;

import java.util.Iterator;
import java.util.List;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.eledge.domain.Course;
import org.eledge.domain.User;
import org.eledge.domain.permissions.PermissionDeterminer;

public class CourseModel implements IPropertySelectionModel {

    private List<Course> courseList;

    @SuppressWarnings("unchecked")
    public CourseModel(User viewingUser) {
        courseList = Course.lookupAllCourses();
        init(viewingUser);
    }

    /***************************************************************************
     * Used to initialie the state of the object. Will remove courses for which
     * the viewing user doesn't have permission.
     */
    protected void init(User user) {
        for (Iterator<Course> it = courseList.iterator(); it.hasNext(); ) {
            Course c = it.next();
            if (!PermissionDeterminer.hasEditPermission(user, c)) {
                it.remove();
            }
        }
    }

    protected List<Course> getCourseList() {
        return courseList;
    }

    public int getOptionCount() {
        return courseList.size();
    }

    public Object getOption(int index) {
        return courseList.get(index);
    }

    public String getLabel(int index) {
        return ((Course) courseList.get(index)).getUserReadableValue();
    }

    public String getValue(int index) {
        return Integer.toString(index);
    }

    public Object translateValue(String value) {
        return courseList.get(Integer.parseInt(value));
    }
}

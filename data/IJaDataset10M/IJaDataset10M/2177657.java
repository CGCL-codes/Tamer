package CSIS543TFinalProject;

import java.util.*;
import CSIS543TFinalProject.db.*;

/**
 class Course contains information about a Course. we don't know when courses
 are offered, so, we let the user find that out when the 'schedule' their
 selected courses.
 */
public class Course {

    private String Name;

    private String OldName;

    private String Title;

    private String Description;

    private int Credits;

    private List<Course> PreRequisteCourses = new ArrayList<Course>();

    /**
    create a new Course object.
    */
    public Course() {
    }

    /**
    create a new Course object, given on the unique course name.  used
    */
    public Course(String name) {
        this();
        this.Name = name;
    }

    /**
    create a new Course object with all the main items. this is used heavily by
    the the unit test that actually loads the database.
    */
    public Course(String name, String oldName, String title, String description, int credits) {
        this(name);
        this.OldName = oldName;
        this.Title = title;
        this.Description = description;
        this.Credits = credits;
    }

    /**
    */
    public String toString() {
        String s = String.format("%s %s %s %d %s", Name, OldName, Title, Credits, Description);
        return s;
    }

    public void AddPrerequisite(Course course) {
        if (!PreRequisteCourses.contains(course)) {
            PreRequisteCourses.add(course);
        } else {
        }
    }

    /**
    return a nicely formatted string with all the course names that the GUI can
    display when the user wants to check for missing prerequisite courses.
    
    we create Course objects (from the database) for each of the course names,
    and then check to see if the Course prerequisites are in the same list.
    */
    public static String getMissingPreReqsMessage(List<String> courseNames) {
        DbHelper dbh = new DbHelper();
        Set<String> missingPreReqCourseNames = new HashSet<String>();
        for (String courseName : courseNames) {
            Course course = dbh.GetCourse(courseName);
            List<Course> preReqCourses = course.getPreRequisiteCourses();
            for (Course preReqCourse : preReqCourses) {
                if (!courseNames.contains(preReqCourse.getName())) {
                    missingPreReqCourseNames.add(preReqCourse.getName());
                }
            }
        }
        dbh.close();
        String msg = "Prerequistes OK";
        if (missingPreReqCourseNames.size() > 0) {
            msg = "Missing prerequisite courses:\n";
            for (String missingPreReqCourseName : missingPreReqCourseNames) {
                msg += missingPreReqCourseName + "\n";
            }
        }
        return msg;
    }

    public List<Course> getPreRequisiteCourses() {
        return PreRequisteCourses;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public int getCredits() {
        return Credits;
    }

    public void setCredits(int credits) {
        this.Credits = credits;
    }

    public List<Course> getPreRequisteCourses() {
        return PreRequisteCourses;
    }

    public void setPreRequisteCourses(List<Course> PreRequisteCourses) {
        this.PreRequisteCourses = PreRequisteCourses;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getOldName() {
        return OldName;
    }

    public void setOldName(String OldName) {
        this.OldName = OldName;
    }
}

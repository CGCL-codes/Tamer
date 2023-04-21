package exceptions;

/**
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class CourseIsMissingDependenciesException extends Exception {

    /**
	 * This is a serialized id used for the input/output streams
	 */
    private static final long serialVersionUID = 1264886153105068668L;

    /**
	 * The ID of the course in question
	 */
    private final String courseID;

    /**
	 * The list of missing dependencies.
	 */
    private final String missing;

    /**
	 * @param missing The missing courses. 
	 * @param courseID The ID of the course.
	 */
    public CourseIsMissingDependenciesException(String courseID, String missing) {
        super("*** Kurset, " + courseID + " kunne ikke tilføjes, følgende kurser kræves " + missing + " ***");
        this.missing = missing;
        this.courseID = courseID;
    }

    /**
	 * Get a list of the missing courses.
	 * @return The missing courses as a string.
	 */
    public String getMissingCourses() {
        return missing;
    }

    /**
	 * Get the ID of the course.
	 * @return the ID of the course.
	 */
    public String getCourseID() {
        return courseID;
    }
}

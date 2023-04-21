package org.yarl.db;

import java.util.Collection;
import org.yarl.db.Course;
import org.yarl.db.CourseDelegate;
import org.yarl.db.CourseTree;
import org.yarl.db.PersonDelegate;
import org.yarl.db.WorkoutType;
import org.yarl.db.WorkoutTypeDelegate;
import org.yarl.util.Log4jConfigurator;
import org.apache.log4j.Logger;
import junit.framework.TestCase;

public class CourseTreeTest extends TestCase {

    private static Logger log4j = Logger.getLogger(CourseTreeTest.class);

    public CourseTreeTest(String name) {
        super(name);
        Log4jConfigurator.configure();
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCourseTree() {
        CourseTree ct = new CourseTree();
        assertTrue(ct.getCourses().size() == 0);
    }

    public void testSetWorkoutType() {
        CourseTree ct = new CourseTree();
        WorkoutType wt = WorkoutTypeDelegate.findWorkoutType("Run");
        ct.setWorkoutType(wt);
        assertTrue(ct.getWorkoutType().getName().equals("Run"));
    }

    public void testSetCourses() {
        CourseTree ct = new CourseTree();
        Collection<Course> c = CourseDelegate.findCourses(PersonDelegate.findDefaultPerson());
        int numberOfCourses = c.size();
        log4j.info("course collection size is " + c.size());
        ct.setCourses(c);
        assertTrue(ct.getCourses().size() == numberOfCourses);
    }
}

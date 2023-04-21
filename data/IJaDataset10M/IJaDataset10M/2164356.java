package test;

import junit.framework.TestCase;
import dataClass.Course;
import dataClass.SelectedCourse;
import databases.CourseBase;

/**
 * Tests the class SelectedCourses by usint jUnit
 * @author Morten Sørensen
 * @author Frederik Nordahl Sabroe
 * @author Niels Thykier
 */
public class SelectedCourseTest extends TestCase {

    /**
	 * Sets up the class variable used for SelectedCourse
	 */
    SelectedCourse sc;

    /**
	 * Sets up the class variable used for CourseBase
	 */
    CourseBase cb;

    /**
	 * Sets up some data needed for the tests
	 * @see junit.framework.TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        super.setUp();
        cb = new CourseBase();
        sc = new SelectedCourse(cb.findCourse("01715"), 3);
    }

    /**
	 * A positive test of the comparing function
	 */
    public void testCompareToPositive() {
        SelectedCourse sc2;
        try {
            sc2 = new SelectedCourse(cb.findCourse("01715"), 1);
        } catch (Exception e) {
            fail(e.toString());
            return;
        }
        int test = 0;
        if (sc.compareTo(sc2) == 1) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(sc2, 3);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.compareTo(sc2) == 0) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(sc2, 5);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.compareTo(sc2) == -1) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(cb.findCourse("01005"), 1);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.compareTo(sc2) == 1) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(sc2, 3);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.compareTo(sc2) == 1) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(sc2, 5);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.compareTo(sc2) == -1) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(cb.findCourse("02101"), 1);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.compareTo(sc2) == 1) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(sc2, 3);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.compareTo(sc2) == -1) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(sc2, 5);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.compareTo(sc2) == -1) {
            test++;
        }
        assertTrue(test == 9);
    }

    /**
	 * A negative test of the comparing function
	 */
    public void testCompareToNegative() {
        try {
            sc.compareTo((SelectedCourse) new Object());
            fail("Should not allow us to do that");
        } catch (Exception e) {
        }
        try {
            SelectedCourse sc2 = (SelectedCourse) new Course("01005", " ");
            sc.compareTo(sc2);
            fail("uncompareble objects");
        } catch (Exception e) {
        }
    }

    /**
	 * Testing if it can get the right starting semester for a course
	 */
    public void testGetStartingSemester() {
        SelectedCourse sc2 = null;
        try {
            sc2 = new SelectedCourse(cb.findCourse("01005"), 1);
        } catch (Exception e) {
            System.out.println(e);
        }
        assertTrue(sc2.getStartingSemester() == 1);
    }

    /**
	 * Testing if it can get the right starting period for a course
	 */
    public void testGetStartingPeriod() {
        SelectedCourse sc2 = null;
        try {
            sc2 = new SelectedCourse(cb.findCourse("01005"), 1);
        } catch (Exception e) {
            System.out.println(e);
        }
        assertTrue(sc2.getStartingPeriod() == 1);
    }

    /**
	 * Testing if it can get the right finishing period
	 */
    public void testGetFinishingPeriod() {
        assertTrue(sc.getFinishingPeriod() == 5);
    }

    /**
	 * Testing if a course is in the same semester as another course
	 */
    public void testGetIsInSameSemester() {
        SelectedCourse sc2;
        try {
            sc2 = new SelectedCourse(cb.findCourse("02101"), 1);
        } catch (Exception e) {
            fail(e.toString());
            return;
        }
        int test = 0;
        if (sc.getIsInSameSemester(sc2) == 1) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(sc2, 3);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.getIsInSameSemester(sc2) == 0) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(sc2, 5);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.getIsInSameSemester(sc2) == -1) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(cb.findCourse("01005"), 1);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.getIsInSameSemester(sc2) == 1) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(sc2, 3);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.getIsInSameSemester(sc2) == 0) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(sc2, 5);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.getIsInSameSemester(sc2) == -1) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(cb.findCourse("02101"), 1);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.getIsInSameSemester(sc2) == 1) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(sc2, 3);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.getIsInSameSemester(sc2) == 0) {
            test++;
        }
        try {
            sc2 = new SelectedCourse(sc2, 5);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.getIsInSameSemester(sc2) == -1) {
            test++;
        }
        assertTrue(test == 9);
    }

    /**
	 * Testing which period a course is in
	 */
    public void testGetIsInPeriod() {
        int test = 0;
        if (sc.getIsInPeriod(1, 4) == 1) {
            test++;
        }
        if (sc.getIsInPeriod(5, 5) == 0) {
            test++;
        }
        if (sc.getIsInPeriod(4, 6) == 0) {
            test++;
        }
        if (sc.getIsInPeriod(6, 20) == -1) {
            test++;
        }
        try {
            sc = new SelectedCourse(sc, 1);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.getIsInPeriod(1, 2) == 0) {
            test++;
        }
        if (sc.getIsInPeriod(3, 4) == -1) {
            test++;
        }
        if (sc.getIsInPeriod(3, 4) == -1) {
            test++;
        }
        try {
            sc = new SelectedCourse(sc, 3);
        } catch (Exception e) {
            fail("Could not create course.");
        }
        if (sc.getIsInPeriod(1, 2) == 1) {
            test++;
        }
        if (sc.getIsInPeriod(3, 4) == 1) {
            test++;
        }
        if (sc.getIsInPeriod(5, 6) == 0) {
            test++;
        }
        assertTrue(test == 10);
    }

    /**
	 * Checks if a semester is valid
	 */
    public void testIsValidSemesterPositive() {
        assertTrue(SelectedCourse.isValidSemester(2));
    }

    /**
	 * Checks if a semester is invalid
	 */
    public void testIsValidSemesterNegative() {
        boolean test1;
        boolean test2;
        test1 = SelectedCourse.isValidSemester(-1);
        test2 = SelectedCourse.isValidSemester(21);
        assertFalse(test1 || test2);
    }

    /**
	 * Tears down everything the test has created, including resetting the class variable
	 * @see junit.framework.TestCase#tearDown()
	 */
    protected void tearDown() throws Exception {
        super.tearDown();
        sc = null;
    }
}

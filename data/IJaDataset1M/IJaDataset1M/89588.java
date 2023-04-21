package de.bea.domingo.groupware.map;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Suite of all tests for the groupware mappers.
 *
 * @author <a href=mailto:kriede@users.sourceforge.net>Kurt Riede</a>
 */
public final class AllTests {

    /**
     * Private constructor to prevent instantiation.
     */
    public AllTests() {
    }

    /**
     * The suite.
     *
     * @return Test
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("All tests for package de.bea.domingo.groupware.map");
        suite.addTestSuite(CalendarEntryMapperTest.class);
        suite.addTestSuite(EmailMapperTest.class);
        return suite;
    }
}

package org.thirdway.service;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.thirdway.service.person.PersonServiceTestSuite;
import org.thirdway.service.systemuser.SystemUserServiceTestSuite;

/**
 * ServiceAllTests
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * </tr>
 * <tr>
 * <td>crosenquest</td>
 * <td>Apr 28, 2006</td>
 * </tr>
 * </table>
 */
public class ServiceAllTests extends TestSuite {

    /**
     * suite to run all ServiceTests.
     * 
     * @return Test
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("org.thirdway.test.service.ServiceAllTests");
        suite.addTest(SystemUserServiceTestSuite.suite());
        suite.addTest(PersonServiceTestSuite.suite());
        return suite;
    }
}

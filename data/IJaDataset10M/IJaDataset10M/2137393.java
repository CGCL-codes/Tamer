package org.nightlabs.jfire.testsuite.base;

import junit.framework.TestCase;
import org.nightlabs.jfire.testsuite.JFireTestSuite;

/**
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 *
 */
@JFireTestSuite(JFireBaseTestSuite.class)
public class UserGroupTest extends TestCase {

    public UserGroupTest() {
        this("Testing the functionality around usergroups.");
    }

    /**
	 * @param name
	 */
    public UserGroupTest(String name) {
        super(name);
    }

    public void testCreateUserGroup() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

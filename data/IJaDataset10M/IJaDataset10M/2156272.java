package com.avatal.test;

import junit.framework.*;
import com.avatal.test.httpunit.*;

/**
 * @author Thomas Fuhrmann
 *
 * Testsuite f�r alle Systemtests mit HttpUnit
 * 
 */
public class HttpUnitTestSuite_1 {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(LoginTest.class);
        suite.addTestSuite(HeaderTest.class);
        suite.addTestSuite(SummaryCurrentTest.class);
        suite.addTestSuite(SummaryCurrentComponentsTest.class);
        suite.addTestSuite(SummaryOfferTest.class);
        suite.addTestSuite(SummaryAttendedTest.class);
        suite.addTestSuite(ProfileEditTest.class);
        suite.addTestSuite(UserCreateTest.class);
        suite.addTestSuite(UserEditTest.class);
        suite.addTestSuite(UserUpdateTest.class);
        suite.addTestSuite(GroupsCreateTest.class);
        suite.addTestSuite(GroupsEditTest.class);
        suite.addTestSuite(GroupsUpdateTest.class);
        suite.addTestSuite(GroupsAssignUserTest.class);
        suite.addTestSuite(GroupsAssignGroupsTest.class);
        suite.addTestSuite(GroupsAssignNewsTest.class);
        suite.addTestSuite(RolesAssignUserTest.class);
        return suite;
    }
}

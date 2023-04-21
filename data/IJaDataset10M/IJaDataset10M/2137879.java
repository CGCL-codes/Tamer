package org.josef.test.web.jsf.beans;

import static org.josef.annotations.Status.Stage.DEVELOPMENT;
import org.josef.annotations.Review;
import org.josef.annotations.Status;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * JUnit Test Suite for the web package.
 * @author Kees Schotanus
 * @version 1.1 $Revision: 1748 $
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ ActionBeanTest.class, CrudEntitiesBeanTest.class, CrudEntitiesUsingSessionBeanTest.class, UserSettingsBeanTest.class })
@Status(stage = DEVELOPMENT)
@Review(by = "Kees Schotanus", at = "2010-05-06")
public final class JsfBeansTestSuite {

    /**
     * Private constructor prevents creation of an instance outside this class.
     */
    private JsfBeansTestSuite() {
    }

    /**
     * Simple main method to run/debug tests using text mode version of JUnit.
     * @param args Not used.
     */
    public static void main(final String[] args) {
        org.junit.runner.JUnitCore.runClasses(JsfBeansTestSuite.class);
    }
}

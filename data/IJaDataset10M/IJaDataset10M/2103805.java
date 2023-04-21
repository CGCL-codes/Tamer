package org.hibernate.jsr303.tck.tests.constraints.constraintdefinition;

import com.google.gwt.junit.client.GWTTestCase;
import org.hibernate.jsr303.tck.util.client.Failing;
import org.hibernate.jsr303.tck.util.client.NonTckTest;

/**
 * Test wrapper for {@link ConstraintDefinitionsTest}.
 */
public class ConstraintDefinitionsGwtTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.hibernate.jsr303.tck.tests.constraints.constraintdefinition.TckTest";
    }

    @Failing(issue = 6284)
    public void testConstraintWithCustomAttributes() {
        fail();
    }

    @Failing(issue = 6284)
    public void testDefaultGroupAssumedWhenNoGroupsSpecified() {
        fail();
    }

    @NonTckTest
    public void testThereMustBeOnePassingTest() {
    }
}

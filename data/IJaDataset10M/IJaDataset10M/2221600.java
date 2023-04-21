package org.ujorm.orm.inheritance;

import junit.framework.TestCase;
import org.ujorm.orm.inheritance.sample.SampleOfInheritance;

/**
 * The tests of the SQL LIMIT & OFFSET.
 * @author Pavel Ponec
 */
public class InheritanceTest extends TestCase {

    public InheritanceTest(String testName) {
        super(testName);
    }

    private static Class suite() {
        return InheritanceTest.class;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SuppressWarnings("deprecation")
    public void testInheritance() {
        SampleOfInheritance.main(new String[] {});
    }

    public static void main(java.lang.String[] argList) {
        junit.textui.TestRunner.run(suite());
    }
}

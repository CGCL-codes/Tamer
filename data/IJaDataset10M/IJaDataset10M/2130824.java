package org.apache.myfaces.trinidad.component.core.output;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.apache.myfaces.trinidad.component.UIXObject;

/**
 * Unit tests for CoreSeparator.
 *
 */
public class CoreSeparatorTest extends UIXObjectTestCase {

    /**
   * Creates a new CoreSeparatorTest.
   *
   * @param testName  the unit test name
   */
    public CoreSeparatorTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public static Test suite() {
        return new TestSuite(CoreSeparatorTest.class);
    }

    /**
   * Tests the initial values for the component attributes.
   */
    public void testInitialAttributeValues() {
        CoreSeparator component = new CoreSeparator();
        assertEquals(true, component.isRendered());
    }

    /**
   * Tests the transparency of the component attribute by comparing
   * bean accessor and mutator methods with attribute map accessor
   * and mutator methods.
   */
    public void testAttributeTransparency() {
        CoreSeparator component = new CoreSeparator();
        doTestAttributeTransparency(component, "rendered", Boolean.TRUE, Boolean.FALSE);
    }

    /**
   * Tests the transparency of the component facets by comparing
   * bean accessor and mutator methods with facet map accessor
   * and mutator methods.
   */
    public void testFacetTransparency() {
    }

    @Override
    protected UIXObject createTestComponent() {
        return new CoreSeparator();
    }

    public static void main(String[] args) {
        TestRunner.run(CoreSeparatorTest.class);
    }
}

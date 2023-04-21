package org.apache.myfaces.trinidad.component.html;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.myfaces.trinidad.component.UIComponentTestCase;

/**
 * Unit tests for HtmlCellFormat.
 *
 */
public class HtmlCellFormatTest extends UIComponentTestCase {

    /**
   * Creates a new HtmlCellFormatTest.
   *
   * @param testName  the unit test name
   */
    public HtmlCellFormatTest(String testName) {
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
        return new TestSuite(HtmlCellFormatTest.class);
    }

    /**
   * Tests the initial values for the component attributes.
   */
    public void testInitialAttributeValues() {
        HtmlCellFormat component = new HtmlCellFormat();
        assertTrue(component.isRendered());
    }

    /**
   * Tests the transparency of the component attribute by comparing
   * bean accessor and mutator methods with attribute map accessor
   * and mutator methods.
   */
    public void testAttributeTransparency() {
        HtmlCellFormat component = new HtmlCellFormat();
        doTestAttributeTransparency(component, "columnSpan", new Integer(1), new Integer(2));
        doTestAttributeTransparency(component, "halign", "top", "bottom");
        doTestAttributeTransparency(component, "header", Boolean.TRUE, Boolean.FALSE);
        doTestAttributeTransparency(component, "headers", "h1 h2", "h3 h4");
        doTestAttributeTransparency(component, "height", "50%", "100%");
        doTestAttributeTransparency(component, "rowSpan", new Integer(3), new Integer(4));
        doTestAttributeTransparency(component, "shortText", "foo", "bar");
        doTestAttributeTransparency(component, "valign", "left", "right");
        doTestAttributeTransparency(component, "width", "25%", "75%");
        doTestAttributeTransparency(component, "wrappingDisabled", Boolean.TRUE, Boolean.FALSE);
    }

    /**
   * Tests the transparency of the component facets by comparing
   * bean accessor and mutator methods with facet map accessor
   * and mutator methods.
   */
    public void testFacetTransparency() {
    }

    /**
   * Tests the apply-request-values lifecycle phase.
   */
    public void testApplyRequestValues() {
        HtmlCellFormat component = new HtmlCellFormat();
        doTestApplyRequestValues(component);
    }

    /**
   * Tests the process-validations lifecycle phase.
   */
    public void testProcessValidations() {
        HtmlCellFormat component = new HtmlCellFormat();
        doTestProcessValidations(component);
    }

    /**
   * Tests the update-model-values lifecycle phase.
   */
    public void testUpdateModelValues() {
        HtmlCellFormat component = new HtmlCellFormat();
        doTestUpdateModelValues(component);
    }

    /**
   * Tests the invoke-application lifecycle phase.
   */
    public void testInvokeApplication() {
        HtmlCellFormat component = new HtmlCellFormat();
        doTestInvokeApplication(component, null);
    }

    /**
   * Tests the render-response lifecycle phase.
   *
   * @throws IOException  when test fails
   */
    public void testRenderResponse() throws IOException {
        HtmlCellFormat component = new HtmlCellFormat();
        doTestRenderResponse(component);
    }
}

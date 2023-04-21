package com.volantis.mcs.protocols.html.css.emulator.renderer;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertyRenderer;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests the vertical alignment renderer.
 */
public class HTML3_2VerticalAlignEmulationPropertyRendererTestCase extends TestCaseAbstract {

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * Tests that vertical alignment is rendered correctly.
     */
    public void testAddingVerticalAlignmentAttribute() throws Exception {
        final HTML3_2VerticalAlignEmulationPropertyRenderer renderer = new HTML3_2VerticalAlignEmulationPropertyRenderer();
        final StyleKeyword kvalue = VerticalAlignKeywords.TOP;
        assertTrue("align attribute should be added to the img element", doTest(renderer, kvalue, "img", "<img align=\"top\"/>"));
        assertTrue("align attribute should be added to the input element", doTest(renderer, kvalue, "input", "<input align=\"top\"/>"));
        assertTrue("valign attribute should be added to the tr element", doTest(renderer, kvalue, "tr", "<tr valign=\"top\"/>"));
        assertTrue("valign attribute should be added to the td element", doTest(renderer, kvalue, "td", "<td valign=\"top\"/>"));
        assertTrue("valign attribute should be added to the th element", doTest(renderer, kvalue, "th", "<th valign=\"top\"/>"));
    }

    /**
     * Helper method which tests that the StyleValue added as an attribute to
     * the named element is as expected.
     * @param renderer the StyleEmulationPropertyRenderer to test
     * @param kvalue the style value to add as an attribute
     * @param elementName the name of the element to which the attribute is
     * added
     * @param expectedResult the expected result of the attribute addition
     * @return true if the rendering was as expected; false otherwise
     */
    private boolean doTest(StyleEmulationPropertyRenderer renderer, StyleKeyword kvalue, String elementName, String expectedResult) {
        Element elem = domFactory.createElement();
        elem.setName(elementName);
        renderer.apply(elem, kvalue);
        return DOMUtilities.toString(elem).equals(expectedResult);
    }
}

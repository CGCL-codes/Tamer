package javax.swing.text.html;

import javax.swing.BasicSwingTestCase;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.CSS.Attribute;
import junit.framework.TestCase;

public class StyleSheet_ConvertAttr_ListStylePositionTest extends TestCase {

    private StyleSheet ss;

    private MutableAttributeSet simple;

    private Object cssValue;

    protected void setUp() throws Exception {
        super.setUp();
        ss = new StyleSheet();
        simple = new SimpleAttributeSet();
    }

    public void testListStylePositionInside() throws Exception {
        ss.addCSSAttribute(simple, Attribute.LIST_STYLE_POSITION, "inside");
        cssValue = simple.getAttribute(Attribute.LIST_STYLE_POSITION);
        assertEquals("inside", cssValue.toString());
    }

    public void testListStylePositionOutside() throws Exception {
        ss.addCSSAttribute(simple, Attribute.LIST_STYLE_POSITION, "outside");
        cssValue = simple.getAttribute(Attribute.LIST_STYLE_POSITION);
        assertEquals("outside", cssValue.toString());
    }

    public void testListStylePositionCompact() throws Exception {
        ss.addCSSAttribute(simple, Attribute.LIST_STYLE_POSITION, "compact");
        cssValue = simple.getAttribute(Attribute.LIST_STYLE_POSITION);
        if (BasicSwingTestCase.isHarmony()) {
            assertEquals(0, simple.getAttributeCount());
            assertNull(cssValue);
        } else {
            assertEquals("compact", cssValue.toString());
        }
    }
}

package javax.swing.text.html;

import javax.swing.BasicSwingTestCase;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;

public class StyleSheet_ConvertAttr_LineHeightTest extends StyleSheet_ConvertAttr_LengthTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        cssKey = CSS.Attribute.LINE_HEIGHT;
        negativeValuesInvalid = true;
    }

    public void testNormal() {
        ss.addCSSAttribute(simple, cssKey, "normal");
        cssValue = simple.getAttribute(cssKey);
        assertEquals("normal", cssValue.toString());
    }

    public void testMedium() {
        ss.addCSSAttribute(simple, cssKey, "medium");
        cssValue = simple.getAttribute(cssKey);
        if (!BasicSwingTestCase.isHarmony()) {
            assertEquals(1, simple.getAttributeCount());
            assertEquals("medium", cssValue.toString());
            return;
        }
        assertEquals(0, simple.getAttributeCount());
        assertNull(cssValue);
    }

    public void testLength11_1() {
        ss.addCSSAttribute(simple, cssKey, "11.1");
        cssValue = simple.getAttribute(cssKey);
        assertEquals(1, simple.getAttributeCount());
        assertEquals("11.1", cssValue.toString());
    }

    public void testLineSpacing() {
        StyleSheet ss = new StyleSheet();
        AttributeSet attr = ss.addAttribute(ss.getEmptySet(), StyleConstants.LineSpacing, new Float(1.1));
        assertEquals(1, attr.getAttributeCount());
        assertNull(attr.getAttribute(CSS.Attribute.LINE_HEIGHT));
    }

    public void testLengthMinus11_1pt() {
        negativeValuesInvalid = BasicSwingTestCase.isHarmony();
        super.testLengthMinus11_1pt();
    }

    public void testLengthMinus11_1Percent() {
        negativeValuesInvalid = BasicSwingTestCase.isHarmony();
        super.testLengthMinus11_1Percent();
    }
}

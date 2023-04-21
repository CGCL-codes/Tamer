package javax.swing.text.html;

public class StyleSheet_ConvertAttr_MarginTest extends StyleSheet_ConvertAttr_SpaceTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        shorthandKey = CSS.Attribute.MARGIN;
        topKey = CSS.Attribute.MARGIN_TOP;
        rightKey = CSS.Attribute.MARGIN_RIGHT;
        bottomKey = CSS.Attribute.MARGIN_BOTTOM;
        leftKey = CSS.Attribute.MARGIN_LEFT;
        defaultValue = "0";
    }
}

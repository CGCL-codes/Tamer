package com.volantis.styling.device;

import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.CSSCompiler;
import com.volantis.styling.engine.AttributesMock;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import java.io.StringReader;

/**
 * Test cases for {@link DeviceStylingEngine}.
 */
public class DeviceStylingEngineTestCase extends TestCaseAbstract {

    private StylingFactory factory;

    protected void setUp() throws Exception {
        super.setUp();
        factory = StylingFactory.getDefaultInstance();
    }

    public void test() throws Exception {
        final AttributesMock attributesMock = new AttributesMock("attributesMock", expectations);
        attributesMock.expects.getAttributeValue(null, "class").returns(null);
        attributesMock.expects.getAttributeValue(null, "class").returns(null);
        attributesMock.expects.getAttributeValue(null, "class").returns(null);
        CSSCompiler compiler = factory.createDeviceCSSCompiler(DeviceOutlook.REALISTIC);
        CompiledStyleSheet compiledSheet = compiler.compile(new StringReader("* {text-align: left}" + "a {font-family: <not-set>}" + "a:link {font-size: <unknown> !-medium}" + "a:link:hover {color: <default>}" + "a:first-letter:link {color: green}"), "");
        DeviceStylingEngine engine = factory.createDeviceStylingEngine(compiledSheet);
        DeviceStyles styles;
        styles = engine.startElement("body", attributesMock);
        assertNotNull("Styles should not be null", styles);
        assertEquals("text-align:left", styles.getStandardCSS());
        styles = engine.startElement("a", attributesMock);
        assertNotNull("Styles should not be null", styles);
        assertEquals("{font-family:<not-set>;text-align:left}\n" + ":first-letter {...<default>...}\n" + ":first-letter:link {color:green}\n" + ":link:hover {color:<default>}\n" + ":link {font-size:<unknown> !-medium}", styles.getStandardCSS());
        styles = engine.startElement("p", attributesMock);
        assertNotNull("Styles should not be null", styles);
        assertEquals("text-align:left", styles.getStandardCSS());
    }
}

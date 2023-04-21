package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.css.impl.parser.ParsingMockTestCaseAbstract;
import com.volantis.mcs.themes.MutableStylePropertiesMock;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.StyleValueTypeVisitor;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import junit.framework.Test;
import junit.framework.TestSuite;
import java.util.Iterator;
import java.util.Set;

/**
 * Test that the parameterized property parser works properly.
 */
public class ParameterizedPropertyParserTestCase extends ParsingMockTestCaseAbstract implements StyleValueTypeVisitor {

    private static final StyleValueFactory STYLE_VALUE_FACTORY = StyleValueFactory.getDefaultInstance();

    private final Set supportedTypes;

    private final StyleProperty property;

    private final StyleValueType type;

    /**
     * Dynamically create a test suite that contains tests for all the
     * properties that use a parameterized parser.
     *
     * @return A test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        PropertyParserFactory factory = new DefaultPropertyParserFactory("PropertyParsers.properties");
        StylePropertyDefinitions definitions = StylePropertyDetails.getDefinitions();
        for (Iterator i = definitions.stylePropertyIterator(); i.hasNext(); ) {
            StyleProperty property = (StyleProperty) i.next();
            PropertyParser parser = factory.getPropertyParser(property.getName());
            if (parser instanceof ParameterizedPropertyParser) {
                Set supportedTypes = property.getStandardDetails().getSupportedTypes();
                TestSuite propertySuite = new TestSuite("test '" + property.getName() + "'");
                for (Iterator t = supportedTypes.iterator(); t.hasNext(); ) {
                    StyleValueType type = (StyleValueType) t.next();
                    propertySuite.addTest(new ParameterizedPropertyParserTestCase(property, supportedTypes, type));
                }
                suite.addTest(propertySuite);
            }
        }
        return suite;
    }

    public ParameterizedPropertyParserTestCase(StyleProperty property, Set supportedTypes, StyleValueType type) {
        this.property = property;
        this.supportedTypes = supportedTypes;
        this.type = type;
        setName(type.getType());
    }

    protected void runTest() throws Throwable {
        type.accept(this);
    }

    private void parseValue(String css, StyleValue expected) {
        final MutableStylePropertiesMock mutableStylePropertiesMock = new MutableStylePropertiesMock("mutableStylePropertiesMock", expectations);
        styleSheetFactoryMock.expects.createStyleProperties().returns(mutableStylePropertiesMock);
        PropertyValue propertyValue = ThemeFactory.getDefaultInstance().createPropertyValue(property, expected);
        mutableStylePropertiesMock.expects.setPropertyValue(propertyValue);
        parseDeclarations(property.getName() + ":" + css);
    }

    public void visitAngle() {
        parseValue("10deg", ANGLE_10DEG);
        parseValue("-10deg", ANGLE_NEGATIVE_10DEG);
        parseValue("20rad", ANGLE_20RAD);
        parseValue("30grad", ANGLE_30GRAD);
    }

    public void visitColor() {
        parseValue("green", COLOR_GREEN);
        parseValue("rgb(10%,20%,30%)", COLOR_10PC_20PC_30PC);
        parseValue("rgb(10,20,30)", COLOR_3_7_11);
        parseValue("#fff", COLOR_FFF);
        parseValue("#123456", COLOR_123456);
    }

    public void visitFunction() {
    }

    public void visitComponentURI() {
        parseValue("mcs-component-url(/image.mimg)", IMAGE_MIMG);
        parseValue("mcs-component-url(\"/image.mimg\")", IMAGE_MIMG);
    }

    public void visitTranscodableURI() {
        parseValue("mcs-transcodable-url(\"http://localhost:8080/tomcat.gif\")", STYLE_VALUE_FACTORY.getTranscodableURI(null, "http://localhost:8080/tomcat.gif"));
        parseValue("mcs-transcodable-url('http://localhost:8080/tomcat.gif')", STYLE_VALUE_FACTORY.getTranscodableURI(null, "http://localhost:8080/tomcat.gif"));
    }

    public void visitIdentifier() {
        throw new UnsupportedOperationException();
    }

    public void visitInherit() {
        parseValue("inherit", INHERIT);
    }

    public void visitInteger() {
        parseValue("10", INTEGER_10);
        parseValue("-10", INTEGER_NEGATIVE_10);
    }

    public void visitKeyword() {
        AllowableKeywords allowableKeywords = property.getStandardDetails().getAllowableKeywords();
        StyleKeyword keyword = (StyleKeyword) allowableKeywords.getKeywords().get(0);
        parseValue(keyword.getName(), keyword);
    }

    public void visitLength() {
        if (!supportedTypes.contains(StyleValueType.NUMBER)) {
            parseValue("0", LENGTH_0PX);
        }
        parseValue("1px", LENGTH_1PX);
        parseValue("2cm", LENGTH_2CM);
        parseValue("-2pt", LENGTH_NEGATIVE_2PT);
    }

    public void visitList() {
        throw new UnsupportedOperationException();
    }

    public void visitNumber() {
        parseValue("10", NUMBER_10);
        parseValue("1.5", NUMBER_1_5);
        parseValue("-1.5", NUMBER_NEGATIVE_1_5);
    }

    public void visitPair() {
        throw new UnsupportedOperationException();
    }

    public void visitPercentage() {
        parseValue("25%", PERCENTAGE_25);
    }

    public void visitString() {
        parseValue("\"string\"", STRING_STRING);
        parseValue("'string'", STRING_STRING);
    }

    public void visitTime() {
        parseValue("10s", TIME_10S);
        parseValue("-10ms", TIME_NEGATIVE_10MS);
    }

    public void visitURI() {
        parseValue("url(/image.png)", IMAGE_PNG);
        parseValue("url(\"/image.png\")", IMAGE_PNG);
    }

    public void visitFrequency() {
        parseValue("10hz", FREQ_10HZ);
        parseValue("10khz", FREQ_10KHZ);
    }

    public void visitFraction() {
        parseValue("2cm/10s", FRACTION_FIFTH_CM_PER_SEC);
        parseValue("-2pt/10s", FRACTION_NEG_FIFTH_PT_PER_SEC);
    }
}

package org.apache.axis2.schema.populate.derived;

import org.apache.axis2.databinding.utils.ConverterUtil;

public class DerivedTypeShortPopulateTest extends AbstractDerivedPopulater {

    private String values[] = { "17444", "-12343", "2", "0", "-6" };

    private String xmlString[] = { "<DerivedShort xmlns=\"http://soapinterop.org/xsd\">" + values[0] + "</DerivedShort>", "<DerivedShort xmlns=\"http://soapinterop.org/xsd\">" + values[1] + "</DerivedShort>", "<DerivedShort xmlns=\"http://soapinterop.org/xsd\">" + values[2] + "</DerivedShort>", "<DerivedShort xmlns=\"http://soapinterop.org/xsd\">" + values[3] + "</DerivedShort>", "<DerivedShort xmlns=\"http://soapinterop.org/xsd\">" + values[4] + "</DerivedShort>" };

    protected void setUp() throws Exception {
        className = "org.soapinterop.xsd.DerivedShort";
        propertyClass = Short.class;
    }

    public void testPopulate() throws Exception {
        for (int i = 0; i < values.length; i++) {
            checkValue(xmlString[i], values[i]);
        }
    }

    protected String convertToString(Object o) {
        return ConverterUtil.convertToString((Short) o);
    }
}

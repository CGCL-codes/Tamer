package org.apache.axis2.schema.populate.simple;

import org.apache.axis2.databinding.types.Time;
import org.apache.axis2.databinding.utils.ConverterUtil;

public class SimpleTypeTimePopulateTest extends AbstractSimplePopulater {

    private String values[] = { "13:20:00Z", "23:59:59+05:30", "23:59:59" };

    private String xmlString[] = { "<timeParam xmlns=\"http://soapinterop.org/xsd\">" + values[0] + "</timeParam>", "<timeParam xmlns=\"http://soapinterop.org/xsd\">" + values[1] + "</timeParam>" };

    protected void setUp() throws Exception {
        className = "org.soapinterop.xsd.TimeParam";
        propertyClass = Time.class;
    }

    public void testPopulate() throws Exception {
        Time time;
        for (int i = 0; i < 2; i++) {
            time = new Time(values[i]);
            checkValue(xmlString[i], time.toString());
        }
    }

    protected String convertToString(Object o) {
        return ConverterUtil.convertToString((Time) o);
    }
}

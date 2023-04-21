package com.volantis.synergetics.reporting;

import com.volantis.synergetics.reporting.Log4jReportHandler;

public class Log4jHandlerReportingTestCase extends ReportingTestCaseAbstract {

    public String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<reporting-config xmlns=\"http://www.volantis.com/xmlns/2005/10/storefront/reporting-configuration\"\n" + "     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + "     xsi:schemaLocation=\"http://www.volantis.com/xmlns/2005/10/storefront/reporting-configuration file:/opt/work/2006050805/Synergetics/src/com/volantis/synergetics/reporting/reporting-configuration.xsd\">\n" + "    <report binding=\"" + TestCounterGroup.class.getName() + "\" enabled=\"true\">\n" + "        <generic-handler class=\"" + Log4jReportHandler.class.getName() + "\">\n" + "            <param>\n" + "                <name>message.format</name>\n" + "                <value>MY MESSAGE {Floaty} with {Intlike}</value>\n" + "            </param>\n" + "        </generic-handler>\n" + "    </report>\n" + "   \n" + "</reporting-config>";

    public String getConfig() {
        return s;
    }
}

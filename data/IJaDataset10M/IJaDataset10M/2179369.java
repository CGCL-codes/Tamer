package org.opennms.netmgt.config.poller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.runners.Parameterized.Parameters;
import org.opennms.core.test.xml.XmlTest;

public class OutagesTest extends XmlTest<Outages> {

    public OutagesTest(final Outages sampleObject, final String sampleXml, final String schemaFile) {
        super(sampleObject, sampleXml, schemaFile);
    }

    @Parameters
    public static Collection<Object[]> data() throws ParseException {
        final Outage outage = new Outage();
        outage.setName("junit test");
        outage.setType("weekly");
        final Interface intf = new Interface();
        intf.setAddress("match-any");
        outage.addInterface(intf);
        final Time time = new Time();
        time.setDay("monday");
        time.setBegins("13:30:00");
        time.setEnds("14:45:00");
        outage.addTime(time);
        Outages outages = new Outages();
        outages.addOutage(outage);
        return Arrays.asList(new Object[][] { { outages, "<outages>\n" + "  <outage name='junit test' type='weekly'>\n" + "    <time day='monday' begins='13:30:00' ends='14:45:00'/>\n" + "    <interface address='match-any'/>\n" + "  </outage>\n" + "</outages>\n", "target/classes/xsds/poll-outages.xsd" } });
    }
}

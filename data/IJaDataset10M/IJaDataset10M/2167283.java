package org.matsim.events;

import org.matsim.basic.v01.IdImpl;
import org.matsim.testcases.MatsimTestCase;

/**
 * @author mrieser
 */
public class AgentArrivalEventTest extends MatsimTestCase {

    public void testWriteReadXml() {
        final AgentArrivalEvent event = XmlEventsTester.testWriteReadXml(getOutputDirectory() + "events.xml", new AgentArrivalEvent(68423.98, new IdImpl("443"), new IdImpl("78-3")));
        assertEquals(68423.98, event.getTime(), EPSILON);
        assertEquals(new IdImpl("443"), event.getPersonId());
        assertEquals(new IdImpl("78-3"), event.getLinkId());
    }
}

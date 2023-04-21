package org.matsim.events;

import org.matsim.basic.v01.IdImpl;
import org.matsim.testcases.MatsimTestCase;

/**
 * @author mrieser
 */
public class ActStartEventTest extends MatsimTestCase {

    public void testWriteReadXml() {
        final ActStartEvent event = XmlEventsTester.testWriteReadXml(getOutputDirectory() + "events.xml", new ActStartEvent(5668.27, new IdImpl("a92"), new IdImpl("l081"), "work"));
        assertEquals(5668.27, event.getTime(), EPSILON);
        assertEquals("a92", event.agentId);
        assertEquals(new IdImpl("l081"), event.getLinkId());
        assertEquals("work", event.getActType());
    }
}

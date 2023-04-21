package org.matsim.core.events;

import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.network.NetworkChangeEvent;
import org.matsim.testcases.MatsimTestCase;

/**
 * @author cdobler
 */
public class LinkChangeFreespeedEventTest extends MatsimTestCase {

    public void testWriteReadXml() {
        final NetworkChangeEvent.ChangeValue changeValue = new NetworkChangeEvent.ChangeValue(NetworkChangeEvent.ChangeType.ABSOLUTE, 100.0);
        final LinkChangeFreespeedEventImpl event1 = new LinkChangeFreespeedEventImpl(6823.8, new IdImpl("abcd"), changeValue);
        final LinkChangeFreespeedEventImpl event2 = XmlEventsTester.testWriteReadXml(getOutputDirectory() + "events.xml", event1);
        assertEquals(event1.getTime(), event2.getTime(), EPSILON);
        assertEquals(event1.getLinkId(), event2.getLinkId());
        assertEquals(event1.getChangeValue().getType(), event2.getChangeValue().getType());
        assertEquals(event1.getChangeValue().getValue(), event2.getChangeValue().getValue());
    }
}

package org.charvolant.tmsnet.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.charvolant.tmsnet.resources.ResourceLocator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link PVRState} class
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
public class PVRStateTest implements PropertyChangeListener {

    /** The state to test */
    private PVRState state;

    /** The received property changes */
    private List<PropertyChangeEvent> events;

    /** The start date */
    private Date now;

    /** The event id */
    private int eventId;

    /**
   * @throws java.lang.Exception
   */
    @Before
    public void setUp() throws Exception {
        TestPVRStateFactory factory = TestPVRStateFactory.getInstance();
        this.now = new Date();
        this.state = new PVRState();
        this.state.setLocator(new ResourceLocator());
        factory.buildChannels(this.state, this.now);
        this.events = new ArrayList<PropertyChangeEvent>();
        this.state.addPropertyChangeListener(this);
        this.eventId = 5000;
    }

    /**
   * @throws java.lang.Exception
   */
    @After
    public void tearDown() throws Exception {
    }

    /**
   * Generate a list of events
   * 
   * @param channel The channel index
   * @param n The number of events
   * @param offsetStart where to start from (now) in hours
   * @param offsetEnd where to end from (now) in hours
   * 
   * @return The list of events
   */
    protected List<EventDescription> generateEvents(int channel, int n, int offsetStart, int offsetEnd) {
        ArrayList<EventDescription> evts = new ArrayList<EventDescription>(n);
        Calendar cal = Calendar.getInstance();
        int mins = (offsetEnd - offsetStart) * 60 / n;
        EventDescription evt;
        cal.setTime(this.now);
        cal.add(Calendar.HOUR, offsetStart);
        for (int i = 0; i < n; i++) {
            evt = new EventDescription();
            evt.setDescription("Event " + (i % 20) + " on " + channel);
            evt.setDuration(mins);
            evt.setStart(cal.getTime());
            cal.add(Calendar.MINUTE, mins);
            evt.setFinish(cal.getTime());
            evt.setEventId(this.eventId++);
            evt.setRunning(!this.now.before(evt.getStart()) && evt.getFinish().after(this.now));
            evt.setServiceId(channel);
            evt.setTitle("Event " + channel + "/" + (i % 10));
            evt.setTransportStreamId(channel);
            evts.add(evt);
        }
        return evts;
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.PVRState#partition(java.util.Collection)}.
   */
    @Test
    public void testPartition1() {
        List<EventDescription> list = this.generateEvents(5, 2, -1, 1);
        List<List<EventDescription>> partition = this.state.partition(list);
        Assert.assertEquals(6, partition.size());
        Assert.assertNull(partition.get(0));
        list = partition.get(5);
        Assert.assertNotNull(list);
        Assert.assertEquals(2, list.size());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.PVRState#partition(java.util.Collection)}.
   */
    @Test
    public void testPartition2() {
        List<EventDescription> list = this.generateEvents(5, 2, -1, 1);
        List<List<EventDescription>> partition;
        list.addAll(this.generateEvents(3, 4, -1, 1));
        partition = this.state.partition(list);
        Assert.assertEquals(6, partition.size());
        Assert.assertNull(partition.get(0));
        list = partition.get(3);
        Assert.assertNotNull(list);
        Assert.assertEquals(4, list.size());
        list = partition.get(5);
        Assert.assertNotNull(list);
        Assert.assertEquals(2, list.size());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.PVRState#partition(java.util.Collection)}.
   */
    @Test
    public void testPartition3() {
        List<EventDescription> list = this.generateEvents(5, 2, -1, 1);
        List<List<EventDescription>> partition;
        list.addAll(this.generateEvents(3, 4, -1, 1));
        list.addAll(this.generateEvents(5, 4, 1, 2));
        partition = this.state.partition(list);
        Assert.assertEquals(6, partition.size());
        Assert.assertNull(partition.get(0));
        list = partition.get(3);
        Assert.assertNotNull(list);
        Assert.assertEquals(4, list.size());
        list = partition.get(5);
        Assert.assertNotNull(list);
        Assert.assertEquals(6, list.size());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.PVRState#updateEvents(java.util.Collection)}.
   */
    @Test
    public void testUpdateEventsCollectionOfEvent1() {
        List<EventDescription> list = this.generateEvents(5, 2, -1, 1);
        Channel epg;
        this.state.updateEPG(5, list);
        Assert.assertNotNull(this.state.getEPG());
        Assert.assertEquals(6, this.state.getEPG().size());
        epg = this.state.getEPG().get(5);
        Assert.assertNotNull(epg);
        Assert.assertEquals(2, epg.getEvents().size());
        Assert.assertEquals(1, this.events.size());
        Assert.assertEquals(this.state, this.events.get(0).getSource());
        Assert.assertEquals("epg", this.events.get(0).getPropertyName());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.PVRState#updateEPG(java.util.Collection)}.
   */
    @Test
    public void testUpdateEPGCollectionOfEvent2() {
        List<EventDescription> list = this.generateEvents(5, 2, -1, 1);
        Channel epg;
        this.state.updateEPG(5, list);
        list = this.generateEvents(2, 4, 1, 2);
        this.state.updateEPG(2, list);
        list = this.generateEvents(5, 4, 1, 2);
        this.state.updateEPG(5, list);
        Assert.assertNotNull(this.state.getEPG());
        Assert.assertEquals(6, this.state.getEPG().size());
        epg = this.state.getEPG().get(5);
        Assert.assertNotNull(epg);
        Assert.assertEquals(6, epg.getEvents().size());
        epg = this.state.getEPG().get(2);
        Assert.assertNotNull(epg);
        Assert.assertEquals(4, epg.getEvents().size());
        Assert.assertEquals(3, this.events.size());
        Assert.assertEquals(this.state, this.events.get(0).getSource());
        Assert.assertEquals("epg", this.events.get(0).getPropertyName());
    }

    /**
   * Test method for {@link org.charvolant.tmsnet.model.PVRState#updateEPG(java.util.Collection)}.
   */
    @Test
    public void testUpdateEPGCollectionOfEvent3() {
        List<EventDescription> list = this.generateEvents(5, 3, -1, 2);
        Channel epg;
        this.state.updateEPG(5, list);
        list = this.generateEvents(5, 2, 0, 1);
        this.state.updateEPG(5, list);
        Assert.assertNotNull(this.state.getEPG());
        Assert.assertEquals(6, this.state.getEPG().size());
        epg = this.state.getEPG().get(5);
        Assert.assertNotNull(epg);
        Assert.assertEquals(4, epg.getEvents().size());
        Assert.assertEquals(2, this.events.size());
        Assert.assertEquals(this.state, this.events.get(0).getSource());
        Assert.assertEquals("epg", this.events.get(0).getPropertyName());
        Assert.assertEquals(this.state, this.events.get(1).getSource());
        Assert.assertEquals("epg", this.events.get(1).getPropertyName());
    }

    /**
   * Time trials for EPG update
   */
    @Test
    public void testUpdateEPGCollectionSpeed1() {
        List<EventDescription> list = this.generateEvents(5, 60, 0, 30);
        long time = System.currentTimeMillis();
        this.state.updateEPG(5, list);
        time = System.currentTimeMillis() - time;
        System.out.println("Update took " + time + "ms");
    }

    /**
   * Time trials for EPG update
   */
    @Test
    public void testUpdateEPGCollectionSpeed2() {
        List<EventDescription> list = this.generateEvents(5, 120, 0, 30);
        long time = System.currentTimeMillis();
        this.state.updateEPG(5, list);
        time = System.currentTimeMillis() - time;
        System.out.println("Update took " + time + "ms");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.events.add(evt);
    }
}

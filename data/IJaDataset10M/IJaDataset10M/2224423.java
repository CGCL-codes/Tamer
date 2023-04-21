package com.christianposta.learnddd.domain.model.handling;

import static com.christianposta.learnddd.domain.model.location.SampleLocations.CHICAGO;
import static com.christianposta.learnddd.domain.model.location.SampleLocations.HELSINKI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.christianposta.learnddd.domain.model.cargo.Cargo;
import com.christianposta.learnddd.domain.model.cargo.RouteSpecification;
import com.christianposta.learnddd.domain.model.cargo.TrackingId;

public class HandlingHistoryTest {

    private Cargo cargo;

    @Before
    public void beforeEachTest() {
        TrackingId trackingId = new TrackingId("CARGO123");
        RouteSpecification routeSpecification = new RouteSpecification(CHICAGO, HELSINKI, new Date());
        this.cargo = new Cargo(trackingId, routeSpecification);
    }

    @Test
    public void emptyHandlingHistory() {
        HandlingHistory history = new HandlingHistory(new ArrayList<HandlingEvent>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidHandlingEvents() {
        HandlingHistory history = new HandlingHistory(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidHandlingEventsWithNullElement() {
        ArrayList<HandlingEvent> eventArrayList = new ArrayList<HandlingEvent>();
        eventArrayList.add(null);
        HandlingHistory history = new HandlingHistory(eventArrayList);
    }

    @Test
    public void duplicateHandlingEvents() {
        HandlingEvent e1 = new HandlingEvent(cargo, new Date(1), new Date(2), HandlingType.CLAIM, HELSINKI);
        HandlingEvent e2 = new HandlingEvent(cargo, new Date(1), new Date(2), HandlingType.CLAIM, HELSINKI);
        List<HandlingEvent> events = Arrays.asList(e1, e2);
        HandlingHistory history = new HandlingHistory(events);
        List<HandlingEvent> distinctEvents = history.distinctEventsByCompletionTime();
        assertEquals(1, distinctEvents.size());
        assertTrue(cargo.sameIdentityAs(distinctEvents.get(0).cargo()));
        assertTrue(HELSINKI.sameIdentityAs(distinctEvents.get(0).location()));
    }

    @Test
    public void completionTimeOrdering() {
        HandlingEvent e1 = new HandlingEvent(cargo, new Date(1), new Date(12), HandlingType.RECEIVE, CHICAGO);
        HandlingEvent e2 = new HandlingEvent(cargo, new Date(2), new Date(13), HandlingType.CLAIM, HELSINKI);
        HandlingEvent e3 = new HandlingEvent(cargo, new Date(3), new Date(14), HandlingType.CUSTOMS, HELSINKI);
        List<HandlingEvent> events = Arrays.asList(e2, e1, e3);
        HandlingHistory history = new HandlingHistory(events);
        List<HandlingEvent> orderedEvents = history.distinctEventsByCompletionTime();
        HandlingEvent event = orderedEvents.get(0);
        assertTrue(HandlingType.RECEIVE.sameValueAs(event.type()));
        assertTrue(CHICAGO.sameIdentityAs(event.location()));
        event = orderedEvents.get(1);
        assertTrue(HandlingType.CLAIM.sameValueAs(event.type()));
        assertTrue(HELSINKI.sameIdentityAs(event.location()));
        event = orderedEvents.get(2);
        assertTrue(HandlingType.CUSTOMS.sameValueAs(event.type()));
        assertTrue(HELSINKI.sameIdentityAs(event.location()));
    }

    @Test
    public void mostRecentEvent() {
        HandlingEvent e1 = new HandlingEvent(cargo, new Date(1), new Date(12), HandlingType.RECEIVE, CHICAGO);
        HandlingEvent e2 = new HandlingEvent(cargo, new Date(2), new Date(13), HandlingType.CLAIM, HELSINKI);
        HandlingEvent e3 = new HandlingEvent(cargo, new Date(3), new Date(14), HandlingType.CUSTOMS, HELSINKI);
        List<HandlingEvent> events = Arrays.asList(e2, e3, e1);
        HandlingHistory history = new HandlingHistory(events);
        HandlingEvent mostRecentEvent = history.mostRecentlyCompletedEvent();
        assertTrue(HandlingType.CUSTOMS.sameValueAs(mostRecentEvent.type()));
        assertTrue(HELSINKI.sameIdentityAs(mostRecentEvent.location()));
    }
}

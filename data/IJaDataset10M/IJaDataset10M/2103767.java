package upcoming.client.test;

import junit.framework.TestCase;
import upcoming.client.*;
import java.util.*;

public class ClientTest extends TestCase {

    private static final String ZIPCODE_SEATTLE = "98103";

    private static final String CALIFORNIA_WOEID = "2347563";

    static {
        System.getProperties().put("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "trace");
    }

    public Client getClient() {
        String upcomingApiKey = "455ebcdb86";
        Client c = new Client(upcomingApiKey);
        c.setCompressionEnabled(true);
        return c;
    }

    public void testFindVenuesNear() throws Exception {
        Client c = getClient();
        final double lat = 45.521694;
        final double lon = -122.691806;
        List<Venue> venues = c.findVenuesNear(lat, lon, 5);
        assertVenues(venues);
        if (venues.size() > 0) {
            String id = venues.get(0).getId();
            Venue v = c.getVenue(id);
            assertNotNull(v);
            assertValid(v);
            assertEquals(v.getId(), id);
        }
    }

    public void assertVenues(List<Venue> venues) {
        assertNotNull(venues);
        for (Venue v : venues) {
            assertValid(v);
        }
    }

    public void assertValid(Venue v) {
        System.out.println("venue: " + v.getName());
        assertNotNull(v);
        assertNotNull(v.getId());
    }

    public void testFindEventsNear() throws Exception {
        Client c = getClient();
        final double lat = 45.521694;
        final double lon = -122.691806;
        List<Event> events = c.findEventsNear(lat, lon, 5);
        assertEvents(events);
        events = c.findEventsNear("music", lat, lon, 4);
        assertEvents(events);
    }

    public void testMetro() throws Exception {
        Client c = getClient();
        final double lat = 45.521694;
        final double lon = -122.691806;
        Metro m = c.getMetro(lat, lon);
        assertNotNull(m);
        assertValid(m);
        String location = m.getName() + " " + m.getStateName();
        List<Event> events = c.findEventsNear(location, 5);
        assertEvents(events);
    }

    public void testGetCategories() throws Exception {
        Client c = getClient();
        List<Category> categories = c.getCategories();
        assertValidCategoryList(categories);
    }

    public void assertValidCategoryList(List<Category> categories) {
        assertNotNull(categories);
        for (Category c : categories) {
            assertValid(c);
        }
    }

    public void assertValid(Category c) {
        assertNotNull(c);
        assertNotNull(c.getId());
        assertNotNull(c.getName());
        assertNotNull(c.getDescription());
    }

    public void testBestInPlace() throws Exception {
        Client c = getClient();
        List<Event> events = c.getBestInPlaceByWhereOnEarthId(CALIFORNIA_WOEID);
        assertEvents(events);
    }

    protected void assertValid(Metro m) {
        assertNotNull(m.getId());
        assertNotNull(m.getName());
        assertNotNull(m.getCode());
        assertNotNull(m.toString());
        assertNotNull(m.getStateCode());
        assertNotNull(m.getStateId());
        assertNotNull(m.getStateName());
        assertNotNull(m.getCountryCode());
        assertNotNull(m.getCountryId());
        assertNotNull(m.getCountryName());
    }

    protected void assertEvents(List<Event> events) {
        assertNotNull(events);
        for (Event event : events) {
            assertValid(event);
        }
    }

    protected void assertValid(Event ev) {
        System.out.println("event: " + ev.getName());
        assertNotNull(ev);
        assertNotNull(ev.getId());
        assertNotNull(ev.toString());
    }
}

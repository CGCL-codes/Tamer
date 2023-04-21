package net.sf.barttracker.model;

import java.util.List;
import net.sf.barttracker.model.BartHtmlParseException;
import net.sf.barttracker.model.ETAHtmlParser;
import net.sf.barttracker.model.EstimatedArrival;
import net.sf.barttracker.model.Station;
import junit.framework.TestCase;

/** Test HTML parser */
public class ETAHtmlParserTest extends TestCase {

    private ETAHtmlParser htmlParser;

    public void setUp() {
        htmlParser = new ETAHtmlParser();
    }

    /** Multiple daly city trains arriving */
    public void testMultiArrival() {
        String html = "<td class=\"dest\">Daly City</td>\n" + "<td class=\"eta\">26 min, 28 min, 30 min</td>";
        List<EstimatedArrival> etas = htmlParser.extractEstimatedArrivals(Station.MONT, html);
        assertEquals("Wrong number of arrivals " + etas.toString(), 3, etas.size());
        for (EstimatedArrival eta : etas) {
            assertEquals("Wrong station", Station.MONT, eta.getStation());
            assertEquals("Wrong destination", Station.DALY, eta.getDestination());
        }
        assertEquals("Wrong arrival 0", 26, etas.get(0).getMinutes());
        assertEquals("Wrong arrival 1", 28, etas.get(1).getMinutes());
        assertEquals("Wrong arrival 2", 30, etas.get(2).getMinutes());
    }

    /** Single daly city train arriving */
    public void testSingleArrival() {
        String html = "<td class=\"dest\">Daly City</td>\n" + "<td class=\"eta\">26 min</td>";
        List<EstimatedArrival> etas = htmlParser.extractEstimatedArrivals(Station.MONT, html);
        assertEquals("Wrong number of arrivals " + etas.toString(), 1, etas.size());
        EstimatedArrival eta = etas.get(0);
        assertEquals("Wrong station", Station.MONT, eta.getStation());
        assertEquals("Wrong destination", Station.DALY, eta.getDestination());
        assertEquals("Wrong arrival 0", 26, eta.getMinutes());
    }

    /** Test single daly city train arrived on the platform */
    public void testTrainArrived() {
        String html = "<td class=\"dest\">Daly City</td>\n" + "<td class=\"eta\">Arrived</td>";
        List<EstimatedArrival> etas = htmlParser.extractEstimatedArrivals(Station.MONT, html);
        assertEquals("Wrong number of arrivals " + etas.toString(), 1, etas.size());
        EstimatedArrival eta = etas.get(0);
        assertEquals("Wrong station", Station.MONT, eta.getStation());
        assertEquals("Wrong destination", Station.DALY, eta.getDestination());
        assertEquals("Wrong arrival 0", 0, eta.getMinutes());
    }

    /** Test invalid HTML with an odd number of table cells */
    public void testInvalidOddCells() {
        String html = "<td class=\"dest\">Daly City</td>\n" + "<td class=\"dest\">Daly City</td>" + "<td class=\"eta\">26 min</td>";
        expectFailure(html);
    }

    /** invalid HTML wiith two destinations in a row */
    public void testTwoDests() {
        String html = "<td class=\"dest\">Daly City</td>\n" + "<td class=\"dest\">Daly City</td>";
        expectFailure(html);
    }

    /** invalid HTML with two ETAs in a row */
    public void testTwoEtas() {
        String html = "<td class=\"eta\">26 min</td>\n" + "<td class=\"eta\">26 min</td>";
        expectFailure(html);
    }

    /** blank HTML */
    public void testBlank() {
        List<EstimatedArrival> etas = htmlParser.extractEstimatedArrivals(Station.MONT, "");
        assertEquals("Wrong number of ETAs", 0, etas.size());
    }

    /** unrecognized station name asdf */
    public void testInvalidStation() {
        String html = "<td class=\"dest\">asdf</td>\n" + "<td class=\"eta\">26 min</td>";
        expectFailure(html);
    }

    /** Test retrieving a real live ETA URL and parsing it */
    public void testIntegration() throws Exception {
        for (Station station : Station.values()) {
            URLFetcher uf = new URLFetcher("http://bart.gov/scripts/aspx/station_eta_ajax.aspx?stn=" + station.getRealCode());
            String html = uf.call();
            List<EstimatedArrival> etas = htmlParser.extractEstimatedArrivals(station, html);
            assertTrue("No etas: " + station, etas.size() > 0);
            for (EstimatedArrival eta : etas) {
                assertEquals(eta.toString(), eta.getStation(), station);
                assertNotNull(station + " destination", eta.getDestination());
            }
        }
    }

    private BartHtmlParseException expectFailure(String html) {
        try {
            htmlParser.extractEstimatedArrivals(Station.MONT, html);
            fail("Passed, but should have failed");
            return null;
        } catch (BartHtmlParseException e) {
            return e;
        }
    }
}

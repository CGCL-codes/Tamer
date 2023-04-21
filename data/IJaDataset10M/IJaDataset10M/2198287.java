package edu.hawaii.halealohacli.command;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Test;
import org.wattdepot.client.WattDepotClient;
import org.wattdepot.util.tstamp.Tstamp;

/**
 * JUnit test module for the <code>RankTowersModule</code> class.
 * 
 * @author bking
 * 
 */
public class TestRankTowersModule {

    String url = "http://server.wattdepot.org:8190/wattdepot/";

    RankTowersModule module = new RankTowersModule();

    WattDepotClient client = new WattDepotClient(url);

    String command = "rank-tower";

    String date1 = "2011-11-25";

    String date2 = "2011-11-26";

    String date3 = "2011.11.25";

    String date4 = "2011.11.26";

    /**
   * Test the parse input method.
   */
    @Test
    public void testInput() {
        assertTrue("Test if the length matches", module.parseInput(client, String.format("%s %s %s", command, date1, date2)));
        assertFalse("Test if it ignores extra arguments", module.parseInput(client, String.format("%s %s %s %s", command, date1, date2, date3)));
        assertFalse("Test for invalid start time", module.parseInput(client, String.format("%s %s %s", command, date3, date2)));
        assertFalse("Test for invalid end time", module.parseInput(client, String.format("%s %s %s", command, date1, date4)));
        assertFalse("Test if there are no arguments", module.parseInput(client, "rank-tower             "));
        assertFalse("Test for reversed time stamps", module.parseInput(client, String.format("rank-tower %s %s", date2, date1)));
    }

    /**
   * Test the <code>parseStartTime</code> and <code>parseEndTime</code> methods.
   */
    @Test
    public void testTimeInput() {
        GregorianCalendar today = new GregorianCalendar();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        assertNull("Test if it checks the time stamp length", module.parseStartTime("2011-11"));
        assertNull("Checks the time stamp format", module.parseStartTime("2011-11-AA"));
        assertNull("Test for invalid dates", module.parseStartTime("2011-99-99"));
        assertNull("Test if it checks the time stamp length", module.parseEndTime("2011-11"));
        assertNull("Checks the time stamp format", module.parseEndTime("2011-11-AA"));
        assertNull("Test for invalid dates", module.parseEndTime("2011-99-99"));
        assertNotNull("Test if it returns a time stamp for today", module.parseEndTime(format.format(today.getTime())));
        today.add(Calendar.DATE, -2);
        assertNotNull("Check if it tests for different times", module.parseEndTime(format.format(today.getTime())));
    }

    /**
   * Test the <code>getTowerEnergy</code> method.
   * 
   * @throws DatatypeConfigurationException when an exception occurs
   */
    @Test
    public void testGetEnergy() throws DatatypeConfigurationException {
        GregorianCalendar calendar = new GregorianCalendar();
        XMLGregorianCalendar startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        XMLGregorianCalendar endTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        startTime = Tstamp.incrementDays(startTime, -2);
        assertNull("Test if it checks for bad timestamp input", module.getTowerEnergy(client, startTime, endTime));
        endTime = Tstamp.incrementDays(endTime, -1);
        assertNotNull("Test if it is able to execute correctly", module.getTowerEnergy(client, startTime, endTime));
    }

    /**
   * Test the <code>printTowerRanks</code> method.
   * 
   * @throws DatatypeConfigurationException when an exception occurs
   */
    @Test
    public void testPrintTowers() throws DatatypeConfigurationException {
        GregorianCalendar calendar = new GregorianCalendar();
        XMLGregorianCalendar startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        XMLGregorianCalendar endTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        startTime = Tstamp.incrementDays(startTime, -2);
        endTime = Tstamp.incrementDays(endTime, -1);
        Map<Long, String> map = module.getTowerEnergy(client, startTime, endTime);
        assertNotNull("Test if it is able to execute correctly", module.printTowerRanks(map, startTime, endTime));
    }
}

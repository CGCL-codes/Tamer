package net.sourceforge.cruisecontrol.sourcecontrols;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import junit.framework.TestCase;
import net.sourceforge.cruisecontrol.CruiseControlException;
import net.sourceforge.cruisecontrol.Modification;
import net.sourceforge.cruisecontrol.util.ManagedCommandline;

/**
 * The unit test for an AlienBrain source control interface for
 * CruiseControl
 *
 * @author <a href="mailto:scottj+cc@escherichia.net">Scott Jacobs</a>
 */
public class AlienBrainTest extends TestCase {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("M/d/yyyy z");

    private static final Date NT_TIME_ZERO;

    private static final Date JAVA_TIME_ZERO;

    static {
        try {
            NT_TIME_ZERO = DATE_FORMAT.parse("1/1/1601 UTC");
            JAVA_TIME_ZERO = DATE_FORMAT.parse("1/1/1970 UTC");
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Just want to see if the AlienBrain class can even be found.
     */
    public void testConstruction() {
        new AlienBrain();
    }

    public void testProperty() {
        AlienBrain ab = new AlienBrain() {

            protected List getModificationsFromAlienBrain(Date lastBuild, Date now) throws IOException, CruiseControlException {
                List mods = new ArrayList();
                mods.add("modification");
                return mods;
            }
        };
        ab.setPath("path");
        ab.getModifications(new Date(), new Date());
        assertEquals(0, ab.getProperties().size());
        ab.setProperty("property");
        ab.getModifications(new Date(), new Date());
        Map properties = ab.getProperties();
        assertEquals(1, properties.size());
        assertTrue(properties.containsKey("property"));
    }

    /**
     */
    public void testValidate() {
        AlienBrain ab = new AlienBrain();
        try {
            ab.validate();
            fail("AlienBrain should throw exceptions when required " + "attributes are not set.");
        } catch (CruiseControlException expected) {
        }
        ab.setPath("Module1");
        try {
            ab.validate();
        } catch (CruiseControlException expected) {
            fail("AlienBrain should not throw exceptions when required " + "attributes are set.\n" + expected);
        }
    }

    public void testDateToFiletime() throws ParseException {
        assertEquals(0L, AlienBrain.dateToFiletime(NT_TIME_ZERO));
        assertEquals(116444736000000000L, AlienBrain.dateToFiletime(JAVA_TIME_ZERO));
        assertEquals(127610208000000000L, AlienBrain.dateToFiletime(DATE_FORMAT.parse("5/20/2005 UTC")));
    }

    public void testFiletimeToDate() throws ParseException {
        assertEquals(NT_TIME_ZERO, AlienBrain.filetimeToDate(0L));
        assertEquals(JAVA_TIME_ZERO, AlienBrain.filetimeToDate(116444736000000000L));
        assertEquals(DATE_FORMAT.parse("5/20/2005 UTC"), AlienBrain.filetimeToDate(127610208000000000L));
        Date now = new Date();
        assertEquals(now, AlienBrain.filetimeToDate(AlienBrain.dateToFiletime(now)));
    }

    public void testBuildGetModificationsCommand() throws ParseException {
        AlienBrain ab = new AlienBrain();
        ab.setUser("FooUser");
        ab.setPath("FooProject");
        Date date = DATE_FORMAT.parse("5/20/2005 -0400");
        ManagedCommandline cmdLine = ab.buildGetModificationsCommand(date, date);
        assertEquals("ab -u FooUser find FooProject -regex \"SCIT > " + "127610352000000000\" " + "-format \"#SCIT#|#DbPath#|#Changed By#|#CheckInComment#\"", cmdLine.toString());
    }

    public void testParseModificationDescription() throws ParseException {
        Modification m = AlienBrain.parseModificationDescription("127610352000000000|/a/path/to/a/file.cpp|sjacobs|" + "A change that probably breaks everything.");
        assertEquals(DATE_FORMAT.parse("5/20/2005 -0400"), m.modifiedTime);
        assertEquals("sjacobs", m.userName);
        assertEquals("A change that probably breaks everything.", m.comment);
        assertEquals(1, m.files.size());
        assertEquals("/a/path/to/a/file.cpp", ((Modification.ModifiedFile) (m.files.get(0))).fileName);
    }

    /**
     * Returns a file as a List of Strings, one String per line.
     */
    private List loadTestLog(String name) throws IOException {
        InputStream testStream = getClass().getResourceAsStream(name);
        assertNotNull("failed to load resource " + name + " in class " + getClass().getName(), testStream);
        List lines = new ArrayList();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(testStream));
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    public void testParseModifications() throws IOException, ParseException {
        List results = loadTestLog("alienbrain_modifications.txt");
        AlienBrain ab = new AlienBrain();
        List modifications = ab.parseModifications(results);
        assertEquals("Returned wrong number of modifications.", 7, modifications.size());
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy HH:mm:ss z");
        assertEquals("Wrong modification time", dateFormat.parse("4/19/2005 16:51:55 -0400"), ((Modification) modifications.get(0)).modifiedTime);
        assertEquals("Wrong path", "/FooProject/Code/Vehicles/Src/Position.cpp", ((Modification.ModifiedFile) (((Modification) modifications.get(0)).files.get(0))).fileName);
        assertEquals("Wrong user", "User 1", ((Modification) modifications.get(0)).userName);
        assertEquals("Wrong comment", "Passenger Animatoin", ((Modification) modifications.get(0)).comment);
        assertEquals("Wrong modification time", dateFormat.parse("5/7/2005 7:44:45 -0400"), ((Modification) modifications.get(6)).modifiedTime);
        assertEquals("Wrong path", "/FooProject/Code/Vehicles/Src/Materialnfo.cpp", ((Modification.ModifiedFile) (((Modification) modifications.get(6)).files.get(0))).fileName);
        assertEquals("Wrong user", "User 1", ((Modification) modifications.get(6)).userName);
        assertEquals("Wrong comment", "Import from 2004", ((Modification) modifications.get(6)).comment);
    }

    /**
     */
    public void testParseNoModifications() throws IOException {
        List results = loadTestLog("alienbrain_nomodifications.txt");
        AlienBrain ab = new AlienBrain();
        List modifications = ab.parseModifications(results);
        assertEquals(0, modifications.size());
    }
}

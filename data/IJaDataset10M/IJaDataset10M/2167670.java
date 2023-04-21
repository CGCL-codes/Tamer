package spumoni.test;

import java.util.Vector;
import junit.framework.TestCase;
import spumoni.StatsProgram;
import spumoni.StatsProgramList;
import spumoni.StatsValue;
import spumoni.StatsValueList;

/**
 * <P>JUnit test case for the StatsProgramList class.</P>
 *
 * @author <a href="mailto:smccrory@users.sourceforge.net">Scott McCrory</a>.
 * @version CVS $Id: StatsProgramListTest.java,v 1.3 2002/09/15 01:07:19 smccrory Exp $
 */
public class StatsProgramListTest extends TestCase {

    /** A class-wide storage container for the command-line arguments **/
    private static String[] myargs;

    /** The StatsValue object we'll pre-initialize **/
    private StatsValue sv = new StatsValue();

    /** The StatsValueList object we'll pre-initialize **/
    private StatsValueList svl = new StatsValueList();

    /** The StatsProgram object we'll pre-initialize **/
    private StatsProgram sProg = new StatsProgram();

    /** The Vector object we'll pre-initialize **/
    private Vector reqModules = new Vector();

    /** The StatsProgramList object we'll pre-initialize **/
    private StatsProgramList spl = new StatsProgramList();

    /**
     * StatsProgramListTest constructor
     * @param name java.lang.String
     */
    public StatsProgramListTest(String name) {
        super(name);
        try {
            sv = new StatsValue("pingMin", "1.2.3.1", 50, 100);
            sv.setValue("1567");
            svl = new StatsValueList(sv);
            sv = new StatsValue("pingMin", "1.2.3.4", 50, 100);
            sv.setValue("1568");
            svl.add(sv);
            sv = new StatsValue("pingMin", "1.2.3.4", 50, 100);
            sv.setValue("1569");
            svl.add(sv);
            reqModules.add("nt");
            sProg = new StatsProgram("nt_ping.xml", "dir", "(\\d+)", reqModules, 30, svl);
            spl = new StatsProgramList(sProg);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should not encounter " + e.getMessage());
        }
    }

    /**
     * Runs the JUnit test from the command line.
     * @param args java.lang.String[]
     */
    public static void main(String[] args) {
        myargs = args;
        junit.textui.TestRunner.run(StatsProgramListTest.class);
    }

    /**
     * Tests the StatsProgramList getOidConflicts method
     */
    public void testOidConflicts() {
        try {
            assertNotNull(spl.getOidConflicts());
            assertEquals((spl.getOidConflicts()).size(), 2);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should not encounter " + e.getMessage());
        }
    }

    /**
     * Tests some StatsProgramList operations.
     */
    public void testStatsProgramList() {
        try {
            assertNotNull(spl);
            assertEquals(spl.size(), 1);
            assertEquals(spl.getValueByOid("1.2.3.1"), "1567");
            assertEquals(spl.getValueByOid("1.2.3.4"), "1568");
            Vector oids = spl.getOids();
            assertEquals(oids.size(), 3);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should not encounter " + e.getMessage());
        }
    }
}

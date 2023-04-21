package test.org.mitre.mrald.query;

import java.util.ArrayList;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.parser.MraldParser;
import org.mitre.mrald.query.TimeElement;
import org.mitre.mrald.util.FormTags;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    December 25, 2004
 */
public class TimeElementTest extends TestCase {

    protected MsgObject msg;

    protected TimeElement timeElement;

    public void testPreProcess() {
        endDateTest();
        setUp();
        durationTest();
    }

    /**
     *  A unit test for JUnit
     */
    protected void durationTest() {
        String currentName = "Time1";
        assertEquals("Checking number of name/value pairs", 10, msg.getSize());
        timeElement.preProcess(msg, currentName);
        try {
            timeElement.process(msg.getValue(currentName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(msg.toString(), 1, msg.getSize());
        ArrayList<String> fromList = new ArrayList<String>();
        fromList = timeElement.buildFrom(fromList);
        assertEquals(timeElement.toString(), 1, fromList.size());
        String from = fromList.get(0);
        assertEquals(timeElement.toString(), "subject", from);
        ArrayList<String> whereList = new ArrayList<String>();
        try {
            whereList = timeElement.buildWhere(whereList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, whereList.size());
        String where = whereList.get(0);
        assertEquals("  ( subject.initial_exam >= to_date('1997-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') AND subject.initial_exam <= to_date('2001-01-01 23:59:00', 'YYYY-MM-DD HH24:MI:SS') ) ", where);
    }

    /**
     *  A unit test for JUnit
     */
    protected void endDateTest() {
        msg.setValue(FormTags.ENABLETIME, FormTags.ENDTIME);
        String currentName = "Time1";
        assertEquals("Checking number of name/value pairs", 11, msg.getSize());
        timeElement.preProcess(msg, currentName);
        try {
            timeElement.process(msg.getValue(currentName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(msg.toString(), 2, msg.getSize());
        ArrayList<String> fromList = new ArrayList<String>();
        fromList = timeElement.buildFrom(fromList);
        assertEquals(timeElement.toString(), 1, fromList.size());
        String from = fromList.get(0);
        assertEquals(timeElement.toString(), "subject", from);
        ArrayList<String> whereList = new ArrayList<String>();
        try {
            whereList = timeElement.buildWhere(whereList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, whereList.size());
        String where = whereList.get(0);
        assertEquals("  ( subject.initial_exam >= to_date('1997-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') AND subject.initial_exam <= to_date('2001-01-01 23:59:00', 'YYYY-MM-DD HH24:MI:SS') ) ", where);
    }

    /**
     *  The JUnit setup method
     */
    protected void setUp() {
        Properties parserTargets = new Properties();
        parserTargets.put("Time", "org.mitre.mrald.query.TimeElement");
        MraldParser.setBuildables(parserTargets);
        msg = new MsgObject();
        msg.setValue("Time1", "Table:subject~Field:initial_exam");
        msg.setValue("Time1~Day", "31");
        msg.setValue("Time1~EndDate", "1/1/2001");
        msg.setValue("Time1~EndTime", "23:59");
        msg.setValue("Time1~Hour", "23");
        msg.setValue("Time1~Minute", "59");
        msg.setValue("Time1~Month", "47");
        msg.setValue("Time1~Second", "1");
        msg.setValue("Time1~StartDate", "1/1/1997");
        msg.setValue("Time1~StartTime", "00:00");
        timeElement = new TimeElement();
    }

    /**
     *  The JUnit teardown method
     */
    protected void tearDown() {
    }

    /**
     *  The main program for the TimeElementTest class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     *  A unit test suite for JUnit
     *
     *@return    The test suite
     */
    public static Test suite() {
        return new TestSuite(TimeElementTest.class);
    }
}

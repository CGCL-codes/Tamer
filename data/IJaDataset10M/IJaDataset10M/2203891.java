package org.netbeans.server.uihandler;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.prefs.Preferences;
import javax.servlet.jsp.PageContext;
import org.netbeans.junit.Log;
import org.netbeans.junit.MockServices;
import org.netbeans.junit.NbTestCase;

/**
 *
 * @author Jaroslav Tulach
 */
public class StatisticsParsingTest extends NbTestCase {

    private LogsManager result;

    public StatisticsParsingTest(String testName) {
        super(testName);
    }

    @Override
    protected Level logLevel() {
        return Level.FINE;
    }

    private File logs() throws IOException {
        File f = new File(getWorkDir(), "logs");
        f.mkdirs();
        return f;
    }

    @Override
    protected void setUp() throws Exception {
        clearWorkDir();
        DatabaseTestCase.initPersitenceUtils(getWorkDir());
        MockServices.setServices(CntngStat.class);
        File log = LogsManagerTest.extractResourceAs(logs(), "1.log", "log444");
        result = LogsManagerTest.createManager(logs());
    }

    public void testHowManyLogsInALog() throws Exception {
        PageContext cnt = DatabaseTestCase.createPageContext();
        result.preparePageContext(cnt, "log444", null);
        assertEquals("once1", 1, CntngStat.finishSessionUpload.size());
        Object obj = cnt.getAttribute("globalCnts");
        assertNotNull("Data are there", obj);
        assertEquals(Integer.class, obj.getClass());
        assertEquals(18, obj);
        obj = cnt.getAttribute("lastCnts");
        assertNotNull("Last Data are there", obj);
        assertEquals(Integer.class, obj.getClass());
        assertEquals(18, obj);
        obj = cnt.getAttribute("userCnts");
        assertNotNull("User Data are there", obj);
        assertEquals(Integer.class, obj.getClass());
        assertEquals(18, obj);
        File newLog = LogsManagerTest.extractResourceAs(logs(), "1.log", "log444.1");
        result.addLog(newLog, "127.0.0.1");
        result.preparePageContext(cnt, "log444", null);
        assertEquals("Twice", 2, CntngStat.finishSessionUpload.size());
        obj = cnt.getAttribute("globalCnts");
        assertNotNull("Data are there", obj);
        assertEquals(Integer.class, obj.getClass());
        assertEquals(36, obj);
        obj = cnt.getAttribute("lastCnts");
        assertNotNull("Last Data are there", obj);
        assertEquals(Integer.class, obj.getClass());
        assertEquals(18, obj);
        obj = cnt.getAttribute("userCnts");
        assertNotNull("User Data are there", obj);
        assertEquals(Integer.class, obj.getClass());
        assertEquals(36, obj);
        File newUser = LogsManagerTest.extractResourceAs(logs(), "1.log", "log337");
        result.addLog(newUser, "127.0.0.1");
        result.preparePageContext(cnt, "log337", null);
        assertEquals("three times", 3, CntngStat.finishSessionUpload.size());
        Object global;
        global = obj = cnt.getAttribute("globalCnts");
        assertNotNull("Data are there", obj);
        assertEquals(Integer.class, obj.getClass());
        assertEquals(54, obj);
        Object last;
        last = obj = cnt.getAttribute("lastCnts");
        assertNotNull("Last Data are there", obj);
        assertEquals(Integer.class, obj.getClass());
        assertEquals(18, obj);
        Object user;
        user = obj = cnt.getAttribute("userCnts");
        assertNotNull("User Data are there", obj);
        assertEquals(Integer.class, obj.getClass());
        assertEquals(18, obj);
        int previous = CntngStat.operations;
        CntngStat.joins = 0;
        CharSequence log = Log.enable(LogsManager.class.getName(), Level.FINE);
        result = LogsManagerTest.createManager(logs());
        result.preparePageContext(cnt, "log337", null);
        assertEquals("no new finishSessionUpload", 3, CntngStat.finishSessionUpload.size());
        if (log.toString().indexOf("reading previously stored globals") == -1) {
            fail("Load globals: " + log);
        }
        assertEquals(-1, log.toString().indexOf("no stored globals"));
        assertEquals("Still the same globals", global, cnt.getAttribute("globalCnts"));
        assertEquals("Still the same globals", last, cnt.getAttribute("lastCnts"));
        assertEquals("Still the same globals", user, cnt.getAttribute("userCnts"));
    }

    public static final class CntngStat extends Statistics<Integer> {

        static List<Object[]> finishSessionUpload = new LinkedList<Object[]>();

        static int operations;

        static int joins;

        public CntngStat() {
            super("Cnts");
        }

        protected Integer newData() {
            operations++;
            return 0;
        }

        protected Integer process(LogRecord rec) {
            operations++;
            return 1;
        }

        protected Integer join(Integer one, Integer two) {
            operations++;
            joins++;
            return one + two;
        }

        protected Integer finishSessionUpload(String userId, int sessionNumber, boolean initialParse, Integer d) {
            Object[] rec = { userId, sessionNumber, initialParse };
            finishSessionUpload.add(rec);
            return d;
        }

        protected void write(Preferences pref, Integer d) {
            pref.putInt("value", d);
        }

        protected Integer read(Preferences pref) {
            return pref.getInt("value", 0);
        }
    }
}

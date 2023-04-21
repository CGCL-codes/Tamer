package com.entelience.test.test04probe;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import com.entelience.probe.ControlProblem;
import com.entelience.probe.DatabaseProbeContainer;
import com.entelience.probe.DatePatternGenerator;
import com.entelience.probe.ProbeFactory;
import com.entelience.probe.RunStatus;
import com.entelience.sql.DbHelper;
import com.entelience.util.DateHelper;

/** 
 * Import data from MS ISA db
 */
public class test22MsIsaDb extends com.entelience.test.DbProcessTestCase {

    /** JUnit 2.0 */
    public static Test suite() {
        return new TestSuite(test22MsIsaDb.class);
    }

    private static final org.apache.log4j.Logger _logger = com.entelience.util.Logs.getLogger();

    public void test01_1db() throws Exception, ControlProblem {
        DatabaseProbeContainer dpc = new DatabaseProbeContainer(false, "net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://10.10.0.125/ISALOG_20080106_WEB_000", "esis", "esis", null);
        dpc.setDb(statusDb);
        ProbeFactory pf = new ProbeFactory(statusDb, "com.entelience.probe.httplog.MsIsaDb");
        Map<String, String> params = new HashMap<String, String>();
        pf.setParams(params);
        dpc.addProbe(pf);
        RunStatus rs = dpc.run();
        assertTrue("did nothing", rs.getDidSomething());
        assertTrue("not successful, check logs for errors", rs.getSuccess());
    }

    public void test02_multidb() throws Exception, ControlProblem {
        DatabaseProbeContainer dpc = new DatabaseProbeContainer(false, "net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://10.10.0.125/ISALOG_[YYYY][MM][DD]_WEB_[NNN]", "esis", "esis", null, DatePatternGenerator.DAY, 5, false);
        dpc.setDb(statusDb);
        dpc.setForceDate(DateHelper.YYYYMMDD_dash("2008-01-09"));
        ProbeFactory pf = new ProbeFactory(statusDb, "com.entelience.probe.httplog.MsIsaDb");
        Map<String, String> params = new HashMap<String, String>();
        pf.setParams(params);
        dpc.addProbe(pf);
        RunStatus rs = dpc.run();
        assertTrue("did nothing", rs.getDidSomething());
        assertTrue("not successful, check logs for errors", rs.getSuccess());
    }
}

package com.entelience.test.test02import;

import org.junit.*;
import static org.junit.Assert.*;
import com.entelience.probe.ControlProblem;
import com.entelience.probe.FileProbeContainer;
import com.entelience.probe.ProbeFactory;
import com.entelience.probe.RunStatus;
import com.entelience.probe.UnitTestMirror;

public class test21ISSAlertCon extends com.entelience.test.DbProcessTestCase {

    @Test
    public void test01_import() throws Exception, ControlProblem {
        FileProbeContainer fpc = new FileProbeContainer(false, ".temp/probe/02/21issalertcon-01");
        fpc.setDb(statusDb);
        UnitTestMirror m = new UnitTestMirror(this);
        m.addFile("data/xforce-iss/index.html", null);
        fpc.addProbe(new ProbeFactory(statusDb, "com.entelience.probe.virus.ISSAlertCon"));
        fpc.addMirror(m);
        RunStatus rs = fpc.run();
        assertTrue("did nothing", rs.getDidSomething());
        assertTrue("not successful, check logs for errors", rs.isSuccess());
    }
}

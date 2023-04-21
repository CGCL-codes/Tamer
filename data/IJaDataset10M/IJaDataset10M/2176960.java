package com.entelience.test.test04probe.net;

import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;
import com.entelience.probe.ControlProblem;
import com.entelience.probe.FileProbeContainer;
import com.entelience.probe.FtpMirror;
import com.entelience.probe.ProbeFactory;
import com.entelience.probe.RunStatus;
import com.entelience.probe.GunzipExpand;

/** 
* 
*/
public class test04JuniperNetScreen extends com.entelience.test.DbProcessTestCase {

    @Test
    public void test00_clean_data() throws Exception {
        db.begin();
        db.executeSql("TRUNCATE e_probe_file_history;");
        db.executeSql("TRUNCATE e_remote_file_state;");
        db.executeSql("TRUNCATE e_local_file_state;");
        db.executeSql("DELETE FROM net.t_rule WHERE probe_name='com.entelience.probe.net.JuniperNetScreen';");
        db.executeSql("DELETE FROM net.t_custom_service WHERE probe_name='com.entelience.probe.net.JuniperNetScreen';");
        db.commit();
    }

    /** test the probe */
    @Test
    public void test01_juniper_netscreen() throws Exception, ControlProblem {
        ProbeFactory pf = new ProbeFactory(statusDb, "com.entelience.probe.net.JuniperNetScreen");
        Map<String, String> params = new HashMap<String, String>();
        params.put("sendIncidentForNewIPsWithoutLocations", "false");
        params.put("createAssetsForAll", "true");
        pf.setParams(params);
        FileProbeContainer fpc = new FileProbeContainer(false, ".temp/probe/04/42Juniper-01");
        fpc.setDb(statusDb);
        fpc.addMirror(new FtpMirror("/data/juniper", "127.0.0.1", 10021, "anonymous", "user@example.com", 0, true, true, true, null, null));
        fpc.addProbeSelect(Pattern.compile("^sample.netscreen.log.gz$"), pf);
        fpc.addExpand(Pattern.compile("\\.gz$"), new GunzipExpand(null));
        RunStatus rs = fpc.run();
        assertTrue("did nothing", rs.getDidSomething());
        assertTrue("not successful, check logs for errors", rs.isSuccess());
    }
}

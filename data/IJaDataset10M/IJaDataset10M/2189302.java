package com.entelience.test.test04probe.advisories;

import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import static org.junit.Assert.*;
import com.entelience.probe.ControlProblem;
import com.entelience.probe.CustomProbeContainer;
import com.entelience.probe.ProbeFactory;
import com.entelience.probe.RunStatus;

/** 
*    Online test for SecurityFocus data.
*/
public class test03SecurityFocus extends com.entelience.test.DbProcessTestCase {

    /** test the securityfocus probe */
    @Test
    public void test01_probe() throws Exception, ControlProblem {
        CustomProbeContainer cpc = new CustomProbeContainer(false);
        cpc.setDb(statusDb);
        ProbeFactory pf = new ProbeFactory(statusDb, "com.entelience.probe.patch.SecurityFocus");
        Map<String, String> params = new HashMap<String, String>();
        params.put("investigateRepairedWithExploit", "true");
        params.put("investigateAcceptRiskWithExploit", "true");
        params.put("investigateAcceptRiskWithUpdate", "false");
        params.put("renewIgnoredWithExploit", "true");
        params.put("renewAcceptRiskWithExploit", "false");
        params.put("renewIgnoredWithUpdate", "false");
        params.put("renewRepairedWithExploit", "false");
        params.put("renewAcceptRiskWithUpdate", "false");
        pf.setParams(params);
        cpc.addProbe(pf);
        com.entelience.probe.patch.VulnProbe p = ((com.entelience.probe.patch.SecurityFocus) pf.getConfiguredProbe(statusDb)).getConfig();
        assertTrue(p.investigateRepairedWithExploit());
        assertTrue(p.investigateAcceptRiskWithExploit());
        assertFalse(p.investigateAcceptRiskWithUpdate());
        assertTrue(p.renewIgnoredWithExploit());
        assertFalse(p.renewAcceptRiskWithExploit());
        assertFalse(p.renewIgnoredWithUpdate());
        assertFalse(p.renewRepairedWithExploit());
        assertFalse(p.renewAcceptRiskWithUpdate());
        RunStatus rs = cpc.run();
        assertTrue("did nothing", rs.getDidSomething());
        assertTrue("not successful, check logs for errors", rs.isSuccess());
    }
}

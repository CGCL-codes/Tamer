package org.apache.jetspeed.modules.actions.controls;

import org.apache.turbine.modules.Action;
import org.apache.turbine.util.RunData;
import org.apache.jetspeed.services.rundata.JetspeedRunData;

/**
 * Display information about the selected portlet
 * 
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @author <a href="mailto:mark_orciuch@ngsltd.com">Mark Orciuch</a>
 */
public class Info extends Action {

    public void doPerform(RunData rundata) throws Exception {
        String peid = rundata.getParameters().getString("js_peid");
        if (peid == null) {
            rundata.setScreenTemplate("Ecs");
            return;
        }
        JetspeedRunData jdata = (JetspeedRunData) rundata;
        jdata.setJs_peid(peid);
        rundata.setScreenTemplate("Info");
    }
}

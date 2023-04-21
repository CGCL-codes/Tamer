package de.iritgo.aktera.ui.tools;

import de.iritgo.aktera.configuration.SystemConfigManager;
import de.iritgo.aktera.model.ModelException;
import de.iritgo.aktera.model.ModelRequest;
import de.iritgo.aktera.model.ModelResponse;
import de.iritgo.aktera.model.Output;
import de.iritgo.aktera.model.StandardLogEnabledModel;

/**
 * @avalon.component
 * @avalon.service type="de.iritgo.aktera.model.Model"
 * @x-avalon.info name="aktera.ui.check-config-change"
 * @x-avalon.lifestyle type="singleton"
 * @model.model name="aktera.ui.check-config-change" id="aktera.ui.check-config-change" logger="pbx"
 *
 * @version $Id: CheckConfigChange.java,v 1.12 2006/10/13 14:24:01 haardt Exp $
 */
public class CheckConfigChange extends StandardLogEnabledModel {

    /**
	 * Execute the model.
	 *
	 * @param req The model request.
	 * @throws ModelException In case of a business failure.
	 */
    public ModelResponse execute(ModelRequest req) throws ModelException {
        ModelResponse res = req.createResponse();
        if (!UserTools.currentUserIsInGroup(req, "root")) {
            return res;
        }
        SystemConfigManager systemConfigManager = (SystemConfigManager) req.getSpringBean(SystemConfigManager.ID);
        String state = systemConfigManager.getConfigState();
        if (SystemConfigManager.CHANGE_REBOOT.equals(state)) {
            Output out = res.createOutput("warning", "configurationWasModifiedAndNeedsRebootHint");
            out.setAttribute("bundle", "IPtellBase");
            res.add(out);
        } else if (systemConfigManager.isRebootFlagSet()) {
            Output out = res.createOutput("warning", "rebootIsNeededHint");
            out.setAttribute("bundle", "IPtellBase");
            res.add(out);
        } else if (systemConfigManager.configNeedsRestart()) {
            Output out = res.createOutput("warning", "restartIsNeededHint");
            out.setAttribute("bundle", "IPtellBase");
            res.add(out);
        } else if (!SystemConfigManager.CHANGE_NO.equals(state)) {
            Output out = res.createOutput("warning", "configurationWasModifiedHint");
            out.setAttribute("bundle", "IPtellBase");
            res.add(out);
        }
        return res;
    }
}

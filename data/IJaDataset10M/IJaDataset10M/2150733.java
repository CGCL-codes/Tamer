package org.pustefixframework.editor.webui.handlers;

import org.pustefixframework.container.annotations.Inject;
import org.pustefixframework.editor.webui.resources.SessionResource;
import org.pustefixframework.editor.webui.wrappers.UserLoginSwitch;
import de.schlund.pfixcore.generator.IHandler;
import de.schlund.pfixcore.generator.IWrapper;
import de.schlund.pfixcore.workflow.Context;

/**
 * Handles enable/disable of user logins
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class UserLoginSwitchHandler implements IHandler {

    private SessionResource sessionResource;

    public void handleSubmittedData(Context context, IWrapper wrapper) throws Exception {
        UserLoginSwitch input = (UserLoginSwitch) wrapper;
        if (input.getAllow() != null) {
            sessionResource.setUserLoginsAllowed(input.getAllow().booleanValue());
        }
    }

    public void retrieveCurrentStatus(Context context, IWrapper wrapper) throws Exception {
    }

    public boolean prerequisitesMet(Context context) throws Exception {
        return true;
    }

    public boolean isActive(Context context) throws Exception {
        return true;
    }

    public boolean needsData(Context context) throws Exception {
        return false;
    }

    @Inject
    public void setSessionResource(SessionResource sessionResource) {
        this.sessionResource = sessionResource;
    }
}

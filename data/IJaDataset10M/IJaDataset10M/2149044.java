package edu.columbia.hypercontent.contentmanager.commands;

import edu.columbia.hypercontent.CMSException;
import edu.columbia.hypercontent.CMSPermissible;
import edu.columbia.hypercontent.contentmanager.CMSessionData;
import edu.columbia.hypercontent.contentmanager.ICommand;
import org.jasig.portal.channels.permissionsmanager.CPermissionsManagerServantFactory;
import org.jasig.portal.services.LogService;

/**
 * Part of the Columbia University Content Management Suite
 *
 * @author Alex Vigdor av317@columbia.edu
 * @version $Revision: 1.1.1.1 $
 */
public class AssignPermissions implements ICommand {

    public AssignPermissions() {
    }

    public void execute(CMSessionData session) throws Exception {
        String path = session.runtimeData.getParameter("path");
        if (session.global.hasPermission(GRANT_PERMISSION, path)) {
            LogService.instance().log(LogService.DEBUG, "Assigning Permissions on path " + path);
            CMSPermissible permissible = new CMSPermissible(session.global.getProject());
            LogService.instance().log(LogService.DEBUG, "Using " + String.valueOf(permissible.getActivityTokens().length) + " activities for " + permissible.getOwnerName());
            session.permissionsManager = CPermissionsManagerServantFactory.getPermissionsServant(permissible, session.staticData, null, permissible.getActivityTokens(), new String[] { path });
        } else {
            throw new CMSException(CMSException.NOT_PERMITTED);
        }
    }
}

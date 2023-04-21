package org.campware.cream.modules.screens;

import org.apache.turbine.util.RunData;
import org.apache.torque.util.Criteria;
import org.campware.cream.om.CreamUser;
import org.campware.cream.om.CreamUserPeer;
import org.apache.velocity.context.Context;

/**
 * Grab all the records in a table using a Peer, and
 * place the Vector of data objects in the context
 * where they can be displayed by a #foreach loop.
 *
 * @author <a href="mailto:jvanzyl@periapt.com">Jason van Zyl</a>
 */
public class Index extends SecureScreen {

    /**
     * Place all the data object in the context
     * for use in the template.
     */
    public void doBuildTemplate(RunData data, Context context) {
        try {
            Criteria crit = new Criteria();
            crit.add(CreamUserPeer.LOGIN_NAME, data.getUser().getName());
            CreamUser entry = (CreamUser) CreamUserPeer.doSelect(crit).get(0);
            context.put("preferences", entry);
        } catch (Exception e) {
        }
    }
}

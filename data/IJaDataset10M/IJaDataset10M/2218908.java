package com.liferay.portal.jaas.ext.pramati;

import java.security.Principal;
import com.liferay.portal.jaas.ext.BasicLoginModule;
import com.pramati.security.util.User;

/**
 * <a href="PortalLoginModule.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.5 $
 *
 */
public class PortalLoginModule extends BasicLoginModule {

    protected Principal getPortalPrincipal(String userId) {
        return new User(userId);
    }
}

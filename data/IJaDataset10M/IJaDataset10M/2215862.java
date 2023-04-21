package com.liferay.portal.jaas.ext.tomcat;

import com.liferay.portal.jaas.PortalRole;
import com.liferay.portal.jaas.ext.BasicLoginModule;

/**
 * <a href="PortalLoginModule.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.2 $
 *
 */
public class PortalLoginModule extends BasicLoginModule {

    public boolean commit() {
        boolean commitValue = super.commit();
        if (commitValue) {
            PortalRole role = new PortalRole("users");
            getSubject().getPrincipals().add(role);
        }
        return commitValue;
    }
}

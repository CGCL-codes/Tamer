package net.sf.doolin.gui.auth.support;

import java.security.Principal;
import net.sf.doolin.gui.auth.AuthorizableItem;
import net.sf.doolin.gui.core.View;

/**
 * Default implementation for an authorization manager. It allows everything.
 * 
 * @author Damien Coraboeuf
 * @version $Id: DefaultAuthManager.java,v 1.1 2007/08/17 15:06:55 guinnessman Exp $
 */
public class DefaultAuthManager extends AbstractAuthManager {

    /**
	 * Always returns <code>true</code>.
	 * 
	 * @see net.sf.doolin.gui.auth.support.AbstractAuthManager#isAuthorized(Principal,
	 *      View, AuthorizableItem)
	 */
    @Override
    protected boolean isAuthorized(Principal user, View view, AuthorizableItem item) {
        return true;
    }
}

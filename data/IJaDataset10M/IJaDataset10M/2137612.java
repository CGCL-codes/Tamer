package com.controltier.ctl.authorization;

import com.controltier.ctl.common.CmdHandler;
import com.controltier.ctl.common.IContext;
import com.controltier.ctl.common.ICommand;
import com.controltier.ctl.common.ICommand;

/**
 * Instances of classes that implement this interface  take context and command info
 * and lookup whether the user can execute the specified handler.
 * ControlTier Software Inc.
 * User: alexh
 * Date: Jul 12, 2005
 * Time: 9:29:07 AM
 */
public interface Authorization {

    /**
     * gets role memberships
     *
     * @return
     */
    String[] getMatchedRoles();

    /**
     * lists formatted list of matched roles
     *
     * @return
     */
    String listMatchedRoles();

    public boolean authorize(final String user, final IContext obj, final ICommand command) throws AuthorizationException;

    /**
     * Checks if user can execute handler for given context
     *
     * @param user
     * @param obj
     * @param hdler
     * @return
     * @throws AuthorizationException
     */
    boolean authorize(String user, IContext obj, CmdHandler hdler) throws AuthorizationException;

    /**
     * script authorization
     *
     * @param user user name
     * @param depot depot name
     * @param adhocScript script to execute
     *
     * @return
     *
     * @throws com.controltier.ctl.authorization.AuthorizationException
     */
    boolean authorizeScript(String user, String depot, String adhocScript) throws AuthorizationException;
}

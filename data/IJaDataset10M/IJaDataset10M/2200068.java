package de.fhg.igd.semoa.compat.jade;

import de.fhg.igd.util.AbstractPermission;
import java.security.*;
import java.util.*;

/**
 * Defines a permission class to control access to register an FIPA DF
 * (directory facilitator) and FIPA AMS (agent management system) at
 * the communication infrastructure. This is used for mapping between
 * well known agents names, e.g. <code><b>df</b>@platform</code>, and
 * the unique agent names. The registration is done at a
 * <code>MessageRouter</code>.
 *
 * The action list consist of a number of identifiers
 * separated by commas and/or spaces.
 *
 * The following actions are supported:
 * <dl>
 * <dt> <i>df</i>
 * <dd> This permission allows to register the unique name of the
 *   agent acting as FIPA DF at the MessageRouter.
 * <dt> <i>ams</i>
 * <dd> This permission allows to register the unique name of the
 *   agent acting as FIPA AMS at the MessageRouter.
 * </dl>
 *
 * @author Ulrich Pinsdorf
 * @version "$Id: RegistrationPermission.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public class RegistrationPermission extends AbstractPermission {

    /**
     * The list of actions supported by this class.
     */
    private static final String[] acronyms_ = { "df", "ams" };

    /**
     * Creates an instance with the given permission list.
     *
     * @param name the String holding the comma-separated actions.
     */
    public RegistrationPermission(String name, String actions) {
        super(name, actions, acronyms_);
    }

    /**
     * Returns the actions string.
     *
     * @return The actions string.
     */
    public String getActions() {
        return super.getActions(acronyms_);
    }

    /**
     * Returns a string representation of this instance.
     *
     * @return The string representation.
     */
    public String toString() {
        return super.toString(acronyms_);
    }

    /**
     * @return A new <code>PermissionCollection</code> for
     *   <code>AgentPermission</code> instances.
     */
    public PermissionCollection newPermissionCollection() {
        return super.newPermissionCollection();
    }
}

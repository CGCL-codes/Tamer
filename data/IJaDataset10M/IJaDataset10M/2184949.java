package org.osmius.service;

import org.osmius.model.OsmRole;
import java.util.List;

/**
 * This and others classes of the web console are based on <a href="http://www.appfuse.org">Appfuse</a> (v1.9.4),
 * a tool from <a href="http://raibledesigns.com">Matt Raible</a> to facilitate the web application development.
 * Thanks for his great effort.
 * <p/>
 * Business Service Interface to handle communication between web and persistence layer.
 * Exposes the neccessary methods to handle a role
 */
public interface OsmRoleManager extends Manager {

    /**
    * Gets roles
    * @param osmRole A parametrized role
    * @return A list of roles
    */
    public List getRoles(OsmRole osmRole);

    /**
    * Get a role by its rolename
    * @param rolename The role name
    * @return The role
    */
    public OsmRole getRole(String rolename);

    /**
    * Saves a role
    * @param osmRole The role to be saved
    */
    public void saveRole(OsmRole osmRole);

    /**
    * Deletes a role
    * @param rolename The role to be deleted
    */
    public void removeRole(String rolename);

    /**
    * Gets the roles by user
    * @param idnUser The user
    * @return The list of roles (actually only one role)
    */
    public List getRolesByUser(String idnUser);
}

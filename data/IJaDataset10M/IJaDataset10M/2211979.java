package org.hip.vif.authorization;

import java.util.Collection;

/**
 * Interface for Authorization objects.
 * 
 * @author Benno Luthiger
 */
public interface IAuthorization {

    /**
	 * Does this authorized person have the specified permission?
	 * 
	 * @param inPermissionLabel java.lang.String
	 * @return boolean
	 */
    boolean hasPermission(String inPermissionLabel);

    /**
	 * Returns the separated list of permissions.
	 * 
	 * @return Collection<String> of permissions.
	 */
    public Collection<String> getPermissions();

    /**
	 * Returns the Authorization as XML serialized.
	 * 
	 * @return java.lang.String
	 */
    String toXML();
}

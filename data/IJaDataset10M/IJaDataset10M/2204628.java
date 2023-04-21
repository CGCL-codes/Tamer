package de.iritgo.aktera.persist.defaultpersist;

import de.iritgo.aktera.authorization.InvokationSecurable;
import java.util.Map;

/**
 * Simple extension to DefaultPersistent to allow for row-level security
 * via the InvokationSecurable
 */
public class RowSecurablePersistent extends DefaultPersistent implements InvokationSecurable {

    /**
	 * Return all of the fields in this persistent object as the properties
	 * used to determine authorization for this persistent. This allows
	 * any field (or fields) to be used to determine if the user is or is not
	 * allowed to access the record.
	 *
	 * @see de.iritgo.aktera.authorization.InvokationSecurable#getAuthorizationProperties()
	 */
    public Map getAuthorizationProperties() {
        return fieldData;
    }
}

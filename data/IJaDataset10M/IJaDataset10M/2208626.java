package org.bluesock.bluemud.lib;

import org.bluesock.bluemud.driver.Driver;
import org.bluesock.bluemud.driver.OID;

/**
 * Represents an in-game object in the MUD. All objects
 * that exist within the game world must ultimately
 * extend from this class.
 */
public class MudObject {

    private final OID objectID;

    public MudObject() {
        objectID = Driver.nextObjectID();
    }

    /**
   * Retrieve the unique ID of this MUD object.
   *
   * @return the object ID.
   */
    public final OID getObjectID() {
        return objectID;
    }
}

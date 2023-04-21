package com.cube42.echoverse.identity;

import java.rmi.RemoteException;
import com.cube42.echoverse.model.EntityID;
import com.cube42.util.gui.SubSystemControlPanel;
import com.cube42.util.logging.Logger;
import com.cube42.util.system.SubSystemCoreFoundation;
import com.cube42.util.system.SubSystemID;
import com.cube42.util.system.SubSystemStatus;

/**
 * Implementation of the IdentityManager
 *
 * @author  Matt Paulin
 * @version $Id: IdentityManagerImpl.java,v 1.2 2003/03/12 01:48:40 zer0wing Exp $
 */
public class IdentityManagerImpl extends SubSystemCoreFoundation implements IdentityManager {

    /**
     * The number of entities that have been created
     */
    private long numEntities;

    /**
     * Initializes the subsystem core
     */
    public void initSubSystemCore() {
        this.numEntities = 0;
    }

    /**
     * Returns the next available entityID
     *
     * @throws  RemoteException if a network error occurs
     */
    public EntityID getEntityID() throws RemoteException {
        if (this.numEntities == Long.MAX_VALUE) {
            Logger.fatal(IdentityManagerSystemCodes.OUT_OF_ENTITY_IDS);
        }
        return new EntityID(this.numEntities++);
    }

    /**
     * Returns the SubSystemID
     *
     * @return  The SubSystemID of the subsystem.
     */
    public SubSystemID getSubSystemID() {
        return IdentityManager.SUBSYSTEM_ID;
    }

    /**
     * Tells the subsystem to shutdown
     *
     * @throws  A remote exception if a network error occurs
     */
    public void shutdownSubSystemCore() {
    }

    /**
     * Returns the SubSystemControlPanel used to control the IdentityManager
     *
     * @return  The SubSystemControlPanel used to control the IdentityManager
     */
    public SubSystemControlPanel getSubSystemControlPanel() {
        return null;
    }

    /**
     * Returns the status of the remote subsystem
     *
     * @return  The status of the RemoteSubSystem
     */
    public SubSystemStatus getStatus() {
        SubSystemStatus status = new SubSystemStatus(this.getSubSystemID());
        return status;
    }
}

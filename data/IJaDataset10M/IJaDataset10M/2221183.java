package com.cube42.echoverse.observation;

import java.rmi.RemoteException;
import com.cube42.echoverse.entity.ActionType;
import com.cube42.echoverse.model.EntityID;
import com.cube42.echoverse.model.EntityIDCollection;
import com.cube42.util.system.RemoteSubSystem;
import com.cube42.util.system.SubSystemID;

/**
 * SubSystem responsible for managing what entities are observing what.
 *
 * @author  Matt Paulin
 * @version $Id: ObserverManager.java,v 1.2 2003/03/12 01:48:39 zer0wing Exp $
 */
public interface ObserverManager extends RemoteSubSystem {

    /**
     * The SubSystemID for the ObserverManager.
     */
    public static final SubSystemID SUBSYSTEM_ID = new SubSystemID("ObserverManager");

    /**
     * Returns all the entities observing the specified action on
     * the specified entity
     *
     * @param   entityID    The entity in question
     * @param   actionType  The type of action in question
     * @return  A collecion of all the entityIDs of all the observers
     *          for the entity with the specified action, null if there
     *          are none
     * @throws  RemoteException if a network error occurs
     */
    public EntityIDCollection getObservers(EntityID entityID, ActionType actionType) throws RemoteException;

    /**
     * Removes the specified entity from the ObserverManager, this is used
     * when the entity is completly removed from the system
     *
     * @param   entityID    The entity to remove
     * @throws  RemoteException if a network error occurs
     */
    public void removeEntity(EntityID entityID) throws RemoteException;
}

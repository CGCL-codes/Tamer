package com.cube42.echoverse.entity;

import java.rmi.RemoteException;
import com.cube42.echoverse.model.ControlledEntityListener;
import com.cube42.echoverse.model.EntityID;
import com.cube42.echoverse.model.EntityIDCollection;
import com.cube42.echoverse.model.WorldSeed;
import com.cube42.util.exception.Cube42Exception;
import com.cube42.util.instruction.Instruction;
import com.cube42.util.instruction.InstructionDefCollection;
import com.cube42.util.system.RemoteSubSystem;
import com.cube42.util.system.SubSystemID;

/**
 * Template to use when creating a SubSystemCore
 *
 * @author  Matt Paulin
 * @version $Id: EntityManager.java,v 1.2 2003/03/12 01:48:28 zer0wing Exp $
 */
public interface EntityManager extends RemoteSubSystem {

    /**
     * The SubSystemID for the EntityManager.
     */
    public static final SubSystemID SUBSYSTEM_ID = new SubSystemID("EntityManager");

    /**
     * Returns a collection of EntityIDs of all the controlled entities
     * in this manager
     *
     * @return  EntityIDCollection representing all the entities contained
     *          in this entity manager
     * @throws  RemoteException if an error occurs
     */
    public EntityIDCollection getControlledEntities() throws RemoteException;

    /**
     * Adds a world seed to the system so that it can initialize a world
     *
     * @param   worldSeed   The world seed to add
     * @throws  RemoteException if a network error occurs
     * @throws  Cube42Exception if this is not possible
     */
    public void plantWorldSeed(WorldSeed worldSeed) throws RemoteException, Cube42Exception;

    /**
     * Returns a collection of all the instructions the controlled entity
     * will respond to
     *
     * @param   entityID    The EntityID of the controlled entity that is
     *                      involved
     * @return  A InstructionDefCollection of all the instructions involved with
     *          this controlled entity
     * @throws  Cube42Exception if this is not possible
     * @throws  RemoteException if a network error occurs
     */
    public InstructionDefCollection getControlledEntityInstructions(EntityID entityID) throws Cube42Exception, RemoteException;

    /**
     * Sends the instruction to the specified Entity
     *
     * @param   enactor     The entity that sent this instruction
     * @param   reactor     The entityID of the entity that is to receive the
     *                      instruction
     * @param   instruction The instruction the entity is to receive
     * @throws  RemoteException if a network error occurs
     */
    public void processInstruction(EntityID enactor, EntityID reactor, Instruction instruction) throws RemoteException;

    /**
     * Registers an ControlledEntityListener to listen for EntityInfo changes
     * on the specified entity
     *
     * @param   focusEntityID   The entityID of the entity that the listener
     *                          wants information about.  Info about this entity
     *                          and the entities near it will be sent to the
     *                          listener
     * @param   listener        The ControlledEntityListener that will be notified
     *                          of the changes
     * @param   listenerName    The name to call the listener
     * @throws  RemoteException if a network error occurs
     * @throws  Cube42Exception if this cannot be done
     */
    public void registerEntityInfoChangeListener(EntityID focusEntityID, ControlledEntityListener listener, String listenerName) throws RemoteException, Cube42Exception;
}

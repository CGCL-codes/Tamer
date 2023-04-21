package com.cube42.echoverse.model;

import java.io.Serializable;
import com.cube42.util.exception.Cube42Exception;
import com.cube42.util.instruction.Instruction;
import com.cube42.util.math.Position;

/**
 * Represents a single construct in the Echoverse.  The Entity reacts to
 * actions preformed on it.
 *
 * @author  Matt Paulin
 * @version $Id: Entity.java,v 1.2 2003/03/12 01:48:33 zer0wing Exp $
 */
public abstract class Entity implements Serializable {

    /**
     * The EntityInfo for this Entity
     */
    private EntityInfo entityInfo;

    /**
     * Flag used to tell if the ID has been attempted to be reset
     */
    private boolean entityIDSetFlag;

    /**
     * Collection used to store all the information about this entity
     */
    private InformationStore info;

    /**
     * Constructs the Entity
     */
    public Entity() {
        super();
        this.entityIDSetFlag = false;
        this.info = new InformationStore();
        this.entityInfo = new EntityInfo(this.getClass().getName(), this.info);
    }

    /**
     * Sets the EntityID
     * <p>
     * NOTE: This can be set only once or an exception will be thrown
     *
     * @param   id  The ID the entity is to be set with
     * @throws  Cube42Exception if the id is attempted to be set more than
     *          once
     */
    public void setEntityID(EntityID id) throws Cube42Exception {
        if (this.entityIDSetFlag) {
            throw new Cube42Exception(ModelSystemCodes.ENTITY_ID_RESET_ATTEMPT, new Object[] { this.entityInfo.getEntityID(), id });
        }
        this.entityInfo.setEntityID(id);
        this.entityIDSetFlag = true;
    }

    /**
     * Returns the unique identifier for this entity.
     *
     * @return the not-repepated 'name' of this entity.
     */
    public EntityID getEntityID() {
        return this.entityInfo.getEntityID();
    }

    /**
     * Returns the position of the entity.
     */
    public Position getPosition() {
        return this.entityInfo.getPosition();
    }

    /**
     * Sets the position of the entity.
     */
    public void setPosition(Position position) {
        this.entityInfo.setPosition(position);
    }

    /**
     * Returns the current information store
     *
     * @return  The information store this entity uses to store information
     */
    public InformationStore getInformationStore() {
        return this.info;
    }

    /**
     * Returns the EntityInfo used to view this entity
     *
     * @return  The EntityInfo used to view this entity
     */
    public EntityInfo getEntityInfo() {
        return this.entityInfo;
    }

    /**
     * Source entity will request to absorb a resource from a target resource
     *
     * @param   reactor     The entity that will be requested to have its
     *                      resource absorbed
     * @param   resource    The type of resource to radiate
     * @param   amount      The amount (in egram) of the resouce to radiate
     */
    public final void absorbEnaction(EntityID reactor, WorldResource resource, Egram amount) {
        ActionImplementation.getActionImplementation().absorb(this.getEntityID(), this.getEntityID(), reactor, resource, amount);
    }

    /**
     * Source entity will request an absorb from the target entity
     *
     * @param   enactor     The entity will appear to be wanting to absorb
     *                      the resource
     * @param   reactor     The entity that will be requested to have its
     *                      resource absorbed
     * @param   resource    The type of resource requested
     * @param   amount      The amount (in egram) of the resouce being absorbed
     */
    public final void absorbDiaction(EntityID enactor, EntityID reactor, WorldResource resource, Egram amount) {
        ActionImplementation.getActionImplementation().absorb(this.getEntityID(), enactor, reactor, resource, amount);
    }

    /**
     * Registers this entity as an Interceptor for the absorb action
     *
     * @param   entityID    The entity that this entity wants to intercept
     *                      absorb actions from
     * @param   priority    The priority of the interception
     */
    public final void registerAbsorbInterceptor(EntityID entityID, InterceptorPriority priority) {
        InterceptorImplementation.getInterceptorImplementation().registerAbsorbInterceptor(this.getEntityID(), entityID, priority);
    }

    /**
     * Unregisters this entity from all Absorb Interceptions
     */
    public final void unregisterAllAbsorbInterceptors() {
        InterceptorImplementation.getInterceptorImplementation().unregisterAllAbsorbInterceptors(this.getEntityID());
    }

    /**
     * Unregisters this entity as an Interceptor for the absorb action
     *
     * @param   entityID    The entity that this entity does not want to
     *                      intercept absorb actions from anymore
     */
    public final void unregisterAbsorbInterceptor(EntityID entityID) {
        InterceptorImplementation.getInterceptorImplementation().unregisterAbsorbInterceptor(this.getEntityID(), entityID);
    }

    /**
     * Registers this entity as an Observer for the absorb action
     *
     * @param   entityID    The entity that this entity want to observe for
     *                      absorb actions
     */
    public final void registerAbsorbObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().registerAbsorbObserver(this.getEntityID(), entityID);
    }

    /**
     * Unregisters this entity from all absorb Observations
     */
    public final void unregisterAllAbsorbObservers() {
        ObserverImplementation.getObserverImplementation().unregisterAllAbsorbObservers(this.getEntityID());
    }

    /**
     * Unregisters this entity from Observing absorb actions
     *
     * @param   entityID    The entity to unregister for the observer action
     */
    public final void unregisterAbsorbObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().unregisterAbsorbObserver(this.getEntityID(), entityID);
    }

    /**
     * Sends information to all entities within range of this entity
     * <p>
     * NOTE: there is no corrisponding announce reaction class.  Announcements
     * are just like informing all the entities near the enactor entity
     *
     * @param   key         The key describing what this information pertains
     *                      to
     * @param   info        The information to send.
     */
    public final void announceEnaction(InformationKey key, Information info) {
        ActionImplementation.getActionImplementation().announce(this.getEntityID(), this.getEntityID(), key, info);
    }

    /**
     * Sends information to all entities within range of this entity
     * <p>
     * NOTE: there is no corrisponding announce reaction class.  Announcements
     * are just like informing all the entities near the enactor entity
     *
     * @param   enactor     The entity that will have appeared to have made
     *                      this announcement
     * @param   key         The key describing what this information pertains
     *                      to
     * @param   info        The information to send.
     */
    public final void announceDiaction(EntityID enactor, InformationKey key, Information info) {
        ActionImplementation.getActionImplementation().announce(this.getEntityID(), enactor, key, info);
    }

    /**
     * Registers this entity as an Observer for the collide action
     *
     * @param   entityID    The entity that this entity want to observe for
     *                      collide actions
     */
    public final void registerCollideObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().registerCollideObserver(this.getEntityID(), entityID);
    }

    /**
     * Unregisters this entity from all collide Observations
     */
    public final void unregisterAllCollideObservers() {
        ObserverImplementation.getObserverImplementation().unregisterAllCollideObservers(this.getEntityID());
    }

    /**
     * Unregisters this entity from Observing collide actions
     *
     * @param   entityID    The entity to unregister for the observer action
     */
    public final void unregisterCollideObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().unregisterCollideObserver(this.getEntityID(), entityID);
    }

    /**
     * Causes a entity of the specified type to be created
     * <p>
     * NOTE: The class name provided will be resolved into an entity.  If it
     * does not resolve then an error will be logged.
     *
     * @param   classname   The java classname of the entity to create
     * @param   pos         The entities starting position
     */
    public final void createEnaction(String classname, Position pos) {
        ActionImplementation.getActionImplementation().create(this.getEntityID(), this.getEntityID(), classname, pos);
    }

    /**
     * Causes a entity of the specified type to be created.  It will apear
     * as if the enactor provided created the entity
     * <p>
     * NOTE: The class name provided will be resolved into an entity.  If it
     * does not resolve then an error will be logged.
     *
     * @param   enactor     The entity that that will apear to be creating the
     *                      new entity
     * @param   classname   The java classname of the entity to create
     * @param   pos         The entities starting position
     */
    public final void createDiaction(EntityID enactor, String classname, Position pos) {
        ActionImplementation.getActionImplementation().create(this.getEntityID(), enactor, classname, pos);
    }

    /**
     * Registers this entity as an Interceptor for the create action
     *
     * @param   entityID    The entity that this entity wants to intercept
     *                      create actions from
     * @param   priority    The priority of the interception
     */
    public final void registerCreateInterceptor(EntityID entityID, InterceptorPriority priority) {
        InterceptorImplementation.getInterceptorImplementation().registerCreateInterceptor(this.getEntityID(), entityID, priority);
    }

    /**
     * Unregisters this entity from all Create Interceptions
     */
    public final void unregisterAllCreateInterceptors() {
        InterceptorImplementation.getInterceptorImplementation().unregisterAllCreateInterceptors(this.getEntityID());
    }

    /**
     * Unregisters this entity as an Interceptor for the create action
     *
     * @param   entityID    The entity that this entity does not want to
     *                      intercept create actions from anymore
     */
    public final void unregisterCreateInterceptor(EntityID entityID) {
        InterceptorImplementation.getInterceptorImplementation().unregisterCreateInterceptor(this.getEntityID(), entityID);
    }

    /**
     * Registers this entity as an Observer for the create action
     *
     * @param   entityID    The entity that this entity want to observe for
     *                      create actions
     */
    public final void registerCreateObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().registerCreateObserver(this.getEntityID(), entityID);
    }

    /**
     * Unregisters this entity from all create Observations
     */
    public final void unregisterAllCreateObservers() {
        ObserverImplementation.getObserverImplementation().unregisterAllCreateObservers(this.getEntityID());
    }

    /**
     * Unregisters this entity from Observing create actions
     *
     * @param   entityID    The entity to unregister for the observer action
     */
    public final void unregisterCreateObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().unregisterCreateObserver(this.getEntityID(), entityID);
    }

    /**
     * Request the destruction of an entity with the specified entityID
     *
     * @param   reactor     The entity to destroy
     */
    public final void destroyEnaction(EntityID reactor) {
        ActionImplementation.getActionImplementation().destroy(this.getEntityID(), this.getEntityID(), reactor);
    }

    /**
     * Request the destruction of an entity with the specified entityID,
     * on behalf of the specified enactor entity.
     *
     * @param   enactor     The entity that will appear to be destroying the
     *                      reactor entity
     * @param   reactor     The entity to destroy
     */
    public final void destroyDiaction(EntityID enactor, EntityID reactor) {
        ActionImplementation.getActionImplementation().destroy(this.getEntityID(), enactor, reactor);
    }

    /**
     * Registers this entity as an Interceptor for the destroy action
     *
     * @param   entityID    The entity that this entity wants to intercept
     *                      destroy actions from
     * @param   priority    The priority of the interception
     */
    public final void registerDestroyInterceptor(EntityID entityID, InterceptorPriority priority) {
        InterceptorImplementation.getInterceptorImplementation().registerDestroyInterceptor(this.getEntityID(), entityID, priority);
    }

    /**
     * Unregisters this entity from all Destroy Interceptions
     */
    public final void unregisterAllDestroyInterceptors() {
        InterceptorImplementation.getInterceptorImplementation().unregisterAllDestroyInterceptors(this.getEntityID());
    }

    /**
     * Unregisters this entity as an Interceptor for the destroy action
     *
     * @param   entityID    The entity that this entity does not want to
     *                      intercept destroy actions from anymore
     */
    public final void unregisterDestroyInterceptor(EntityID entityID) {
        InterceptorImplementation.getInterceptorImplementation().unregisterDestroyInterceptor(this.getEntityID(), entityID);
    }

    /**
     * Registers this entity as an Observer for the destroy action
     *
     * @param   entityID    The entity that this entity want to observe for
     *                      destroy actions
     */
    public final void registerDestroyObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().registerDestroyObserver(this.getEntityID(), entityID);
    }

    /**
     * Unregisters this entity from all destroy Observations
     */
    public final void unregisterAllDestroyObservers() {
        ObserverImplementation.getObserverImplementation().unregisterAllDestroyObservers(this.getEntityID());
    }

    /**
     * Unregisters this entity from Observing destroy actions
     *
     * @param   entityID    The entity to unregister for the observer action
     */
    public final void unregisterDestroyObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().unregisterDestroyObserver(this.getEntityID(), entityID);
    }

    /**
     * Sends information to the specified reactor entity.
     *
     * @param   reactor     The entity that the information will be sent to
     * @param   key         The key describing what this information pertains
     *                      to
     * @param   info        The information to send.
     */
    public final void informEnaction(EntityID reactor, InformationKey key, Information info) {
        ActionImplementation.getActionImplementation().inform(this.getEntityID(), this.getEntityID(), reactor, key, info);
    }

    /**
     * Sends information to the specified reactor entity, on behalf of the
     * specified enactor entity.
     *
     * @param   enactor     The entity that will appear to be informing the
     *                      reactor entity
     * @param   reactor     The entity that the information will be sent to
     * @param   key         The key describing what this information pertains
     *                      to
     * @param   info        The information to send.
     */
    public final void informDiaction(EntityID enactor, EntityID reactor, InformationKey key, Information info) {
        ActionImplementation.getActionImplementation().inform(this.getEntityID(), enactor, reactor, key, info);
    }

    /**
     * Registers this entity as an Interceptor for the inform action
     *
     * @param   entityID    The entity that this entity wants to intercept
     *                      inform actions from
     * @param   priority    The priority of the interception
     */
    public final void registerInformInterceptor(EntityID entityID, InterceptorPriority priority) {
        InterceptorImplementation.getInterceptorImplementation().registerInformInterceptor(this.getEntityID(), entityID, priority);
    }

    /**
     * Unregisters this entity from all Inform Interceptions
     */
    public final void unregisterAllInformInterceptors() {
        InterceptorImplementation.getInterceptorImplementation().unregisterAllInformInterceptors(this.getEntityID());
    }

    /**
     * Unregisters this entity as an Interceptor for the inform action
     *
     * @param   entityID    The entity that this entity does not want to
     *                      intercept inform actions from anymore
     */
    public final void unregisterInformInterceptor(EntityID entityID) {
        InterceptorImplementation.getInterceptorImplementation().unregisterInformInterceptor(this.getEntityID(), entityID);
    }

    /**
     * Registers this entity as an Observer for the inform action
     *
     * @param   entityID    The entity that this entity want to observe for
     *                      inform actions
     */
    public final void registerInformObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().registerInformObserver(this.getEntityID(), entityID);
    }

    /**
     * Unregisters this entity from all inform Observations
     */
    public final void unregisterAllInformObservers() {
        ObserverImplementation.getObserverImplementation().unregisterAllInformObservers(this.getEntityID());
    }

    /**
     * Unregisters this entity from Observing inform actions
     *
     * @param   entityID    The entity to unregister for the observer action
     */
    public final void unregisterInformObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().unregisterInformObserver(this.getEntityID(), entityID);
    }

    /**
     * Action that lets an entity "pull" information from another
     * entity.  It does this by requesting that
     * a specific entity informs this entity of some specific information,
     * It is still the choice of the entity weather or not to send the information`
     * <p>
     * Source entity will request information from a target entity
     *
     * @param   reactor     The entity that the resource will be inqueryed from
     * @param   key         The information key representing the information
     *                      the enactor is interested in
     */
    public final void inqueryEnaction(EntityID reactor, InformationKey key) {
        ActionImplementation.getActionImplementation().inquery(this.getEntityID(), this.getEntityID(), reactor, key);
    }

    /**
     * Enactor entity will request information from the target entity
     *
     * @param   enactor     The entity that is spoofed so that it looks like it
     *                      is attempting to inquery the reactor
     * @param   reactor     The entity that will be requested for information
     *                      from the enactor
     * @param   key         The information key representing the information
     *                      the enactor is interested in
     */
    public final void inqueryDiaction(EntityID enactor, EntityID reactor, InformationKey key) {
        ActionImplementation.getActionImplementation().inquery(this.getEntityID(), enactor, reactor, key);
    }

    /**
     * Registers this entity as an Interceptor for the inquery action
     *
     * @param   entityID    The entity that this entity wants to intercept
     *                      inquery actions from
     * @param   priority    The priority of the interception
     */
    public final void registerInqueryInterceptor(EntityID entityID, InterceptorPriority priority) {
        InterceptorImplementation.getInterceptorImplementation().registerInqueryInterceptor(this.getEntityID(), entityID, priority);
    }

    /**
     * Unregisters this entity from all Inquery Interceptions
     */
    public final void unregisterAllInqueryInterceptors() {
        InterceptorImplementation.getInterceptorImplementation().unregisterAllInqueryInterceptors(this.getEntityID());
    }

    /**
     * Unregisters this entity as an Interceptor for the inquery action
     *
     * @param   entityID    The entity that this entity does not want to
     *                      intercept inquery actions from anymore
     */
    public final void unregisterInqueryInterceptor(EntityID entityID) {
        InterceptorImplementation.getInterceptorImplementation().unregisterInqueryInterceptor(this.getEntityID(), entityID);
    }

    /**
     * Registers this entity as an Observer for the inquery action
     *
     * @param   entityID    The entity that this entity want to observe for
     *                      inquery actions
     */
    public final void registerInqueryObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().registerInqueryObserver(this.getEntityID(), entityID);
    }

    /**
     * Unregisters this entity from all inquery Observations
     */
    public final void unregisterAllInqueryObservers() {
        ObserverImplementation.getObserverImplementation().unregisterAllInqueryObservers(this.getEntityID());
    }

    /**
     * Unregisters this entity from Observing inquery actions
     *
     * @param   entityID    The entity to unregister for the observer action
     */
    public final void unregisterInqueryObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().unregisterInqueryObserver(this.getEntityID(), entityID);
    }

    /**
     * Sends an instruction to the reactor entity
     *
     * @param   reactor     The entity that will recieve the instruction
     * @param   instruction The instruction for the reactor entity
     */
    public final void instructEnaction(EntityID reactor, Instruction instruction) {
        ActionImplementation.getActionImplementation().instruct(this.getEntityID(), this.getEntityID(), reactor, instruction);
    }

    /**
     * Sends out an instruction on behalf of the enactor entity.
     *
     * @param   enactor     The entity that will appear to have sent the
     *                      instruction
     * @param   reactor     The entity the instruction is sent too
     * @param   instruction The instruction sent
     */
    public final void instructDiaction(EntityID enactor, EntityID reactor, Instruction instruction) {
        ActionImplementation.getActionImplementation().instruct(this.getEntityID(), enactor, reactor, instruction);
    }

    /**
     * Registers this entity as an Interceptor for the instruct action
     *
     * @param   entityID    The entity that this entity wants to intercept
     *                      instruct actions from
     * @param   priority    The priority of the interception
     */
    public final void registerInstructInterceptor(EntityID entityID, InterceptorPriority priority) {
        InterceptorImplementation.getInterceptorImplementation().registerInstructInterceptor(this.getEntityID(), entityID, priority);
    }

    /**
     * Unregisters this entity from all Instruct Interceptions
     */
    public final void unregisterAllInstructInterceptors() {
        InterceptorImplementation.getInterceptorImplementation().unregisterAllInstructInterceptors(this.getEntityID());
    }

    /**
     * Unregisters this entity as an Interceptor for the instruct action
     *
     * @param   entityID    The entity that this entity does not want to
     *                      intercept instruct actions from anymore
     */
    public final void unregisterInstructInterceptor(EntityID entityID) {
        InterceptorImplementation.getInterceptorImplementation().unregisterInstructInterceptor(this.getEntityID(), entityID);
    }

    /**
     * Registers this entity as an Observer for the instruct action
     *
     * @param   entityID    The entity that this entity want to observe for
     *                      instruct actions
     */
    public final void registerInstructObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().registerInstructObserver(this.getEntityID(), entityID);
    }

    /**
     * Unregisters this entity from all instruct Observations
     */
    public final void unregisterAllInstructObservers() {
        ObserverImplementation.getObserverImplementation().unregisterAllInstructObservers(this.getEntityID());
    }

    /**
     * Unregisters this entity from Observing instruct actions
     *
     * @param   entityID    The entity to unregister for the instruct action
     */
    public final void unregisterInstructObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().unregisterInstructObserver(this.getEntityID(), entityID);
    }

    /**
     * Moves this entity
     *
     * @param   entity  The entity calling this action
     * @param   rotMove The rotation movement that this entity is to make,
     *                  null if there is to be no rotational movement
     * @param   tranMove    The transitional movement the entity is to make,
     *                      null if there is to be no transitional movement
     */
    public final void moveEnaction(RotationalMovement rotMove, TransitionalMovement tranMove) {
        ActionImplementation.getActionImplementation().move(this.getEntityID(), this.getEntityID(), this.getEntityID(), rotMove, tranMove);
    }

    /**
     * Moves another entity
     *
     * @param   entity  The entity calling this action
     * @param   reactor The entity that is being told to move
     * @param   rotMove The rotation movement that the reactor is to make,
     *                  null if there is to be no rotational movement
     * @param   tranMove    The transitional movement the reactor is to make,
     *                      null if there is to be no transitional movement
     */
    public final void moveEnaction(EntityID reactor, RotationalMovement rotMove, TransitionalMovement tranMove) {
        ActionImplementation.getActionImplementation().move(this.getEntityID(), this.getEntityID(), reactor, rotMove, tranMove);
    }

    /**
     * Moves the reactor entity on behalf of the enactor entity
     *
     * @param   enactor     The entity that will appear to have told the
     *                      reactor to move
     * @param   reactor     The entity that is being told to move
     * @param   rotMove     The rotation movement that the reactor is to make,
     *                      null if there is to be no rotational movement
     * @param   tranMove    The transitional movement the reactor is to make,
     *                      null if there is to be no transitional movement
     */
    public final void moveDiaction(EntityID enactor, EntityID reactor, RotationalMovement rotMove, TransitionalMovement tranMove) {
        ActionImplementation.getActionImplementation().move(this.getEntityID(), enactor, reactor, rotMove, tranMove);
    }

    /**
     * Registers this entity as an Interceptor for the move action
     *
     * @param   entityID    The entity that this entity wants to intercept
     *                      move actions from
     * @param   priority    The priority of the interception
     */
    public final void registerMoveInterceptor(EntityID entityID, InterceptorPriority priority) {
        InterceptorImplementation.getInterceptorImplementation().registerMoveInterceptor(this.getEntityID(), entityID, priority);
    }

    /**
     * Unregisters this entity from all Move Interceptions
     */
    public final void unregisterAllMoveInterceptors() {
        InterceptorImplementation.getInterceptorImplementation().unregisterAllMoveInterceptors(this.getEntityID());
    }

    /**
     * Unregisters this entity as an Interceptor for the move action
     *
     * @param   entityID    The entity that this entity does not want to
     *                      intercept move actions from anymore
     */
    public final void unregisterMoveInterceptor(EntityID entityID) {
        InterceptorImplementation.getInterceptorImplementation().unregisterMoveInterceptor(this.getEntityID(), entityID);
    }

    /**
     * Registers this entity as an Observer for the move action
     *
     * @param   entityID    The entity that this entity want to observe for
     *                      move actions
     */
    public final void registerMoveObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().registerMoveObserver(this.getEntityID(), entityID);
    }

    /**
     * Unregisters this entity from all move Observations
     */
    public final void unregisterAllMoveObservers() {
        ObserverImplementation.getObserverImplementation().unregisterAllMoveObservers(this.getEntityID());
    }

    /**
     * Unregisters this entity from Observing move actions
     *
     * @param   entityID    The entity to unregister for the observer action
     */
    public final void unregisterMoveObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().unregisterMoveObserver(this.getEntityID(), entityID);
    }

    /**
     * Enactor entity pushes a resource to the reactor entity
     *
     * @param   reactor     The entity being sent the resource
     * @param   resource    The type of resource to radiate
     * @param   amount      The amount (in egram) of the resouce to radiate
     */
    public final void radiateEnaction(EntityID reactor, WorldResource resource, Egram amount) {
        ActionImplementation.getActionImplementation().radiate(this.getEntityID(), this.getEntityID(), reactor, resource, amount);
    }

    /**
     * Makes it appear that the enactor entity pushed the specified
     * resources to the reactor entity.
     *
     * @param   enactor     The entity that will appear to have send the
     *                      resources
     * @param   reactor     The entity being sent the resource
     * @param   resource    The type of resource to radiate
     * @param   amount      The amount (in egram) of the resouce to radiate
     */
    public final void radiateDiaction(EntityID enactor, EntityID reactor, WorldResource resource, Egram amount) {
        ActionImplementation.getActionImplementation().radiate(this.getEntityID(), enactor, reactor, resource, amount);
    }

    /**
     * Registers this entity as an Interceptor for the radiate action
     *
     * @param   entityID    The entity that this entity wants to intercept
     *                      radiate actions from
     * @param   priority    The priority of the interception
     */
    public final void registerRadiateInterceptor(EntityID entityID, InterceptorPriority priority) {
        InterceptorImplementation.getInterceptorImplementation().registerRadiateInterceptor(this.getEntityID(), entityID, priority);
    }

    /**
     * Unregisters this entity from all Radiate Interceptions
     */
    public final void unregisterAllRadiateInterceptors() {
        InterceptorImplementation.getInterceptorImplementation().unregisterAllRadiateInterceptors(this.getEntityID());
    }

    /**
     * Unregisters this entity as an Interceptor for the radiate action
     *
     * @param   entityID    The entity that this entity does not want to
     *                      intercept radiate actions from anymore
     */
    public final void unregisterRadiateInterceptor(EntityID entityID) {
        InterceptorImplementation.getInterceptorImplementation().unregisterRadiateInterceptor(this.getEntityID(), entityID);
    }

    /**
     * Registers this entity as an Observer for the radiate action
     *
     * @param   entityID    The entity that this entity want to observe for
     *                      radiate actions
     */
    public final void registerRadiateObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().registerRadiateObserver(this.getEntityID(), entityID);
    }

    /**
     * Unregisters this entity from all radiate Observations
     */
    public final void unregisterAllRadiateObservers() {
        ObserverImplementation.getObserverImplementation().unregisterAllRadiateObservers(this.getEntityID());
    }

    /**
     * Unregisters this entity from Observing radiate actions
     *
     * @param   entityID    The entity to unregister for the observer action
     */
    public final void unregisterRadiateObserver(EntityID entityID) {
        ObserverImplementation.getObserverImplementation().unregisterRadiateObserver(this.getEntityID(), entityID);
    }

    /**
     * Request that the wakeReaction is called after the specified amount of
     * time.
     *
     * @param   time    Time in echoverse seconds
     */
    public final void timerEnaction(Esec time) {
        ActionImplementation.getActionImplementation().timer(this.getEntityID(), this.getEntityID(), this.getEntityID(), time);
    }

    /**
     * Request that the wakeReaction is called after the specified amount of
     * time.
     *
     * @param   reactor The entity to tell to wake after the specified amount
     *                  of time.
     * @param   time    Time in echoverse seconds
     */
    public final void timerEnaction(EntityID reactor, Esec time) {
        ActionImplementation.getActionImplementation().timer(this.getEntityID(), this.getEntityID(), reactor, time);
    }
}

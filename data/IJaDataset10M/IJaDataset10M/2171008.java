package com.cube42.hworld.world;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import com.cube42.echoverse.model.AlertEntity;
import com.cube42.echoverse.model.Egram;
import com.cube42.echoverse.model.Emet;
import com.cube42.echoverse.model.EntityID;
import com.cube42.echoverse.model.EntityInfo;
import com.cube42.echoverse.model.EntityInfoCollection;
import com.cube42.echoverse.model.Esec;
import com.cube42.echoverse.model.RotationalMovement;
import com.cube42.echoverse.model.TransitionalMovement;
import com.cube42.echoverse.model.WorldResource;
import com.cube42.echoverse.model.absorb.AbsorbObservation;
import com.cube42.echoverse.model.collide.CollideReaction;
import com.cube42.echoverse.model.collide.CollisionShape;
import com.cube42.echoverse.model.collide.CollisionSphere;
import com.cube42.echoverse.model.create.CreateReaction;
import com.cube42.echoverse.model.radiate.RadiateReaction;
import com.cube42.echoverse.model.timer.TimerReaction;
import com.cube42.util.math.Orientation;
import com.cube42.util.math.Position;

/**
 * Entity seeks out FryDaddies and eats them
 *
 * @author  Matt Paulin
 * @version $Id: Fob.java,v 1.2 2003/03/12 01:48:32 zer0wing Exp $
 */
public class Fob extends AlertEntity implements AbsorbObservation, CreateReaction, TimerReaction, RadiateReaction, CollideReaction {

    /**
     * The size of the Fob
     */
    public static final Emet SIZE = new Emet(40);

    /**
     * The amount of resources lost by the fob every fob interval
     */
    public static final Egram RESOURCE_LOSS = new Egram(5);

    /**
     * The amount of resources the fob can eat in one collision
     */
    public static final Egram HUNGER_AMOUNT = new Egram(100);

    /**
     * The amount of time in a Fob interval
     */
    public static final Esec FOB_INTERVAL = new Esec(200);

    /**
     * Represents the total amount of resource x in this FryDaddy
     */
    private Egram totalResourceX;

    /**
     * Constructs the Fob
     */
    public Fob() {
        this.totalResourceX = new Egram(100);
    }

    /**
     * Tells the entity that it was created
     *
     * @param   enactor     The entity that created this entity
     */
    public void createReaction(EntityID enactor) {
        this.timerEnaction(FOB_INTERVAL);
    }

    /**
     * A timer set for this entity has gone off
     *
     * @param   enactor     The entity that started the timer
     * @param   time        The amount of time the timer had been set for
     */
    public void timerReaction(EntityID enactor, Esec time) {
        if (enactor.equals(this.getEntityID())) {
            this.totalResourceX.reduce(RESOURCE_LOSS);
            if (this.totalResourceX.getValue() < 0) {
                this.destroyEnaction(this.getEntityID());
            } else {
                this.timerEnaction(FOB_INTERVAL);
            }
        }
    }

    /**
     * Tells the Observer that an absorption has been observered
     *
     * @param   enactor     The entity absorbing the resource
     * @param   enactorPos  The position of the enactor
     * @param   reactor     The entity being asked to give up a resource
     * @param   reactorPos  The position of the reactor
     * @param   resource    The resource type being sent
     * @param   amount      The amount of the resource being sent
     */
    public void absorbObservation(EntityID enactor, Position enactorPos, EntityID reactor, Position reactorPos, WorldResource resource, Egram amount) {
        Position pos = this.getPosition();
        Point3d point = pos.getLocation();
        double xdiff = point.x - enactorPos.getLocation().x;
        double ydiff = point.y - enactorPos.getLocation().y;
        double changeAngle = Math.atan((xdiff / ydiff));
        double turnAngle = this.getPosition().getOrientation().getAngle();
        double turn = changeAngle - turnAngle;
        turn = (turn * 180) / Math.PI;
        Orientation ori = new Orientation();
        ori.turnRightDegrees(turn);
        RotationalMovement rm = new RotationalMovement(ori, new Esec(1));
        Vector3d heading = new Vector3d(0, 30, 0);
        TransitionalMovement tm = new TransitionalMovement(heading, new Esec(5));
        this.moveEnaction(rm, tm);
    }

    /**
     * Called when a entity has become known to this alert entity
     *
     * @param   entityInfo  The new known entity
     */
    public void knownEntityAdded(EntityInfo entityInfo) {
        if (entityInfo.getEntityID().getType().equals("com.cube42.hworld.world.Spud")) {
            this.registerAbsorbObserver(entityInfo.getEntityID());
        }
    }

    /**
     * Called when a collection of entities have become known to this alert entity
     *
     * @param   entityInfos The collection of known entities that are to be
     *                      added
     */
    public void knownEntitiesAdded(EntityInfoCollection entityInfos) {
        EntityInfo tempInfo;
        for (int i = 0; i < entityInfos.size(); i++) {
            tempInfo = (EntityInfo) entityInfos.getEntityInfoAt(i);
            this.knownEntityAdded(tempInfo);
        }
    }

    /**
     * Called when a entity leaves the range of this alert entity
     *
     * @param   entityInfo  The entity that is now unknown to this entity
     */
    public void knownEntityRemoved(EntityInfo entityInfo) {
    }

    /**
     * Reports that a collision has taken place between this entity and
     * something else.
     *
     * @param   enactor     The ID of the entity collided with.
     */
    public void collideReaction(EntityID enactor) {
        if (enactor.getType().equals("com.cube42.hworld.world.FryDaddy")) {
            this.absorbEnaction(enactor, HelloWorldResources.RESOURCE_X, Fob.HUNGER_AMOUNT);
        }
    }

    /**
     * Called when a resource has been radiated from the enactor entity to
     * the reactor entity.
     *
     * @param   enactor     The entity that radiated the resource
     * @param   resource    The world resource that was radiated
     * @param   amount      The amount of resource radiated in egrams
     */
    public void radiateReaction(EntityID enactor, WorldResource resource, Egram amount) {
        if (resource.equals(HelloWorldResources.RESOURCE_X)) {
            this.totalResourceX.increase(amount);
        }
    }

    /**
     * Returns the collision shape that represents the shape of the entity
     * in space, cannot return null.
     * <p>
     * Note: The shape cannot change dynamically.  Do not change the shape
     * the entity is using.  This will cause unknown results
     *
     * @return  A collision shape that represents this entity
     */
    public CollisionShape getCollisionShape() {
        CollisionShape shape = new CollisionSphere(Fob.SIZE);
        shape.setPenetrable(true);
        return shape;
    }
}

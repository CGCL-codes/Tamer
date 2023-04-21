package org.itver.graphics.controller;

import java.util.Enumeration;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.Node;
import javax.media.j3d.Shape3D;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;
import javax.media.j3d.WakeupOnCollisionMovement;
import javax.media.j3d.WakeupOr;
import org.itver.graphics.model.EnvironmentLimits;
import org.itver.graphics.model.MainSceneComponent;

/**
 * Clase de {@code comportamiento} que se encarga de detectar las colisiones de
 * un objeto particular con su entorno.
 * >>>>>>>>>>>>>BUSCAR de dónde me apoyé para hacer este código
 * @author Karo
 */
public class CollisionDetector extends Behavior {

    /** The separate criteria used to wake up this beahvior. */
    protected WakeupCriterion[] theCriteria;

    /** The OR of the separate criteria. */
    protected WakeupOr oredCriteria;

    /** The shape that is watched for collision. */
    protected MainSceneComponent collidingObject;

    private final float REARRANGEMENT = 0.1f;

    /**
     * @param collidingObject
     *            Shape3D that is to be watched for collisions.
     * @param theBounds
     *            Bounds that define the active region for this behaviour
     */
    public CollisionDetector(MainSceneComponent collidingObject, Bounds theBounds) {
        this.collidingObject = collidingObject;
        setSchedulingBounds(theBounds);
    }

    /**
     * This creates an entry, exit and movement collision criteria. These are
     * then OR'ed together, and the wake up condition set to the result.
     */
    @Override
    public void initialize() {
        theCriteria = new WakeupCriterion[3];
        theCriteria[0] = new WakeupOnCollisionEntry(collidingObject, WakeupOnCollisionEntry.USE_GEOMETRY);
        theCriteria[1] = new WakeupOnCollisionExit(collidingObject, WakeupOnCollisionExit.USE_GEOMETRY);
        theCriteria[2] = new WakeupOnCollisionMovement(collidingObject, WakeupOnCollisionMovement.USE_GEOMETRY);
        oredCriteria = new WakeupOr(theCriteria);
        wakeupOn(oredCriteria);
    }

    /**
     * Where the work is done in this class. A message is printed out using the
     * userData of the object collided with. The wake up condition is then set
     * to the OR'ed criterion again.
     */
    @Override
    public void processStimulus(Enumeration criteria) {
        while (criteria.hasMoreElements()) {
            WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();
            if (theCriterion instanceof WakeupOnCollisionEntry) {
                Node theLeaf = ((WakeupOnCollisionEntry) theCriterion).getTriggeringPath().getObject();
                this.checkForLimitCollision(theLeaf);
            } else if (theCriterion instanceof WakeupOnCollisionExit) {
                Node theLeaf = ((WakeupOnCollisionExit) theCriterion).getTriggeringPath().getObject();
            } else {
                Node theLeaf = ((WakeupOnCollisionMovement) theCriterion).getTriggeringPath().getObject();
                this.checkForLimitCollision(theLeaf);
            }
        }
        wakeupOn(oredCriteria);
    }

    private void checkForLimitCollision(Node leaf) {
        BoundingSphere bSphere = new BoundingSphere(collidingObject.getBounds());
        if (leaf.getUserData() instanceof EnvironmentLimits) {
            Shape3D limitCollided = (Shape3D) leaf;
            Shape3D[] limits = EnvironmentLimits.getInstance().getShapeLimits();
            if (limitCollided == limits[EnvironmentLimits.RIGHT]) {
                double newXPos = collidingObject.getPosition().x - REARRANGEMENT;
                collidingObject.setXPosition(newXPos);
            } else {
                if (limitCollided == limits[EnvironmentLimits.LEFT]) {
                    double newXPos = collidingObject.getPosition().x + REARRANGEMENT;
                    collidingObject.setXPosition(newXPos);
                } else {
                    if (limitCollided == limits[EnvironmentLimits.BOTTOM]) {
                        double newYPos = collidingObject.getPosition().y + REARRANGEMENT;
                        collidingObject.setYPosition(newYPos);
                    } else {
                        if (limitCollided == limits[EnvironmentLimits.TOP]) {
                            double newYPos = collidingObject.getPosition().y - REARRANGEMENT;
                            collidingObject.setYPosition(newYPos);
                        } else {
                            if (limitCollided == limits[EnvironmentLimits.FRONT]) {
                                double newZPos = collidingObject.getPosition().z - REARRANGEMENT;
                                collidingObject.setZPosition(newZPos);
                            } else {
                                if (limitCollided == limits[EnvironmentLimits.BACK]) {
                                    double newZPos = collidingObject.getPosition().z + REARRANGEMENT;
                                    collidingObject.setZPosition(newZPos);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

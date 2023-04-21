package breed.model.util;

import breed.model.AbstractActor;
import breed.model.AbstractDataWorld;
import breed.model.AbstractEntity;

/**
 * A shot is an entity that causes damage when colliding with an actor.
 */
public final class Shot extends AbstractEntity {

    /** The actor whose sight rect defines my area of effect. */
    private AbstractActor oActor;

    /** Whether the shot is active at all. */
    protected boolean tShotActive = false;

    /**
     * Creates a new shot which lives in the given data world. Since I have only
     * effect when inside the actors view, deliver actor.
     * 
     * @param iX The position (x).
     * @param iY The position (y).
     * @param oDataWorld The world in which I am living.
     * @param oActor The actor who defines my effect area.
     */
    public Shot(int iX, int iY, AbstractDataWorld oDataWorld, AbstractActor oActor) {
        super(iX, iY, oDataWorld);
        this.oActor = oActor;
    }

    /**
     * Just advance the shot.
     */
    public void tickAction() {
        if (tShotActive) {
            super.tickMove();
        }
    }

    /**
     * If the actor can see me (read: I am still alive), check whether I hit
     * someone.
     */
    public void tickEffect() {
        if (oActor.getSightRect().isVisible(iX, iY, iWid, iHei)) {
            AbstractActor oActor = oDataWorld.getCollidingActor(iX, iY, iWid, iHei);
            if (null != oActor) {
                oActor.collide(this);
                this.collide(oActor);
            }
        } else {
            die();
        }
    }

    /**
     * A shot dies if colliding with someone.
     */
    public void collide(AbstractEntity oEntity) {
        if (oEntity instanceof AbstractActor) {
            die();
        }
    }

    /**
     * Initializes the shot for new use.
     * 
     * @param iX The x position of the shot.
     * @param iY The y position of the shot.
     * @param iAngle The angle in which the shot was fired.
     * @param iSpeed The speed of the shot.
     */
    public void fire(int iX, int iY, int iAngle, int iSpeed) {
        tShotActive = true;
        super.iX = iX;
        super.iY = iY;
        if (iAngle < 0 || iAngle > 359) {
            throw new IllegalArgumentException("Shot angle of " + iAngle + " is not valid [0..359].");
        }
        super.iNextDirection = iAngle;
        super.iNextSpeed = iSpeed;
        super.iDX = super.iDY = 0;
    }

    /**
     * Deactivates the shot.
     */
    public void die() {
        tShotActive = false;
    }

    /**
     * Indicates whether I am firing.
     */
    public boolean isFiring() {
        return tShotActive;
    }
}

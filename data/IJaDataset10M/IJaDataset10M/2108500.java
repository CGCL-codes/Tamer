package com.cube42.echoverse.model;

import java.io.Serializable;
import javax.vecmath.AxisAngle4d;
import com.cube42.util.exception.Cube42NullParameterException;
import com.cube42.util.math.Orientation;

/**
 * Represents rotational velocity that will last a certain length of time
 *
 * @author Matt Paulin
 * @version $Id: RotationalMovement.java,v 1.2 2003/03/12 01:48:33 zer0wing Exp $
 */
public class RotationalMovement implements Serializable {

    /**
     * The amount that the entity is suppose to change its orientation every
     * Esec
     */
    private Orientation rotationalChange;

    /**
     * The duration for how long the rotational change should occure
     */
    private Esec duration;

    /**
     * Constructs the RotationalMovement
     *
     * @param   rotationalChange    The change in orientation for the entity
     *                              every second
     * @param   duration            How long the entity is to be making this
     *                              rotational change
     */
    public RotationalMovement(Orientation rotationalChange, Esec duration) {
        super();
        Cube42NullParameterException.checkNullInConstructor(rotationalChange, "rotationalChange", this);
        this.rotationalChange = rotationalChange;
        this.setDuration(duration);
    }

    /**
     * Calculates a new orientation, using this rotational movement
     *
     * @param   originalOri     The original orientation
     * @param   time            The amount of time that has passed
     * @return  The new orientation
     */
    public Orientation calcOrientation(Orientation originalOri, Esec time) {
        if (time.getValue() == 0) {
            return originalOri;
        }
        if (time.getValue() > this.duration.getValue()) {
            time = this.duration;
        }
        AxisAngle4d tempAA = this.rotationalChange.getAxisAngle4d();
        tempAA.angle = tempAA.angle * time.getValue();
        Orientation tempOri = new Orientation();
        tempOri.setAxisAngle4d(tempAA);
        tempOri.addOrientation(originalOri);
        this.duration.reduce(time);
        return tempOri;
    }

    /**
     * Returns true if the duration is zero, or the transilation matrix
     * is an identity matrix (meaning its not specifying any turning)
     *
     * @return  True if the duration has been reduced to zero
     */
    public boolean isCompleted() {
        if (this.rotationalChange.isIdentity()) {
            return true;
        }
        if (this.duration.getValue() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns the rotational change over the duration.  The orientation
     * returned represents the amount that the orientation of the entity
     * should change over time.
     *
     * @return  The orientational change over one esec
     */
    public Orientation getRotationalChange() {
        return this.rotationalChange;
    }

    /**
     * Sets the duration
     *
     * @param   duration    The new duration
     */
    public void setDuration(Esec duration) {
        Cube42NullParameterException.checkNull(duration, "duration", "setDuration", this);
        this.duration = new Esec(duration.getValue());
    }

    /**
     * Returns the amount of time in esec that this change should occure for
     *
     * @return  The amount of time in esec that the rotational change should
     *          occur for
     */
    public Esec getDuration() {
        return this.duration;
    }
}

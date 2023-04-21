package com.team1160.breakaway.model;

import com.team1160.breakaway.api.Constants;
import com.team1160.breakaway.camera.TargetOrientation;
import com.team1160.breakaway.util.Point2D;
import com.team1160.breakaway.util.Point2DDouble;
import java.util.Vector;

/**
 * Targetting data structure.  Not used yet, but if
 * we want to communicate matches to the dashboard
 * we will need to populate the RobotState with this
 * and have that sent.
 *
 * @author nttoole
 */
public class TargettingState {

    protected int targetOrientation;

    protected Point2DDouble normalizedTarget;

    protected Point2D target;

    protected Vector entries;

    public TargettingState() {
        this.entries = new Vector();
    }

    public void setTarget(Point2D target) {
        this.target = target;
    }

    public void setNormalizedTarget(Point2DDouble normTarget) {
        this.normalizedTarget = normTarget;
        this.updateTargetOrientation();
    }

    public Point2D getTarget() {
        return this.target;
    }

    public Point2DDouble getNormalizedTarget() {
        return this.normalizedTarget;
    }

    public int getOrientation() {
        return this.targetOrientation;
    }

    protected void updateTargetOrientation() {
        if (this.normalizedTarget == null) {
            this.targetOrientation = TargetOrientation.NOT_FOUND;
            return;
        }
        double x = this.normalizedTarget.getX();
        if (x < (-1 * Constants.CENTER_HALF_RANGE_NORMALIZED)) {
            this.targetOrientation = TargetOrientation.LEFT;
        } else if (x > Constants.CENTER_HALF_RANGE_NORMALIZED) {
            this.targetOrientation = TargetOrientation.RIGHT;
        } else {
            this.targetOrientation = TargetOrientation.CENTER;
        }
    }

    public void addEntry(TargettingStateEntry entry) {
        if (!this.entries.contains(entry)) this.entries.addElement(entry);
    }

    public TargettingStateEntry getEntry(int index) {
        if (index < 0 || index > getSize()) return null; else return (TargettingStateEntry) this.entries.elementAt(index);
    }

    public int getSize() {
        return this.entries.size();
    }
}

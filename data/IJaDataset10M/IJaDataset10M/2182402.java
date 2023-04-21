package com.mikecreighton.flow;

import processing.core.*;

public class FlowPoint {

    private PVector pos;

    private PVector vel;

    private float time;

    /**
	 * Constructor
	 * 
	 * @param position A vector representing the position of this point in 2D space
	 * @param velocity A vector representing change in position since the last position in this point's stroke
	 * @param time A float representing the temporal change since the drawing began (in seconds)
	 */
    public FlowPoint(PVector position, PVector velocity, float time) {
        pos = new PVector();
        pos.set(position);
        vel = new PVector();
        vel.set(velocity);
        this.time = time;
    }

    /**
	 * @return A vector representing the position of this point in 2D space
	 */
    public PVector getPosition() {
        PVector p = new PVector();
        p.set(pos);
        return p;
    }

    /**
	 * @param position A vector representing the position of this point in 2D space
	 */
    public void setPosition(PVector position) {
        pos.set(position);
    }

    /**
	 * @return A vector representing change in position since the last position in this point's stroke
	 */
    public PVector getVelocity() {
        PVector v = new PVector();
        v.set(vel);
        return v;
    }

    /**
	 * @param velocity A vector representing change in position since the last position in this point's stroke
	 */
    public void setVelocity(PVector velocity) {
        vel.set(velocity);
    }

    /**
	 * @return The x position of this point in 2D space
	 */
    public float getX() {
        return pos.x;
    }

    /**
	 * @return The y position of this point in 2D space
	 */
    public float getY() {
        return pos.y;
    }

    /**
	 * @return The change in x position since the last point in this point's stroke.
	 */
    public float getXVel() {
        return vel.x;
    }

    /**
	 * @return The change in y position since the last point in this point's stroke.
	 */
    public float getYVel() {
        return vel.y;
    }

    /**
	 * @return A float representing the temporal change since the drawing began (in seconds)
	 */
    public float getTime() {
        return time;
    }

    /**
	 * @param time A float representing the temporal change since the drawing began (in seconds)
	 */
    public void setTime(float time) {
        this.time = time;
    }
}

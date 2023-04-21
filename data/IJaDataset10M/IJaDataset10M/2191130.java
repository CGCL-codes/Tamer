package org.nwolf.carwars.core;

import java.io.Serializable;
import sdljava.video.SDLSurface;

/** 
 * @author lemmings
 * 
 * Class Summary :
 * Genric class used by all graphical game object (Cars, Weapons, ...)
 * 
 */
public abstract class GameGUIObject implements Serializable {

    protected double absolute_position_x;

    protected double absolute_position_y;

    protected long size_x;

    protected long size_y;

    protected float current_speed;

    protected double current_angle;

    protected boolean visibility;

    protected SDLSurface visible_surface;

    protected SDLSurface mask_surface;

    public GameGUIObject() {
        absolute_position_x = 0;
        absolute_position_y = 0;
        size_x = 0;
        size_y = 0;
    }

    public void calculateNewPosition() {
        absolute_position_x += (Math.sin(Math.toRadians(current_angle))) * current_speed;
        absolute_position_y += (Math.cos(Math.toRadians(current_angle))) * current_speed;
    }

    public void setAbsolutePosition(long x, long y) {
        this.absolute_position_x = x;
        this.absolute_position_y = y;
    }

    public int getCenteredPositionX() {
        return (int) (this.absolute_position_x - size_x / 2);
    }

    public int getCenteredPositionY() {
        return (int) (this.absolute_position_y - size_y / 2);
    }

    public void setAngle(double a) {
        this.current_angle = a;
    }

    public boolean isVisible() {
        return this.visibility;
    }
}

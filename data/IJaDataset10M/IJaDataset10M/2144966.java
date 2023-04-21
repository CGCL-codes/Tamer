package net.worship.entities;

import net.worship.core.Game;
import org.openmali.vecmath.Vector2f;
import org.xith3d.scenegraph.Shape3D;
import org.xith3d.scenegraph.Geometry;
import org.xith3d.scenegraph.Appearance;
import org.xith3d.scenegraph.TransformGroup;

/**
 *
 * @author William
 */
public class Entity {

    protected boolean active;

    protected boolean moveable;

    private boolean selected;

    protected Vector2f position;

    protected float angle = 0f;

    private String filename;

    protected String uid;

    /** Creates a new instance of Entity */
    public Entity(boolean moveable, Vector2f pos, String filename) {
        this.moveable = moveable;
        this.filename = filename;
        this.position = pos;
        uid = this.createUID();
    }

    public void setActive(boolean act) {
        active = act;
    }

    public boolean isActive() {
        return active;
    }

    public String getUID() {
        return uid;
    }

    private String createUID() {
        String id = "T" + System.currentTimeMillis() + "N" + (Math.random() * 119);
        return id;
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float a) {
        angle = a;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (selected) {
            Game.getGame().getWorld().addEntitySelection(this);
        } else {
            Game.getGame().getWorld().removedFromSelection(this);
        }
        this.selected = selected;
    }
}

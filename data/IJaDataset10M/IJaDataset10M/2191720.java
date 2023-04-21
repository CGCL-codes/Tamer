package org.chaoticengine.cgll.camera;

import org.chaoticengine.cgll.entity.IEntity;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * This is a simple camera class, suitable for use in top-down or side-scrolling
 * games that have maps larger than the screen. It is a Singleton, because the
 * engine only supports having a single camera.
 *
 * The camera position is the centre of the viewport, so if you have an 800x600
 * window and a camera position of (0,0), the top-left corner of the screen will
 * be at (-400,-300).
 *
 * @author Matt v.d. Westhuizen
 */
public class Camera {

    /** Camera position - the top-left corner of the viewport rectangle. */
    protected Vector2f position = new Vector2f();

    /** Camera viewport. */
    protected Rectangle viewport = new Rectangle(0, 0, 100, 100);

    /** Screen viewport */
    protected Rectangle screen = viewport;

    /** Focus rectangle, the camera tries to keep its subject within this rectangle. */
    protected Rectangle focus = new Rectangle(0, 0, 100, 100);

    /** Camera velocity. */
    protected Vector2f velocity = new Vector2f();

    /** Area outside which the camera may not see. */
    protected Shape bounds = new Rectangle(0, 0, 0, 0);

    /** Entity the camera should follow. */
    protected IEntity target = null;

    /** Position the camera should move to. */
    protected Vector2f targetPosition = null;

    public Camera() {
    }

    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        screen = new Rectangle(0, 0, gc.getWidth(), gc.getHeight());
        if (target != null) {
            Vector2f tPos = target.getPosition().copy();
            Vector2f pos = position.copy().add(new Vector2f(viewport.getWidth() / 2.0f, viewport.getHeight() / 2.0f));
            Vector2f diff = tPos.sub(pos);
            float dist = diff.length();
            float vel = velocity.length();
            if (dist > 0) {
                velocity = velocity.add(diff.scale(10 * dist * delta / 1000.0f));
                diff = diff.normalise();
                if (vel > 0) {
                    velocity = velocity.scale(dist / vel);
                }
            }
            setPosition(position.add(velocity.copy().scale(delta / 1000.0f)));
        } else if (targetPosition != null) {
            Vector2f tPos = targetPosition.copy();
            Vector2f pos = position.copy().add(new Vector2f(viewport.getWidth() / 2.0f, viewport.getHeight() / 2.0f));
            Vector2f diff = tPos.sub(pos);
            float dist = diff.length();
            float vel = velocity.length();
            float accel = 0.1f;
            if (dist > 0) {
                diff = diff.normalise();
                velocity = velocity.add(diff.scale(10 * dist * delta / 1000.0f));
                if (vel > 0) {
                    velocity = velocity.scale(dist / vel);
                }
            }
            setPosition(position.add(velocity.copy().scale(delta / 1000.0f)));
        }
    }

    public void renderBefore(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        grphcs.translate(-position.x, -position.y);
        grphcs.setClip(viewport);
    }

    public void renderAfter(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        grphcs.translate(position.x, position.y);
        grphcs.setClip(null);
    }

    public void renderDebug(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        if (target == null) {
            return;
        }
        grphcs.setColor(Color.blue);
        grphcs.draw(focus);
        grphcs.drawLine(position.x, position.y, position.x, position.y);
        Shape tRect = target.getBounds();
        Vector2f tPos = target.getPosition().add(new Vector2f(tRect.getWidth() / 2.0f, tRect.getHeight() / 2.0f));
        grphcs.setColor(Color.orange);
        grphcs.drawLine(tPos.x, tPos.y, tPos.x, tPos.y);
        grphcs.setColor(Color.blue);
        Vector2f diff = tPos.sub(position);
        grphcs.drawLine(position.x, position.y, position.x + diff.x, position.y + diff.y);
        grphcs.setColor(Color.red);
        grphcs.drawLine(position.x, position.y, position.x + velocity.x, position.y + velocity.y);
    }

    /**
     * The simplest way to move the camera - it is instantly transported to the
     * new location (provided that the location is within bounds.
     *
     * As with all other Camera movement methods, calling this method disables
     * the other movement modes, so velocity and target are reset if the
     * teleport succeeds.
     *
     * @param pos Target position.
     */
    public void teleportTo(Vector2f pos) {
        if (bounds.contains(pos.x, pos.y)) {
            position = pos;
            velocity = new Vector2f();
            target = null;
            targetPosition = null;
        }
    }

    /**
     * A useful way to move the camera for things such as cut-scenes or
     * story-telling segments - the camera automatically moves at maximum speed
     * from its current position to its target position. Fails and does nothing
     * if the target position is not within bounds.
     *
     * As with all other Camera movement methods, calling this method disables
     * the other movement modes, so target is reset if the method succeeds.
     *
     * @param pos Target position.
     */
    public void goTo(Vector2f pos) {
        if (bounds.contains(pos.x, pos.y)) {
            if (target != null) {
                target = null;
            }
            targetPosition = pos;
        }
    }

    /**
     * Probably the most useful way to move the camera - the camera will try to
     * follow the target Entity wherever it moves, provided it stays within the
     * camera bounds (camera bounds and player bounds should correlate closely).
     *
     * As with all other Camera movement methods, calling this method disables
     * the other movement modes, so velocity is reset if the method succeeds.
     *
     * @param pos Target position.
     */
    public void follow(IEntity e) {
        velocity = new Vector2f();
        target = e;
        targetPosition = null;
    }

    public Shape getBounds() {
        return bounds;
    }

    public void setBoundingRectangle(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Vector2f getPosition() {
        return position.copy();
    }

    public void setPosition(Vector2f position) {
        this.position = position;
        focus.setX(position.x);
        focus.setY(position.y);
    }

    public Vector2f getTargetPosition() {
        if (targetPosition != null) {
            return (targetPosition.copy());
        } else {
            return (null);
        }
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public float getFocusWidth() {
        return (focus.getWidth());
    }

    public void setFocusWidth(float f) {
        focus.setWidth(f);
        setPosition(position);
    }

    public float getFocusHeight() {
        return (focus.getHeight());
    }

    public void setFocusHeight(float f) {
        focus.setHeight(f);
        setPosition(position);
    }

    public Rectangle getViewport() {
        return viewport;
    }

    public void setViewport(Rectangle viewport) {
        this.viewport = viewport;
    }

    public Rectangle getScreen() {
        return (screen);
    }
}

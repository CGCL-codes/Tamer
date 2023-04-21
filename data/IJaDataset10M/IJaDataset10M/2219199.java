package org.chaoticengine.cgll.entity.component.move;

import java.util.HashMap;
import java.util.Map;
import org.chaoticengine.cgll.entity.component.ActiveComponent;
import org.chaoticengine.cgll.input.Command;
import org.chaoticengine.cgll.entity.component.IMovementComponent;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.simpleframework.xml.Element;

/**
 * Basic movement class from which other classes should be derived. It provides
 * simple mechanisms for applying a force to the owning entity in any direction
 * or in the facing direction.
 * 
 * It treats all distance units input to it as pixels and all time components
 * are assumed to be seconds.
 *
 * @author Matt v.d. Westhuizen
 */
public class BasicMovementComponent extends ActiveComponent implements IMovementComponent {

    /** Current velocity */
    protected Vector2f v = new Vector2f();

    /** Current acceleration */
    protected Vector2f a = new Vector2f();

    /** Acceleration per second in screen heights */
    protected float aPerS = 0.0f;

    /** Maximum velocity */
    protected float maxV = 0.0f;

    /** Friction */
    protected float friction = 0.015f;

    /** Available commands */
    protected Map<String, Command> commands = new HashMap<String, Command>();

    ;

    public BasicMovementComponent() {
    }

    /** Accelerates in the specified direction. */
    public void accelerate(Vector2f direction) {
        a = a.add(direction).normalise();
    }

    /** Accelerates in the facing direction by the specified amount. */
    public void accelerate() {
        a = a.add(getOwner().getDirection()).normalise();
    }

    /** Accelerates in the facing direction by the specified amount. */
    public void decelerate() {
        a = a.add(getOwner().getDirection().negate()).normalise();
    }

    /** Accelerates in the opposite of the facing direction by the specified amount. */
    public void decelerate(Vector2f direction) {
        a = a.add(direction.negate()).normalise();
    }

    /** Rotates the craft by the specified amount. */
    public void rotate(float radians) {
        owner.setRotation(owner.getRotation() + radians);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        super.update(gc, sb, delta);
        v = v.add(a.scale(aPerS * gc.getHeight() * (delta / 1000.0f)));
        if (a.length() == 0) {
            v = v.scale(1.0f - friction / 1000.0f * delta);
            if (v.length() < 1.0f) {
                v = new Vector2f();
            } else if (v.length() >= 1.0f) {
                v = v.add(v.copy().normalise().negate().scale(aPerS * gc.getHeight() * (delta / 1000.0f)));
            }
        }
        clipVelocity(gc);
        owner.setPosition(owner.getPosition().add(v.copy().scale(delta / 1000.0f)));
        a.scale(0.0f);
        if (v.length() == 0.0f) {
            getOwner().setMoving(false);
        } else {
            getOwner().setMoving(true);
        }
    }

    protected void clipVelocity(GameContainer gc) {
        if (v.length() > maxV * gc.getHeight()) {
            v = v.scale((maxV * gc.getHeight()) / v.length());
        }
    }

    @Override
    public Map<String, Command> getCommands() {
        return (commands);
    }

    public Vector2f getVelocity() {
        return (v.copy());
    }

    public void setVelocity(Vector2f vel) {
        this.v = vel.copy();
    }

    @Element(name = "max_velocity", required = false)
    public float getMaxVelocity() {
        return (this.maxV);
    }

    @Element(name = "max_velocity", required = false)
    public void setMaxVelocity(float v) {
        this.maxV = v;
    }

    @Element(name = "friction", required = false)
    public float getFriction() {
        return (this.friction);
    }

    @Element(name = "friction", required = false)
    public void setFriction(float f) {
        this.friction = f;
    }

    @Element(name = "acceleration")
    public float getAcceleration() {
        return (aPerS);
    }

    @Element(name = "acceleration")
    public void setAcceleration(float acc) {
        this.aPerS = acc;
    }

    @Override
    public void onSetOwner() {
        getOwner().getComponentManager().registerSpecialComponent("move", this);
    }
}

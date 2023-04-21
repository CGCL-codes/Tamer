package org.chaoticengine.jumpers.model.component.collisions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.chaoticengine.cgll.entity.Entity;
import org.chaoticengine.cgll.entity.Entity.EntityType;
import org.chaoticengine.cgll.input.Command;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * A collision component that causes damage in a radius to any entities with 
 * a 'health'-component that collided with that radius. The radius can be static
 * or can expand at a set rate.
 *
 * @author Matt v.d. Westhuizen
 */
@Root
public class DamageRadiusCollisionComponent extends DamageComponent {

    protected Circle circle = new Circle(0.0f, 0.0f, 0.0f);

    protected float startRadius = 0.0f;

    protected float endRadius = 0.0f;

    protected int duration = 0;

    protected int timePassed = 0;

    protected int delta = 0;

    protected Damage damage = null;

    protected Vector2f translation = new Vector2f();

    protected List<Entity> damaged = new ArrayList<Entity>();

    protected HashMap<String, Command> commands = new HashMap<String, Command>();

    @Override
    public void onSetOwner() {
        super.onSetOwner();
        circle = new Circle(getOwner().getPosition().x, getOwner().getPosition().y, startRadius);
        getOwner().setBounds(circle);
        getOwner().getComponentManager().registerSpecialComponent("damage", this);
    }

    @Override
    public void handleCollision(Entity e) {
        if (e.getType() == EntityType.NONE) {
            return;
        }
        if ((this.getOwner().getType() == EntityType.BULLET) && (e.getType() == EntityType.BULLET)) {
            return;
        }
        HealthComponent hc = (HealthComponent) e.getComponentManager().getSpecialComponent("health");
        if (hc != null) {
            Vector2f targetCenter = new Vector2f(e.getBounds().getCenter());
            Vector2f myCenter = new Vector2f(circle.getCenter());
            float d = myCenter.sub(targetCenter).length();
            if (d <= endRadius) {
                Damage dmg = damage.copy();
                dmg.setDamage(damage.getDamage() * (delta / (float) duration) * ((endRadius - d) / endRadius));
                hc.damage(dmg);
                dmg.destroy();
                damaged.add(e);
            }
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sb, int delta) {
        super.update(gc, sb, delta);
        if (timePassed >= duration) {
            return;
        }
        if (owner.getComponentManager().getSpecialComponent("move") != null) {
            owner.getComponentManager().removeComponent(owner.getComponentManager().getSpecialComponent("move"));
            owner.getOwner().updateCapabilities(owner);
            getOwner().setBounds(circle);
        }
        this.delta = delta;
        timePassed += delta;
        circle.setRadius(startRadius + (endRadius - startRadius) * (timePassed / (float) duration));
        Vector2f pos = getOwner().getPosition().copy();
        circle.setLocation(pos.x + translation.x * circle.getWidth() - circle.radius, pos.y + translation.y * circle.getHeight() - circle.radius);
    }

    @Override
    public Map<String, Command> getCommands() {
        return (commands);
    }

    public Damage getDamage() {
        return damage;
    }

    public void setDamage(Damage damage) {
        this.damage = damage;
    }

    @Element(name = "duration")
    public int getDuration() {
        return duration;
    }

    @Element(name = "duration")
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Element(name = "radius_start")
    public float getStartRadius() {
        return startRadius;
    }

    @Element(name = "radius_start")
    public void setStartRadius(float radius) {
        this.startRadius = radius;
    }

    @Element(name = "radius_end")
    public float getEndRadius() {
        return endRadius;
    }

    @Element(name = "radius_end")
    public void setEndRadius(float endRadius) {
        this.endRadius = endRadius;
    }

    @Element(name = "translation", required = false)
    public Vector2f getTranslation() {
        return translation;
    }

    @Element(name = "translation", required = false)
    public void setTranslation(Vector2f translation) {
        this.translation = translation;
    }
}

package org.roguesinspace.agents;

import org.roguesinspace.actions.*;
import org.roguesinspace.things.AbstractContainer;
import org.roguesinspace.things.Container;
import org.roguesinspace.things.Descriptor;
import org.roguesinspace.things.Thing;
import org.roguesinspace.things.ThingType;
import org.roguesinspace.things.Usable;
import org.roguesinspace.things.Weapon;
import org.roguesinspace.things.Wearable;
import org.roguesinspace.ai.Controller;
import com.jme.math.Vector3f;
import java.util.Vector;
import java.util.Iterator;

/**
 * The <code>BaseAgent<code> class is a fully functional <code>Agent</code>
 * implementation.
 * 
 * @see org.roguesinspace.agents.Agent
 *
 * @author 	jglover
 * @since	0.1
 */
public class BaseAgent extends AbstractContainer implements Agent {

    /** <code>Descriptor</code> that describes the this <code>Agent</code> */
    protected Descriptor descriptor;

    /** <code>Controller</code> which controls this <code>Agent</code> */
    protected Controller controller;

    /** A <code>Vector</code> containing the <code>Skill</code>s belonging to this <code>Agent</code> */
    protected Vector skills;

    /** A <Code>Vector</code> of the <code>Attribute</code>s belonging to this <code>Agent</code> */
    protected Vector attributes;

    /** A <code>Vector</code> of <code>Ability</code> objects belonging to this <Code>Agent</code> */
    protected Vector abilities;

    /** A <code>Vector</code> of <code>Action</code>s belonging to this <code>Agent</code> */
    protected Vector actions;

    /** The amount of health this <code>Agent</code> has */
    protected int health;

    /** The <code>Weapon</code> this <code>Agent</code> is currently using */
    protected Weapon weapon;

    /** The <code>Armor</code> This <code>Agent</code> is currently using */
    protected Wearable armor;

    /**
     * Constructs a new <code>AbstractContainer</code> with the specified
     * parameters.
     * @param container		<code>Container</code> this is contained in
     * @param weight		<code>float</code> amount this weighs
     * @param owner			<code>Thing</code> that owns this
     * @param descriptor	<code>Descriptor</code> describing this
     * @param type			<code>ThingType</code> representing this
     * @param location		<code>Vector3f</code> representing 3d location
     * @param size			<code>Vector3f</code> representing size from imaginary origin
     */
    public BaseAgent(Container container, float weight, Thing owner, Descriptor descriptor, ThingType type, Vector3f location, Vector3f size, Controller controller) {
        super(container, weight, owner, descriptor, type, location, size);
        this.skills = new Vector();
        this.attributes = new Vector();
        for (int i = 0; i < 6; ++i) {
            attributes.add(new BaseAttribute(this, i));
        }
        this.abilities = new Vector();
        this.actions = new Vector();
        actions.add(new AttackAction(this, 10, null));
        actions.add(new MoveAction(this, 20, null));
    }

    public void processEvent(Event event) {
        if (event instanceof DamageEvent) {
            this.processEvent((DamageEvent) event);
        }
        if (event instanceof MoveEvent) {
            this.processEvent((MoveEvent) event);
        }
    }

    /**
     * Processes a <code>DamageEvent</code> to this <code>Agent</code>.
     * @param event	the <code>DamageEvent</code> to process.
     */
    protected void processEvent(DamageEvent event) {
        Damage damage = event.getDamage();
        int amount = ((100 - armor.protectionAgainst(damage.getType())) * damage.getAmount()) / 100;
        this.health -= amount;
        if (amount <= 0) {
            this.die(event.getInitiator());
        }
        return;
    }

    public boolean useSkill(String skill) {
        return false;
    }

    public boolean useAttribute(String attribute) {
        return false;
    }

    public boolean hasAttribute(String attribute) {
        return false;
    }

    public boolean hasAbility(String ability) {
        Iterator iter = abilities.iterator();
        while (iter.hasNext()) {
            Ability ab = (Ability) iter.next();
            if (ability.equals(ab)) {
                return true;
            }
        }
        return false;
    }

    public boolean useAbility(String ability) {
        return false;
    }

    public boolean hasSkill(String skill) {
        Iterator iter = skills.iterator();
        while (iter.hasNext()) {
            Skill sk = (Skill) iter.next();
            if (skill.equals(sk)) {
                return true;
            }
        }
        return false;
    }

    public Weapon getWeapon() {
        return this.weapon;
    }

    public Skill getResponsibleSkill(Thing thing) {
        Iterator iter = skills.iterator();
        while (iter.hasNext()) {
            Skill skill = (Skill) iter.next();
            if (thing.usableOnThis(skill)) {
                return skill;
            }
        }
        return null;
    }

    public int getDefenseValue() {
        if (this.armor != null) {
            return armor.getDefenseValue();
        } else {
            return 0;
        }
    }

    public boolean usableOnThis(Skill skill) {
        return false;
    }

    public boolean usableOnThis(Usable thing) {
        return false;
    }

    public void die(Thing cause) {
    }

    public boolean capableOf(Action action) {
        Iterator iter = actions.iterator();
        while (iter.hasNext()) {
            if ((iter.next()).getClass() == action.getClass()) {
                return true;
            }
        }
        return false;
    }
}

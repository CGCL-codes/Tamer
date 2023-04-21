package org.roguesinspace.demo;

import org.roguesinspace.actions.Action;
import org.roguesinspace.actions.Event;
import org.roguesinspace.agents.Agent;
import org.roguesinspace.agents.Skill;
import org.roguesinspace.things.Container;
import org.roguesinspace.things.Descriptor;
import org.roguesinspace.things.Thing;
import org.roguesinspace.things.ThingType;
import org.roguesinspace.things.Usable;
import org.roguesinspace.things.Weapon;
import com.jme.math.Vector3f;

/**
 * TODO document me.
 *
 * @author 	jglover
 * @since	
 */
public class TargetAgent implements Agent {

    /**
     * 
     */
    public TargetAgent() {
        super();
    }

    public boolean hasAttribute(String attribute) {
        return false;
    }

    public boolean useAttribute(String attribute) {
        return false;
    }

    public boolean hasAbility(String ability) {
        return false;
    }

    public boolean hasSkill(String skill) {
        return false;
    }

    public boolean useSkill(String skill) {
        return false;
    }

    public Weapon getWeapon() {
        return null;
    }

    public Skill getResponsibleSkill(Thing thing) {
        return null;
    }

    public boolean capableOf(Action action) {
        return false;
    }

    public boolean add(Thing thing) {
        return false;
    }

    public boolean remove(Thing thing) {
        return false;
    }

    public boolean containsType(Thing thing) {
        return false;
    }

    public boolean containsSimilar(Thing thing) {
        return false;
    }

    public boolean containsThis(Thing thing) {
        return false;
    }

    public Container getContainer() {
        return null;
    }

    public void putInto(Container container) {
    }

    public Vector3f getLocation() {
        return null;
    }

    public float getWeight() {
        return 0;
    }

    public void setWeight(float weight) {
    }

    public boolean isContained() {
        return false;
    }

    public Vector3f getSize() {
        return null;
    }

    public Thing getOwner() {
        return null;
    }

    public boolean isOwned() {
        return false;
    }

    public Descriptor getDescriptor() {
        return null;
    }

    public void processEvent(Event event) {
    }

    public ThingType getType() {
        return null;
    }

    public boolean isSimilar(Thing thing) {
        return false;
    }

    public boolean isType(Thing thing) {
        return false;
    }

    public boolean isThis(Thing thing) {
        return false;
    }

    public float getDistance(Thing thing) {
        return 0;
    }

    public int getDefenseValue() {
        return 0;
    }

    public boolean usableOnThis(Skill skill) {
        return false;
    }

    public boolean usableOnThis(Usable thing) {
        return false;
    }

    public void die(Thing cause) {
    }

    public void move(Vector3f location) {
    }
}

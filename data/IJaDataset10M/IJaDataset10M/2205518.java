package sratworld.base.actor;

import sratworld.base.*;
import java.util.*;

/** A default implementation of actor.
    Author: Henrik B�rbak Christensen 
*/
public class ActorImpl implements Actor {

    protected AttributeMap attr;

    protected AttributeMap wear;

    protected List<Item> inventory;

    public ActorImpl() {
        attr = new AttributeMap();
        wear = new AttributeMap();
        inventory = new ArrayList<Item>();
    }

    public AttributeMap getAttributes() {
        return attr;
    }

    public AttributeMap getWear() {
        return wear;
    }

    public List<Item> getInventory() {
        return inventory;
    }
}

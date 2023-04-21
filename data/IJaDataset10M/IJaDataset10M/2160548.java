package org.vrspace.server.object;

import org.vrspace.server.*;
import org.vrspace.attributes.*;
import java.util.*;
import org.vrspace.util.*;

/**
Generic robot class.
It can be owned, can own things, has parent Transform.
It can take/drop some items or equipment, i.e. ProximitySensor
*/
public class Robot extends PublicVRObject implements Owned, Owner, HasTransform {

    public ID[] equipment;

    public long transform;

    protected HashSet owners;

    protected HashSet owned;

    protected Vector equip = new Vector();

    protected Transform trans = new Transform();

    /**
  take - object changes ownership and disappears from the scene
  */
    public void take(VRObject o) {
        if (o instanceof Owned) {
            addOwned((Owned) o);
        }
        if (o instanceof HasTransform) {
            ((HasTransform) o).setTransform(null);
        }
    }

    /**
  drop - the opposite of take<br>
  broken
  */
    public void drop(VRObject o) {
        if (o instanceof Owned) {
            removeOwned((Owned) o);
        }
        if (o instanceof HasTransform && ((HasTransform) o).transform().equals(this)) {
            Transform t = new Transform(trans.x, trans.y, trans.z);
            try {
                ((HasTransform) o).setTransform(t);
            } catch (Exception e) {
                Logger.logError(e);
            }
        }
    }

    /**
  equip <o>
  object remains in the scene
  */
    public void addEquipment(VRObject o) {
        if (o instanceof Owned) {
            ((Owned) o).addOwner(this);
        }
        equip.add(o);
        equipment = (ID[]) equip.toArray(new ID[0]);
        trans.addMember(o);
        if (o instanceof HasTransform) {
            ((HasTransform) o).setTransform(trans);
        }
    }

    /**
  unequip <o>
  object disappears from the scene
  */
    public void removeEquipment(VRObject o) {
        if (o instanceof Owned) {
            ((Owned) o).addOwner(this);
        }
        equip.removeElement(o);
        trans.removeMember(o);
        equipment = (ID[]) equip.toArray(new ID[0]);
        if (o instanceof HasTransform) {
            ((HasTransform) o).setTransform(null);
        }
    }

    /**
  calls removeEquipment for all equipped objects
  */
    public void removeAllEquipment() {
        for (int i = 0; i < equipment.length; i++) {
            removeEquipment((VRObject) equip.elementAt(i));
        }
    }

    /**
  unequip <o>
  object remains in the scene
  */
    public void dropEquipment(VRObject o) {
        equip.removeElement(o);
        trans.removeMember(o);
        equipment = (ID[]) equip.toArray(new ID[0]);
        drop(o);
    }

    /**
  calls dropEquipment for all equipped objects
  */
    public void dropAllEquipment() {
        for (int i = 0; i < equip.size(); i++) {
            dropEquipment((VRObject) equip.elementAt(i));
        }
    }

    /**
  Interface HasTransform
  */
    public Transform transform() {
        return trans;
    }

    public long getTransformID() {
        return transform;
    }

    public void setTransform(Transform t) {
        trans = t;
    }

    /**
  Interface Owner
  */
    public void addOwned(Owned o) {
        owned.add(o);
        if (!o.isOwned(this)) {
            ((Owned) o).addOwner(this);
        }
    }

    public void removeOwned(Owned o) {
        owned.remove(o);
        if (o.isOwned(this)) {
            o.removeOwner(this);
        }
    }

    public boolean isOwner(Owned o) {
        return owned.contains(o);
    }

    public void updateOwnership(Owned o, Object obj) {
    }

    /**
  Interface Owned
  */
    public void addOwner(Owner c) {
        owners.add(c);
        if (!c.isOwner(this)) {
            c.addOwned(this);
        }
    }

    public void removeOwner(Owner c) {
        owners.remove(c);
        if (c.isOwner(this)) {
            c.removeOwned(this);
        }
    }

    public boolean isOwned(Owner c) {
        return owners.contains(c);
    }

    public void notifyOwners(Object o) {
        Iterator i = owners.iterator();
        while (i.hasNext()) {
            ((Owner) i.next()).updateOwnership(this, o);
        }
    }

    public void setEquipment(String equipment) {
        if (equipment != null) {
            this.equipment = (ID[]) VRObject.stringToArray(ID.class, equipment);
        }
    }

    public String getEquipment() {
        if (this.equipment != null) {
            return VRObject.arrayToString(this.equipment);
        }
        return null;
    }
}

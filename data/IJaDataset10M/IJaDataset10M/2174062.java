package ca.sqlpower.architect.olap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.sqlpower.object.SPObject;
import ca.sqlpower.object.annotation.Accessor;
import ca.sqlpower.object.annotation.NonProperty;
import ca.sqlpower.object.annotation.Transient;

/**
 * The top of the OLAP business model. This root object contains OLAPSessions, each
 * of which contains exactly one Schema object.
 */
public class OLAPRootObject extends OLAPObject {

    /**
     * Defines an absolute ordering of the child types of this class.
     * 
     * IMPORTANT!: When changing this, ensure you maintain the order specified by {@link #getChildren()}
     */
    public static final List<Class<? extends SPObject>> allowedChildTypes = Collections.<Class<? extends SPObject>>singletonList(OLAPSession.class);

    private final List<OLAPSession> olapSessions = new ArrayList<OLAPSession>();

    public OLAPRootObject() {
        setName("OLAP Root Object");
    }

    @Override
    protected void addChildImpl(SPObject child, int index) {
        if (child instanceof OLAPSession) {
            olapSessions.add(index, (OLAPSession) child);
            child.setParent(this);
            fireChildAdded(OLAPSession.class, child, index);
        }
    }

    @Override
    protected boolean removeChildImpl(SPObject child) {
        if (child instanceof OLAPSession) {
            return removeOLAPSession((OLAPSession) child);
        }
        return false;
    }

    /** 
     * Removes the given child object, firing an OLAPChildEvent if the child was found.
     *
     * @return true if the item was removed (because it was in the list); false if the item was not removed.
     */
    public boolean removeOLAPSession(OLAPSession removeChild) {
        int pos = olapSessions.indexOf(removeChild);
        if (pos != -1) {
            removeOLAPSession(pos);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes the child object at the given position, firing an OLAPChildEvent.
     *
     * @return The item that was removed.
     */
    public OLAPSession removeOLAPSession(int pos) {
        OLAPSession removedItem = olapSessions.remove(pos);
        if (removedItem != null) {
            fireChildRemoved(OLAPSession.class, removedItem, pos);
            removedItem.setParent(null);
        }
        return removedItem;
    }

    public boolean allowsChildren() {
        return true;
    }

    @NonProperty
    public List<OLAPSession> getChildren() {
        return Collections.unmodifiableList(olapSessions);
    }

    public int childPositionOffset(Class<? extends SPObject> childType) {
        if (OLAPSession.class.equals(childType)) {
            return 0;
        } else {
            throw new IllegalArgumentException("Child type " + childType + " is not a valid child type of " + OLAPRootObject.class);
        }
    }

    @Transient
    @Accessor
    public List<Class<? extends SPObject>> getAllowedChildTypes() {
        return allowedChildTypes;
    }

    @Transient
    @Accessor
    public List<? extends SPObject> getDependencies() {
        return Collections.emptyList();
    }

    public void removeDependency(SPObject dependency) {
    }
}

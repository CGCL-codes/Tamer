package net.sourceforge.ondex.core;

import javax.naming.OperationNotSupportedException;
import net.sourceforge.ondex.config.ONDEXRegistry;
import net.sourceforge.ondex.core.security.perm.AccessScope;
import net.sourceforge.ondex.core.security.perm.Permission;
import net.sourceforge.ondex.core.util.UpdateListener;
import net.sourceforge.ondex.event.GraphEvent;
import net.sourceforge.ondex.event.GraphListener;
import net.sourceforge.ondex.event.type.EventType;
import net.sourceforge.ondex.exception.type.AccessDeniedException;

/**
 * Common functionality for security management and update tracking.
 * 
 * @author taubertj
 * 
 */
public abstract class AbstractONDEXEntity {

    /**
	 * A unique session id for each graph for security. Currently derived from
	 * nano system time at construction of AbstractONDEXGraph.
	 */
    protected long sid;

    /**
	 * Contains registered listener for any changes to parts of the core data
	 * structure, transient. Necessary for persistent environments.
	 */
    private transient UpdateListener listener = null;

    /**
	 * Provides a byte array serialisation of this AbstractONDEXEntity.
	 * 
	 * @return serialisation in the form of byte[]
	 */
    public abstract byte[] serialise();

    /**
	 * Returns the unique id associated with the parent AbstractONDEXGraph.
	 * 
	 * @return unique id of parent AbstractONDEXGraph
	 */
    public long getSID() {
        return sid;
    }

    /**
	 * Sets the UpdateListener for this AbstractONDEXEntity. Only one parent
	 * UpdateListener is allowed to register at one time.
	 * 
	 * @param l
	 *            UpdateListener of persistent environment
	 */
    public void setUpdateListener(UpdateListener l) {
        listener = l;
    }

    /**
	 * Removes the UpdateListener currently associated with this
	 * AbstractONDEXEntity.
	 */
    public void removeUpdateListener() {
        listener = null;
    }

    /**
	 * Returns the current UpdateListener.
	 * 
	 * @return UpdateListener of persistent environment
	 */
    public UpdateListener getUpdateListener() {
        return listener;
    }

    /**
	 * Notify listener of this AbstractONDEXEntity.
	 * 
	 * @param o
	 *            actual object in need of an update
	 */
    public void fireUpdate(Object o) {
        if (listener != null) {
            listener.performUpdate(o);
        }
    }

    /**
	 * Notify all listeners that have registered with this class.
	 * 
	 * @param eventName
	 *            name of event
	 */
    public void fireEventOccurred(EventType e) {
        AbstractONDEXGraph aog = ONDEXRegistry.graphs.get(sid);
        GraphEvent oe = new GraphEvent(this, e);
        for (GraphListener l : aog.getONDEXGraphListeners()) {
            l.eventOccurred(oe);
        }
    }

    /**
	 * set permission on this object to p for the given scope s.
	 * @param s the access scope
	 * @param p the permissions level.
	 * @throws AccessDeniedException if not owner.
	 */
    public abstract void setPermission(AccessScope s, Permission p) throws AccessDeniedException, OperationNotSupportedException;

    /**
	 * returns the current permissions level on this object for
	 * a given access scope.
	 * @param s the access scope to query for
	 * @return the current permission level
	 */
    public abstract Permission getPermission(AccessScope s);

    /**
	 * if called by root, this sets the owner user id for this object
	 * to the given value. 
	 * @param uid the user id to set.
	 * @throws AccessDeniedException if not root.
	 */
    public abstract void setOwnerUserID(int uid) throws AccessDeniedException, OperationNotSupportedException;

    /**
	 * returns the owner user id for this object.
	 * @return the owner user id.
	 */
    public abstract int getOwnerUserID();

    /**
	 * if called by root, this sets the owner group id for this object
	 * to the given value. 
	 * @param gid the group id to set.
	 * @throws AccessDeniedException if not root.
	 */
    public abstract void setOwnerGroupID(int gid) throws AccessDeniedException, OperationNotSupportedException;

    /**
	 * returns the owner group id of this object.
	 * @return the owner group id
	 */
    public abstract int getOwnerGroupID();
}

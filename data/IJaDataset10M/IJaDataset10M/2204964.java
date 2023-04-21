package org.nightlabs.jfire.jdo.notification;

import java.util.Collection;
import java.util.EventObject;
import javax.jdo.PersistenceManager;

public class JDOLifecycleRemoteEvent extends EventObject {

    private static final long serialVersionUID = 2L;

    public JDOLifecycleRemoteEvent(Object source, PersistenceManager persistenceManager, Collection<DirtyObjectID> dirtyObjectIDs) {
        super(source);
        this.persistenceManager = persistenceManager;
        this.dirtyObjectIDs = dirtyObjectIDs;
    }

    private Collection<DirtyObjectID> dirtyObjectIDs;

    public Collection<DirtyObjectID> getDirtyObjectIDs() {
        return dirtyObjectIDs;
    }

    private transient PersistenceManager persistenceManager;

    /**
	 * With only the objectIDs, it's impossible to do anything meaningful in
	 * the {@link IJDOLifecycleListenerFilter#filter(JDOLifecycleRemoteEvent)} method.
	 * Hence, you have the possibility to use the given PersistenceManager for
	 * <b>READING</b> from the datastore. Writing is not possible.
	 * <p>
	 * Note, that this PersistenceManager is a new one - not the one that was originally
	 * used for modification of the given objects.
	 * </p>
	 * <p>
	 * Multiple calls to this method always return the same instance.
	 * </p>
	 *
	 * @return Returns a new PersistenceManager that shall
	 */
    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
}

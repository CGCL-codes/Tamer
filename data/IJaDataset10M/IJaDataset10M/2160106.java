package com.foursoft.fourever.consistency;

import java.util.Date;
import java.util.ListIterator;
import com.foursoft.fourever.consistency.event.ConsistencyListener;
import com.foursoft.fourever.consistency.exception.CheckException;
import com.foursoft.fourever.objectmodel.ObjectModel;

/**
 * A list of conflicts corresponding to an object model and a set of
 * constraints.
 * 
 * @author sihling
 * 
 */
public interface ConflictList {

    /**
	 * Checks an object modell and creates a conflict list listing all conflicts
	 * of the object modell
	 * 
	 * @throws CheckException
	 *             if checking failed
	 */
    public void performChecks() throws CheckException;

    /**
	 * Returns the latest timestamp of listed conflicts
	 * 
	 * @return timestamp of the latest conflict otherwise null
	 */
    public Date getLatestTimestamp();

    /**
	 * Returns all conflicts of this conflict list.
	 * 
	 * @return an Iterator<Conflict> containing conflicts.
	 */
    public ListIterator<Conflict> getConflicts();

    /**
	 * @return the number of conflicts in this list
	 */
    public int size();

    /**
	 * Returns the object model of the conflict list
	 * 
	 * @return object model
	 */
    public ObjectModel getObjectModel();

    /**
	 * A consistency listener can be registered with this conflict list to be
	 * informed about possible changes of the list. This is especially useful
	 * for corresponding GUI elements (like updating conflictlist).
	 * 
	 * @param listener
	 *            the listener to register
	 */
    public void registerConsistencyListener(ConsistencyListener listener);

    /**
	 * Removes the previously registered listener.
	 * 
	 * @param listener
	 *            the listener to unregister
	 */
    public void unregisterConsistencyListener(ConsistencyListener listener);
}

package prajna.semantic.stream;

import prajna.semantic.DataAccessor;

/**
 * Interface for a DataAccessor which accepts streaming data. This interface
 * adds support for a DataStructureListener. It also supports setting an aging
 * time to determine when a particular data element should be aged off. The
 * listeners should be called when adding, removing or changing a data element
 * results in a change to the data structure associated with that
 * DataStructureListener.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 * @param <N> The node class, used in grids, datasets, trees, and the nodes of
 *            graphs
 * @param <E> the edge class for graphs
 */
public interface StreamingAccessor<N, E> extends DataAccessor<N, E> {

    /**
     * Add a data structure listener to listen for events when a particular
     * data structure changes.
     * 
     * @param listener the DataStructureListener
     */
    public void addDataStructureListener(DataStructureListener listener);

    /**
     * Get the length of time that a particular data element will be retained.
     * If the value is less than 0, the data elements are not aged. The aging
     * time is specified in seconds.
     * 
     * @return the aging time
     */
    public int getAgingTime();

    /**
     * Set the length of time that a particular data element will be retained.
     * If the value is less than 0, the data elements are not aged. The aging
     * time is specified in seconds. The default value is -1.
     * 
     * @param time the aging time
     */
    public void setAgingTime(int time);
}

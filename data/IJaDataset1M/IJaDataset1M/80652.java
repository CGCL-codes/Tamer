package redstone.prevalence;

import java.io.Serializable;

/**
 *  Represents a snapshot of the complete domain model. A snapshot,
 *  combined with zero or more transactions in a transaction log
 *  may be used to restore a domain model to its latest state.<p>
 *  
 *  Snapshots are basically used to make the restoration of the model
 *  go faster since less transactions need to be executed against
 *  it. Normally an application wants to take a snapshot before
 *  closing down or at regular intervals (once per day for instance)
 *  to have the application start quicker in case of a program crash.
 *  
 *  When making a snapshot, the transaction log is emptied since
 *  the snapshot contains the latest and the greatest of the model
 *  and needs no transactions to be able to restore it.
 *
 *  @author Greger Ohlson
 *  @version $Revision: 1.1.1.1 $
 */
public interface Snapshot<T extends Serializable> {

    /**
     *  Stores the supplied model somehow.
     * 
     *  @param root
     */
    void store(T root);

    /**
     *  Restores the model from a snapshot somehow.
     * 
     *  @return
     */
    T read();

    /**
     *  Removes the snapshot.
     */
    void clear();
}

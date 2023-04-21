package com.continuent.tungsten.replicator.event;

import java.io.Serializable;

/**
 * This class is the superclass from which all replication events inherit. It
 * defines minimal shared behavior. This is currently restricted to providing a
 * common serialization interface and estimated size to help with memory
 * management. Estimated size is a hint and does not have to be exact. It is
 * designed to help us tell whether the object in question needs a lot of heap
 * memory.
 * 
 * @author <a href="mailto:alexey.yurchenko@continuent.com">Alex Yurchenko</a>
 * @version 1.0
 */
public abstract class ReplEvent implements Serializable {

    private static final long serialVersionUID = 1300;

    private transient int estimatedSize;

    public ReplEvent() {
    }

    /**
     * Returns the estimated serialized size of this event, if known.
     */
    public int getEstimatedSize() {
        return estimatedSize;
    }

    /**
     * Sets the estimated serialized size of this event.
     */
    public void setEstimatedSize(int estimatedSize) {
        this.estimatedSize = estimatedSize;
    }
}

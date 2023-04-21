package de.fuh.xpairtise.common.replication;

import java.io.Serializable;

/**
 * This is the base class for all replicated objects. It defines methods for
 * unique identification of replicated object instances(elementId) and for
 * detecting sequential updates(sequenceId). It also implements the Serializable
 * interface, so that it is possible to pass instances via the serialization
 * mechanism.
 */
public abstract class ReplicatedObject implements Serializable {

    private int elementId;

    private long sequenceId;

    /**
   * Returns the element ID of this instance.
   * 
   * @return element ID
   */
    public int getElementId() {
        return elementId;
    }

    /**
   * Sets a new element ID.
   * 
   * @param elementId 
   *          ID to be set
   */
    public void setElementId(int elementId) {
        this.elementId = elementId;
    }

    /**
   * Returns the sequence ID of this instance.
   * 
   * @return sequence ID
   */
    public long getSequenceId() {
        return sequenceId;
    }

    /**
   * Sets a new sequence ID.
   * 
   * @param sequenceId 
   *          sequence ID to be set
   */
    public void setSequenceId(long sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Override
    public String toString() {
        return "elementId=" + getElementId() + ", sequenceId=" + getSequenceId() + ", class=" + getClass().toString();
    }
}

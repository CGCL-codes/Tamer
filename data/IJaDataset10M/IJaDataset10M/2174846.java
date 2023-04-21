package org.fgraph.event;

/**
 *  A special kind of observable graph event that indicates
 *  that a GraphObject's property has changed.
 *
 *  @version   $Revision: 569 $
 *  @author    Paul Speed
 */
public interface PropertyEvent extends GraphEvent {

    /**
     *  Returns the name of the property that has changed.
     */
    public String getPropertyName();

    /**
     *  Returns the value of the property before the change.
     */
    public Object getOldValue();

    /**
     *  Returns the value of the property after the change.
     */
    public Object getNewValue();
}

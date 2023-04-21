package net.sf.brightside.gymcalendar.core.beans;

import java.io.Serializable;

/**
 * Object that have an unique identifier.
 * 
 * @author Dragan Djuric <dragan@dragandjuric.com/>
 * @since Mar 2, 2005
 **/
public interface Identifiable {

    /**
     * Gets the element's unique identifier as int.
     * @return the identifier
     */
    public Integer getId();

    public void setId(Integer id);
}

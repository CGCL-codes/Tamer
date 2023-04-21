package de.nava.informa.core;

import java.util.Date;

/**
 * This interface is implemented by objects representing the 
 * source of a news item.
 *
 * @author Michael Harhen
 */
public interface ItemSourceIF extends WithNameMIF {

    public String getLocation();

    public void setLocation(String location);

    /**
   * @return The time was published by the original source
   */
    Date getTimestamp();

    void setTimestamp(Date timestamp);
}

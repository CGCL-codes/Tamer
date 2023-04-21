package ch.iserver.ace.collaboration;

import java.beans.PropertyChangeListener;

/**
 * A RemoteUser is a local representation of a remote user. It is a proxy
 * providing all the user related operations in one convenient place.
 * The Collaboration Layer makes sure that there is only one RemoteUser
 * object per unique user id. A PropertyChangeListener can be added
 * that gets notified whenever a property of this object changes.
 */
public interface RemoteUser {

    /**
	 * The name of the name property.
	 */
    String NAME_PROPERTY = "name";

    /**
	 * Gets the unique identifier of the user.
	 * 
	 * @return the unique identifier of the user
	 */
    String getId();

    /**
	 * Gets the name of the user.
	 * 
	 * @return the name of the user
	 */
    String getName();

    /**
	 * Registers a property change listener on this object.
	 * 
	 * @param listener the property change listener
	 */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
	 * Removes a property change listener from this object.
	 * 
	 * @param listener the listener
	 */
    void removePropertyChangeListener(PropertyChangeListener listener);
}

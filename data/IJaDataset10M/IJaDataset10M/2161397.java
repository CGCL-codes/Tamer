package org.freeworld.prilib.util;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 * <p>Just verifies that a property change listener is persistable when it is
 * required based on the implementation</p>
 * 
 * @author dchemko
 */
public interface PersistentPropertyChangeListener extends PropertyChangeListener, Serializable {
}

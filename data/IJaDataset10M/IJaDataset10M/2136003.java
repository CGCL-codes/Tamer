package com.iparelan.util.beans;

import com.iparelan.util.annotations.Copyright;

/**
 * An alternative to {@link java.beans.PropertyChangeSupport}.
 *
 * @author Greg Mattes
 * @version September 2008
 */
@Copyright("Copyright &copy; 2008, Iparelan Solutions, LLC. All rights reserved.")
public interface PropertyChangeSupport extends PropertyChangeable, PropertyFireable {
}

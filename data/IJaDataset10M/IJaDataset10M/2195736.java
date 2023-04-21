package com.trollworks.ttk.widgets;

/** Used for listening to changes in an object's modification state. */
public interface DataModifiedListener {

    /**
	 * Called whenever a monitored object's modification state changes.
	 * 
	 * @param obj The object that was modified.
	 * @param modified Whether the object is now modified or not.
	 */
    void dataModificationStateChanged(Object obj, boolean modified);
}

package org.marcont.services.definitions.process;

/**
 * Implementations of this listener may be registered with instances of org.marcont.services.definitions.process.Local to 
 * receive notification when properties changed, added or removed.
 * <br>
 */
public interface LocalListener extends com.ibm.adtech.jastor.ThingListener {

    /**
	 * Called when a value of parameterValue has been added
	 * @param source the affected instance of org.marcont.services.definitions.process.Local
	 * @param newValue the object representing the new value
	 */
    public void parameterValueAdded(org.marcont.services.definitions.process.Local source, java.lang.String newValue);

    /**
	 * Called when a value of parameterValue has been removed
	 * @param source the affected instance of org.marcont.services.definitions.process.Local
	 * @param oldValue the object representing the removed value
	 */
    public void parameterValueRemoved(org.marcont.services.definitions.process.Local source, java.lang.String oldValue);

    /**
	 * Called when parameterType has changed
	 * @param source the affected instance of org.marcont.services.definitions.process.Local
	 */
    public void parameterTypeChanged(org.marcont.services.definitions.process.Local source);
}

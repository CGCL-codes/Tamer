package org.marcont.services.definitions.process;

/**
 * Implementations of this listener may be registered with instances of org.marcont.services.definitions.process.ValueOf to 
 * receive notification when properties changed, added or removed.
 * <br>
 */
public interface ValueOfListener extends com.ibm.adtech.jastor.ThingListener {

    /**
	 * Called when fromProcess has changed
	 * @param source the affected instance of org.marcont.services.definitions.process.ValueOf
	 */
    public void fromProcessChanged(org.marcont.services.definitions.process.ValueOf source);

    /**
	 * Called when theVar has changed
	 * @param source the affected instance of org.marcont.services.definitions.process.ValueOf
	 */
    public void theVarChanged(org.marcont.services.definitions.process.ValueOf source);
}

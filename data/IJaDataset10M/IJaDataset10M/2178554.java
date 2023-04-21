package org.marcont.services.definitions.service;

/**
 * Implementations of this listener may be registered with instances of org.marcont.services.definitions.service.ServiceProfile to 
 * receive notification when properties changed, added or removed.
 * <br>
 */
public interface ServiceProfileListener extends com.ibm.adtech.jastor.ThingListener {

    /**
	 * Called when a value of presentedBy has been added
	 * @param source the affected instance of org.marcont.services.definitions.service.ServiceProfile
	 * @param newValue the object representing the new value
	 */
    public void presentedByAdded(org.marcont.services.definitions.service.ServiceProfile source, org.marcont.services.definitions.service.Service newValue);

    /**
	 * Called when a value of presentedBy has been removed
	 * @param source the affected instance of org.marcont.services.definitions.service.ServiceProfile
	 * @param oldValue the object representing the removed value
	 */
    public void presentedByRemoved(org.marcont.services.definitions.service.ServiceProfile source, org.marcont.services.definitions.service.Service oldValue);

    /**
	 * Called when a value of provides has been added
	 * @param source the affected instance of org.marcont.services.definitions.service.ServiceProfile
	 * @param newValue the object representing the new value
	 */
    public void providesAdded(org.marcont.services.definitions.service.ServiceProfile source, org.marcont.services.definitions.service.Service newValue);

    /**
	 * Called when a value of provides has been removed
	 * @param source the affected instance of org.marcont.services.definitions.service.ServiceProfile
	 * @param oldValue the object representing the removed value
	 */
    public void providesRemoved(org.marcont.services.definitions.service.ServiceProfile source, org.marcont.services.definitions.service.Service oldValue);
}

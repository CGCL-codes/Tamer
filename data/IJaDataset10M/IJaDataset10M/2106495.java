package com.bitmovers.maui.engine.cachemanager.events;

import java.util.EventObject;

/**
* Repositoryevent <p>
* Event Object class which describes a repository event
*
*/
public class RepositoryEvent extends A_CacheEvent {

    /**
	* Simple constructor
	*
	* @param aEventSource The event source
	* @param aEventCode The event code
	* @param aEventType The event type
	* @param aArbitrary The arbitrary Object associated with the event.
	*/
    public RepositoryEvent(Object aSource, int aEventCode, int aEventType, Object aArbitrary) {
        super(aSource, aEventCode, aEventType, aArbitrary);
    }
}

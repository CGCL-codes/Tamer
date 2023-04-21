package org.isistan.flabot.coremodel;

import org.eclipse.emf.ecore.EObject;

/**
 * Represents something that can be registered in a ResponsibilityRegistry
 * @author $Author: mblech $
 * @model interface="true"
 */
public interface Registrable extends EObject {

    /**
	 * This method is callede whenever this registrable has been registered
	 * @param registry
	 * @param role 
	 * @param component 
	 * @param responsibility 
	 */
    void registered(ResponsibilityRegistry registry, Responsibility responsibility, ComponentModel component, ComponentRole role);

    /**
	 * This method is called whenever this registrable has been unregistered
	 * @param registry
	 * @param role 
	 * @param component 
	 * @param responsibility 
	 */
    void unregistered(ResponsibilityRegistry registry, Responsibility responsibility, ComponentModel component, ComponentRole role);
}

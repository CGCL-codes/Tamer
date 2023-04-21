package com.fortuityframework.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;

/**
 * Component instantiation listener that scans Wicket components for Fortuity annotations, and sets up the WicketEventListenerLocator
 * to delegate events to them.
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class FortuityComponentInstantiationListener implements IComponentInstantiationListener {

    private WicketEventListenerLocator locator;

    /**
	 * Creates a new Fortuity instantiation listener that will listen for Wicket component instantiation, and
	 * scans the component for fortuity event responder methods
	 * @param locator The WicketEventListenerLocator to delegate components to 
	 */
    public FortuityComponentInstantiationListener(WicketEventListenerLocator locator) {
        this.locator = locator;
    }

    /**
	 * @see org.apache.wicket.application.IComponentInstantiationListener#onInstantiation(org.apache.wicket.Component)
	 */
    @Override
    public void onInstantiation(Component component) {
        this.locator.onComponentAdded(component);
    }
}

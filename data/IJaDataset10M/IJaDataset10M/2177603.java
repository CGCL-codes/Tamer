package org.ikasan.testharness.flow.expectation.model;

import org.ikasan.spec.component.endpoint.Consumer;

/**
 * Consumer Component type.
 * 
 * @author Ikasan Development Team
 * 
 */
public class ConsumerComponent extends AbstractComponent {

    /** 
     * Constructor
     * @param componentName
     */
    public ConsumerComponent(String componentName) {
        super(componentName, Consumer.class);
    }
}

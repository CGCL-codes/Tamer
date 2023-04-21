package edu.indiana.extreme.xbaya.component.registry;

import edu.indiana.extreme.xbaya.component.Component;
import java.util.Collections;
import java.util.List;

/**
 * @author Satoshi Shirasuna
 */
public class ComponentOperationReference extends ComponentReference {

    private Component component;

    private List<Component> components;

    /**
     * Constructs a BasicComponentReference.
     * 
     * @param name
     * @param component
     */
    public ComponentOperationReference(String name, Component component) {
        super(name);
        this.component = component;
        this.components = Collections.singletonList(component);
    }

    /**
     * @see edu.indiana.extreme.xbaya.component.registry.ComponentReference#getComponent()
     */
    @Override
    public Component getComponent() {
        return this.component;
    }

    /**
     * @see edu.indiana.extreme.xbaya.component.registry.ComponentReference#getComponents()
     */
    @Override
    public List<? extends Component> getComponents() {
        return this.components;
    }
}

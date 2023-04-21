package edu.luc.cs.trull;

import java.util.List;

/**
 * A building block for building complex components from existing components.
 */
public interface Combinator extends Component {

    /**
	 * Sets the child components of this combinator.
	 */
    void setComponents(List components);

    /**
	 * Adds a component to this combinator.
	 */
    void addComponent(Component comp);

    /**
	 * Adds a child component to this combinator 
	 * using the given constraints to determine the role of the component.
	 */
    void addComponent(Component comp, Object constraints);

    /**
	 * Retrieves a child component from this combinator.
	 */
    Component getComponent(int i);

    /**
	 * Retrieves all child components from this combinator as an array.
	 */
    List getComponents();

    /**
	 * Returns the number of child components currently in this
	 * combinator. 
	 */
    int getComponentCount();

    /**
	 * Removes the specified child component from this combinator.
	 */
    void removeComponent(Component comp);

    /**
	 * Removes all components from this combinator.
	 */
    void removeAllComponents();
}

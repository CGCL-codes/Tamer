package org.argouml.core.propertypanels.ui;

import java.util.List;
import javax.swing.JComponent;

/**
 * An interface to be implemented by all factories that create
 * components for property panels.
 * @author Bob Tarling
 */
interface ComponentFactory {

    /**
     * Create the Component for the given model element
     * @param modelElement a UML model element
     * @return a component with which the user can edit the model element
     * value or navigate through the property panels.
     */
    JComponent createComponent(final Object modelElement, final String propName, final List<Class<?>> types);
}

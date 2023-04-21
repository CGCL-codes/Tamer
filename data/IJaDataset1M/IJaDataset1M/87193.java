package org.sapient_platypus.utils.bindml;

import org.jdom.Element;

/**
 * @author Nicholas Daley
 *
 */
public interface ComponentTagHandler {

    /**
     * Gets a component from a JDOM element.
     * @param element The JDOM element.
     * @return The created component.
     */
    Object getComponent(Element element);
}

package org.genxdm.processor.w3c.xs.validation.impl;

import javax.xml.namespace.QName;
import org.genxdm.xs.components.ElementDefinition;
import org.genxdm.xs.components.SchemaWildcard;

/**
 * State-machine abstraction used for XML content model validation of a child axis.
 */
interface SmContentFiniteStateMachine {

    /**
	 * Steps the state machine upon completion of all elements.
	 * 
	 * @return <code>true</code> if no more elements are expected.
	 */
    boolean end();

    /**
	 * Returns an element declaration if an element is matched.
	 */
    ElementDefinition getElement();

    /**
	 * Returns a wildcard if a wildcard is matched.
	 */
    SchemaWildcard getWildcard();

    /**
	 * Determines whether the last step matched an element declaration.
	 */
    boolean isElementMatch();

    /**
	 * Determines whether the last step matched a wildcard.
	 */
    boolean isWildcardMatch();

    /**
	 * Steps the state machine upon arrival of an element with the specified name.
	 * 
	 * @param name
	 *            The name of the element.
	 * @return <code>true</code> if a transition exists for the element.
	 */
    boolean step(QName name);
}

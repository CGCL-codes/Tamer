package org.vesuf.model.uml.foundation.extensions;

import org.vesuf.model.uml.foundation.core.*;

/**
 *	Stereotype.
 */
public class Stereotype extends ModelElement implements IStereotype {

    /**
	 *	Create model element.
	 *	@param name	The name.
	 *	@param namespace	The namespace of the element.
	 */
    public Stereotype(String name, Namespace namespace) {
        super(name, namespace);
    }
}

package net.sourceforge.hlm.library.objects;

import net.sourceforge.hlm.library.*;
import net.sourceforge.hlm.library.parameters.*;

/**
 * A math object is a definition (construction, constructor,
 * predicate, operator) or theorem. It has a list of
 * parameters, which must be substituted when the object is
 * used.
 */
public abstract interface MathObject extends LibraryObject {

    /**
	 * Returns the object's parameter list.
	 */
    ParameterList getParameters();
}

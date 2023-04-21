package net.sourceforge.hlm.library.parameters;

import net.sourceforge.hlm.generic.exceptions.*;
import net.sourceforge.hlm.library.contexts.*;

/**
 * A parameter list defines the variables and constraints that
 * specify the domain of a definition, theorem, or quantifier.
 */
public interface ParameterList extends ContextList<Parameter> {

    /**
	 * Returns a parameter by its type and name.
	 * Returns null if no such parameter was found.
	 */
    <A extends Parameter> A find(Class<? extends A> type, String name, boolean includeInnerParameters) throws TypeMismatchException;

    /**
	 * Same as find, but throws ItemNotFoundException instead
	 * of returning null.
	 */
    <A extends Parameter> A get(Class<? extends A> type, String name, boolean includeInnerParameters) throws ItemNotFoundException, TypeMismatchException;

    /**
	 * Checks whether the given parameter is defined in
	 * this parameter list.
	 */
    boolean contains(Parameter parameter, boolean includeInnerParameters);

    /**
	 * Returns the binding or element parameter that this list
	 * is part of, if any.
	 */
    Parameter getParentParameter();
}

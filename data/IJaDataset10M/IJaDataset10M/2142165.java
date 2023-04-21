package net.sourceforge.hlm.library.parameters;

import net.sourceforge.hlm.generic.annotations.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.terms.set.*;

/**
 * A "binding" parameter introduces a bound variable, as well
 * as a list of parameters that depend on it.
 */
@SubId(SubId.ContextItem.BINDING)
public interface BindingParameter extends Parameter {

    /**
	 * An element parameter with the same name as the binding
	 * parameter, which can be referred to in the bound
	 * parameters.
	 */
    ElementParameter getElementParameter();

    /**
	 * Returns the list of parameters that depend on the
	 * bound variable.
	 */
    ParameterList getBoundParameters();

    /**
	 * Shortcut for getElementParameter().getSet().
	 */
    ContextPlaceholder<SetTerm> getSet();
}

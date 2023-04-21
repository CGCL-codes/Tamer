package net.sourceforge.hlm.library.parameters;

import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.proofs.*;

/**
 * A variable definition is not a real parameter, but simply
 * defines a variable as a shortcut for a given term. It does
 * not have any corresponding argument.
 *
 * It can also be used as a proof step.
 */
public abstract interface VariableDefinition<T> extends Parameter, ProofStep {

    /**
	 * Returns a placeholder for the term that defines the
	 * variable.
	 */
    ContextPlaceholder<T> getTerm();
}

package net.sourceforge.hlm.library.proofs;

import net.sourceforge.hlm.generic.*;
import net.sourceforge.hlm.generic.annotations.*;

/**
 * This interface represents a proof step that establishes a
 * custom formula, defined by the explicit result of the step.
 */
@SubId(SubId.ContextItem.STATE_FORMULA)
public interface StateFormulaStep extends IndependentIntermediateStep {

    /**
	 * Returns a placeholder for a proof of the custom
	 * formula.
	 */
    FixedPlaceholder<Proof> getSubProof();
}

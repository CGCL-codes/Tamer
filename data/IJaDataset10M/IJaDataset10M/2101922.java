package net.sourceforge.hlm.library.proofs;

import net.sourceforge.hlm.generic.annotations.*;

/**
 * This interface represents a proof step that resolves
 * variable definitions or explicit definitions of operators
 * contained in the previous result.
 */
@SubId(SubId.ContextItem.RESOLVE)
public interface ResolveStep extends DependentIntermediateStep {
}

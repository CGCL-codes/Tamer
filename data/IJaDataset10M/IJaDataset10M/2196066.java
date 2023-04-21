package net.sourceforge.hlm.library.proofs;

import net.sourceforge.hlm.generic.annotations.*;

/**
 * This interface represents a goal-specific proof step that
 * proves the definition of the formula or term involved. The
 * exact meaning of "definition" depends on the formula; it
 * can also be defined by the system (e.g. the definition of
 * a "subset" formula).
 */
@SubId(SubId.ContextItem.PROVE_DEFINITION)
public interface ProveDefinitionStep extends ProveSingleStep, DefinitionStep {
}

package net.sourceforge.hlm.library.objects.constructions;

import net.sourceforge.hlm.generic.*;
import net.sourceforge.hlm.generic.annotations.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.parameters.*;
import net.sourceforge.hlm.library.proofs.*;
import net.sourceforge.hlm.library.terms.element.*;

/**
 * A structural case list assigns a formula or term to each
 * constructor of a construction, which formalizes structural
 * induction. 
 */
public interface StructuralCaseList<T> extends FixedMap<Constructor, StructuralCaseList.Case<T>> {

    /**
	 * Returns the content type of the case list
	 * (Formula.class, ElementTerm.class, or
	 * SetTerm.class).
	 */
    Class<? extends T> getContentType();

    /**
	 * Returns a reference to the construction that is
	 * used. Changing this construction clears all cases.
	 */
    Reference<Construction> getConstruction();

    /**
	 * Returns a placeholder that can be used to set the
	 * term used in the induction. It must be an element
	 * of the construction, but this is not enforced
	 * here, as it is a type issue.
	 */
    ContextPlaceholder<ElementTerm> getSample();

    /**
	 * A structural case is an item in a structural case
	 * list. Depending on the use of the list, it defines a
	 * formula or term for a specific constructor.
	 */
    @Id(Id.STRUCTURAL_CASE)
    interface Case<T> {

        /**
		 * Returns a modifiable parameter list, which
		 * must be set up to match the constructor
		 * parameters (except for names).
		 */
        ParameterList getParameters();

        /**
		 * Specifies whether the case should use the
		 * rewrite rule of the constructor.
		 */
        void setRewrite(boolean rewrite);

        /**
		 * Returns the value set by setRewrite.
		 */
        boolean getRewrite();

        /**
		 * Returns a placeholder for the formula or
		 * term of this case.
		 */
        ContextPlaceholder<T> getContents();

        /**
		 * Returns a placeholder for a proof that the
		 * case is well-defined, i.e. that for two
		 * different sets of arguments defining equal
		 * constructor terms (according to the custom
		 * equality definition), the result is again
		 * equal (or equivalent, respectively).
		 */
        FixedPlaceholder<Proof> getWellDefinednessProof();
    }
}

package net.sourceforge.hlm.library.terms.set;

import net.sourceforge.hlm.generic.annotations.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.terms.element.*;

/**
 * An enumeration term represents a set with a finite number
 * of explicitly specified elements.
 */
@SubId(SubId.SetTerm.ENUMERATION)
public interface EnumerationTerm extends SetTerm {

    /**
	 * Returns a list of element terms representing the
	 * members of the set.
	 */
    ContextList<ElementTerm> getTerms();
}

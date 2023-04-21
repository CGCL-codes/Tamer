package de.tudresden.inf.lat.jcel.core.normalization;

import java.util.Set;
import de.tudresden.inf.lat.jcel.core.datatype.IntegerAxiom;

/**
 * This interface is implemented by normalization rules. A normalization rule
 * transforms a non-normalized axiom into one or more normalized axioms.
 * 
 * @author Julian Mendez
 */
public interface NormalizationRule {

    /**
	 * Applies the normalization rule to a specified axiom.
	 * 
	 * @param axiom
	 *            axiom to be processed
	 * @return the set of axioms after the application of the normalization rule
	 */
    public Set<IntegerAxiom> apply(IntegerAxiom axiom);
}

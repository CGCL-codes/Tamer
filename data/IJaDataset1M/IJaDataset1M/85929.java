package org.opt4j.operator.algebra;

import org.opt4j.core.Genotype;
import org.opt4j.core.optimizer.Operator;
import com.google.inject.ImplementedBy;

/**
 * <p>
 * The {@link Algebra} operator. By default this class is implemented by the
 * {@link AlgebraDouble}.
 * </p>
 * <p>
 * Since the operator method is accepting {@code varargs} it cannot be
 * parameterized for each genotype.
 * </p>
 * 
 * @author lukasiewycz
 * 
 * @param <G>
 *            the type of genotype
 */
@ImplementedBy(AlgebraGenericImplementation.class)
public interface Algebra<G extends Genotype> extends Operator<G> {

    /**
	 * Performs the {@link Term} for each element of the given {@link Genotype}s
	 * and returns the resulting {@link Genotype}.
	 * 
	 * @param term
	 *            the term
	 * @param genotypes
	 *            the genotypes
	 * @return the resulting genotype
	 */
    public G algebra(Term term, Genotype... genotypes);
}

package org.opt4j.core.problem;

import org.opt4j.core.Genotype;
import org.opt4j.core.Phenotype;

/**
 * The {@link Decoder} decodes {@link Genotype}s into {@link Phenotype}s.
 * 
 * @author glass, lukasiewycz
 * @see Genotype
 * @see Phenotype
 * @param <G>
 *            the type of genotype that is decoded
 * @param <P>
 *            the type of the resulting phenotype}
 */
public interface Decoder<G extends Genotype, P extends Phenotype> {

    /**
	 * Decodes a given {@link Genotype} to the corresponding {@link Phenotype}.
	 * 
	 * @param genotype
	 *            the genotype to decode
	 * @return the decoded phenotype
	 */
    public P decode(G genotype);
}

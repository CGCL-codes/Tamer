package org.opt4j.ea;

import java.util.Collection;
import org.opt4j.core.Individual;

/**
 * The {@code Mating} creates offspring from a given set of parents.
 * 
 * @author glass
 * 
 */
public interface Mating {

    /**
	 * Returns the offspring produced by the given set of parents.
	 * 
	 * @param size
	 *            the number of offspring
	 * @param parents
	 *            the parents
	 * @return the offspring produced by the given set of parents
	 */
    public Collection<Individual> getOffspring(int size, Collection<Individual> parents);
}

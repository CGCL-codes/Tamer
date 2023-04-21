package org.jgap;

import java.io.*;
import org.jgap.util.ICloneable;

/**
 * Interface for GA breeders. A breeder evolves a population by performing
 * genetic operations.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IBreeder extends ICloneable, Serializable, Comparable {

    /** String containing the CVS revision. Read out via reflection!*/
    static final String CVS_REVISION = "$Revision: 1.5 $";

    /**
   * Evolves the population of Chromosomes within a Genotype. This will
   * execute all of the genetic operators added to the present active
   * configuration and then invoke the natural selector to choose which
   * chromosomes will be included in the next generation population.
   *
   * @param a_pop the population to evolve
   * @param a_conf the configuration to use for evolution
   *
   * @author Klaus Meffert
   * @since 3.2
   */
    Population evolve(Population a_pop, Configuration a_conf);
}

package org.jgap.gp;

import java.io.*;
import org.jgap.gp.impl.*;

/**
 * Interface for algorithms selecting individuals for evolutionary operations.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public interface INaturalGPSelector extends Serializable {

    /** String containing the CVS revision. Read out via reflection!*/
    static final String CVS_REVISION = "$Revision: 1.6 $";

    /**
   * Select an individual based on an arbitrary algorithm.
   *
   * @param a_genotype the genotype used
   * @return the individual chosen from the genotype's population
   *
   * @author Klaus Meffert
   * @since 3.0
   */
    IGPProgram select(GPGenotype a_genotype);
}

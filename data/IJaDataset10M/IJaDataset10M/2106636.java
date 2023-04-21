package org.ensembl.variation.datamodel;

import org.ensembl.datamodel.Feature;
import org.ensembl.driver.AdaptorException;

/**
 * An allele located on a genome.
 *
 * @author <a href="mailto:craig@ebi.ac.uk">Craig Melsopp</a>
 */
public interface AlleleFeature extends Feature {

    /**
   * Return the allele string.
   * @return allele as a string.
   */
    String getAlleleString();

    /**
   * Variation this allele belongs to.
   * @return internal ID of the variation this allele belongs to.
   */
    long getVariationInternalID();

    /**
   * Variation this allele belongs to.
   * @return variation this allele belongs to.
   */
    Variation getVariation() throws AdaptorException;
}

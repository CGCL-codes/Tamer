package org.ensembl.variation.driver;

import java.util.List;
import org.ensembl.driver.Adaptor;
import org.ensembl.driver.AdaptorException;
import org.ensembl.variation.datamodel.AlleleGroup;
import org.ensembl.variation.datamodel.VariationGroup;

/**
 * Retrieves Allele groups from an ensembl database.
 * 
 * @author <a href="mailto:craig@ebi.ac.uk">Craig Melsopp</a>
 *
 */
public interface AlleleGroupAdaptor extends Adaptor {

    static final String TYPE = "allele_group";

    /**
   * Fetch Allele group with specified internal ID.
   * @return specified allele group or null if none found.
   */
    AlleleGroup fetch(long internalID) throws AdaptorException;

    /**
   * Fetch Allele group with specified name.
   * @return specified allele group or null if none found.
   */
    AlleleGroup fetch(String name) throws AdaptorException;

    /**
   * Fetch all allele groups that are part of the Variation group.
   * @return zero or more AlleleGroups.
   */
    List fetch(VariationGroup variationGroup) throws AdaptorException;
}

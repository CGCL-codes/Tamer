package gov.usda.gdpc;

import java.util.List;

/**
 * Collection of genotype experiments.
 *
 * @author  terryc
 */
public interface GenotypeExperimentGroup extends DBElementGroup {

    /**
     * Get the genotype experiment group matching the given filter from
     * this genotype experiment group.
     *
     * @param filter filter
     *
     * @return genotype experiment group
     */
    public GenotypeExperimentGroup getGenotypeExperimentGroup(GenotypeExperimentFilter filter);

    /**
     * This returns the genotype experiment instance from this group that matches the 
     * specified id and source. Null will be returned if its not in
     * this group.
     *
     * @param dataSource data source
     * @param id identifier
     *
     * @return genotype experiment or null if not in this group.
     */
    public GenotypeExperiment getGenotypeExperiment(String dataSource, Identifier id);

    /**
     * Returns list of unique loci associated with
     * genotype experiments in this group.
     *
     * @return list of unique loci
     */
    public List getLoci();
}

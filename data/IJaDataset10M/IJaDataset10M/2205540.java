package net.boogie.calamari.genetic.model.impl;

import net.boogie.calamari.domain.exception.ExceptionUtils;
import net.boogie.calamari.genetic.model.IGenomeFactory;
import net.boogie.calamari.genetic.model.IPopulation;
import net.boogie.calamari.genetic.model.IPopulationFactory;
import org.apache.log4j.Logger;

public class SimplePopulationFactory implements IPopulationFactory {

    private static final long serialVersionUID = 1L;

    private static Logger s_log = Logger.getLogger(SimplePopulationFactory.class);

    /**
     * @return the genomeFactory
     */
    @Override
    public IGenomeFactory getGenomeFactory() {
        return _genomeFactory;
    }

    private IGenomeFactory _genomeFactory;

    private int _minimumGenomesPerPopulation;

    private int _maximumGenomesPerPopulation;

    public SimplePopulationFactory(int minimumGenomesPerPopulation, int maximumGenomesPerPopulation, IGenomeFactory genomeFactory) {
        ;
        ExceptionUtils.throwIfNull(genomeFactory, "genomeFactory");
        _genomeFactory = genomeFactory;
        _minimumGenomesPerPopulation = minimumGenomesPerPopulation;
        _maximumGenomesPerPopulation = maximumGenomesPerPopulation;
    }

    @Override
    public IPopulation newItem() {
        s_log.debug("Constructing new SimplePopulation...");
        s_log.debug("   minimumGenomesPerPopulation = " + _minimumGenomesPerPopulation);
        s_log.debug("   maximumGenomesPerPopulation = " + _maximumGenomesPerPopulation);
        IPopulation population = new SimplePopulation(_minimumGenomesPerPopulation, _maximumGenomesPerPopulation, _genomeFactory);
        s_log.debug("Finished constructing new SimplePopulation.");
        return population;
    }
}

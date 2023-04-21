package playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.selectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.jgap.Configuration;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.NaturalSelectorExt;
import org.jgap.Population;
import playground.thibautd.jointtrips.config.JointReplanningConfigGroup;
import playground.thibautd.jointtrips.replanning.modules.jointplanoptimizer.configuration.JointPlanOptimizerJGAPConfiguration;

/**
 * Selector using a "restricted tournament" (cf Harik 1995) to generate the new 
 * population.
 * The idea is the following:
 * <ul>
 *  <li>for each "new born", <i>w</i> chromosomes from the population are selected
 *   randomly.
 *  <li>the "closest" chromosome of this set is selected for competition
 *  <li>if the new born is fittest than the chromosome competitor, it replaces it
 *   (and becomes thus part of the possible competitors for not yet added new
 *   born)
 * </ul>
 *
 * @author thibautd
 */
public class RestrictedTournamentSelector extends NaturalSelectorExt {

    private static final long serialVersionUID = 1L;

    private final List<IChromosome> newBorned = new ArrayList<IChromosome>();

    protected final List<IChromosome> agedIndividuals = new ArrayList<IChromosome>();

    private final int windowSize;

    private final Configuration jgapConfig;

    private final ChromosomeDistanceComparator distanceComparator;

    private final Random random;

    /**
	 * @param jgapConfig the config used in the optimisation. The sample Chomosome
	 * and the population size MUST be initialized.
	 */
    public RestrictedTournamentSelector(final Configuration jgapConfig, final JointReplanningConfigGroup configGroup, final ChromosomeDistanceComparator distanceComparator) throws InvalidConfigurationException {
        super(jgapConfig);
        int paramWindowSize = (int) Math.ceil(configGroup.getWindowSizeIntercept() + configGroup.getWindowSizeCoef() * jgapConfig.getPopulationSize());
        paramWindowSize = Math.min(paramWindowSize, jgapConfig.getPopulationSize());
        this.windowSize = Math.max(1, paramWindowSize);
        this.jgapConfig = jgapConfig;
        this.random = new Random(jgapConfig.getRandomGenerator().nextLong() + 194534);
        this.distanceComparator = distanceComparator;
    }

    @Override
    public void empty() {
        this.newBorned.clear();
        this.agedIndividuals.clear();
    }

    @Override
    public boolean returnsUniqueChromosomes() {
        return true;
    }

    @Override
    protected void add(final IChromosome chromosome) {
        if (chromosome.getAge() == 0) {
            this.newBorned.add(chromosome);
        } else {
            this.agedIndividuals.add(chromosome);
        }
    }

    /**
	 * Selects the chromosomes for the next generation.
	 */
    @Override
    protected void selectChromosomes(final int nToSelect, final Population nextGeneration) {
        List<IChromosome> window;
        IChromosome closestOldCompetitor;
        for (IChromosome competitor : this.newBorned) {
            window = getWindow();
            this.distanceComparator.setComparisonData(competitor, window);
            closestOldCompetitor = Collections.min(window, this.distanceComparator);
            if (competitor.getFitnessValue() > closestOldCompetitor.getFitnessValue()) {
                this.agedIndividuals.add(competitor);
                this.agedIndividuals.remove(closestOldCompetitor);
            }
        }
        for (IChromosome chrom : this.agedIndividuals) {
            nextGeneration.addChromosome(chrom);
        }
        if (nextGeneration.size() != nToSelect) {
            throw new IllegalArgumentException("RTS must be used to generate the" + " full population: toSelect=" + nToSelect + ", generationSize=" + nextGeneration.size());
        }
    }

    private List<IChromosome> getWindow() {
        Collections.shuffle(agedIndividuals, random);
        return this.agedIndividuals.subList(0, windowSize);
    }
}

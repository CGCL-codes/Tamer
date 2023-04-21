package examples.grid.evolutionDistributed;

import org.jgap.distr.grid.*;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.event.*;
import org.homedns.dade.jcgrid.client.*;

/**
 * Main configuration for defining the problem and the way it is solved in the
 * grid. Thus, the most important class in a JGAP Grid!
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class GridConfiguration extends GridConfigurationBase {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.2 $";

    public GridConfiguration() {
        super();
    }

    public void initialize(GridNodeClientConfig a_gridconfig) throws Exception {
        if (a_gridconfig != null) {
            a_gridconfig.setSessionName("JGAP_evolution_distributed");
        }
        Configuration jgapconfig = new DefaultConfiguration();
        jgapconfig.setEventManager(new EventManager());
        jgapconfig.setPopulationSize(500);
        jgapconfig.setKeepPopulationSizeConstant(true);
        jgapconfig.setFitnessFunction(new SampleFitnessFunction());
        IChromosome sample = new Chromosome(jgapconfig, new BooleanGene(jgapconfig), 16);
        jgapconfig.setSampleChromosome(sample);
        setWorkerReturnStrategy(new MyWorkerReturnStrategy());
        setGenotypeInitializer(new MyGenotypeInitializer());
        setWorkerEvolveStrategy(new MyEvolveStrategy());
        setRequestSplitStrategy(new MyRequestSplitStrategy(jgapconfig));
        setConfiguration(jgapconfig);
        setClientEvolveStrategy(new ClientEvolveStrategy());
    }

    public void validate() throws Exception {
        if (getRequestSplitStrategy() == null) {
            throw new RuntimeException("Please set the request split strategy first!");
        }
        if (getConfiguration() == null) {
            throw new RuntimeException("Please set the configuration first!");
        }
    }
}

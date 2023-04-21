package preprocessing.automatic.Mutation;

import game.trainers.gradient.DifferentialEvolution.Individual;
import game.utils.Exceptions.InvalidArgument;
import preprocessing.automatic.links.ClassificatorLink;
import preprocessing.automatic.Configuration.GeneticConfig;
import preprocessing.automatic.Population.APAIndividual;
import preprocessing.automatic.Population.DataSplit;
import preprocessing.automatic.Mutation.DifferentialEvolution.DifferentialEvolutionController;
import preprocessing.storage.PreprocessingStorage;

/**
 * Created by IntelliJ IDEA.
 * User: lagon
 * Date: Nov 4, 2009
 * Time: 9:05:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class DifferentialEvolutionConfigMutation implements ConfigurationMutation {

    @Override
    public void mutateConfiguration(APAIndividual individualToMutate, DataSplit<PreprocessingStorage> dataSplit, ClassificatorLink link, GeneticConfig config) throws InvalidArgument {
        DifferentialEvolutionController ctrl = new DifferentialEvolutionController(individualToMutate, config, dataSplit, link);
        ctrl.initiateGeneticAlgorithm();
        ctrl.runAllGenerations();
        ctrl.fillInBestIndividual();
    }
}

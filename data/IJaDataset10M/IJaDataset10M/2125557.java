package preprocessing.automatic.Mutation;

import preprocessing.automatic.Population.APAIndividual;
import preprocessing.automatic.Population.DataSplit;
import preprocessing.automatic.links.ClassificatorLink;
import preprocessing.automatic.Configuration.GeneticConfig;
import preprocessing.automatic.Mutation.QuasiNewton.QuasiNewtonConfigOptimization;
import preprocessing.storage.PreprocessingStorage;
import game.utils.Exceptions.InvalidArgument;

/**
 * Created by IntelliJ IDEA.
 * User: lagon
 * Date: 08-Dec-2009
 * Time: 11:49:18
 * To change this template use File | Settings | File Templates.
 */
public class QuasiNewtonMutation implements ConfigurationMutation {

    private QuasiNewtonConfigOptimization qnco;

    @Override
    public void mutateConfiguration(APAIndividual individualToMutate, DataSplit<PreprocessingStorage> dataSplit, ClassificatorLink link, GeneticConfig config) throws InvalidArgument {
        qnco = new QuasiNewtonConfigOptimization(individualToMutate, config, dataSplit);
        qnco.setup();
        qnco.run();
    }

    public double[] getProgressData() {
        Double[] progress = qnco.getOptimizationProgress();
        double[] pr = new double[progress.length];
        for (int i = 0; i < progress.length; i++) {
            pr[i] = progress[i];
        }
        return pr;
    }

    public double[][] getOptimizationValuesData() {
        return qnco.getOptimizationValues();
    }
}

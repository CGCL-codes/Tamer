package tutorials.featureselection;

import java.io.File;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.featureselection.ensemble.LinearRankingEnsemble;
import net.sf.javaml.featureselection.ranking.RecursiveFeatureEliminationSVM;
import net.sf.javaml.tools.data.FileHandler;

/**
 * Tutorial to illustrate ensemble feature selection.
 * 
 * @author Thomas Abeel
 * 
 */
public class TutorialEnsembleFeatureSelection {

    /**
     * Shows the basic steps to use ensemble feature selection
     * 
     * @author Thomas Abeel
     * 
     */
    public static void main(String[] args) throws Exception {
        Dataset data = FileHandler.loadDataset(new File("devtools/data/iris.data"), 4, ",");
        RecursiveFeatureEliminationSVM[] svmrfes = new RecursiveFeatureEliminationSVM[10];
        for (int i = 0; i < svmrfes.length; i++) svmrfes[i] = new RecursiveFeatureEliminationSVM(0.2);
        LinearRankingEnsemble ensemble = new LinearRankingEnsemble(svmrfes);
        ensemble.build(data);
        for (int i = 0; i < ensemble.noAttributes(); i++) System.out.println(ensemble.rank(i));
    }
}

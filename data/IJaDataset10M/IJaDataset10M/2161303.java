package iclab.optimization.eda.reduction;

import iclab.core.ICData;
import iclab.exceptions.ICParameterException;
import iclab.optimization.eda.problem.ICProblem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ICBestElitismReduction implements ICReduction {

    double _percentage;

    public ICBestElitismReduction(double percentage) {
        if (percentage <= 0.0 || percentage >= 1.0) {
            _percentage = 0.5;
        } else {
            _percentage = percentage;
        }
    }

    public void reduce(ICData population, ArrayList<double[]> eval, ArrayList<Integer> selPopulation, ICData newPopulation, ArrayList<double[]> newEval, ICProblem problem) {
        try {
            int selPopSize;
            int i;
            selPopSize = selPopulation.size();
            newPopulation.addInstances(population.getInstances(selPopulation));
            for (i = 0; i < selPopSize; i++) {
                newEval.add(eval.get(selPopulation.get(i)));
            }
            eval.clear();
            eval.trimToSize();
            eval.addAll(newEval);
            Collections.sort(eval, problem);
            return;
        } catch (ICParameterException ex) {
            Logger.getLogger(ICBestElitismReduction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

package iclab.optimization.eda.selection;

import iclab.core.ICData;
import iclab.exceptions.ICInvalidSelectionRate;
import java.util.ArrayList;

public class ICSimpleSelection implements ICSelectionMethod {

    double _percentage;

    public ICSimpleSelection(double percentage) throws ICInvalidSelectionRate {
        if (percentage <= 0.0 || percentage > 1.0) {
            throw new ICInvalidSelectionRate("The selected percentage is invalid");
        }
        _percentage = percentage;
    }

    public ArrayList<Integer> select(ICData pop, ArrayList<double[]> eval) {
        ArrayList<Integer> finalPop;
        int nSelIndividuals;
        int i;
        finalPop = new ArrayList<Integer>();
        nSelIndividuals = (int) (pop.numInstances() * _percentage);
        for (i = 0; i < nSelIndividuals; i++) {
            double idx = eval.get(i)[0];
            finalPop.add(new Integer((int) idx));
        }
        return finalPop;
    }
}

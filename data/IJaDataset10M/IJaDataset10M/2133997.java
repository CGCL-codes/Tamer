package net.sf.myra.datamining.measure;

import java.util.Arrays;
import net.sf.myra.datamining.Measure;
import net.sf.myra.datamining.Model;
import net.sf.myra.datamining.Unit;
import net.sf.myra.datamining.data.Dataset;
import net.sf.myra.datamining.data.Instance;
import net.sf.myra.datamining.data.Label;
import net.sf.myra.datamining.model.ProbabilisticRuleModel;
import net.sf.myra.datamining.statistics.Curve;
import net.sf.myra.datamining.statistics.CurveFactory;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 2155 $ $Date:: 2009-06-14 20:22:03#$
 */
public class AveragedPRCurveMeasure implements Measure {

    public Unit<?> evaluate(Dataset dataset, Model model) {
        return evaluate(dataset, (ProbabilisticRuleModel) model);
    }

    public Unit<Double> evaluate(Dataset dataset, ProbabilisticRuleModel model) {
        Label label = dataset.getMetadata().getLabel();
        String root = dataset.getMetadata().getClassHierarchy().getRoot().getLabel();
        boolean[] enabled = new boolean[label.length()];
        Arrays.fill(enabled, true);
        int count = 0;
        for (int i = 0; i < enabled.length; i++) {
            if (root.equals(label.names()[i]) || dataset.getInstances(label.names()[i]).size() == 0) {
                enabled[i] = false;
            } else {
                count++;
            }
        }
        double[][] values = new double[dataset.getSize()][2];
        double area = 0.0;
        for (int i = 0; i < enabled.length; i++) {
            if (enabled[i]) {
                int index = 0;
                int positives = 0;
                for (Instance instance : dataset) {
                    boolean[] original = instance.getLabel().flags();
                    double[] probabilities = model.probabilities(instance);
                    values[index][0] = probabilities[i];
                    values[index][1] = (original[i] ? 1 : 0);
                    positives += values[index][1];
                    index++;
                }
                Curve curve = CurveFactory.PRECISION_RECALL.create(values);
                area += curve.area();
            }
        }
        return new Unit<Double>(area / count);
    }
}

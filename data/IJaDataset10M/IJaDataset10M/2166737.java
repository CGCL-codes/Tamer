package moa.evaluation;

import java.util.ArrayList;
import moa.cluster.Clustering;
import moa.gui.visualization.DataPoint;

/**
 *
 * @author jansen
 */
public class F1 extends MeasureCollection {

    @Override
    protected String[] getNames() {
        String[] names = { "F1-P", "F1-R", "Purity" };
        return names;
    }

    public void evaluateClustering(Clustering clustering, Clustering trueClustering, ArrayList<DataPoint> points) {
        if (clustering.size() < 0) {
            addValue(0, 0);
            addValue(1, 0);
            return;
        }
        MembershipMatrix mm = new MembershipMatrix(clustering, points);
        int numClasses = mm.getNumClasses();
        if (mm.hasNoiseClass()) numClasses--;
        double F1_P = 0.0;
        double purity = 0;
        int realClusters = 0;
        for (int i = 0; i < clustering.size(); i++) {
            int max_weight = 0;
            int max_weight_index = -1;
            for (int j = 0; j < numClasses; j++) {
                if (mm.getClusterClassWeight(i, j) > max_weight) {
                    max_weight = mm.getClusterClassWeight(i, j);
                    max_weight_index = j;
                }
            }
            if (max_weight_index != -1) {
                realClusters++;
                double precision = mm.getClusterClassWeight(i, max_weight_index) / (double) mm.getClusterSum(i);
                double recall = mm.getClusterClassWeight(i, max_weight_index) / (double) mm.getClassSum(max_weight_index);
                double f1 = 0;
                if (precision > 0 || recall > 0) {
                    f1 = 2 * precision * recall / (precision + recall);
                }
                F1_P += f1;
                purity += precision;
                clustering.get(i).setMeasureValue("F1-P", Double.toString(f1));
            }
        }
        if (realClusters > 0) {
            F1_P /= realClusters;
            purity /= realClusters;
        }
        addValue("F1-P", F1_P);
        addValue("Purity", purity);
        double F1_R = 0.0;
        for (int j = 0; j < numClasses; j++) {
            double max_f1 = 0;
            for (int i = 0; i < clustering.size(); i++) {
                double precision = mm.getClusterClassWeight(i, j) / (double) mm.getClusterSum(i);
                double recall = mm.getClusterClassWeight(i, j) / (double) mm.getClassSum(j);
                double f1 = 0;
                if (precision > 0 || recall > 0) {
                    f1 = 2 * precision * recall / (precision + recall);
                }
                if (max_f1 < f1) {
                    max_f1 = f1;
                }
            }
            F1_R += max_f1;
        }
        F1_R /= numClasses;
        addValue("F1-R", F1_R);
    }
}

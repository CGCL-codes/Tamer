package edu.colorado.emml.ensemble.ensembleselection;

import edu.colorado.emml.ensemble.IClassifier;
import edu.colorado.emml.ensemble.LabeledDataSet;
import edu.colorado.emml.ensemble.ModelSorter;
import edu.colorado.emml.ensemble.WeightedEnsemble;
import edu.colorado.emml.ensemble.metrics.Accuracy;
import edu.colorado.emml.ensemble.metrics.IMetric;

public interface IEnsembleSelectionInitialization {

    WeightedEnsemble initEnsembleSelectionModel(IClassifier[] models, LabeledDataSet trainSet);

    public static class EmptyEnsembleInitialization implements IEnsembleSelectionInitialization {

        public WeightedEnsemble initEnsembleSelectionModel(IClassifier[] models, LabeledDataSet trainSet) {
            return new WeightedEnsemble();
        }

        public String toString() {
            return "empty";
        }
    }

    public static class BestNEnsembleInitialization implements IEnsembleSelectionInitialization {

        private IMetric metric;

        private int n;

        public BestNEnsembleInitialization(int n) {
            this(n, new Accuracy());
        }

        public BestNEnsembleInitialization(int n, IMetric metric) {
            this.n = n;
            this.metric = metric;
        }

        public String toString() {
            return "best-" + n + "-on-" + metric;
        }

        public WeightedEnsemble initEnsembleSelectionModel(IClassifier[] models, LabeledDataSet trainSet) {
            WeightedEnsemble weightedEnsemble = new WeightedEnsemble();
            ModelSorter sortByAccuracy = new ModelSorter(metric);
            IClassifier[] sorted = sortByAccuracy.sort(models, trainSet);
            for (int i = 0; i < n; i++) {
                weightedEnsemble.add(sorted[sorted.length - 1 - i], 1.0);
            }
            return weightedEnsemble;
        }
    }
}

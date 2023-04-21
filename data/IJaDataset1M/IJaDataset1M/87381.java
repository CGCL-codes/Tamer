package uima.taes.interestingness.classifiers;

import org.apache.uima.jcas.JCas;

public class IncreasingWeightTix extends Tix {

    public IncreasingWeightTix(String category, int featureSize) {
        super(category, featureSize);
    }

    protected double computeWeight(JCas jcas) {
        return currentWeight;
    }
}

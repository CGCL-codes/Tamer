package uima.taes.interestingness.classifiers;

import uima.types.NewsItem;
import org.apache.uima.jcas.JCas;

public abstract class UIMAMTTBiasedClassifier extends UIMAClassifier {

    public UIMAMTTBiasedClassifier() {
        super();
    }

    public UIMAMTTBiasedClassifier(String category, int featureSize) {
        super(category, featureSize);
    }

    public double evaluate(NewsItem newsItem, JCas jcas) {
        double[] featureVector = this.getFeatureVector(jcas);
        int MTTIndex = (Integer) this.featuresMap.get(this.MTTfeatureID);
        double mttScore = featureVector[MTTIndex];
        double score = learn(featureVector, isInteresting(jcas), jcas);
        return mttScore * score;
    }
}

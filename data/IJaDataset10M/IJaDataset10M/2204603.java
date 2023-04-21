package uima.taes.interestingness.classifiers;

public class TopicDrivenNaiveBayesUpdateable2 extends UIMATopicDrivenClassifier2 {

    public TopicDrivenNaiveBayesUpdateable2() {
        super();
    }

    public TopicDrivenNaiveBayesUpdateable2(String category, int featureSize, int categoryID) {
        super(category, featureSize, categoryID);
    }

    protected void instantiateClassifier() {
        weka.classifiers.bayes.NaiveBayesUpdateable bayes = new weka.classifiers.bayes.NaiveBayesUpdateable();
        bayes.setUseKernelEstimator(true);
        classifier = bayes;
    }

    @Override
    protected UIMAClassifier buildUIMAClassifier() {
        UIMAClassifier classifier = new NaiveBayesUpdateable(category, featureSize);
        classifier.buildClassifier();
        return classifier;
    }
}

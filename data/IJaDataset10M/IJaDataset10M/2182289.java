package uima.taes.interestingness.classifiers;

import org.apache.uima.analysis_engine.annotator.AnnotatorContext;
import org.apache.uima.analysis_engine.annotator.AnnotatorContextException;
import weka.classifier.PassThroughClassifier;

public class PassThrough extends UIMAClassifier {

    public PassThrough() {
        super();
    }

    public PassThrough(String category, int featureSize) {
        super(category, featureSize);
    }

    protected void instantiateClassifier() {
        classifier = new PassThroughClassifier(0);
    }

    protected static String CLASSIFIER_ID = "classifierID";

    protected void getParameters(AnnotatorContext aContext) {
        super.getParameters(aContext);
        try {
            if (aContext.getConfigParameterValue(CLASSIFIER_ID) != null) {
                classifierID = ((Integer) aContext.getConfigParameterValue(CLASSIFIER_ID)).intValue();
            }
        } catch (AnnotatorContextException e) {
            e.printStackTrace();
        }
    }

    protected int getClassifierID() {
        if (classifierID != 0) return classifierID; else return super.getClassifierID();
    }
}

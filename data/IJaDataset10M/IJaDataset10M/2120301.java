package uima.taes.interestingness.classifiers;

import org.apache.uima.analysis_engine.annotator.AnnotatorContext;
import org.apache.uima.analysis_engine.annotator.AnnotatorContextException;

public class LinearCorrelator extends UIMAClassifier {

    public LinearCorrelator(String category, int featureSize) {
        super(category, featureSize);
    }

    protected static String MEASURE_TO_MAXIMIZE = "measureToMaximize";

    protected static String SCALE = "scale";

    protected static String PRECISION = "precision";

    protected String measureToMaximize;

    protected String measureToUseForSlope;

    protected double scale;

    protected double precision;

    protected void getParameters(AnnotatorContext aContext) {
        super.getParameters(aContext);
        try {
            measureToMaximize = (String) aContext.getConfigParameterValue(MEASURE_TO_MAXIMIZE);
            scale = ((Float) aContext.getConfigParameterValue(SCALE)).doubleValue();
            precision = ((Float) aContext.getConfigParameterValue(PRECISION)).doubleValue();
        } catch (AnnotatorContextException e) {
            e.printStackTrace();
        }
    }

    protected void instantiateClassifier() {
        classifier = new weka.classifier.LinearCorrelator(measureToMaximize, scale, precision);
    }

    public String getParameters() {
        return "measureToMaximize=" + measureToMaximize + "; scale=" + scale + "; precision=" + precision;
    }
}

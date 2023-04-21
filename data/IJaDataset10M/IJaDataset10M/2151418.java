package edu.colorado.emml.classifiers.weka;

import weka.classifiers.trees.DecisionStump;
import edu.colorado.emml.classifiers.ClassifierDescription;

/**
 * Created by: Sam
 * May 2, 2008 at 1:44:50 PM
 */
public class WekaDecisionStump extends BatchWekaClassifier<DecisionStump> {

    public WekaDecisionStump() {
        super(new DecisionStump());
    }

    public ClassifierDescription getClassifierDescription() {
        return new ClassifierDescription(getClass());
    }

    public void setClassifierDescription(ClassifierDescription options) {
    }
}

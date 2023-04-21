package org.cleartk.classifier.test;

import java.util.ArrayList;
import java.util.List;
import org.cleartk.classifier.CleartkProcessingException;
import org.cleartk.classifier.Feature;
import org.cleartk.classifier.ScoredOutcome;
import org.cleartk.classifier.encoder.features.FeaturesEncoder;
import org.cleartk.classifier.encoder.features.NameNumber;
import org.cleartk.classifier.encoder.outcome.OutcomeEncoder;
import org.cleartk.classifier.jar.Classifier_ImplBase;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * 
 * @author Philip Ogren
 * 
 */
public abstract class TestClassifier_ImplBase<OUTCOME_TYPE> extends Classifier_ImplBase<List<NameNumber>, OUTCOME_TYPE, String> {

    public TestClassifier_ImplBase(FeaturesEncoder<List<NameNumber>> featuresEncoder, OutcomeEncoder<OUTCOME_TYPE, String> outcomeEncoder) {
        super(featuresEncoder, outcomeEncoder);
    }

    public OUTCOME_TYPE classify(List<Feature> features) throws CleartkProcessingException {
        String encodedOutcome = "" + features.size();
        return outcomeEncoder.decode(encodedOutcome);
    }

    @Override
    public List<ScoredOutcome<OUTCOME_TYPE>> score(List<Feature> features, int maxResults) throws CleartkProcessingException {
        List<ScoredOutcome<OUTCOME_TYPE>> returnValues = new ArrayList<ScoredOutcome<OUTCOME_TYPE>>();
        OUTCOME_TYPE outcome = outcomeEncoder.decode("" + features.size());
        returnValues.add(new ScoredOutcome<OUTCOME_TYPE>(outcome, 1.0d));
        return returnValues;
    }
}

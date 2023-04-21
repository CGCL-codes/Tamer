package org.cleartk.classifier.jar;

import org.cleartk.classifier.SequenceClassifier;
import org.cleartk.classifier.SequenceClassifierFactory;

/**
 * <br>
 * Copyright (c) 2011, Regents of the University of Colorado <br>
 * All rights reserved.
 */
public class SequenceJarClassifierFactory<OUTCOME_TYPE> extends GenericJarClassifierFactory<SequenceClassifier<OUTCOME_TYPE>> implements SequenceClassifierFactory<OUTCOME_TYPE> {

    @Override
    @SuppressWarnings("unchecked")
    protected Class<SequenceClassifier<OUTCOME_TYPE>> getClassifierClass() {
        return (Class<SequenceClassifier<OUTCOME_TYPE>>) (Class<?>) SequenceClassifier.class;
    }
}

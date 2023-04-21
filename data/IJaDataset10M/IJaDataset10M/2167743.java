package org.cleartk.classifier;

import java.io.IOException;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 */
public interface SequenceClassifierFactory<OUTCOME_TYPE> {

    public SequenceClassifier<OUTCOME_TYPE> createClassifier() throws IOException;
}

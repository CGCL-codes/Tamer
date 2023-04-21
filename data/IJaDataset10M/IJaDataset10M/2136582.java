package org.cleartk.classifier.libsvm;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 */
public class MultiClassLIBSVMClassifierBuilder extends LIBSVMClassifierBuilder<MultiClassLIBSVMClassifier, String, Integer, libsvm.svm_model> {

    @Override
    protected MultiClassLIBSVMClassifier newClassifier() {
        return new MultiClassLIBSVMClassifier(this.featuresEncoder, this.outcomeEncoder, this.model);
    }
}

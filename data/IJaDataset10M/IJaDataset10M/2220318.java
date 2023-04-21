package com.rapidminer.operator.features.transformation;

import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.features.AbstractFeatureProcessing;

/** 
 * Abstract super class of all operators transforming the feature space.
 *
 * @author Simon Fischer
 */
public abstract class AbstractFeatureTransformation extends AbstractFeatureProcessing {

    public AbstractFeatureTransformation(OperatorDescription description) {
        super(description);
    }
}

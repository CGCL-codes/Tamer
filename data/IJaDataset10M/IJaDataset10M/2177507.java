package com.rapidminer.operator.features.selection;

import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.features.AbstractFeatureProcessing;
import com.rapidminer.operator.ports.metadata.ExampleSetMetaData;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.parameter.UndefinedParameterError;

/**
 * Abstract superclass of all feature processing operators who remove features
 * from the example set.
 *
 * @author Simon Fischer
 */
public abstract class AbstractFeatureSelection extends AbstractFeatureProcessing {

    public AbstractFeatureSelection(OperatorDescription description) {
        super(description);
    }

    @Override
    protected MetaData modifyMetaData(ExampleSetMetaData metaData) throws UndefinedParameterError {
        metaData.attributesAreSubset();
        return metaData;
    }

    @Override
    public boolean writesIntoExistingData() {
        return false;
    }
}

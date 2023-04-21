package com.rapidminer.operator.preprocessing.weighting;

import com.rapidminer.example.Attributes;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.ports.metadata.AttributeMetaData;
import com.rapidminer.operator.ports.metadata.ExampleSetMetaData;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.SetRelation;
import com.rapidminer.operator.preprocessing.AbstractDataProcessing;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.math.container.Range;

/** Abstract superclass of operators adding a weight attribute.
 * 
 * @author Simon Fischer
 *
 */
public abstract class AbstractExampleWeighting extends AbstractDataProcessing {

    public AbstractExampleWeighting(OperatorDescription description) {
        super(description);
    }

    @Override
    protected MetaData modifyMetaData(ExampleSetMetaData metaData) {
        AttributeMetaData weightAttribute = new AttributeMetaData(Attributes.WEIGHT_NAME, Ontology.REAL, Attributes.WEIGHT_NAME);
        weightAttribute.setValueRange(getWeightAttributeRange(), getWeightAttributeValueRelation());
        metaData.addAttribute(weightAttribute);
        return metaData;
    }

    protected Range getWeightAttributeRange() {
        return new Range(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    protected SetRelation getWeightAttributeValueRelation() {
        return SetRelation.UNKNOWN;
    }
}

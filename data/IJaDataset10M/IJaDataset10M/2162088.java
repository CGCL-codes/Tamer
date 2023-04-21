package gov.nist.maia;

import gov.nist.atlas.type.ATLASType;
import gov.nist.atlas.util.ATLASImplementation;
import org.dom4j.Element;

abstract class FeatureDataBuilder extends ATLASTypeBuilder {

    FeatureDataBuilder(ATLASImplementation implementation, ATLASTypeBuilderFactory builderFactory, MAIAScheme scheme) {
        super(implementation, builderFactory, scheme);
    }

    protected void populateFeatureData(Element e, ATLASType type) {
        addFragments(e.elementIterator(MAIALoader.PARAMETER), builderFactory.getParameterTypeBuilder(scheme), false, type);
        addFragments(e.elementIterator(MAIALoader.FEATURE), builderFactory.getFeatureTypeBuilder(scheme), false, type);
    }
}

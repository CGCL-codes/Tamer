package com.ibm.wala.cast.js.ipa.callgraph.correlations.extraction;

import com.ibm.wala.cast.tree.CAstEntity;

public abstract class ExtractionPolicyFactory {

    public abstract ExtractionPolicy createPolicy(CAstEntity entity);
}

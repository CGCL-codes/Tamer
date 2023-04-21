package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.policies.variants.Variant;

/**
 *
 */
public interface VariablePolicyTypeSpecificSelector {

    Variant selectVariant(SelectionContext context, ActivatedVariablePolicy variablePolicy);
}

package de.tum.in.botl.util;

import java.util.Map;
import de.tum.in.botl.metamodel.interfaces.MetamodelInterface;
import de.tum.in.botl.ruleSet.implementation.RuleSetException;
import de.tum.in.botl.ruleSet.implementation.UnsupportedTermException;
import de.tum.in.botl.ruleSet.interfaces.ModelVariableInterface;
import de.tum.in.botl.ruleSet.interfaces.RuleSetInterface;

public interface RuleSetCreatorInterface {

    public abstract Map getOld2NewMap();

    public abstract ModelVariableInterface getCopy(ModelVariableInterface mvOrg, boolean removeNegativeContext, boolean invertNegativeContext) throws UnsupportedTermException, RuleSetException;

    /**
   * Inverts the given rule set.
   * @param mm The new target metamodel. This metamodel must be one of the rule sets existing source metamodels.
   * @throws Exception 
   */
    public abstract RuleSetInterface createInverted(RuleSetInterface rs, MetamodelInterface newTarget) throws Exception;
}

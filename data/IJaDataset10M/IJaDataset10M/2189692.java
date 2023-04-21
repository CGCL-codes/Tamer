package com.sun.xacml.cond.cluster;

import com.sun.xacml.cond.FunctionBase;
import com.sun.xacml.cond.ConditionBagFunction;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Clusters all the functions supported by <code>ConditionBagFunction</code>.
 *
 * @since 1.2
 * @author Seth Proctor
 */
public class ConditionBagFunctionCluster implements FunctionCluster {

    public Set getSupportedFunctions() {
        Set set = new HashSet();
        Iterator it = ConditionBagFunction.getSupportedIdentifiers().iterator();
        while (it.hasNext()) set.add(new ConditionBagFunction((String) (it.next())));
        return set;
    }
}

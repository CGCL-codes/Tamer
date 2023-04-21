package com.sun.xacml.cond.cluster;

import com.sun.xacml.cond.SubtractFunction;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Clusters all the functions supported by <code>SubtractFunction</code>.
 *
 * @since 1.2
 * @author Seth Proctor
 */
public class SubtractFunctionCluster implements FunctionCluster {

    public Set getSupportedFunctions() {
        Set set = new HashSet();
        Iterator it = SubtractFunction.getSupportedIdentifiers().iterator();
        while (it.hasNext()) set.add(new SubtractFunction((String) (it.next())));
        return set;
    }
}

package com.sun.xacml.cond.cluster;

import com.sun.xacml.cond.HigherOrderFunction;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Clusters all the functions supported by <code>HigherOrderFunction</code>.
 *
 * @since 1.2
 * @author Seth Proctor
 */
public class HigherOrderFunctionCluster implements FunctionCluster {

    public Set getSupportedFunctions() {
        Set set = new HashSet();
        Iterator it = HigherOrderFunction.getSupportedIdentifiers().iterator();
        while (it.hasNext()) set.add(new HigherOrderFunction((String) (it.next())));
        return set;
    }
}

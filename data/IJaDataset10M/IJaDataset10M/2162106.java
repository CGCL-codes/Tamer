package com.sun.xacml.cond.cluster;

import com.sun.xacml.cond.StringNormalizeFunction;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Clusters all the functions supported by
 * <code>StringNormalizeFunction</code>.
 *
 * @since 1.2
 * @author Seth Proctor
 */
public class StringNormalizeFunctionCluster implements FunctionCluster {

    public Set getSupportedFunctions() {
        Set set = new HashSet();
        Iterator it = StringNormalizeFunction.getSupportedIdentifiers().iterator();
        while (it.hasNext()) set.add(new StringNormalizeFunction((String) (it.next())));
        return set;
    }
}

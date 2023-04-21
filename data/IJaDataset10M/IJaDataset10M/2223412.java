package com.jujunie.integration.bugzilla;

import java.util.Collections;
import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class represents a set of parameter values for a selector. It gives a value to apply for each parameter
 * defined in the selector query.
 * @author julien
 * @since 0.05.01
 */
public class Set {

    /** Logger */
    private static final Log log = LogFactory.getLog(Set.class);

    /** List of parameters composing the set */
    private LinkedList<Param> params = new LinkedList<Param>();

    /**
     * Add a parameter to the set
     * @param p the parameter to add
     */
    public void addParam(Param p) {
        this.params.add(p);
    }

    /**
     * @return the set
     */
    Object[] getSet() {
        log.debug("Getting the set");
        Collections.sort(this.params);
        int i = 0;
        Object[] res = new Object[this.params.size()];
        for (Param param : this.params) {
            res[i++] = param.getValue();
        }
        return res;
    }
}

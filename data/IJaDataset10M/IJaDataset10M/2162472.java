package com.hp.hpl.jena.reasoner.rulesys;

import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.reasoner.TriplePattern;

/**
 * Interface through which the current bound values of variables
 * can be found. Many of the details vary between the forward and
 * backward chaining system - this interface is the minimal one needed
 * by most builtins the specific implementations offer richer functionality.
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.9 $ on $Date: 2006/03/22 13:52:20 $
 */
public interface BindingEnvironment {

    /**
     * Return the most ground version of the node. If the node is not a variable
     * just return it, if it is a varible bound in this environment return the binding,
     * if it is an unbound variable return the variable.
     */
    public Node getGroundVersion(Node node);

    /**
     * Bind a variable in the current envionment to the given value.
     * Checks that the new binding is compatible with any current binding.
     * @param var a Node_RuleVariable defining the variable to bind
     * @param value the value to bind
     * @return false if the binding fails
     */
    public boolean bind(Node var, Node value);

    /**
      * Instantiate a triple pattern against the current environment.
      * This version handles unbound varibles by turning them into bNodes.
      * @param pattern the triple pattern to match
      * @return a new, instantiated triple
      */
    public Triple instantiate(TriplePattern pattern);
}

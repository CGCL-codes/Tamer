package gov.sns.xal.model.gen;

import java.util.*;

/**
 *  Interface to be exposed by classes implementing lattice element generation rules.  Such classes
 *  should take a set of AcceleratorNode objects (including a single element) and return an ordered 
 *  list of Element objects modeling the node set [set the function getElements()].
 *
 * @author  CKAllen
 */
public interface IRule {

    /**
     *  Returns a set of AcceleratorNode type string identifiers (order is not important).
     *  The class implementing this interface is able to generate an order list of Element
     *  objects that model this node combination.
     *  
     *  @return     set (unordered) of AcceleratorNode objects that generation rule handles
     */
    public Set<String> getNodeCombination();

    public List getElements(Set setNodeCombin) throws GenerationException;
}

;

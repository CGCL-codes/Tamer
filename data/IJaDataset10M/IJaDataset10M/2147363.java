package com.reserveamerica.elastica.cluster;

import java.util.*;

/**
 * The delegate group definition contains the definition of a delegate group. It aggregates the list of DelegateDefinition 
 * objects that make up the group. It also holds the ID of a NodeResolver object that acts as the default target..
 * 
 * @see DelegateDefinition 
 * @see NodeResolver
 * @author BStasyszyn
 */
public interface DelegateGroupDefinition extends NodeResolverDefinition {

    /**
   * A list of delegate definitions for the delegate group.
   * 
   * @return List< DelegateDefinition >
   */
    List<DelegateDefinition> getDelegateDefinitions();

    /**
   * Adds a new delegate definition to the group.
   * 
   * @param delegateDef DelegateDefinition
   */
    void addDelegate(DelegateDefinition delegateDef);

    /**
   * The ID of the node-resolver to use if no delegates match or are available.
   * 
   * @return - The target ID or null.
   */
    String getDefaultTargetId();
}

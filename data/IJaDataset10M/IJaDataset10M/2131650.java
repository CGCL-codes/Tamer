package org.progeeks.meta;

/**
 *  Experimental meta-object interface for exploring functors.
 *
 *  @version   $Revision: 1.2 $
 *  @author    Paul Speed
 */
public interface MetaObjectX extends MetaObject {

    /**
     *  Executes the appropriate functor for the specified name
     *  and meta-object class, passing the meta-object parameters
     *  to the underlying implementation.
     */
    public Object execute(String functorName, MetaObject parms);
}

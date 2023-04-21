package com.hp.hpl.jena.graph.query;

import com.hp.hpl.jena.util.iterator.*;

/**
    A BindingQueryPlan is something that can run executeBindings() to get back an
    ExtendedIterator over Domains, ie, Lists.
    
	@author kers
*/
public interface BindingQueryPlan {

    public ExtendedIterator executeBindings();
}

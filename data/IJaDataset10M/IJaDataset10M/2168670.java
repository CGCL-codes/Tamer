package com.sun.ebxml.registry.query.filter;

import com.sun.ebxml.registry.*;
import org.oasis.ebxml.registry.bindings.query.*;
import org.oasis.ebxml.registry.bindings.rs.*;

public interface SQLConverter {

    /**
	 * Convert the specified Object obj to an equivalent SQL query string.
	 */
    public String convertToSQL(Object obj) throws RegistryException;
}

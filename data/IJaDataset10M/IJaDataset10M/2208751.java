package org.freebxml.omar.server.query.filter;

import javax.xml.registry.RegistryException;
import org.oasis.ebxml.registry.bindings.query.FilterType;
import org.oasis.ebxml.registry.bindings.query.StringFilterType;

/**
 * A FilterProcessor for string valued attributes
 * 
 * @author Farrukh Najmi
 *
 */
class StringFilterProcessor extends SimpleFilterProcessor {

    public StringFilterProcessor(FilterQueryProcessor parentQueryProcessor, FilterType filter) throws RegistryException {
        super(parentQueryProcessor, filter);
    }

    protected String getValue() throws RegistryException {
        String value = "'" + ((StringFilterType) filter).getValue() + "'";
        return value;
    }
}

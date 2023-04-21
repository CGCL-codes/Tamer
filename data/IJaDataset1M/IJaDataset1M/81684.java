package org.dcm4chee.xero.metadata.filter;

import java.util.Map;

/**
 * Defines a filter on one or more sources, producing a single output.  Filters must be
 * thread safe, even if they return a stream object - that is, one filter object
 * might have multiple streams reading from separate returned streams.  The returned
 * objects individually do not need to be thread safe, although a particular instance
 * may call for a stronger contract.  
 * @author bwallace
 *
 */
public interface Filter<T> {

    /** Filters out a result, from one or more inputs.  The result maybe a stream
	 * based result, in which case the class will continue to be used in an ongoing
	 * basis until it is completed.
	 * @param params are the values to use to figure out the return value.  Modifiable by the callee
	 * @param filterItem contains information about the available named filters, and the next filter item.
	 * @return A created or modified value based on params.
	 */
    T filter(FilterItem<T> filterItem, Map<String, Object> params);
}

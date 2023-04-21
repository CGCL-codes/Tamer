package net.martinimix.context.support;

import java.util.Map;

/**
 * Provides a service to return reference data for contollers, etc.
 * 
 * @author Scott Rossillo
 *
 */
public interface ReferenceDataService {

    /**
	 * Returns a map containing the reference data for the given
	 * name.
	 * 
	 * @param name the name of the reference data to be returned
	 * 
	 * @return a <code>Map</code> containing the reference data
	 * indentified by the given <code>name</code>
	 */
    public Map getReferenceData(String name);

    /**
	 * Returns the reference data key representing the 
	 * reference data entry for a not selected value.
	 * 
	 * FIXME: this requires better documentation
	 * 
	 * @param name the name of the reference data whose not
	 * selected key should be returned
	 * 
	 * @return the reference data key representing no selection
	 */
    public String getNotSelectedKey(String name);
}

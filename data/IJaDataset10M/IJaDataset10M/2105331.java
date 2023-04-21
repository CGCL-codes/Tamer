package quickfix;

import quickfix.field.ApplVerID;

/**
 * Provide data dictionaries for specified session protocol or application versions.
 */
public interface DataDictionaryProvider {

    /**
     * Retrieve the data dictionary for parsing a specific version of the transport header/trailer.
     * 
     * @param beginString FIX.4.0 through FIXT.1.1
     * @return the data dictionary for the specified session protocol version 
     * or null if no such dictionary is available.
     * @see FixVersions
     */
    DataDictionary getSessionDataDictionary(String beginString);

    /**
     * Retrieve the data dictionary for the specified application version.
     * 
     * @param applVerID the application version ID. Prior to FIXT.1.1, the applVerID 
     * will be determined by BeginString field.
     * @return the data dictionary for the specified application version or null if no such
     * dictionary is available.
     */
    DataDictionary getApplicationDataDictionary(ApplVerID applVerID);
}

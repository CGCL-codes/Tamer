package uk.org.ogsadai.converters.csv;

import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
* Exception raised when an illegal delimeter is used to set CSV file format.
* <p>
* Associated with error ID:
* <code>uk.org.ogsadai.CSV_CONFIGURATION_ILLEGAL_DELIMETER_ERROR</code>.
* 
* @author OGSA-DAI team
*/
public class CSVConfigurationIllegalDelimeterException extends DAIException {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007";

    /**
     * Constructor.
     * 
     * @param delim
     *      The illegal null string.
     */
    public CSVConfigurationIllegalDelimeterException(String delim) {
        super(ErrorID.CSV_CONFIGURATION_ILLEGAL_DELIMETER_ERROR, new Object[] { delim });
    }
}

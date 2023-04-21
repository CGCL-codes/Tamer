package org.ala.dao;

import java.util.List;
import java.util.Map;

/**
 * A scanner allows full table scans over the backend storage, row by row.
 *
 * @author Dave Martin (David.Martin@csiro.au)
 */
public interface Scanner {

    /**
	 * Get the next guid. Return null if scan is complete.
	 * 
	 * @return guid as byte[], null if end of table reached
	 * @throws Exception
	 */
    byte[] getNextGuid() throws Exception;

    /**
	 * Retrieves the current values for the record in the scanner.
	 * Call after getNextGuid to retrieve the values associated with the current guid.
	 * 
	 * @return
	 * @throws Exception
	 */
    Map<String, String> getCurrentValues() throws Exception;

    /**
	 * Retrieves the current value for the specified column.  Will return null if 
	 * the column does not exist OR was not requested in the scan.
	 * 
	 * @param column
	 * @param theClass
	 * @return
	 * @throws Exception
	 */
    Comparable getValue(String column, Class theClass) throws Exception;

    List<Comparable> getListValue(String column, Class theClass) throws Exception;
}

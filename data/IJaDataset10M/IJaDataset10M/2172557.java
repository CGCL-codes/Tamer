package dakside.hacc.modules.databridge.helpers;

import dakside.csv.CSVLine;

/**
 * The interface to convert model from/to CSV
 * @author takaji
 */
public interface TypeDefinition<T> {

    /**
     * Convert an object from CSV line
     * @param line
     * @return
     */
    T fromCSV(CSVLine line);

    /**
     * Validate a line
     * @param line
     * @return an empty array if no error was raised
     */
    DataError[] validate(CSVLine line, int row);

    /**
     * Get CSV header column list
     * @return
     */
    String[] getHeader();

    /**
     * Get CSV columns to represent object data
     * @param obj
     * @return
     */
    Object[] toCSV(T obj);

    /**
     * this object is existing in database
     * @param obj
     * @return
     */
    boolean exist(T obj);
}

package net.frontlinesms.csv;

/**
 * Exception thrown while reading a CSV file.
 * 
 * @author Alex
 */
@SuppressWarnings("serial")
public class CsvParseException extends Exception {

    /** @see Exception#Exception(String) */
    public CsvParseException(String message) {
        super(message);
    }
}

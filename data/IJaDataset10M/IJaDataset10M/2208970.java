package lu.ng.search.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * Contract for specific field enumerations of objects to be recorded to the
 * database.
 * 
 * @author GEORGN
 * @version 1
 */
public interface FieldNameEnum {

    /**
     * Returns the passed document with the appropriate field added to it if its
     * value is not empty.
     * 
     * @param doc
     *            the lucene document instance
     * @param value
     *            the value to add
     * 
     * @return Document the lucene document
     */
    Document add(final Document doc, final String value);

    /**
     * Returns the passed ParsedObject with the value of the appropriate field
     * set if it is not null.
     * 
     * @param field
     *            the field to reverse back to the ParsedObject from the
     *            document
     * @param parsedObject
     *            the ParsedObject
     * 
     * @return ParsedObject the ParsedObject
     */
    ParsedObject reverse(final Field field, final ParsedObject parsedObject);
}

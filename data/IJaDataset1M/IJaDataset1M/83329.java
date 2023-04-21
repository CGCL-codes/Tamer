package org.apache.solr.schema;

/**
 * <code>CopyField</code> contains all the information of a valid copy fields in an index.
 * 
 * @since solr 1.4
 */
public class CopyField {

    private final SchemaField source;

    private final SchemaField destination;

    private final int maxChars;

    public static final int UNLIMITED = 0;

    public CopyField(final SchemaField source, final SchemaField destination) {
        this(source, destination, UNLIMITED);
    }

    /**
   * @param source The SchemaField of the source field.
   * @param destination The SchemaField of the destination field.
   * @param maxChars Maximum number of chars in source field to copy to destination field.
   * If equal to 0, there is no limit.
   */
    public CopyField(final SchemaField source, final SchemaField destination, final int maxChars) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source or Destination SchemaField can't be NULL.");
        }
        if (maxChars < 0) {
            throw new IllegalArgumentException("Attribute maxChars can't have a negative value.");
        }
        this.source = source;
        this.destination = destination;
        this.maxChars = maxChars;
    }

    public String getLimitedValue(final String val) {
        return maxChars == UNLIMITED || val.length() < maxChars ? val : val.substring(0, maxChars);
    }

    /**
   * @return source SchemaField
   */
    public SchemaField getSource() {
        return source;
    }

    /**
   * @return destination SchemaField
   */
    public SchemaField getDestination() {
        return destination;
    }

    /**
   * @return tha maximum number of chars in source field to copy to destination field.
   */
    public int getMaxChars() {
        return maxChars;
    }
}

package javax.jdo.metadata;

/**
 * Represents ordering of a collection field/property.
 * @since 2.3
 */
public interface OrderMetadata extends Metadata {

    /**
     * Method to set the version column name.
     * 
     * @param column Name of the version clumn
     */
    OrderMetadata setColumn(String column);

    /**
     * Accessor for the version column name
     * 
     * @return The version column name
     */
    String getColumn();

    /**
     * Method to set mapped-by information whether the order is present in the element class.
     * 
     * @param mappedBy Field/property name in which to store the ordering in the element
     */
    OrderMetadata setMappedBy(String mappedBy);

    /**
     * Accessor for the mapped-by field/property name in the element class.
     * 
     * @return Name of field/property in element class
     */
    String getMappedBy();

    /**
     * Accessor for all column(s) defined on the ordering.
     * 
     * @return The column(s)
     */
    ColumnMetadata[] getColumns();

    /**
     * Add a column for this ordering.
     * 
     * @return The ColumnMetadata
     */
    ColumnMetadata newColumnMetadata();

    /**
     * Accessor for the number of columns defined for this ordering.
     * 
     * @return The number of columns
     */
    int getNumberOfColumns();

    /**
     * Method to set index metadata for the ordering
     * 
     * @return The metadata for any index
     */
    IndexMetadata newIndexMetadata();

    /**
     * Accessor for any index metadata for the ordering
     * 
     * @return Index metadata
     */
    IndexMetadata getIndexMetadata();
}

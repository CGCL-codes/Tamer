package net.conquiris.schema;

/**
 * Default binary schema item implementation.
 * @author Andres Rodriguez
 */
class DefaultBinarySchemaItem extends AbstractBinarySchemaItem {

    /**
	 * Constructor.
	 * @param name Field name.
	 * @param minOccurs Minimum number of occurrences.
	 * @param maxOccurs Maximum number of occurrences.
	 */
    DefaultBinarySchemaItem(String name, int minOccurs, int maxOccurs) {
        super(name, minOccurs, maxOccurs);
    }
}

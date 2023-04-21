package org.objectwiz.metadata;

/**
 * Type of a property that is linked to another {@link MappedClass}.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class AssociationPropertyType extends PropertyType {

    protected MappedClass associatedMappedClass;

    private String associatedPropertyName;

    private boolean isEmbedded = false;

    private String[] embeddedProperties;

    public AssociationPropertyType(PersistenceUnitMetadata metadata, String typeName, boolean isNullable, boolean isCollection, boolean isIndexedCollection, MappedClass associatedMappedClass, String associatedPropertyName, boolean isEmbedded, String[] embeddedProperties) {
        super(metadata, typeName, isNullable, isCollection, isIndexedCollection);
        this.associatedMappedClass = associatedMappedClass;
        this.associatedPropertyName = associatedPropertyName;
        this.isEmbedded = isEmbedded;
        this.embeddedProperties = embeddedProperties;
    }

    public MappedClass getAssociatedMappedClass() {
        return associatedMappedClass;
    }

    public String getAssociatedPropertyName() {
        return associatedPropertyName;
    }

    public MappedProperty getAssociatedProperty() {
        if (associatedPropertyName == null) return null;
        return getAssociatedMappedClass().getProperty(associatedPropertyName);
    }

    public boolean isEmbedded() {
        return isEmbedded;
    }

    public String[] getEmbeddedProperties() {
        return embeddedProperties;
    }
}

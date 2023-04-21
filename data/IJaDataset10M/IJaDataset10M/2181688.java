package org.datanucleus.enhancer.bcel.metadata;

import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.ClassMetaData;
import org.datanucleus.metadata.DefaultMetaDataFactory;
import org.datanucleus.metadata.FieldMetaData;
import org.datanucleus.metadata.InterfaceMetaData;
import org.datanucleus.metadata.MetaData;
import org.datanucleus.metadata.MetaDataFactory;
import org.datanucleus.metadata.MetaDataManager;
import org.datanucleus.metadata.PackageMetaData;
import org.datanucleus.metadata.PropertyMetaData;

/**
 * Factory for MetaData when enhancing using BCEL.
 * BCEL requires Class/Field MetaData objects furnishing with additional information to aid
 * its enhancement process.
 * 
 * @version $Revision: 1.5 $
 */
public class BCELMetaDataFactory extends DefaultMetaDataFactory implements MetaDataFactory {

    MetaDataManager mgr;

    /**
     * Constructor.
     */
    public BCELMetaDataFactory(MetaDataManager mgr) {
        this.mgr = mgr;
    }

    /**
     * Utility to create a new class component.
     * This method exists so that the Enhancer can create its own ClassMetaData objects using its class.
     * @param pmd MetaData for package
     * @param name Name of class
     * @param identityType identity type
     * @param objectidClass name of objectid class
     * @param requiresExtent Whether an extent is required
     * @param detachable Whether it is detachable
     * @param embeddedOnly Whether it is embedded only
     * @param modifier persistence modifier
     * @param persistenceCapableSuperclass PC superclass (deprecated)
     * @param catalog catalog to use
     * @param schema schema to use
     * @param table table to use
     * @param entityName the entity name required by JPA �4.3.1
     * @return The MetaData for the class
     */
    public ClassMetaData newClassObject(PackageMetaData pmd, String name, String identityType, String objectidClass, String requiresExtent, String detachable, String embeddedOnly, String modifier, String persistenceCapableSuperclass, String catalog, String schema, String table, String entityName) {
        return new BCELClassMetaData(pmd, name, identityType, objectidClass, requiresExtent, detachable, embeddedOnly, modifier, persistenceCapableSuperclass, catalog, schema, table, entityName);
    }

    /**
     * Utility to create a new field component.
     * This method exists so that the Enhancer can create its own FieldMetaData objects using its class.
     * @param md Parent metadata
     * @param name Name of field
     * @param pk Whether it is PK
     * @param modifier persistence modifier
     * @param defaultFetchGroup Whether it is in DFG
     * @param nullValue Behaviour on null value
     * @param embedded Whether it is embedded
     * @param serialized Whether it is serialised
     * @param dependent Whether it is dependent
     * @param mappedBy Field that it is mapped into
     * @param column column to use
     * @param table table to use
     * @param catalog catalog to use
     * @param schema schema to use
     * @param deleteAction Action on FK delete
     * @param indexed whether the column is indexed
     * @param unique Whether the column is unique
     * @param recursionDepth Recursion depth
     * @param loadFetchGroup Whether to load the fetch group
     * @param valueStrategy Value strategy for generating field values
     * @param sequence Sequence name if required
     * @param fieldType Type of the field
     * @return FieldMetaData
     */
    public FieldMetaData newFieldObject(MetaData md, String name, String pk, String modifier, String defaultFetchGroup, String nullValue, String embedded, String serialized, String dependent, String mappedBy, String column, String table, String catalog, String schema, String deleteAction, String indexed, String unique, String recursionDepth, String loadFetchGroup, String valueStrategy, String sequence, String fieldType) {
        return new BCELFieldMetaData(md, name, pk, modifier, defaultFetchGroup, nullValue, embedded, serialized, dependent, mappedBy, column, table, catalog, schema, deleteAction, indexed, unique, recursionDepth, loadFetchGroup, valueStrategy, sequence, fieldType);
    }

    /**
     * Utility to create a new field component copying from the passed object.
     * This method exists so that the Enhancer can create its own FieldMetaData objects using its class.
     * @param md Parent metadata
     * @param referenceFmd MetaData to copy from
     * @return FieldMetaData
     */
    public FieldMetaData newFieldObject(MetaData md, AbstractMemberMetaData referenceFmd) {
        return new BCELFieldMetaData(md, referenceFmd);
    }

    /**
     * Utility to create a new property component.
     * This method exists so that the Enhancer can create its own PropertyMetaData objects using its class.
     * @param md Parent metadata
     * @param name Name of field
     * @param pk Whether it is PK
     * @param modifier persistence modifier
     * @param defaultFetchGroup Whether it is in DFG
     * @param nullValue Behaviour on null value
     * @param embedded Whether it is embedded
     * @param serialized Whether it is serialised
     * @param dependent Whether it is dependent
     * @param mappedBy Field that it is mapped into
     * @param column column to use
     * @param table table to use
     * @param catalog catalog to use
     * @param schema schema to use
     * @param deleteAction Action on FK delete
     * @param indexed whether the column is indexed
     * @param unique Whether the column is unique
     * @param recursionDepth Recursion depth
     * @param loadFetchGroup Whether to load the fetch group
     * @param valueStrategy Value strategy for generating field values
     * @param sequence Sequence name if required
     * @param fieldType Type of the field
     * @param fieldName Name of the field
     * @return PropertyMetaData
     */
    public PropertyMetaData newPropertyObject(MetaData md, String name, String pk, String modifier, String defaultFetchGroup, String nullValue, String embedded, String serialized, String dependent, String mappedBy, String column, String table, String catalog, String schema, String deleteAction, String indexed, String unique, String recursionDepth, String loadFetchGroup, String valueStrategy, String sequence, String fieldType, String fieldName) {
        return new BCELPropertyMetaData(md, name, pk, modifier, defaultFetchGroup, nullValue, embedded, serialized, dependent, mappedBy, column, table, catalog, schema, deleteAction, indexed, unique, recursionDepth, loadFetchGroup, valueStrategy, sequence, fieldType, fieldName);
    }

    /**
     * Utility to create a new property component copying from the passed object.
     * This method exists so that the Enhancer can create its own PropertyMetaData objects using its class.
     * @param md Parent metadata
     * @param referencePmd MetaData to copy from
     * @return PropertyMetaData
     */
    public PropertyMetaData newPropertyObject(MetaData md, PropertyMetaData referencePmd) {
        return new BCELPropertyMetaData(md, referencePmd);
    }

    /**
     * Constructor for an InterfaceMetaData.
     * Takes the basic string information found in the XML/annotations.
     * @param parent MetaData for the package that this class belongs to
     * @param name Name of class
     * @param identityType Type of identity
     * @param objectidClass Class of the object id
     * @param requiresExtent Whether the class requires an extent
     * @param detachable Whether this is detachable
     * @param embeddedOnly embedded-only tag
     * @param catalog Name for catalog
     * @param schema Name for schema
     * @param table Name of the table where to persist objects of this type
     * @param entityName the entity name required by JPA �4.3.1
     */
    public InterfaceMetaData newInterfaceObject(PackageMetaData parent, String name, String identityType, String objectidClass, String requiresExtent, String detachable, String embeddedOnly, String catalog, String schema, String table, String entityName) {
        return super.newInterfaceObject(parent, name, identityType, objectidClass, requiresExtent, detachable, embeddedOnly, catalog, schema, table, entityName);
    }
}

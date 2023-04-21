package org.datanucleus.store.mapped.identifier;

import java.util.Map;
import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.metadata.FieldRole;
import org.datanucleus.store.mapped.DatastoreAdapter;
import org.datanucleus.store.mapped.DatastoreIdentifier;
import org.datanucleus.store.mapped.IdentifierCase;

/**
 * Factory that creates immutable instances of DatastoreIdentifier for mapped datastores.
 * Provides a more concise and consistent alternative to "jpox".
 * Naming as follows:-
 * <ul>
 * <li>Class called "MyClass" will generate table name of "MYCLASS"</li>
 * <li>Field called "myField" will generate column name of "MYFIELD"</li>
 * <li>Datastore id field for class "MyClass" will have the PK field "MYCLASS_ID"</li>
 * <li>Join table will be named after the class and field, so "MyClass" with field "myField" will become
 * a table with name "MYCLASS_MYFIELD".</li>
 * <li>Columns of a join table will be named after the PK fields of the owner and element. So something
 * like "MYCLASS_ID_OID" and "MYELEMENT_ID_EID"</li>
 * <li>Discriminator field columns will, by default, be called "DISCRIMINATOR"</li>
 * <li>Index field columns will, by default, be called "IDX"</li>
 * <li>Version field columns will, by default, be called "VERSION"</li>
 * <li>Adapter index field columns will, by default, be called "IDX"</li>
 * </ul>
 */
public class JPOX2IdentifierFactory extends JPOXIdentifierFactory {

    /**
     * Constructor.
     * The properties accepted are
     * <ul>
     * <li>RequiredCase : what case the identifiers should be in</li>
     * <li>DefaultCatalog : default catalog to use (if any)</li>
     * <li>DefaultSchema : default schema to use (if any)</li>
     * <li>WordSeparator : separator character(s) between identifier words</li>
     * <li>TablePrefix : Prefix to prepend to all table identifiers</li>
     * <li>TableSuffix : Suffix to append to all table identifiers</li>
     * </ul>
     * @param dba Database adapter
     * @param clr ClassLoader resolver
     * @param props Any properties controlling identifier generation
     */
    public JPOX2IdentifierFactory(DatastoreAdapter dba, ClassLoaderResolver clr, Map props) {
        super(dba, clr, props);
    }

    /**
     * Method to generate a join-table identifier. The identifier could be for a foreign-key
     * to another table (if the destinationId is provided), or could be for a simple column
     * in the join table.
     * @param ownerFmd MetaData for the owner field
     * @param relatedFmd MetaData for the related field
     * @param destinationId Identifier for the identity field of the destination (if FK)
     * @param embedded Whether the identifier is for a field embedded
     * @param fieldRole The role to be performed by this column e.g FK, collection element ?
     * @return The identifier.
     */
    public DatastoreIdentifier newJoinTableFieldIdentifier(AbstractMemberMetaData ownerFmd, AbstractMemberMetaData relatedFmd, DatastoreIdentifier destinationId, boolean embedded, int fieldRole) {
        if (destinationId != null) {
            return newDatastoreFieldIdentifier(destinationId.getIdentifierName(), embedded, fieldRole);
        } else {
            String baseName = null;
            if (fieldRole == FieldRole.ROLE_COLLECTION_ELEMENT) {
                baseName = "ELEMENT";
            } else if (fieldRole == FieldRole.ROLE_ARRAY_ELEMENT) {
                baseName = "ELEMENT";
            } else if (fieldRole == FieldRole.ROLE_MAP_KEY) {
                baseName = "KEY";
            } else if (fieldRole == FieldRole.ROLE_MAP_VALUE) {
                baseName = "VALUE";
            } else {
                baseName = "UNKNOWN";
            }
            return newDatastoreFieldIdentifier(baseName);
        }
    }

    /**
     * Method to generate a FK/FK-index field identifier. 
     * The identifier could be for the FK field itself, or for a related index for the FK.
     * @param ownerFmd MetaData for the owner field
     * @param destinationId Identifier for the identity field of the destination table (if strict FK)
     * @param embedded Whether the identifier is for a field embedded
     * @param fieldRole The role to be performed by this column e.g owner, index ?
     * @return The identifier
     */
    public DatastoreIdentifier newForeignKeyFieldIdentifier(AbstractMemberMetaData ownerFmd, DatastoreIdentifier destinationId, boolean embedded, int fieldRole) {
        if (fieldRole == FieldRole.ROLE_OWNER) {
            return newDatastoreFieldIdentifier(ownerFmd.getName() + "." + destinationId.getIdentifierName(), embedded, fieldRole);
        } else if (fieldRole == FieldRole.ROLE_INDEX) {
            return newDatastoreFieldIdentifier(ownerFmd.getName(), embedded, fieldRole);
        } else {
            throw new NucleusException("DatastoreField role " + fieldRole + " not supported by this method").setFatal();
        }
    }

    /**
     * Method to return an identifier for a version datastore field.
     * @return The version datastore field identifier
     */
    public DatastoreIdentifier newVersionFieldIdentifier() {
        String name = "VERSION";
        DatastoreIdentifier identifier = columns.get(name);
        if (identifier == null) {
            identifier = new ColumnIdentifier(this, name);
            columns.put(name, identifier);
        }
        return identifier;
    }

    /**
     * Method to return an identifier for an index (ordering) datastore field.
     * @param mmd MetaData for the field/property - not used here
     * @return The index datastore field identifier
     */
    public DatastoreIdentifier newIndexFieldIdentifier(AbstractMemberMetaData mmd) {
        String name = "IDX";
        DatastoreIdentifier identifier = columns.get(name);
        if (identifier == null) {
            identifier = new ColumnIdentifier(this, name);
            columns.put(name, identifier);
        }
        return identifier;
    }

    /**
     * Method to return an identifier for an adapter index datastore field.
     * An "adapter index" is a column added to be part of a primary key when some other
     * column cant perform that role.
     * @return The index datastore field identifier
     */
    public DatastoreIdentifier newAdapterIndexFieldIdentifier() {
        return newIndexFieldIdentifier(null);
    }

    /**
     * Generate a datastore identifier from a Java identifier.
     *
     * <p>Conversion consists of breaking the identifier into words, converting
     * each word to upper-case, and separating each one with a word separator.
     * Words are identified by a leading upper-case character.
     * Any leading or trailing underscores are removed.</p>
     *
     * @param javaName the Java identifier.
     * @return The datastore identifier
     */
    public String generateIdentifierNameForJavaName(String javaName) {
        if (javaName == null) {
            return null;
        }
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < javaName.length(); ++i) {
            char c = javaName.charAt(i);
            if (c >= 'A' && c <= 'Z' && (identifierCase != IdentifierCase.MIXED_CASE && identifierCase != IdentifierCase.MIXED_CASE_QUOTED)) {
                s.append(c);
            } else if (c >= 'A' && c <= 'Z' && (identifierCase == IdentifierCase.MIXED_CASE || identifierCase == IdentifierCase.MIXED_CASE_QUOTED)) {
                s.append(c);
            } else if (c >= 'a' && c <= 'z' && (identifierCase == IdentifierCase.MIXED_CASE || identifierCase == IdentifierCase.MIXED_CASE_QUOTED)) {
                s.append(c);
            } else if (c >= 'a' && c <= 'z' && (identifierCase != IdentifierCase.MIXED_CASE && identifierCase != IdentifierCase.MIXED_CASE_QUOTED)) {
                s.append((char) (c - ('a' - 'A')));
            } else if (c >= '0' && c <= '9' || c == '_') {
                s.append(c);
            } else if (c == '.') {
                s.append(wordSeparator);
            } else {
                String cval = "000" + Integer.toHexString(c);
                s.append(cval.substring(cval.length() - (c > 0xff ? 4 : 2)));
            }
        }
        while (s.length() > 0 && s.charAt(0) == '_') {
            s.deleteCharAt(0);
        }
        if (s.length() == 0) {
            throw new IllegalArgumentException("Illegal Java identifier: " + javaName);
        }
        return s.toString();
    }
}

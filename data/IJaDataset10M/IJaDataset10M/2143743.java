package org.hibernate.mapping;

import org.hibernate.MappingException;
import org.hibernate.engine.Mapping;

/**
 * A collection with a synthetic "identifier" column
 */
public abstract class IdentifierCollection extends Collection {

    public static final String DEFAULT_IDENTIFIER_COLUMN_NAME = "id";

    private KeyValue identifier;

    public IdentifierCollection(PersistentClass owner) {
        super(owner);
    }

    public KeyValue getIdentifier() {
        return identifier;
    }

    public void setIdentifier(KeyValue identifier) {
        this.identifier = identifier;
    }

    public final boolean isIdentified() {
        return true;
    }

    void createPrimaryKey() {
        if (!isOneToMany()) {
            PrimaryKey pk = new PrimaryKey();
            pk.addColumns(getIdentifier().getColumnIterator());
            getCollectionTable().setPrimaryKey(pk);
        } else {
        }
    }

    public void validate(Mapping mapping) throws MappingException {
        super.validate(mapping);
        if (!getIdentifier().isValid(mapping)) {
            throw new MappingException("collection id mapping has wrong number of columns: " + getRole() + " type: " + getIdentifier().getType().getName());
        }
    }
}

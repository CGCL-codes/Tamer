package org.quickconnect.dbaccess.orm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 
 * @author lee
 * This class can be used directly or used as a base class for specific ManagedEntity sub-classes.  Sub-classes of ManagedEntity 
 * Should not have any public constructors.  Instead they should have their own create*** methods that call this class' create*** methods.
 * <br/><br/>
 * Only ManagedEntities and 
 * ManagedEntity sub-classes are tracked and usable in QCDBAccess ORM.  The default constructor of this class is private.  
 * <br/>Use the create*** methods to instantiate instances.
 */
public class ManagedEntity {

    private String typeName;

    private String uuid;

    private HashMap<String, Object> attributes;

    private HashMap<String, ManagedEntity> toOneRelatedEntities;

    private HashMap<String, ArrayList<ManagedEntity>> toManyRelatedEntities;

    private HashMap<String, RelationshipDef> definedRelationships;

    boolean isTransient;

    private ManagedEntity() {
    }

    /**
	 * Instantiates an instance of the the type indicated
	 * @param aTypeName The name of the type of entity to be created and tracked.  This name must match one of the types declared  
	 * in the data store.
	 * @return A new instance of a ManagedEntity or null if aTypeName is for an unknown type 
	 */
    public static ManagedEntity createManagedEntity(String aTypeName) {
        String aUUID = UUID.randomUUID().toString();
        return createManagedEntity(aTypeName, aUUID);
    }

    /**
	 * Instantiates an instance of the type indicated using the UUID string indicated for the identifier of the new ManagedEntity
	 * @param aTypeName The name of the type of entity to be created and tracked.  This name must match one of the types declared  
	 * in the data store.
	 * @param aUUID A universally unique string that can identify this instance of ManagedEntity
	 * @return  A new instance of a ManagedEntity or null if aTypeName is for an unknown type 
	 */
    public static ManagedEntity createManagedEntity(String aTypeName, String aUUID) {
        ManagedEntity anEntity = new ManagedEntity();
        anEntity.typeName = aTypeName;
        anEntity.uuid = aUUID;
        anEntity.isTransient = true;
        return anEntity;
    }

    /**
	 * Instantiates a ManagedEntity to be used in the {@link RelationalStore} methods
	 * @param aTypeName The name of the type of entity to be created and tracked.  This name must match one of the types declared  
	 * in the data store.
	 * @return a ManagedEntity template
	 */
    public static ManagedEntity createTemplate(String aTypeName) {
        return createManagedEntity(aTypeName, null);
    }

    protected void setUUID(String aUUID) {
        uuid = aUUID;
    }

    /**
	 * Returns the UUID for the entity
	 * @return the UUID String
	 */
    public String getUUID() {
        return uuid;
    }

    /**
	 * Sets a value for a specific key.  ManagedEntity instances respond to key-value coding using this and the getValueForKey method of this class.
	 * @param key Any descriptive string
	 * @param value Any value to be associated with the key
	 */
    public void setValueForKey(String key, Object value) {
        attributes.put(key, value);
    }

    /**
	 * Accessor for a value associated previously associated with a key
	 * @param key Any descriptive string
	 * @return The value associated with the key or null if no association has been made
	 */
    public Object getValueForKey(String key) {
        return attributes.get(key);
    }

    /**
	 * Accessor for all of the previously associated keys and values for an instance
	 * @return A Set of Map key-value entries
	 */
    public Set<Map.Entry<String, Object>> getKeyValuePairs() {
        return attributes.entrySet();
    }

    /**
	 * Removed as deletes any previously associated key-value pairs for an instance
	 */
    protected void clearValues() {
        attributes.clear();
    }

    /**
	 * Accessor for the typeName of the instance
	 * @return the type name String
	 */
    public String getTypeName() {
        return typeName;
    }

    /**
	 * Accessor for a specific to-one related ManagedEntity
	 * @param relationshipName The name of the relationship to the other ManagedEntity
	 * @return  The related ManagedEntity instance or null if there is none
	 */
    public ManagedEntity getRelatedEntity(String relationshipName) {
        ManagedEntity foundEntity = null;
        RelationshipDef foundDef = this.definedRelationships.get(relationshipName);
        if (foundDef == null) {
        } else if (foundDef.isToMany()) {
        }
        foundEntity = toOneRelatedEntities.get(relationshipName);
        if (foundEntity == null) {
        }
        return foundEntity;
    }

    /**
	 * Creates a to-one relationship with another ManagedEntity
	 * @param relationshipName The name of the relationship to be created between entities
	 * @param anEntity The entity to which a relationship is to be created
	 */
    public void setRelatedEntity(String relationshipName, ManagedEntity anEntity) {
        toOneRelatedEntities.put(relationshipName, anEntity);
    }

    /**
	 * Accessor for a list of ManagedEntity instances associated with this instance using the specified relationship name
	 * @param relationshipName The name of the to-many relationship
	 * @return An ArrayList of entities associated with this instance by the relationship name or an empty list if there are none such.
	 */
    public ArrayList<ManagedEntity> getRelatedEntities(String relationshipName) {
        RelationshipDef foundDef = this.definedRelationships.get(relationshipName);
        if (!foundDef.isToMany()) {
        }
        ArrayList<ManagedEntity> relatedEntities = new ArrayList<ManagedEntity>();
        return relatedEntities;
    }

    /**
	 * Adds a ManagedEntity to the to-many relationship declared.  The ManagedEntity being added to the relationship must have a to-one relationship with this entity.
	 * @param relationshipName The name of the to-many relationship
	 * @param anEntity The entity to which a relationship is to be created
	 */
    public void addToManyRelatedEntity(String relationshipName, ManagedEntity anEntity) {
        RelationshipDef foundDef = this.definedRelationships.get(relationshipName);
        if (!foundDef.isToMany()) {
        }
        ArrayList<ManagedEntity> entitiesOfType = toManyRelatedEntities.get(relationshipName);
        if (entitiesOfType == null) {
            entitiesOfType = new ArrayList<ManagedEntity>();
            toManyRelatedEntities.put(relationshipName, entitiesOfType);
        }
        entitiesOfType.add(anEntity);
    }

    protected void setTransient(boolean aFlag) {
        isTransient = aFlag;
    }

    public boolean isTransient() {
        return isTransient;
    }
}

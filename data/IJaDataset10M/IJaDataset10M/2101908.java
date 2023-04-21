package bill.util.persist;

import java.util.*;

/**
 * Holds a collection of related entities. Used for 'queries' that retrieve
 * multiple entities of the same type (i.e. Users, Cars, Trucks, etc).
 */
public abstract class EntityCollection {

    protected String _entityName;

    protected Vector _entities;

    protected Hashtable _criteria;

    /** Sample EntityData of the type contained in the list. Needed so we know
       details about the entity such as its elements. */
    protected EntityData _sampleEntity;

    /**
    * Default creator, normally only used when creating an object via
    * introspection. When an object is created using this creator, the
    * setEntityName and setCriteria must be called as well to complete
    * the setting of the data required by this class.
    */
    public EntityCollection() {
        _entities = new Vector();
    }

    /**
    * Main creator, takes the selection criteria for populating the entity
    * collection as input.
    *
    * @param entityName Name of the entity that is contained in this
    * collection. Must match the entitiy's Java class name as we will use
    * introspection to create one. Use the full package name if neccessary,
    * such as com.mycompany.UserData.
    * @param criteria The criteria to use to populate the entity collection.
    * Keyed by the element name, the result is the element value.
    */
    public EntityCollection(String entityName, Hashtable criteria) throws PersistException {
        _entities = new Vector();
        _entityName = entityName;
        _criteria = criteria;
        _sampleEntity = createSampleEntity();
    }

    /**
    * Sets the class name of the entity that is contained in this collection.
    * Also creates a sample entity of that entity name.
    *
    * @param entityName Value to set the entity name to.
    * @throws PersistException Thrown when a sample entity cannot be created.
    */
    public void setEntityName(String entityName) throws PersistException {
        _entityName = entityName;
        _sampleEntity = createSampleEntity();
    }

    /**
    * Gets the class name of the entities that are contained in this collection.
    *
    * @return The class name of the entities.
    */
    public String getEntityName() {
        return _entityName;
    }

    /**
    * Sets the selection criteria used to populate the entity colleciton.
    *
    * @param criteria The cirteria to use to populate the entity collection.
    * Keyed by the element name, the result is the element value.
    */
    public void setCriteria(Hashtable criteria) {
        _criteria = criteria;
    }

    /**
    * Retrieves the selection criteria used to populate the entity colleciton.
    *
    * @return The selection criteria.
    */
    public Hashtable getCriteria() {
        return _criteria;
    }

    /**
    * Retrieves the list of entities that make up the collection.
    *
    * @return The collection's entities.
    */
    public Vector getEntities() {
        return _entities;
    }

    /**
    * Retrieve a specific entity from the collection.
    *
    * @param index The index of the entity to be retrieved, this is a 0 based
    * index.
    */
    public EntityData getEntity(int index) {
        return (EntityData) _entities.elementAt(index);
    }

    /**
    * Marks all entities in the collection as deleted. If the collection is
    * then save (or if an item in the collection is saved), the entities will
    * then be deleted from their persistent store.
    */
    public void setAllDeleted() {
        for (int i = 0; i < size(); i++) {
            EntityData entity = getEntity(i);
            entity.setDeleted();
        }
    }

    /**
    * Loops through all the entities in the collection and calls their save
    * method so they are updated in the persistent store.
    *
    * @throws PersistException Thrown when the save method fails, forwards on
    * the exception from the save method.
    *
    * @see EntityData#save
    */
    public void save() throws PersistException {
        for (int i = 0; i < size(); i++) {
            EntityData entity = getEntity(i);
            entity.save();
        }
    }

    /**
    * Creates an instance of the entity that is contained in this collecion.
    * We create most of the entities in the collection via introspection, and
    * this is the method that does this.
    *
    * @return A newly created instance of the entity.
    * @throws PersistException Thrown when we cannot instantiate the new
    * entity object.
    */
    protected EntityData createSampleEntity() throws PersistException {
        EntityData sampleEntity = null;
        try {
            sampleEntity = (EntityData) Class.forName(_entityName).newInstance();
        } catch (Exception ex) {
            throw new PersistException("Failed to create entity of type \"" + _entityName + "\" for use in an EntityCollection: " + ex.toString());
        }
        return sampleEntity;
    }

    /**
    * Returns the number of entities contained in this collection.
    *
    * @return The number of entities.
    */
    public int size() {
        return _entities.size();
    }

    /**
    * Must be implemented by the subclass, this is the method that actually
    * performs the read from the persistent data store and populates the
    * collection with it's entities.
    */
    public abstract void populateCollection() throws PersistException;
}

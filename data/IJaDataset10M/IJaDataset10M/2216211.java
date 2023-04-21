package at.ac.tuwien.j3dvn.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import at.ac.tuwien.j3dvn.control.Connector;
import at.ac.tuwien.j3dvn.control.DataConnector;
import at.ac.tuwien.j3dvn.control.EntityConnector;
import at.ac.tuwien.j3dvn.control.PropertyChangeListener;
import at.ac.tuwien.j3dvn.control.RelationConnector;

/**
 * A model is a data structure, which holds all the information of a data model.
 * It consists of two different units: entities and relations. Both can contain
 * all kinds of data (attributes). The difference is that entities are connected
 * to other entities through relations. An entity can have arbitrary many (or
 * zero) relations. A relation is always bidirectional.
 */
public class Model implements PropertyChangeListener {

    /**
	 * Is used to store mininum and maximum values of a property.
	 * 
	 * @param <T>
	 */
    public static class PropertyExtreme<T extends Comparable<? super T>> {

        private String elementName;

        private Class<? extends DataAddon> elementType;

        private T max;

        private T min = null;

        private String propertyName;

        private Class<T> propertyType;

        /**
		 * Creates a new PropertyExtreme.
		 * 
		 * @param elementType
		 * @param elementName
		 * @param propertyType
		 * @param propertyName
		 */
        protected PropertyExtreme(Class<? extends DataAddon> elementType, String elementName, Class<T> propertyType, String propertyName) {
            this.elementType = elementType;
            this.elementName = elementName;
            this.propertyType = propertyType;
            this.propertyName = propertyName;
        }

        public String getElementName() {
            return elementName;
        }

        public Class<? extends DataAddon> getElementType() {
            return elementType;
        }

        public Comparable getMax() {
            return max;
        }

        public Comparable getMin() {
            return min;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public Class<T> getPropertyType() {
            return propertyType;
        }

        /**
		 * Sets new minimums and maximums, if necessary. If value is less than
		 * the current minimum, then value becomes the new minimum. If value is
		 * greater than the current maximum, then value becmoes the new maximum.
		 * If value is null, nothing happens.
		 * 
		 * @param value
		 */
        public void setValue(T value) {
            if (value == null) return;
            if (min == null) {
                min = value;
                max = value;
            } else {
                if (min.compareTo(value) > 0) min = value;
                if (max.compareTo(value) < 0) max = value;
            }
        }
    }

    private static final String NO_PROPERTY_ERROR = "Unknown property for that element.";

    private static final String NO_TYPE_ERROR = "Unknown element type.";

    private final Map<Class<? extends DataAddon>, Map<String, PropertyExtreme<? extends Comparable>>> elementTypes = new HashMap<Class<? extends DataAddon>, Map<String, PropertyExtreme<? extends Comparable>>>();

    private final Set<EntityConnector> entities = new HashSet<EntityConnector>();

    private String name = null;

    private final Set<RelationConnector> relations = new HashSet<RelationConnector>();

    public Model() {
    }

    /**
	 * Adds an entity to the model.
	 * 
	 * @param entity
	 *            The entity, which is added to this model.
	 */
    public void addEntity(EntityConnector entity) {
        entities.add(entity);
        processElement(entity);
        entity.addPropertyChangeListener(this);
    }

    /**
	 * Adds a relation to this model. Before the relation is added, all the
	 * entities, which are connected to the relation, must habe been added.
	 * 
	 * @param relation
	 *            The relation, which is added to this model.
	 */
    public void addRelation(RelationConnector relation) {
        relations.add(relation);
        processElement(relation);
        relation.addPropertyChangeListener(this);
    }

    /**
	 * Retrieves the entities, which are currently loaded in this model.
	 * 
	 * @return A set of all entities, which are currently loaded in this model.
	 */
    public Collection<EntityConnector> getCurrentEntities() {
        return entities;
    }

    /**
	 * Returns all the data addon types of this model. This methods can be used
	 * to find out, which data addon types are used in this model.
	 * 
	 * @return A collection of classes of the differnet data addons used in this
	 *         model.
	 */
    public Collection<Class<? extends DataAddon>> getDataTypes() {
        return new HashSet<Class<? extends DataAddon>>(elementTypes.keySet());
    }

    /**
	 * Retrieves the name of this model.
	 * 
	 * @return The name of this model.
	 */
    public String getName() {
        return name;
    }

    public Comparable getPropertyMaxValue(Class<? extends DataAddon> elementClass, String propertyName) throws IllegalArgumentException {
        PropertyExtreme<? extends Comparable> extreme = getPropertyExtremes(elementClass, propertyName);
        return extreme.getMax();
    }

    public Comparable getPropertyMinValue(Class<? extends DataAddon> elementClass, String propertyName) throws IllegalArgumentException {
        PropertyExtreme<? extends Comparable> extreme = getPropertyExtremes(elementClass, propertyName);
        return extreme.getMin();
    }

    public <T> void propertyChanged(Connector sender, String propertyName, T oldValue, T newValue) {
        if (DataConnector.class.isAssignableFrom(sender.getClass())) processElement((DataConnector) sender);
    }

    /**
	/**
	 * Removes an entity from this model (thus from memory). If the entity
	 * does not exist in the model, nothing happens.
	 * 
	 * @param entity
	 *            The entity, which should be removed from this model.
	 */
    public void removeEntity(EntityConnector entity) {
        if (entity == null) return;
        if (entities.remove(entity)) {
            Collection<RelationConnector> deadRelations = new ArrayList<RelationConnector>(entity.getRelations());
            for (RelationConnector relation : deadRelations) {
                removeRelation(relation);
            }
        }
    }

    /**
	 * Removes a relation from this model (thus from memory). If the relation
	 * does not exist in the model, nothing happens.
	 * 
	 * @param relation
	 *            The relation, which should be removed from this model.
	 */
    public void removeRelation(RelationConnector relation) {
        if (relation == null) return;
        if (relations.remove(relation)) {
            relation.getEntity1().removeRelation(relation);
            relation.getEntity2().removeRelation(relation);
        }
    }

    /**
	 * Sets the name of this model.
	 * 
	 * @param name
	 *            The name of this model.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Adds a new element type and all its properties to the map of used element
	 * types in this model.
	 * 
	 * @param element
	 *            A connector of the new element type.
	 */
    @SuppressWarnings("unchecked")
    private void addElementType(DataConnector element) {
        Map<String, PropertyExtreme<? extends Comparable>> propertyMap = new HashMap<String, PropertyExtreme<? extends Comparable>>();
        Collection<String> propertyNames = element.propertyNames();
        DataAddon addon = element.getAddon();
        Class<? extends DataAddon> addonClass = addon.getClass();
        for (String propertyName : propertyNames) {
            Class<? extends Comparable> propertyType = element.getPropertyType(propertyName);
            PropertyExtreme extreme = new PropertyExtreme(addonClass, addon.getName(), propertyType, propertyName);
            propertyMap.put(propertyName, extreme);
        }
        elementTypes.put(addonClass, propertyMap);
    }

    private PropertyExtreme<? extends Comparable> getPropertyExtremes(Class<? extends DataAddon> elementClass, String propertyName) throws IllegalArgumentException {
        Map<String, PropertyExtreme<? extends Comparable>> properties = elementTypes.get(elementClass);
        if (properties == null) throw new IllegalArgumentException(NO_TYPE_ERROR);
        PropertyExtreme<? extends Comparable> extreme = properties.get(propertyName);
        if (extreme == null) throw new IllegalArgumentException(NO_PROPERTY_ERROR);
        return extreme;
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<? super T>> void processElement(DataConnector element) {
        Class<? extends DataAddon> addonClass = element.getAddon().getClass();
        if (!elementTypes.keySet().contains(addonClass)) addElementType(element);
        Map<String, PropertyExtreme<? extends Comparable>> propertyMap = elementTypes.get(addonClass);
        T property;
        PropertyExtreme<T> extreme;
        for (String propertyName : element.propertyNames()) {
            property = (T) element.getProperty(propertyName);
            extreme = (PropertyExtreme<T>) propertyMap.get(propertyName);
            extreme.setValue(property);
        }
    }
}

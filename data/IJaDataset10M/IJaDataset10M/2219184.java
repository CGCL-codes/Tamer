package adnotatio.rdf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author kotelnikov
 */
public class Resource extends Entity implements BasicVocabulary {

    /**
     * 
     */
    private static final long serialVersionUID = 3643353444506162277L;

    /**
     * If this flag is <code>true</code> then the underlying model just
     * reference this resource and not directly declares it - it means that that
     * this resource is mentioned with rdf:about property and not with rdf:id.
     */
    private boolean fAbout;

    /**
     * The identifier of this resource
     */
    private ID fId;

    /**
     * The map containing all properties
     */
    private Map fMap = new HashMap();

    /**
     * The reference to this resource
     */
    private Reference fReference;

    /**
     * @param factory
     */
    public Resource(Model model, ID resourceId) {
        super(model);
        setValue(BasicVocabulary.RDF_ID, resourceId);
    }

    /**
     * @see adnotatio.rdf.PropertyValue#accept(adnotatio.rdf.IValueVisitor)
     */
    public void accept(IValueVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * This method sets the given value of the property or adds it to the
     * collection of existing properties. This method checks if there is already
     * a value of the specified property. If such a value already exists then it
     * creates a new container and adds to this container the specified value.
     * If the given resource does not contain values of the property then this
     * method just sets the given object as the value of the property.
     * 
     * @param propertyID the identifier of the property
     * @param value the value of the property to set
     */
    public void addValue(ID propertyID, Object value) {
        PropertyValue oldValue = getValue(propertyID);
        if (oldValue != null) {
            Container container = oldValue.getAsContainer();
            if (value instanceof Collection) {
                container.addAllValues((Collection) value);
            } else {
                container.add(value);
            }
        } else {
            setValue(propertyID, value);
        }
    }

    /**
     * Cleans up all fields.
     */
    public void clear() {
        Map map = getMap(true);
        map.clear();
    }

    /**
     * Removes this resource from the underlying model
     */
    public void delete() {
        fModel.removeResource(this);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Resource)) return false;
        Resource resource = (Resource) obj;
        Map m1 = getMap(false);
        Map m2 = resource.getMap(false);
        return m1 != null && m2 != null ? m1.equals(m2) : m1 == m2;
    }

    /**
     * Returns the first value corresponding to the property with the specified
     * URL
     * 
     * @param propertyID the URL of the property for which the corresponding
     *        value is returned
     * @return the first value corresponding to the property with the specified
     *         URL
     */
    public PropertyValue getFirstValue(ID propertyID) {
        if (propertyID == null) return null;
        PropertyValue propertyValue = getValue(propertyID);
        if (propertyValue instanceof Container) {
            return ((Container) propertyValue).getFirstValue();
        }
        return propertyValue;
    }

    /**
     * Returns the first value corresponding to the property with the specified
     * URL
     * 
     * @param propertyID the URL of the property for which the corresponding
     *        value is returned
     * @return the first value corresponding to the property with the specified
     *         URL
     */
    public PropertyValue getFirstValue(String propertyID) {
        return getFirstValue(new ID(propertyID));
    }

    /**
     * Returns the identifier of this resource
     * 
     * @return the identifier of this resource
     */
    public ID getID() {
        return fId;
    }

    /**
     * Returns the internal map with all property values; if the specified flag
     * <code>create</code> is <code>true</code> and the internal map is not
     * initialized then this method will create and return a new map
     * 
     * @param create if this flag is <code>true</code> and the internal map is
     *        not initialized then this method will create a new map
     * @return the internal map with all property values
     */
    private Map getMap(boolean create) {
        if (fMap == null && create) {
            fMap = new HashMap();
        }
        return fMap;
    }

    /**
     * Returns an array of all property identifiers from this resource
     * 
     * @return an array of all property identifiers from this resource
     */
    public ID[] getPropertyIDs() {
        Map map = getMap(false);
        int len = map != null ? map.size() : 0;
        ID[] array = new ID[len + 1];
        int pos = 0;
        array[pos++] = fAbout ? RDF_ABOUT : RDF_ID;
        if (len > 0) {
            for (Iterator iterator = map.keySet().iterator(); iterator.hasNext(); ) {
                ID propertyID = (ID) iterator.next();
                array[pos++] = propertyID;
            }
        }
        return array;
    }

    /**
     * Returns a newly created reference to this resource
     * 
     * @return a newly created reference to this resource
     */
    public Reference getReference() {
        if (fReference == null) {
            fReference = new Reference(fModel, getID());
        }
        return fReference;
    }

    /**
     * @see adnotatio.rdf.Entity#getStringValue()
     */
    protected String getStringValue() {
        ID id = getID();
        return id != null ? id.getURL() : "";
    }

    /**
     * Returns a value corresponding to the property with the specified URL
     * 
     * @param propertyID the URL of the property for which the corresponding
     *        value is returned
     * @return a value corresponding to the property with the specified URL
     */
    public PropertyValue getValue(ID propertyID) {
        if (propertyID == null) return null;
        if (RDF_ABOUT.equals(propertyID) && fAbout) return getReference();
        if (RDF_ID.equals(propertyID) && !fAbout) return getReference();
        Map map = getMap(false);
        return (PropertyValue) (map != null ? map.get(propertyID) : null);
    }

    /**
     * Returns a value corresponding to the property with the specified URL
     * 
     * @param propertyID the URL of the property for which the corresponding
     *        value is returned
     * @return a value corresponding to the property with the specified URL
     */
    public PropertyValue getValue(String propertyID) {
        return getValue(new ID(propertyID));
    }

    /**
     * Returns the representation of a property as a resource identifier.
     * 
     * @param propertyID the URL of the property to return
     * @return the representation of a property as a resource identifier.
     */
    public ID getValueAsID(ID propertyID) {
        Object value = getValue(propertyID);
        return PropertyValue.toID(value);
    }

    /**
     * Returns the representation of a property as a resource identifier.
     * 
     * @param propertyID the URL of the property to return
     * @return the representation of a property as a resource identifier.
     */
    public ID getValueAsID(String propertyID) {
        return getValueAsID(new ID(propertyID));
    }

    /**
     * Returns an integer representation of a property with the specified URL
     * 
     * @param propertyID the URL of the property to return
     * @param defaultValue the default value returned if there is no property
     *        with the given URL
     * @return an integer representation of a property with the specified URL
     */
    public int getValueAsInt(ID propertyID, int defaultValue) {
        Object value = getValue(propertyID);
        if (value == null) return defaultValue;
        if (value instanceof Integer) {
            Integer intValue = (Integer) value;
            return intValue.intValue();
        } else {
            String str = value != null ? value.toString() : null;
            int result = defaultValue;
            try {
                result = Integer.parseInt(str);
            } catch (Exception e) {
                result = defaultValue;
            }
            return result;
        }
    }

    /**
     * Returns an integer representation of a property with the specified URL
     * 
     * @param propertyID the URL of the property to return
     * @param defaultValue the default value returned if there is no property
     *        with the given URL
     * @return an integer representation of a property with the specified URL
     */
    public int getValueAsInt(String propertyID, int defaultValue) {
        return getValueAsInt(new ID(propertyID), defaultValue);
    }

    /**
     * Returns the string representation of the value with the specified
     * property URL
     * 
     * @param propertyID the URL of the property to return
     * @return the string representation of the value with the specified
     *         property URL
     */
    public String getValueAsString(ID propertyID) {
        Object value = getValue(propertyID);
        return value != null ? value.toString() : null;
    }

    /**
     * Returns the string representation of the value with the specified
     * property URL
     * 
     * @param propertyID the URL of the property to return
     * @return the string representation of the value with the specified
     *         property URL
     */
    public String getValueAsString(String propertyID) {
        return getValueAsString(new ID(propertyID));
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        Map map = getMap(false);
        return map != null ? map.hashCode() : 0;
    }

    /**
     * Returns <code>true</code> if this resource is empty (if it does not
     * contain any properties except the identifier -- rdf:id or rdf:about)
     * 
     * @return <code>true</code> if this resource is empty (if it does not
     *         contain any properties except the identifier -- rdf:id or
     *         rdf:about)
     */
    public boolean isEmpty() {
        return fMap.size() < 2;
    }

    /**
     * Removes and returns the value corresponding to the specified property
     * 
     * @param propertyID the URL of the property to remove
     * @return the value of the removed property
     */
    public PropertyValue removeValue(ID propertyID) {
        if (propertyID == null) return null;
        Map map = getMap(false);
        return (PropertyValue) (map != null ? map.remove(propertyID) : null);
    }

    /**
     * Removes and returns the value corresponding to the specified property
     * 
     * @param propertyID the URL of the property to remove
     * @return the value of the removed property
     */
    public PropertyValue removeValue(String propertyID) {
        return removeValue(new ID(propertyID));
    }

    /**
     * Replace existing property values by the specified value.
     * 
     * @param propertyID the property to set
     * @param object the value of the property to set
     */
    public void setValue(ID propertyID, Object object) {
        if (propertyID == null) return;
        Map map = getMap(true);
        PropertyValue v;
        if (RDF_ID.equals(propertyID) || RDF_ABOUT.equals(propertyID)) {
            ID oldId = fId;
            fId = PropertyValue.toID(object);
            fModel.remapResource(oldId, fId, this);
            Reference reference = getReference();
            fAbout = RDF_ABOUT.equals(propertyID);
            reference.setID(fId);
            v = reference;
        } else {
            v = toPropertyValue(object);
            map.put(propertyID, v);
        }
        fModel.registerProperty(propertyID);
    }

    /**
     * Replace existing property values by the specified value.
     * 
     * @param propertyID the property to set
     * @param object the value of the property to set
     */
    public void setValue(String propertyID, Object object) {
        setValue(new ID(propertyID), object);
    }

    /**
     * Adds a new value of the specified property
     * 
     * @param propertyID the property to add
     * @param value the value of the property to add
     */
    public void setValues(ID propertyID, List values) {
        Container container = fModel.newContainer();
        container.setValues(values);
        fMap.put(propertyID, container);
    }

    /**
     * Copies all property values from the specified resource to this resource.
     * This method does not replace existing property values; it just adds new
     * values.
     * 
     * @param resource the resource used as a source of properties to add
     */
    public void setValues(Resource resource) {
        clear();
        ID[] properties = resource.getPropertyIDs();
        for (int i = 0; i < properties.length; i++) {
            PropertyValue propertyValue = resource.getValue(properties[i]);
            fMap.put(properties[i], propertyValue);
        }
    }

    /**
     * Adds a new value of the specified property
     * 
     * @param propertyID the identifier of the property to add
     * @param value the value of the property to add
     */
    public void setValues(String propertyID, List values) {
        setValues(new ID(propertyID), values);
    }
}

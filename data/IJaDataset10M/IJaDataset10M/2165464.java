package org.tinymarbles.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.tinymarbles.exception.UnknownAttributeException;
import org.tinymarbles.model.value.PRef;
import org.tinymarbles.model.value.PSet;

/**
 * This class represents a persistent object in the repository.
 *
 * @author duke
 *
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PObject extends PNode<PObject> {

    @Column(unique = true, nullable = true, updatable = true)
    private String systemId;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @MapKey(name = "name")
    @Cascade(value = { CascadeType.REMOVE, CascadeType.EVICT })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Map<String, PValue> values = new HashMap<String, PValue>();

    @ManyToOne(fetch = FetchType.LAZY)
    private PType type;

    @SuppressWarnings("unused")
    @Version
    private Integer version;

    /**
	 * Empty constructor for Hibernate
	 *
	 */
    protected PObject() {
    }

    /**
	 * Protected constructor for subclasses
	 *
	 * @param type The type that owns this instance
	 * @param systemId the object's system ID (can be null)
	 * @see PType#newInstance(String)
	 */
    protected PObject(PType type, String systemId) {
        this.type = type;
        this.systemId = systemId;
    }

    /**
	 * Gets the persistent version of this type
	 *
	 * @return a version number, if persisted
	 */
    public Integer getVersion() {
        return version;
    }

    /**
	 * Get the systemId, a unique identifier for this instance
	 * @return the systemId
	 */
    public String getSystemId() {
        return this.systemId;
    }

    /**
	 * Sets the systemId of this object. NOTE:
	 *
	 * @param systemId
	 */
    public void setSystemId(String systemId) {
        this.setDirty();
        this.systemId = systemId;
    }

    /**
	 * Gets the object's type
	 * @return the object's type
	 */
    public PType getType() {
        return this.type;
    }

    /**
	 * Sets the object's type
	 * @param type the type to set
	 */
    @SuppressWarnings("unused")
    private void setType(PType type) {
        this.type = type;
    }

    /**
	 * Gets the map of values. Used only for persistence.
	 * @return the persistent map of values.
	 * @see #values()
	 * @see #get(String)
	 * @see #hasAttribute(String)
	 */
    protected Map<String, PValue> getValues() {
        return this.values;
    }

    protected final void setValues(Map<String, PValue> values) {
        this.values = values;
    }

    /**
	 * Sets a value to an attribute. <code>value</code> must be compatible with the attribute type, or you will get a
	 * <code>ClassCastException</code>.
	 * <p>
	 * An exception is thrown if the attribute called <code>name</code> doesn't exist.
	 *
	 * Returns this instance to allow chaining
	 *
	 * @param name the name of the attribute.
	 * @param value the value to be set.
	 * @throws UnknownAttributeException
	 * @return this instance
	 */
    @SuppressWarnings("unchecked")
    public PObject set(String name, Object value) throws UnknownAttributeException {
        if (!this.hasAttribute(name)) {
            throw new UnknownAttributeException(this.getType(), name);
        }
        this.getPValue(name).setValue(value);
        this.getPValue(name).setDirty(true);
        this.setDirty();
        return this;
    }

    /**
	 * Gets the value of the attribute called <code>name</code>
	 *
	 * @param <V> the type of the value, as determined by the attribute
	 * @param name the name of the attribute
	 * @return the value of the attribute or <br>
	 * <b>null</b> if the attribute does not exist
	 */
    @SuppressWarnings("unchecked")
    public <V> V get(String name) {
        if (this.hasAttribute(name)) {
            return (V) this.getPValue(name).getValue();
        }
        throw new UnknownAttributeException(this.getType(), name);
    }

    /**
	 * Adds a new PObject element to the Set called <code>name</code>. <code>name</code> must be obligatorily the
	 * name of an attribute of type Set.
	 *
	 * @param name the name of the attribute
	 * @param el the element to be added
	 * @return true if this set did not already contain the specified element.
	 */
    public boolean add(String name, PObject el) {
        boolean result = this.getSet(name).add(el);
        if (result) {
            this.setDirty();
        }
        return result;
    }

    /**
	 * Adds a new PObject element to the Set called <code>name</code>. <code>name</code> must be obligatorily the
	 * name of an attribute of type Set.
	 *
	 * @param name the name of the attribute
	 * @param el the element to be added
	 * @param inverseProperty the inverse property to be added
	 * @return true if this set did not already contain the specified element.
	 */
    public boolean add(String name, PObject el, String inverseProperty) {
        boolean result = this.add(name, el);
        if (result) {
            PValue inverse = el.getPValue(inverseProperty);
            if (inverse instanceof PSet) {
                PSet p = (PSet) inverse;
                p.add(this);
            } else if (inverse instanceof PRef) {
                PRef p = (PRef) inverse;
                p.setValue(this);
            } else {
                throw new IllegalArgumentException("The inverse property must be a PSet or PRef");
            }
        }
        return result;
    }

    /**
	 * Removes a PObject element from the Set called <code>name</code>. <code>name</code> must be obligatorily the
	 * name of an attribute of type Set.
	 *
	 * @param name the name of the attribute
	 * @param el the element to be removed
	 * @return true if the set contained the specified element.
	 */
    public boolean remove(String name, PObject el) {
        boolean result = this.getSet(name).remove(el);
        if (result) {
            this.setDirty();
        }
        return result;
    }

    /**
	 * Removes a PObject element from the Set called <code>name</code>. <code>name</code> must be obligatorily the
	 * name of an attribute of type Set.
	 *
	 * @param name the name of the attribute
	 * @param el the element to be removed
	 * @param inverseProperty the inverse property to be removed
	 * @return true if the set contained the specified element.
	 */
    public boolean remove(String name, PObject el, String inverseProperty) {
        boolean result = this.remove(name, el);
        if (result) {
            PValue inverse = el.getPValue(inverseProperty);
            if (inverse instanceof PSet) {
                PSet p = (PSet) inverse;
                p.remove(this);
            } else if (inverse instanceof PRef) {
                PRef p = (PRef) inverse;
                p.setValue(null);
            } else {
                throw new IllegalArgumentException("The inverse property must be a PSet or PRef");
            }
        }
        return result;
    }

    /**
	 * Returns <tt>true</tt> if this object has an attribute with name <code>name</code>. This method gives no
	 * guarantee about the value of the attribute, which can be null.
	 *
	 * @param name the name of the attribute
	 * @return <tt>true</tt> if the attribute is present
	 */
    public boolean hasAttribute(String name) {
        return this.getType().hasAttribute(name);
    }

    /**
	 * Returns a PSet associated with the attribute called <code>name</code>. <code>name</code> must be obligatorily
	 * the name of an attribute of type Set.
	 *
	 * @param name
	 * @return the PSet of the attribute
	 * @throws UnknownAttributeException
	 */
    public PSet getSet(String name) throws UnknownAttributeException {
        this.setDirty();
        return (PSet) this.getPValue(name);
    }

    /**
	 * Returns the PValue associated with the attribute called <code>name</code>
	 *
	 * @param name the name of the attribute
	 * @return the value of the attribute
	 * @throws UnknownAttributeException if attribute doesn't exist
	 */
    public PValue getPValue(String name) throws UnknownAttributeException {
        PValue result = this.getValues().get(name);
        if (result == null) {
            result = this.getType().getAttribute(name).newValue(this);
            this.getValues().put(name, result);
        }
        return result;
    }

    /**
	 * Creates a map with keys being the names of its attributes and values being the content of its
	 * persistent values. Thus, the following code is valid:
	 *
	 * <pre><code>
	 * Map<String,Object> map = obj.toMap();
	 * assert map.get("key") == obj.get("key");
	 * </code></pre>
	 *
	 * The map is <strong>not</strong> backed by the object. Changes to the map affect the map only, but
	 * collections are not cloned.
	 *
	 * @return map with attribute name and value
	 */
    public Map<String, Object> toMap() {
        Map<String, Object> newMap = new HashMap<String, Object>();
        for (String attName : this.getType().getAttributeNames()) {
            PValue pv = this.getValues().get(attName);
            newMap.put(attName, pv == null ? null : pv.getValue());
        }
        return newMap;
    }

    /**
	 * Popules this PObject from a Map. The object looks for string keys in the map that correspond
	 * to its attribute names. The values must be of the type expected by the attribute. Entries in the
	 * map that don't correspond to attribute names are ignored.
	 *
	 * @param map a map of attribute names and values
	 * @see #toMap()
	 */
    public void fromMap(Map<String, Object> map) {
        for (String name : this.getType().getAttributeNames()) {
            this.set(name, map.get(name));
        }
    }

    /**
	 * Builds a description based on the attributes defined for this object.
	 * @return a formated description string
	 */
    public String describe() {
        StringBuilder sb = new StringBuilder(this.toString());
        sb.append("[");
        if (this.getParent() != null) {
            sb.append("parent=").append(this.getParent());
        }
        for (PValue pv : this.values()) {
            sb.append(",").append(pv.describe());
        }
        sb.append("]");
        return sb.toString();
    }

    /**
	 * Gets an ordered list with all the PValues associated with this instance.
	 * The list is not immutable and the order reflects the type's ordering of attributes.
	 * @return an unmodifiable list of PValues
	 * @see PType#attributes()
	 * @see #getValues()
	 */
    @SuppressWarnings("unchecked")
    public List<PValue> values() {
        List<PAttribute> attributes = this.getType().attributes();
        List<PValue> list = new ArrayList<PValue>(attributes.size());
        for (PAttribute att : attributes) {
            list.add(this.getPValue(att.getName()));
        }
        return Collections.unmodifiableList(list);
    }

    /**
	 * Returns a string representation of this instance.
	 * @see #describe()
	 */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append("(").append(this.getType().getName());
        if (this.getId() != null) {
            sb.append("#").append(this.getId());
        }
        if (this.getSystemId() != null) {
            sb.append(", systemId=").append(this.getSystemId());
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = this.getType().hashCode();
        if (this.getSystemId() != null) {
            result = result * 29 + this.getSystemId().hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof PObject) {
            PObject other = (PObject) obj;
            if (this.getType().equals(other.getType())) {
                if (this.getId() == null) {
                    if (other.getId() == null) {
                        if (this.getSystemId() == null) {
                            if (other.getSystemId() == null) {
                                return this.getValues().equals(other.getValues());
                            }
                        } else {
                            return this.getSystemId().equals(other.getSystemId());
                        }
                    }
                } else {
                    return (this.getId().equals(other.getId()));
                }
            }
        }
        return false;
    }

    /**
	 * Gets a list of values which are dirty (i.e. not persisted).
	 * Used to track values that are new since the object was created or loaded.
	 * Override with care.
	 * @return the list of dirty PValues ov this instance. Unlike {@link #values()}, this method does <strong>not</strong> return an ordered list.
	 * @see #values()
	 * @see #getValues()
	 */
    public List<PValue> listDirtyValues() {
        List<PValue> result = new ArrayList<PValue>();
        for (PValue pv : this.getValues().values()) {
            if (pv.isDirty()) {
                result.add(pv);
            }
        }
        return result;
    }

    void remove(PValue pv) {
        this.getValues().remove(pv.getName());
    }

    /**
	 * Set this instance with a dirty instance. Useful when commit is called without call save
	 *
	 */
    protected void setDirty() {
        this.getType().addDirtyInstance(this);
    }
}

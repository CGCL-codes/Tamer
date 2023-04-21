package ch.bbv.mda.pim;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.updates.DataObjectUpdate;
import ch.bbv.dog.updates.*;

/**
 * The code type defines the set of values belonging to a reference code type. 
 * The business object framework defines two types of reference codes: regular 
 * ones and hierarchical ones.
 * @version $Revision: 1.11 $
 * @author Marcel Baumann
 */
public class CodeType implements DataObject {

    /**
   * Serial version identifier coded as back compatibility date.
   */
    private static final long serialVersionUID = 99990101;

    /**
   * The qualified name of the class defining the reference code type.
   */
    private String qualifiedName;

    /**
   * Flag indicating if the reference code is hierarchical or not.
   */
    private boolean hierarchical;

    private StorageKind storageKind;

    private String storageKindName;

    /**
   * The code values defined for the code type are the set of legal values for 
   * this type.
   */
    private List<CodeValue> values;

    /** 
   * Unique identifier of the data object instance distinguishing it from all
   * other instances of the same data object type.
   */
    private Integer id;

    /** 
   * Flag indicated if the data object is dirty or not.
   */
    private boolean modified;

    /** 
   * Timestamp used to implement optimistic locking. 
   */
    private Timestamp timestamp;

    /**
   * The update object collecting all modifications made to this instance.
   * After saving the changes made to the data object, the individual modifications
   * get flushed from the update object. 
   */
    protected transient DataObjectUpdate update;

    /** 
   * Default constructor of the class necessary for Java bean convention. The 
   * object identifier is set to null to indicate it is a new object.
   */
    public CodeType() {
        values = new ArrayList<CodeValue>();
    }

    /** 
   * Returns the unique identifier of the data object instance.
   * @return the identifier of the instance.
   * @see #setId(Integer)
   */
    public Integer getId() {
        return id;
    }

    /** 
   * Sets the identifier of the data object instance.
   * @param id new identifier of the instance.
   * @see #getId()
   */
    public void setId(Integer id) {
        this.id = id;
    }

    /** 
   * Returns the flag indicating if the data object instance was modified.
   * @return true if the object was modified otherwise false.
   * @see #setModified
   */
    public boolean isModified() {
        return modified;
    }

    /** 
   * Sets the flag indicating if the data object instance was modified.
   * @param modified new value of the flag.
   * @see #isModified
   */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /** 
   * Convenience method indicating if the instance is a new one or not.
   * @return true if the object is new otherwise false.
   */
    public boolean isNew() {
        return (getId() == null);
    }

    /** 
   * Returns the timestamp of the data object instance.
   * @return the timestamp of the last persistent modification.
   * @see #setTimestamp(Timestamp)
   */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /** 
   * Sets the timestamp of the data object instance.
   * @param timestamp timestamp of the instance.
   * @see #getTimestamp()
   */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /** 
   * Returns the current value of the simple property qualifiedName.
   * @return the value of the simple property qualifiedName.
   * @see #setQualifiedName(String)
   */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /** 
   * Sets the new value of the simple property qualifiedName.
   * @param qualifiedName new value of the property.
   * @see #getQualifiedName()
   */
    public void setQualifiedName(String qualifiedName) {
        if (((this.qualifiedName != null) && !this.qualifiedName.equals(qualifiedName)) || ((this.qualifiedName == null) && (qualifiedName != null))) {
            this.qualifiedName = qualifiedName;
            update.addUpdate(new PrimitiveFieldUpdate("qualifiedName", qualifiedName));
            setModified(true);
        }
    }

    /** 
   * Returns the current value of the simple property hierarchical.
   * @return the value of the simple property hierarchical.
   * @see #setHierarchical(boolean)
   */
    public boolean isHierarchical() {
        return hierarchical;
    }

    /** 
   * Sets the new value of the simple property hierarchical.
   * @param hierarchical new value of the property.
   * @see #isHierarchical()
   */
    public void setHierarchical(boolean hierarchical) {
        if (this.hierarchical != hierarchical) {
            this.hierarchical = hierarchical;
            update.addUpdate(new PrimitiveFieldUpdate("hierarchical", hierarchical));
            setModified(true);
        }
    }

    /** 
   * Returns the current value of the simple property storageKind.
   * @return the value of the simple property storageKind.
   * @see #setStorageKind(StorageKind)
   */
    public StorageKind getStorageKind() {
        return storageKind;
    }

    /** 
   * Sets the new value of the simple property storageKind.
   * @param storageKind new value of the property.
   * @see #getStorageKind()
   */
    public void setStorageKind(StorageKind storageKind) {
        if (((this.storageKind != null) && !this.storageKind.equals(storageKind)) || ((this.storageKind == null) && (storageKind != null))) {
            this.storageKind = storageKind;
            this.storageKindName = storageKind.getShortDescription();
            update.addUpdate(new PrimitiveFieldUpdate("storageKind", storageKind));
            setModified(true);
        }
    }

    /** 
   * Gets all elements of the relation values.
   * @return List of all children.
   */
    public List<CodeValue> getValues() {
        return values;
    }

    /** 
   * Returns the element at the specified position in the indexed property 
   * values.
   * @param index index of element to return. 
   * @return the element at the specified position in this list.
   * @see #setValue(int, CodeValue)
   * @see java.util.List#get(int)
   */
    public CodeValue getValue(int index) {
        return values.get(index);
    }

    /** 
   * Appends all of the elements in the specified collection to the end of 
   * this list, in the order that they are returned by the specified 
   * collection's iterator. The behavior of this operation is unspecified if 
   * the specified collection is modified while the operation is in progress. 
   * @param c collection whose elements are to be added to this list. 
   * @return true if this list changed as a result of the call. 
   * @see java.util.List#addAll(Collection)
   */
    public boolean addValues(Collection<CodeValue> c) {
        setModified(true);
        return values.addAll(c);
    }

    /** 
   * Inserts the given child at the select position in the relation values.
   * @param index index at which the specified element is to be inserted.
   * @param element element to be inserted.
   * @see #getValue(int)
   * @see java.util.List#add(int, Object)
   */
    public void setValue(int index, CodeValue element) {
        update.addUpdate(new ItemInsertionUpdate(element.getUpdate(), "values", index));
        setModified(true);
        values.add(index, element);
    }

    /** 
   * Appends the specified element to the end of this list in the relation 
   * values.
   * @param element to be appended to this list.
   * @return true (as per the general contract of the Collection.add method).   
   * @see java.util.List#add(Object)
   * @pre element != null
   */
    public boolean addValue(CodeValue element) {
        update.addUpdate(new ItemAdditionUpdate(element.getUpdate(), "values"));
        setModified(true);
        return values.add(element);
    }

    /** 
   * Removes the first occurrence in this list of the specified element 
   * If this list does not contain the element, it is unchanged. More formally, 
   * removes the element with the lowest index i such that 
   * (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists). 
   * @param element element to be removed from this list, if present. 
   * @return true if this list contained the specified element. 
   * @see java.util.List#remove(Object)
   */
    public boolean removeValue(CodeValue element) {
        boolean removed = values.remove(element);
        if (removed) {
            update.addUpdate(new ItemRemovalUpdate(element.getUpdate(), "values"));
            setModified(true);
        }
        return removed;
    }

    /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all its relevant children. A relevant
   * child is always another data object.
   * @param visitor visitor to act on the data object.
   * @pre visitor != null
   */
    public void accept(Visitor visitor) {
        assert (visitor != null);
        visitor.setDepth(visitor.getDepth() + 1);
        visitor.visit(this);
        traverse(visitor);
        visitor.setDepth(visitor.getDepth() - 1);
    }

    /** 
   * Creates and returns a deep copy of the object and its descendants. The 
   * properties containing keys are not cloned because they are immutable.
   * @return a deep copy of the instance.
   */
    public CodeType copy() {
        CodeType clone = null;
        try {
            clone = (CodeType) clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
        copy(clone);
        clone.setModified(isModified());
        return clone;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    public void addVetoableChangeListener(VetoableChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    public void addVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    public void removeVetoableChangeListener(String propertyName, VetoableChangeListener listener) {
        throw new UnsupportedOperationException();
    }

    /**
   * Convenience method to set the value of a foreign key.
   * @param bean object which property should be set.
   * @param property property to be set.
   * @param key key identifier to set.
   * @pre (bean != null) && (property != null) && (key != null)
   */
    protected final void setForeignKey(DataObject bean, String property, Integer key) {
        assert ((bean != null) && (property != null));
        try {
            Field field = bean.getClass().getDeclaredField(property);
            if (field != null) {
                field.setAccessible(true);
                field.set(bean, key);
            }
        } catch (NoSuchFieldException e) {
            assert (false);
        } catch (IllegalArgumentException e) {
            assert (false);
        } catch (IllegalAccessException e) {
            assert (false);
        }
    }

    /** 
   * Copies deeply all the attributes of the class and its ancestors.
   * @param clone the copy which deep copy should be generated.
   */
    protected void copy(CodeType clone) {
        clone.values = new ArrayList<CodeValue>();
        for (int i = 0; i < values.size(); i++) {
            clone.values.add(values.get(i).copy());
        }
    }

    /** 
   * Implements the visitor pattern in the data object class. The accept
   * method dispatches the visitor to all relevant children of the ancestors
   * and its children. A relevant child is always another data object.
   * @param visitor visitor to act on the data object.
   */
    protected void traverse(Visitor visitor) {
        for (CodeValue item : getValues()) {
            item.accept(visitor);
        }
    }

    public DataObjectUpdate getUpdate() {
        return update;
    }

    public void resetUpdate() {
        update = new DataObjectUpdate(this);
    }
}

package org.siberia.type.attic;

import java.beans.PropertyVetoException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.siberia.type.SibConfigurable;
import org.siberia.type.SibType;
import org.siberia.type.annotation.bean.Bean;
import org.siberia.type.annotation.bean.BeanProperty;
import org.siberia.type.annotation.bean.ConfigurationItem;
import org.siberia.type.event.ContentChangeEvent;
import org.siberia.type.event.ContentClearedEvent;

/**
 *
 * Abstract class for all classes that a collection of SibType instances<br>
 * This class is configurable and if it is configurable, then, the meta flags can be modified
 *
 * @author alexis
 */
@Bean(name = "SibCollection", internationalizationRef = "org.siberia.rc.i18n.type.SibCollection", expert = false, hidden = false, preferred = true, propertiesClassLimit = Object.class, methodsClassLimit = Object.class)
public abstract class SibCollection<T extends AbstractCollection<SibType>> extends SibConfigurable implements Collection {

    /** property names */
    public static final String PROPERTY_EDIT_AUTHORIZATION = "edit_auth";

    public static final String PROPERTY_CREATE_AUTHORIZATION = "create_auth";

    public static final String PROPERTY_REMOVE_AUTHORIZATION = "remove_auth";

    public static final String PROPERTY_CONTENT = "content";

    public static final String PROPERTY_CONTENT_AS_CHILD = "contentAsChild";

    /** indicates if edit is allowed */
    @BeanProperty(name = PROPERTY_EDIT_AUTHORIZATION, internationalizationRef = "org.siberia.rc.i18n.property.SibCollection_edit_auth", expert = false, hidden = false, preferred = true, bound = true, constrained = true, writeMethodName = "setEditAuthorization", writeMethodParametersClass = { boolean.class }, readMethodName = "isEditAuthorized", readMethodParametersClass = {  })
    @ConfigurationItem
    private boolean couldEdit = true;

    /** indicates if create is allowed */
    @BeanProperty(name = PROPERTY_CREATE_AUTHORIZATION, internationalizationRef = "org.siberia.rc.i18n.property.SibCollection_create_auth", expert = false, hidden = false, preferred = true, bound = true, constrained = true, writeMethodName = "setCreateAuthorization", writeMethodParametersClass = { boolean.class }, readMethodName = "isCreateAuthorized", readMethodParametersClass = {  })
    @ConfigurationItem
    private boolean couldCreate = true;

    /** indicates if remove is allowed */
    @BeanProperty(name = PROPERTY_REMOVE_AUTHORIZATION, internationalizationRef = "org.siberia.rc.i18n.property.SibCollection_remove_auth", expert = false, hidden = false, preferred = true, bound = true, constrained = true, writeMethodName = "setRemoveAuthorization", writeMethodParametersClass = { boolean.class }, readMethodName = "isRemoveAuthorized", readMethodParametersClass = {  })
    @ConfigurationItem
    private boolean couldRemove = true;

    /** indicates if edit is allowed */
    @BeanProperty(name = PROPERTY_CONTENT_AS_CHILD, internationalizationRef = "org.siberia.rc.i18n.property.SibCollection_contentAsChild", expert = false, hidden = false, preferred = true, bound = true, constrained = true, writeMethodName = "setContentItemAsChild", writeMethodParametersClass = { boolean.class }, readMethodName = "isContentItemAsChild", readMethodParametersClass = {  })
    @ConfigurationItem
    private boolean contentItemIsChild = true;

    /** superclass of allowed instantiations */
    private Class contentType = SibType.class;

    /** Collection owned by this instance */
    protected Collection<SibType> collection = null;

    /** create a new instance of SibCollection that could contains SibType<br>
     *  Items can be edited, added and removed.
     */
    public SibCollection() {
        super();
    }

    public Collection get() {
        return this;
    }

    /** return true if content is considered as child
     *  @return a boolean
     */
    public boolean isContentItemAsChild() {
        return this.contentItemIsChild;
    }

    /** indicates if content item as to be considered as children
     *  @param contentItemAsChild true to indicate that the item contained by this collection have to be considered as child
     *          of this collection
     */
    public void setContentItemAsChild(boolean contentItemAsChild) throws PropertyVetoException {
        if (contentItemAsChild != this.isContentItemAsChild()) {
            this.fireVetoableChange(PROPERTY_CONTENT_AS_CHILD, !contentItemAsChild, contentItemAsChild);
            this.checkReadOnlyProperty(PROPERTY_CONTENT_AS_CHILD, !contentItemAsChild, contentItemAsChild);
            this.contentItemIsChild = contentItemAsChild;
            if (this.isContentItemAsChild()) {
                Iterator items = this.iterator();
                while (items.hasNext()) {
                    SibType current = (SibType) items.next();
                    if (current != null) {
                        this.addChildElement(current);
                    }
                }
            } else {
                for (int i = 0; i < this.getChildrenCount(); i++) {
                    SibType current = this.getChildAt(i);
                    if (current != null) {
                        if (this.contains(current)) {
                            this.removeChildElement(current);
                        }
                    }
                }
            }
            this.firePropertyChange(PROPERTY_CONTENT_AS_CHILD, !this.isContentItemAsChild(), this.isContentItemAsChild());
        }
    }

    /** return the number of children for this object
     *  @return the number of children for this object
     */
    @Override
    public int getChildrenCount() {
        if (this.isContentItemAsChild()) return super.getChildrenCount(); else return 0;
    }

    /** return the allowed type of items that could be contained by the collection
     *  @return the allowed type of items that could be contained by the collection
     */
    public Class getAllowedClass() {
        return this.contentType;
    }

    /** initialize the allowed type of items that could be contained by the collection
     *  @param cl the allowed type of items that could be contained by the collection
     */
    public void setAllowedClass(Class cl) {
        this.contentType = cl;
    }

    /** tell if the item can be added to the collection
     *  @param item 
     *  @return true if the item can be added to the collection
     */
    public boolean itemAllowed(Object item) {
        if (this.getAllowedClass() != null && item != null) {
            if ((this.getAllowedClass()).isAssignableFrom(item.getClass())) return true;
        }
        return false;
    }

    /** initialize the collection */
    protected abstract T initCollection();

    /** indicate if the list contains some elements
     *  @return true if the list contains some elements
     */
    public boolean isEmpty() {
        if (this.collection == null) {
            return true;
        }
        return (this.collection.isEmpty());
    }

    /** return the size of the collection
     *  @return the size of the collection
     */
    public int size() {
        if (this.collection == null) return 0;
        return this.collection.size();
    }

    /** indicates if the collection contains the given element
     *  @param o an object
     *  @return true if the collection contains the given element
     */
    public boolean contains(Object o) {
        if (this.collection == null) return false;
        return this.collection.contains(o);
    }

    /** return an iterator over the element of the collection
     *  @return an iterator over the element of the collection
     */
    public Iterator<? extends SibType> iterator() {
        if (this.collection == null) return Collections.EMPTY_LIST.iterator();
        return this.collection.iterator();
    }

    /** return an array with all element of the collection
     *  @return an array with all element of the collection
     */
    public Object[] toArray() {
        if (this.collection == null) return new Object[] {};
        return this.collection.toArray();
    }

    /** return an array with all element of the collection
     *  @return an array with all element of the collection
     */
    public Object[] toArray(Object[] a) {
        if (this.collection == null) return new Object[] {};
        return this.collection.toArray(a);
    }

    /** add a new element 
     *  @param o the element to add
     *  @return true if the collection was modified
     */
    public boolean add(Object o) {
        if (o != null) {
            if (this.itemAllowed(o)) {
                if (o instanceof SibType) {
                    SibType item = (SibType) o;
                    if (this.collection == null) this.collection = this.initCollection();
                    item.setParent(this);
                    this.collection.add(item);
                    this.firePropertyChange(new ContentChangeEvent(this, PROPERTY_CONTENT, ContentChangeEvent.ADD, this.indexOfChild(item), item));
                    this.addChildElement(item);
                    return true;
                }
            }
        }
        return false;
    }

    /** remove an element
     *  @param o the object to remove
     *  @return true if the collection was modified
     */
    public boolean remove(Object o) {
        if (o != null && this.collection != null) {
            if (o instanceof SibType) {
                SibType item = (SibType) o;
                int index = this.indexOfChild(item);
                item.setParent(null);
                this.removeChildElement(item);
                item.clearPropertyChangeListenerList();
                this.firePropertyChange(new ContentChangeEvent(this, PROPERTY_CONTENT, ContentChangeEvent.REMOVE, index, item));
                return this.collection.remove(o);
            }
        }
        return false;
    }

    /** indicates if all elements of the given collection appears in the collection
      * @param c a Collection
      * @return true if all elements of the given collection appears in the collection
      */
    public boolean containsAll(Collection c) {
        if (c != null && this.collection != null) {
            return this.collection.containsAll(c);
        }
        return false;
    }

    /** add a collection
     *  @param c a collection to add
     *  @return true if the collection was modified
     */
    public boolean addAll(Collection c) {
        if (c != null) {
            if (this.collection == null) this.collection = this.initCollection();
            boolean modified = false;
            List items = new ArrayList(c.size());
            for (Iterator it = c.iterator(); it.hasNext(); ) {
                Object object = it.next();
                boolean added = this.add(object);
                if (added && !modified) {
                    modified = true;
                    items.add(object);
                }
            }
            if (modified) {
                int[] positions = new int[items.size()];
                int index = 0;
                for (Iterator<SibType> it2 = items.iterator(); it2.hasNext(); ) positions[index++] = this.indexOfChild(it2.next());
                this.firePropertyChange(new ContentChangeEvent(this, PROPERTY_CONTENT, ContentChangeEvent.ADD, positions, (SibType[]) items.toArray(new SibType[] {})));
            }
            return modified;
        }
        return false;
    }

    /** remove a collection
     *  @param c a collection to remove
     *  @return true if the collection was modified
     */
    public boolean removeAll(Collection c) {
        if (c != null && this.collection != null) {
            boolean modified = false;
            List items = new ArrayList(c.size());
            for (Iterator it = c.iterator(); it.hasNext(); ) {
                Object object = it.next();
                boolean removed = this.remove(object);
                if (removed && !modified) {
                    modified = true;
                    items.add(object);
                }
            }
            if (modified) {
                int[] positions = new int[items.size()];
                int index = 0;
                for (Iterator it2 = items.iterator(); it2.hasNext(); ) positions[index++] = this.indexOfChild(it2.next());
                this.firePropertyChange(new ContentChangeEvent(this, PROPERTY_CONTENT, ContentChangeEvent.REMOVE, positions, (SibType[]) items.toArray(new SibType[] {})));
            }
            return modified;
        }
        return false;
    }

    /** retains all the element contained by the given collection
     *  @param c a collection
     *  @return true if the collection was modified
     */
    public boolean retainAll(Collection c) {
        if (c == null) {
            if (!this.isEmpty()) {
                this.clear();
                return true;
            }
        } else if (c.size() == 0 && !this.isEmpty()) {
            this.clear();
            return true;
        }
        if (!this.isEmpty()) {
            boolean modified = false;
            List items = null;
            Object currentElt = null;
            for (Iterator it = this.collection.iterator(); it.hasNext(); ) {
                currentElt = it.next();
                if (c.contains(currentElt)) {
                    boolean removed = this.collection.remove(currentElt);
                    if (removed && !modified) {
                        modified = true;
                        if (items == null) items = new ArrayList();
                        items.add(currentElt);
                    }
                }
            }
            if (modified) {
                int[] positions = new int[items.size()];
                int index = 0;
                for (Iterator it2 = items.iterator(); it2.hasNext(); ) positions[index++] = this.indexOfChild(it2.next());
                this.firePropertyChange(new ContentChangeEvent(this, PROPERTY_CONTENT, ContentChangeEvent.REMOVE, positions, (SibType[]) items.toArray(new SibType[] {})));
            }
            return modified;
        }
        return false;
    }

    /** clear the collection */
    public void clear() {
        if (this.collection != null) {
            this.collection.clear();
            this.firePropertyChange(new ContentClearedEvent(this, PROPERTY_CONTENT));
        }
    }

    /** indicates if item edition is allowed
     *  @return true if item edition is allowed
     */
    public boolean isEditAuthorized() {
        return couldEdit;
    }

    /** tell if item edition is allowed<br>
     *  The list has to be configurable
     *  @param couldEdit true if item edition is allowed
     */
    public void setEditAuthorization(boolean couldEdit) throws PropertyVetoException {
        if (this.isConfigurable()) {
            this.fireVetoableChange(PROPERTY_EDIT_AUTHORIZATION, this.isEditAuthorized(), couldEdit);
            this.checkReadOnlyProperty(PROPERTY_EDIT_AUTHORIZATION, this.isEditAuthorized(), couldEdit);
            this.couldEdit = couldEdit;
            this.firePropertyChange(PROPERTY_EDIT_AUTHORIZATION, !this.couldEdit, this.couldEdit);
        }
    }

    /** indicates if creating item is allowed
     *  @return true if creating item is allowed
     */
    public boolean isCreateAuthorized() {
        return couldCreate;
    }

    /** tell if item creation is allowed<br>
     *  The list has to be configurable
     *  @param couldCreate true if item creation is allowed
     */
    public void setCreateAuthorization(boolean couldCreate) throws PropertyVetoException {
        if (this.isConfigurable()) {
            this.fireVetoableChange(PROPERTY_CREATE_AUTHORIZATION, this.isCreateAuthorized(), couldCreate);
            this.checkReadOnlyProperty(PROPERTY_CREATE_AUTHORIZATION, this.isCreateAuthorized(), couldCreate);
            this.couldCreate = couldCreate;
            this.firePropertyChange(PROPERTY_CREATE_AUTHORIZATION, !this.couldCreate, this.couldCreate);
        }
    }

    /** indicates if removing item is allowed
     *  @return true if removing item is allowed
     */
    public boolean isRemoveAuthorized() {
        return couldRemove;
    }

    /** tell if removing item is allowed<br>
     *  The list has to be configurable
     *  @param couldRemove true if removing item is allowed
     */
    public void setRemoveAuthorization(boolean couldRemove) throws PropertyVetoException {
        if (this.isConfigurable()) {
            this.fireVetoableChange(PROPERTY_REMOVE_AUTHORIZATION, this.isRemoveAuthorized(), couldRemove);
            this.checkReadOnlyProperty(PROPERTY_REMOVE_AUTHORIZATION, this.isRemoveAuthorized(), couldRemove);
            this.couldRemove = couldRemove;
            this.firePropertyChange(PROPERTY_REMOVE_AUTHORIZATION, !this.couldRemove, this.couldRemove);
        }
    }

    public String valueAsString() {
        StringBuffer value = new StringBuffer();
        if (this.collection != null) {
            Iterator<? extends SibType> it = this.iterator();
            while (it.hasNext()) {
                value.append(it.next().valueAsString());
                if (it.hasNext()) value.append(" ,");
            }
        }
        return value.toString();
    }

    /** return an html representation fo the object without tag html
     *  @return an html representation fo the object without tag html
     */
    public String getHtmlContent() {
        return "   allowed items : " + this.getAllowedClass().getName() + "<br>" + "   could edit    ? " + this.isEditAuthorized() + "<br>" + "   could create  ? " + this.isCreateAuthorized() + "<br>" + "   could remove  ? " + this.isRemoveAuthorized() + "<br>" + super.getHtmlContent();
    }

    public Object clone() {
        SibCollection other = (SibCollection) super.clone();
        other.setAllowedClass(this.getAllowedClass());
        try {
            other.setEditAuthorization(this.isEditAuthorized());
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }
        try {
            other.setCreateAuthorization(this.isCreateAuthorized());
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }
        try {
            other.setRemoveAuthorization(this.isRemoveAuthorized());
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }
        Iterator<? extends SibType> it = this.iterator();
        SibType current = null;
        while (it.hasNext()) {
            current = it.next();
            if (current != null) {
                if (current instanceof SibType) {
                    other.add(((SibType) current).clone());
                }
            }
        }
        return other;
    }
}

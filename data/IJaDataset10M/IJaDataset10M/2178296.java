package com.softntic.meetmemanager.data.dao;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.ComponentType;
import org.hibernate.type.Type;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.MethodProperty;

/**
 * Lazy, almost full-featured, general purpose Hibernate entity container. Makes
 * lots of queries, but shouldn't consume too much memory.
 * <p>
 * HbnContainer is developed and tested with session-per-request pattern, but
 * should work with other session handling mechanisms too. To abstract away
 * session handling, user must provide an implementation of SessionManager
 * interface, via HbnContainer fetches reference to Session instance whenever it
 * needs it. Session returned by HbnContainer is expected to be open.
 * 
 * <p>
 * In in its constructor it will only need entity class type (Pojo) and a
 * SessionManager.
 * <p>
 * HbnContainer also expects that identifiers are auto generated. This matters
 * only if HbnContainer is used to create new entities.
 * <p>
 * Note, container caches size, firstId, lastId to be much faster with large
 * datasets. TODO make this caching optional, actually should trust on
 * Hibernates and DB engines query caches.
 * 
 * <p>
 * See http://vaadin.com/wiki/-/wiki/Main/Using%20Hibernate%20with%20Vaadin?
 * p_r_p_185834411_title=Using%20Hibernate%20with%20Vaadin for a working example
 * application.
 * 
 * @author Matti Tahvonen (IT Mill)
 * @author Henri Sara (IT Mill)
 * @author Daniel Bell (itree.com.au, bugfixes, support for embedded composite
 *         keys, ability to add non Hibernate-mapped properties)
 * @author Marc Englund (IT Mill, weak item cache to conserve memory/return same
 *         item instance) Update item to reference updated pojo.
 * @author Pavel Micka updateEntity method
 */
public class HbnContainer<T> implements Container.Indexed, Container.Sortable, Container.ItemSetChangeNotifier, Container.Filterable {

    private static final int REFERENCE_CLEANUP_INTERVAL = 2000;

    private static final long serialVersionUID = -6410337120924382057L;

    /**
     * SessionManager interface is used by HbnContainer to get reference to
     * Hibernates Session object.
     */
    public interface SessionManager {

        /**
         * @return a Hibernate Session with open transaction
         */
        Session getSession();
    }

    /**
     * Item wrappping a Hibernate mapped entity object. EntityItems are
     * generally instantiated automatically by HbnContainer.
     */
    @SuppressWarnings("hiding")
    public class EntityItem<T> implements Item {

        private static final long serialVersionUID = -2847179724504965599L;

        /**
         * Reference to hibernate mapped entity that this Item wraps.
         */
        protected T pojo;

        /**
         * Instantiated properties of this EntityItem. May be either
         * EntityItemProperty (hibernate field) or manually added container
         * property (MethodProperty).
         */
        protected Map<Object, Property> properties = new HashMap<Object, Property>();

        public EntityItem(Serializable id) {
            pojo = (T) sessionManager.getSession().get(type, id);
            for (String propertyId : addedProperties.keySet()) {
                addItemProperty(propertyId, new MethodProperty(pojo, propertyId));
            }
        }

        /**
         * @return the wrapped entity object.
         */
        public T getPojo() {
            return pojo;
        }

        public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
            properties.put(id, property);
            return true;
        }

        public Property getItemProperty(Object id) {
            Property p = properties.get(id);
            if (p == null) {
                p = new EntityItemProperty(id.toString());
                properties.put(id, p);
            }
            return p;
        }

        public Collection<?> getItemPropertyIds() {
            return getContainerPropertyIds();
        }

        public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
            Property removed = properties.remove(id);
            return removed != null;
        }

        /**
         * EntityItemProperty wraps one Hibernate controlled field of the pojo
         * used by EntityItem. For common fields the field value is the same as
         * Property value. For relation fields it is the identifier of related
         * object or a collection of identifiers.
         */
        public class EntityItemProperty implements Property, Property.ValueChangeNotifier {

            private static final long serialVersionUID = -4086774943938055297L;

            private final String propertyName;

            public EntityItemProperty(String propertyName) {
                this.propertyName = propertyName;
            }

            public EntityItem<T> getEntityItem() {
                return EntityItem.this;
            }

            public T getPojo() {
                return pojo;
            }

            /**
             * A helper method to get raw type of this (Hibernate) property.
             * 
             * @return the raw type of field
             */
            private Type getPropertyType() {
                return getClassMetadata().getPropertyType(propertyName);
            }

            private boolean propertyInEmbeddedKey() {
                Type idType = getClassMetadata().getIdentifierType();
                if (idType.isComponentType()) {
                    ComponentType idComponent = (ComponentType) idType;
                    String[] idPropertyNames = idComponent.getPropertyNames();
                    List<String> idPropertyNameList = Arrays.asList(idPropertyNames);
                    if (idPropertyNameList.contains(propertyName)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            public Class<?> getType() {
                if (propertyInEmbeddedKey()) {
                    ComponentType idType = (ComponentType) getClassMetadata().getIdentifierType();
                    String[] propertyNames = idType.getPropertyNames();
                    for (int i = 0; i < propertyNames.length; i++) {
                        String name = propertyNames[i];
                        if (name.equals(propertyName)) {
                            try {
                                String idName = getClassMetadata().getIdentifierPropertyName();
                                Field idField = type.getDeclaredField(idName);
                                Field propertyField = idField.getType().getDeclaredField(propertyName);
                                return propertyField.getType();
                            } catch (NoSuchFieldException ex) {
                                throw new RuntimeException("Could not find the type of specified container property.", ex);
                            }
                        }
                    }
                }
                Type propertyType = getPropertyType();
                if (propertyType.isCollectionType()) {
                    Class<?> returnedClass = propertyType.getReturnedClass();
                    return returnedClass;
                } else if (propertyType.isAssociationType()) {
                    ClassMetadata classMetadata2 = sessionManager.getSession().getSessionFactory().getClassMetadata(getClassMetadata().getPropertyType(propertyName).getReturnedClass());
                    return classMetadata2.getIdentifierType().getReturnedClass();
                } else {
                    return getClassMetadata().getPropertyType(propertyName).getReturnedClass();
                }
            }

            public Object getValue() {
                if (!sessionManager.getSession().contains(pojo)) {
                    pojo = (T) sessionManager.getSession().get(type, (Serializable) getIdForPojo(pojo));
                }
                if (propertyInEmbeddedKey()) {
                    ComponentType idType = (ComponentType) getClassMetadata().getIdentifierType();
                    String[] propertyNames = idType.getPropertyNames();
                    for (int i = 0; i < propertyNames.length; i++) {
                        String name = propertyNames[i];
                        if (name.equals(propertyName)) {
                            Object id = getClassMetadata().getIdentifier(pojo, EntityMode.POJO);
                            return idType.getPropertyValue(id, i, EntityMode.POJO);
                        }
                    }
                }
                Type propertyType = getPropertyType();
                Object propertyValue = getClassMetadata().getPropertyValue(pojo, propertyName, EntityMode.POJO);
                if (!propertyType.isAssociationType()) {
                    return propertyValue;
                } else if (propertyType.isCollectionType()) {
                    if (propertyValue == null) {
                        return null;
                    }
                    HashSet<Serializable> identifiers = new HashSet<Serializable>();
                    Collection<?> pojos = (Collection) propertyValue;
                    for (Object object : pojos) {
                        if (!sessionManager.getSession().contains(object)) {
                            object = sessionManager.getSession().merge(object);
                        }
                        identifiers.add(sessionManager.getSession().getIdentifier(object));
                    }
                    return identifiers;
                } else {
                    if (propertyValue == null) {
                        return null;
                    }
                    Class<?> propertyTypeClass = propertyType.getReturnedClass();
                    ClassMetadata classMetadata2 = sessionManager.getSession().getSessionFactory().getClassMetadata(propertyTypeClass);
                    Serializable identifier = classMetadata2.getIdentifier(propertyValue, EntityMode.POJO);
                    return identifier;
                }
            }

            public boolean isReadOnly() {
                return false;
            }

            public void setReadOnly(boolean newStatus) {
                throw new UnsupportedOperationException();
            }

            public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
                try {
                    Object value;
                    try {
                        if (newValue == null || getType().isAssignableFrom(newValue.getClass())) {
                            value = newValue;
                        } else {
                            final Constructor<?> constr = getType().getConstructor(new Class[] { String.class });
                            value = constr.newInstance(new Object[] { newValue.toString() });
                        }
                        if (propertyInEmbeddedKey()) {
                            ComponentType idType = (ComponentType) getClassMetadata().getIdentifierType();
                            String[] propertyNames = idType.getPropertyNames();
                            for (int i = 0; i < propertyNames.length; i++) {
                                String name = propertyNames[i];
                                if (name.equals(propertyName)) {
                                    Object id = getClassMetadata().getIdentifier(pojo, EntityMode.POJO);
                                    Object[] values = idType.getPropertyValues(id, EntityMode.POJO);
                                    values[i] = value;
                                    idType.setPropertyValues(id, values, EntityMode.POJO);
                                }
                            }
                        } else {
                            Type propertyType = getClassMetadata().getPropertyType(propertyName);
                            if (propertyType.isCollectionType()) {
                                Field declaredField = type.getDeclaredField(propertyName);
                                java.lang.reflect.Type genericType = declaredField.getGenericType();
                                java.lang.reflect.Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
                                java.lang.reflect.Type assosiatedType = actualTypeArguments[0];
                                String typestring = assosiatedType.toString().substring(6);
                                Collection<Object> pojoCollection = (Collection<Object>) getClassMetadata().getPropertyValue(pojo, propertyName, EntityMode.POJO);
                                if (pojoCollection == null) {
                                    pojoCollection = new HashSet<Object>();
                                    getClassMetadata().setPropertyValue(pojo, propertyName, pojoCollection, EntityMode.POJO);
                                }
                                Collection<Object> orphans = new HashSet<Object>(pojoCollection);
                                Collection<?> identifiers = (Collection<?>) value;
                                Session session = sessionManager.getSession();
                                for (Object id : identifiers) {
                                    Object object = session.get(typestring, (Serializable) id);
                                    if (!pojoCollection.contains(object)) {
                                        pojoCollection.add(object);
                                    } else {
                                        orphans.remove(object);
                                    }
                                }
                                pojoCollection.removeAll(orphans);
                            } else if (propertyType.isAssociationType()) {
                                Class<?> referencedType = getClassMetadata().getPropertyType(propertyName).getReturnedClass();
                                Object object = sessionManager.getSession().get(referencedType, (Serializable) value);
                                getClassMetadata().setPropertyValue(pojo, propertyName, object, EntityMode.POJO);
                                sessionManager.getSession().merge(object);
                                sessionManager.getSession().saveOrUpdate(pojo);
                            } else {
                                getClassMetadata().setPropertyValue(pojo, propertyName, value, EntityMode.POJO);
                            }
                        }
                        T newPojo = (T) sessionManager.getSession().merge(pojo);
                        pojo = newPojo;
                        fireValueChange();
                    } catch (final java.lang.Exception e) {
                        e.printStackTrace();
                        throw new Property.ConversionException(e);
                    }
                } catch (HibernateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String toString() {
                Object v = getValue();
                if (v != null) {
                    return v.toString();
                } else {
                    return null;
                }
            }

            private class HbnPropertyValueChangeEvent implements Property.ValueChangeEvent {

                private static final long serialVersionUID = 166764621324404579L;

                public Property getProperty() {
                    return EntityItemProperty.this;
                }
            }

            private List<ValueChangeListener> valueChangeListeners;

            private void fireValueChange() {
                if (valueChangeListeners != null) {
                    HbnPropertyValueChangeEvent event = new HbnPropertyValueChangeEvent();
                    Object[] array = valueChangeListeners.toArray();
                    for (int i = 0; i < array.length; i++) {
                        ((ValueChangeListener) array[i]).valueChange(event);
                    }
                }
            }

            public void addListener(ValueChangeListener listener) {
                if (valueChangeListeners == null) {
                    valueChangeListeners = new LinkedList<ValueChangeListener>();
                }
                if (!valueChangeListeners.contains(listener)) {
                    valueChangeListeners.add(listener);
                }
            }

            public void removeListener(ValueChangeListener listener) {
                if (valueChangeListeners != null) {
                    valueChangeListeners.remove(listener);
                }
            }
        }
    }

    private static final int ROW_BUF_SIZE = 100;

    private static final int ID_TO_INDEX_MAX_SIZE = 300;

    /** Entity class that will be listed in container */
    protected Class<T> type;

    protected final SessionManager sessionManager;

    private ClassMetadata classMetadata;

    /** internal flag used to temporarily invert order of listing */
    private boolean normalOrder = true;

    /** Row buffer of pojos, used to optimize query count when iterating forward */
    private List<T> ascRowBuffer;

    /**
     * Row buffer of pojos, used to optimize query count when iterating backward
     */
    private List<T> descRowBuffer;

    /** cached last item identifier */
    private Object lastId;

    /** cached first item identifier */
    private Object firstId;

    /**
     * Row buffer of pojos, used to optimize query count when container is
     * accessed with indexes
     */
    private List<T> indexRowBuffer;

    /** Container wide index of the first entity in indexRowBuffer */
    private int indexRowBufferFirstIndex;

    /**
     * Map from entity/item identifiers to index. Maps does not contain mapping
     * for all identifiers in container, but only those that are recently
     * loaded. Map gets cleanded during usage, to free memory.
     */
    private final Map<Object, Integer> idToIndex = new LinkedHashMap<Object, Integer>();

    /**
     * whether sorts are made ascending or descending, see
     * {@link #orderPropertyIds}
     */
    private boolean[] orderAscendings;

    /**
     * Properties among container is sorted by. Used to implement
     * Container.Sortable
     */
    private Object[] orderPropertyIds;

    /** Cached size of container. Used to optimize query count. */
    private Integer size;

    private LinkedList<ItemSetChangeListener> itemSetChangeListeners;

    /**
     * Contains current filters which has been applied to this container. Used
     * to implement Container.Filterable.
     */
    private HashSet<ContainerFilter> filters;

    /** Caches weak references to items, in order to conserve memory. */
    private final HashMap<Object, WeakReference<EntityItem<T>>> itemCache = new HashMap<Object, WeakReference<EntityItem<T>>>();

    /** A map of added javabean property names to their respective types */
    private final Map<String, Class<?>> addedProperties = new HashMap<String, Class<?>>();

    /**
     * counter for items loaded by container, used to implement cleanup of
     * weakreferences
     */
    private int loadCount;

    /**
     * Creates a new instance of HbnContainer, listing all object of given type
     * from database.
     * 
     * @param entityType
     *            Entity class to be listed in container.
     * @param sessionMgr
     *            interface via Hibernate session is fetched
     */
    public HbnContainer(Class<T> entityType, SessionManager sessionMgr) {
        type = entityType;
        sessionManager = sessionMgr;
    }

    /**
     * HbnContainer automatically adds all fields that are mapped by Hibernate
     * to DB. With this method one can add a javabean property to the container
     * that is contained on pojo but not hibernate-mapped.
     * 
     * @see Container#addContainerProperty(Object, Class, Object)
     */
    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException {
        boolean propertyExists = true;
        try {
            new MethodProperty(this.type.newInstance(), propertyId.toString());
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            propertyExists = false;
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            propertyExists = false;
        }
        addedProperties.put(propertyId.toString(), type);
        return propertyExists;
    }

    /**
     * HbnContainer specific method to persist newly created entity.
     * 
     * @param entity
     *            the unsaved entity object
     * @return the identifier for newly persisted entity
     */
    public Serializable saveEntity(T entity) {
        sessionManager.getSession().save(entity);
        clearInternalCache();
        fireItemSetChange();
        return (Serializable) getIdForPojo(entity);
    }

    /**
     * HbnContainer specific method to update entity.
     * 
     * @param entity
     *            to update
     * @return the identifier of the updated entity
     */
    public Serializable updateEntity(T entity) {
        sessionManager.getSession().save(entity);
        EntityItem<T> item = null;
        Serializable itemId = (Serializable) getIdForPojo(entity);
        if (itemCache != null) {
            WeakReference<EntityItem<T>> weakReference = itemCache.get(itemId);
            if (weakReference != null) {
                item = weakReference.get();
                if (item != null) {
                    item.pojo = entity;
                }
            }
        }
        if (item != null) {
            for (Object id : item.getItemPropertyIds()) {
                Property p = item.getItemProperty(id);
                if (p instanceof EntityItem.EntityItemProperty) {
                    EntityItem.EntityItemProperty ep = (EntityItem.EntityItemProperty) p;
                    ep.fireValueChange();
                }
            }
        }
        return itemId;
    }

    public Object addItem() throws UnsupportedOperationException {
        Object o;
        try {
            o = type.newInstance();
            sessionManager.getSession().save(o);
            clearInternalCache();
            fireItemSetChange();
            return getIdForPojo(o);
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Item addItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public boolean containsId(Object itemId) {
        try {
            return (sessionManager.getSession().get(type, (Serializable) itemId) != null);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Property getContainerProperty(Object itemId, Object propertyId) {
        return getItem(itemId).getItemProperty(propertyId);
    }

    @SuppressWarnings("unchecked")
    public Collection getContainerPropertyIds() {
        Collection propertyIds = getSortableContainerPropertyIds();
        propertyIds.addAll(addedProperties.keySet());
        return propertyIds;
    }

    private Collection<String> getEmbeddedKeyPropertyIds() {
        ArrayList<String> embeddedKeyPropertyIds = new ArrayList<String>();
        Type identifierType = getClassMetadata().getIdentifierType();
        if (identifierType.isComponentType()) {
            ComponentType idComponent = (ComponentType) identifierType;
            String[] propertyNameArray = idComponent.getPropertyNames();
            if (propertyNameArray != null) {
                List<String> propertyNames = Arrays.asList(propertyNameArray);
                embeddedKeyPropertyIds.addAll(propertyNames);
            }
        }
        return embeddedKeyPropertyIds;
    }

    /**
     * Helper method to get the Hibernate ClassMetadata for listed entity type.
     * Method lazyly caches the metadata, optimizing the lookup a bit (instead
     * of fetching it continuously from Session).
     * 
     * @return Hibernates ClassMetadata for the listed entity type
     */
    private ClassMetadata getClassMetadata() {
        if (classMetadata == null) {
            classMetadata = sessionManager.getSession().getSessionFactory().getClassMetadata(type);
        }
        return classMetadata;
    }

    public EntityItem<T> getItem(Object itemId) {
        EntityItem<T> item = null;
        if (itemId != null) {
            item = loadItem((Serializable) itemId);
        }
        return item;
    }

    /**
     * This method is used to fetch Items by id. Override this if you need
     * customized EntityItems.
     * 
     * @param itemId
     * @return
     */
    protected EntityItem<T> loadItem(Serializable itemId) {
        cleanCache();
        EntityItem<T> item;
        WeakReference<EntityItem<T>> weakReference = itemCache.get(itemId);
        if (weakReference != null) {
            item = weakReference.get();
            if (item != null) {
                return item;
            }
        }
        item = new EntityItem<T>(itemId);
        itemCache.put(itemId, new WeakReference<EntityItem<T>>(item));
        return item;
    }

    /**
     * Cleans the itemCache of collected item references. This method run every
     * now and then by {@link #loadItem(Serializable)}, but may be run manually
     * too.
     * 
     * <p>
     * TODO figure out if this is the best possible way to free the memory
     * consumed by (empty) weak references and open this mechanism for extension
     * 
     */
    private void cleanCache() {
        if (++loadCount % REFERENCE_CLEANUP_INTERVAL == 0) {
            Set<Entry<Object, WeakReference<EntityItem<T>>>> entries = itemCache.entrySet();
            for (Iterator<Entry<Object, WeakReference<EntityItem<T>>>> iterator = entries.iterator(); iterator.hasNext(); ) {
                Entry<Object, WeakReference<EntityItem<T>>> entry = iterator.next();
                if (entry.getValue().get() == null) {
                    iterator.remove();
                }
            }
        }
    }

    public Collection<?> getItemIds() {
        Criteria crit = getCriteria();
        crit.setProjection(Projections.id());
        return crit.list();
    }

    public Class<?> getType(Object propertyId) {
        if (addedProperties.keySet().contains(propertyId)) {
            return addedProperties.get(propertyId);
        } else if (propertyInEmbeddedKey(propertyId)) {
            ComponentType idType = (ComponentType) getClassMetadata().getIdentifierType();
            String[] propertyNames = idType.getPropertyNames();
            for (int i = 0; i < propertyNames.length; i++) {
                String name = propertyNames[i];
                if (name.equals(propertyId)) {
                    String idName = getClassMetadata().getIdentifierPropertyName();
                    try {
                        Field idField = type.getDeclaredField(idName);
                        Field propertyField = idField.getType().getDeclaredField((String) propertyId);
                        return propertyField.getType();
                    } catch (NoSuchFieldException ex) {
                        throw new RuntimeException("Could not find the type of specified container property.", ex);
                    }
                }
            }
        }
        Type propertyType = getClassMetadata().getPropertyType(propertyId.toString());
        return propertyType.getReturnedClass();
    }

    /**
     * TODO combine with the very same method from EntityItemProperty
     * 
     * @param propertyId
     * @return true if property is part of embedded key of entity
     */
    private boolean propertyInEmbeddedKey(Object propertyId) {
        Type idType = getClassMetadata().getIdentifierType();
        if (idType.isComponentType()) {
            ComponentType idComponent = (ComponentType) idType;
            String[] idPropertyNames = idComponent.getPropertyNames();
            List<String> idPropertyNameList = Arrays.asList(idPropertyNames);
            if (idPropertyNameList.contains(propertyId)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean removeAllItems() throws UnsupportedOperationException {
        return false;
    }

    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        boolean propertyExisted = false;
        Class<?> removed = addedProperties.remove(propertyId);
        if (removed != null) {
            propertyExisted = true;
        }
        return propertyExisted;
    }

    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        Object p = sessionManager.getSession().load(type, (Serializable) itemId);
        sessionManager.getSession().delete(p);
        clearInternalCache();
        fireItemSetChange();
        return true;
    }

    public void addListener(ItemSetChangeListener listener) {
        if (itemSetChangeListeners == null) {
            itemSetChangeListeners = new LinkedList<ItemSetChangeListener>();
        }
        itemSetChangeListeners.add(listener);
    }

    public void removeListener(ItemSetChangeListener listener) {
        if (itemSetChangeListeners != null) {
            itemSetChangeListeners.remove(listener);
        }
    }

    private void fireItemSetChange() {
        if (itemSetChangeListeners != null) {
            final Object[] l = itemSetChangeListeners.toArray();
            final Container.ItemSetChangeEvent event = new Container.ItemSetChangeEvent() {

                private static final long serialVersionUID = -3002746333251784195L;

                public Container getContainer() {
                    return HbnContainer.this;
                }
            };
            for (int i = 0; i < l.length; i++) {
                ((ItemSetChangeListener) l[i]).containerItemSetChange(event);
            }
        }
    }

    public int size() {
        if (size == null) {
            size = ((Number) getBaseCriteria().setProjection(Projections.rowCount()).uniqueResult()).intValue();
        }
        return size.intValue();
    }

    public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets a base listing using current orders etc.
     * 
     * @return criteria with current Order criterias added
     */
    private Criteria getCriteria() {
        Criteria criteria = getBaseCriteria();
        List<Order> orders = getOrder(!normalOrder);
        for (Order order : orders) {
            criteria.addOrder(order);
        }
        return criteria;
    }

    /**
     * Return the ordering criteria in the order in which they should be
     * applied. The composed order must be stable and must include
     * {@link #getNaturalOrder(boolean)} at the end.
     * 
     * @param flipOrder
     *            reverse the order if true
     * @return List<Order> orders to apply, first item has the highest priority
     */
    protected final List<Order> getOrder(boolean flipOrder) {
        List<Order> orders = new ArrayList<Order>();
        orders.addAll(getDefaultOrder(flipOrder));
        orders.add(getNaturalOrder(flipOrder));
        return orders;
    }

    /**
     * Returns the ordering to use for the container contents. The default
     * implementation provides the {@link Container.Sortable} functionality.
     * 
     * Can be overridden to customize item sort order.
     * 
     * @param flipOrder
     *            reverse the order if true
     * @return List<Order> orders to apply, first item has the highest priority
     */
    protected List<Order> getDefaultOrder(boolean flipOrder) {
        List<Order> orders = new ArrayList<Order>();
        if (orderPropertyIds != null) {
            for (int i = 0; i < orderPropertyIds.length; i++) {
                String orderPropertyId = orderPropertyIds[i].toString();
                if (propertyInEmbeddedKey(orderPropertyId)) {
                    String idName = getClassMetadata().getIdentifierPropertyName();
                    orderPropertyId = idName + "." + orderPropertyId;
                }
                boolean a = flipOrder ? !orderAscendings[i] : orderAscendings[i];
                if (a) {
                    orders.add(Order.asc(orderPropertyId));
                } else {
                    orders.add(Order.desc(orderPropertyId));
                }
            }
        }
        return orders;
    }

    /**
     * This method is meant to be called by HbnContainer itself. It will create
     * the base criteria for entity class and add possible restrictions to
     * query. This method is protected so developers can add their own custom
     * criterias.
     * 
     * @return
     */
    protected Criteria getBaseCriteria() {
        Criteria criteria = sessionManager.getSession().createCriteria(type);
        if (filters != null) {
            for (ContainerFilter filter : filters) {
                String filterPropertyName = filter.propertyId.toString();
                if (propertyInEmbeddedKey(filterPropertyName)) {
                    String idName = getClassMetadata().getIdentifierPropertyName();
                    filterPropertyName = idName + "." + filterPropertyName;
                }
                if (filter.ignoreCase) {
                    criteria = criteria.add(Restrictions.ilike(filterPropertyName, filter.filterString, filter.onlyMatchPrefix ? MatchMode.START : MatchMode.ANYWHERE));
                } else {
                    criteria = criteria.add(Restrictions.like(filterPropertyName, filter.filterString, filter.onlyMatchPrefix ? MatchMode.START : MatchMode.ANYWHERE));
                }
            }
        }
        return criteria;
    }

    /**
     * Natural order is the order in which the database is sorted if container
     * has no other ordering set. Natural order is always added as least
     * significant order to queries. This is needed to keep items stable order
     * across queries.
     * <p>
     * The default implementation sorts entities by identifier column.
     * 
     * @param flipOrder
     * @return
     */
    protected Order getNaturalOrder(boolean flipOrder) {
        if (flipOrder) {
            return Order.desc(getIdPropertyName());
        } else {
            return Order.asc(getIdPropertyName());
        }
    }

    public Object firstItemId() {
        if (firstId == null) {
            firstId = firstItemId(true);
        }
        return firstId;
    }

    /**
     * Internanal helper method to implement {@link #firstItemId()} and
     * {@link #lastItemId()}.
     */
    protected Object firstItemId(boolean byPassCache) {
        if (byPassCache) {
            T first = (T) getCriteria().setMaxResults(1).setCacheable(true).uniqueResult();
            Object id = getIdForPojo(first);
            idToIndex.put(id, normalOrder ? 0 : size() - 1);
            return id;
        } else {
            return firstItemId();
        }
    }

    /**
     * Helper method to detect identifier of given entity object.
     * 
     * @param pojo
     *            the entity object which identifier is to be resolved
     * @return the identifier if the given Hibernate entity object
     */
    private Object getIdForPojo(Object pojo) {
        return getClassMetadata().getIdentifier(pojo, EntityMode.POJO);
    }

    public boolean isFirstId(Object itemId) {
        return itemId.equals(firstItemId());
    }

    public boolean isLastId(Object itemId) {
        return itemId.equals(lastItemId());
    }

    public Object lastItemId() {
        if (lastId == null) {
            normalOrder = !normalOrder;
            lastId = firstItemId(true);
            normalOrder = !normalOrder;
        }
        return lastId;
    }

    public Object nextItemId(Object itemId) {
        if (isLastId(itemId)) {
            return null;
        }
        EntityItem<T> item = new EntityItem<T>((Serializable) itemId);
        List<T> buffer = getRowBuffer();
        try {
            int curBufIndex = buffer.indexOf(item.getPojo());
            if (curBufIndex != -1) {
                T object = buffer.get(curBufIndex + 1);
                return getIdForPojo(object);
            }
        } catch (Exception e) {
        }
        int currentIndex = indexOfId(itemId);
        int firstIndex = normalOrder ? currentIndex + 1 : size() - currentIndex - 1;
        Criteria crit = getCriteria();
        crit = crit.setFirstResult(firstIndex).setMaxResults(ROW_BUF_SIZE);
        List<T> newBuffer = crit.list();
        if (newBuffer.size() > 0) {
            setRowBuffer(newBuffer, firstIndex);
            T nextPojo = newBuffer.get(0);
            return getIdForPojo(nextPojo);
        } else {
            return null;
        }
    }

    /**
     * RowBuffer stores a list of entity items to avoid excessive number of DB
     * queries.
     * 
     * @return
     */
    private List<T> getRowBuffer() {
        if (normalOrder) {
            return ascRowBuffer;
        } else {
            return descRowBuffer;
        }
    }

    /**
     * RowBuffer stores some pojos to avoid excessive number of DB queries.
     * 
     * Also updates the idToIndex map.
     */
    private void setRowBuffer(List<T> list, int firstIndex) {
        if (normalOrder) {
            ascRowBuffer = list;
            for (int i = 0; i < list.size(); ++i) {
                idToIndex.put(getIdForPojo(list.get(i)), firstIndex + i);
            }
        } else {
            descRowBuffer = list;
            int lastIndex = size() - 1;
            for (int i = 0; i < list.size(); ++i) {
                idToIndex.put(getIdForPojo(list.get(i)), lastIndex - firstIndex - i);
            }
        }
    }

    /**
     * @return column name of identifier property
     */
    private String getIdPropertyName() {
        return getClassMetadata().getIdentifierPropertyName();
    }

    public Object prevItemId(Object itemId) {
        normalOrder = !normalOrder;
        Object prev = nextItemId(itemId);
        normalOrder = !normalOrder;
        return prev;
    }

    /**
     * Not supported in HbnContainer. Indexing/order is controlled by
     * underlaying database.
     */
    public Object addItemAt(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported in HbnContainer. Indexing/order is controlled by
     * underlaying database.
     */
    public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Object getIdByIndex(int index) {
        if (indexRowBuffer == null) {
            resetIndexRowBuffer(index);
        }
        int indexInCache = index - indexRowBufferFirstIndex;
        if (!(indexInCache >= 0 && indexInCache < indexRowBuffer.size())) {
            resetIndexRowBuffer(index);
            indexInCache = 0;
        }
        T pojo = indexRowBuffer.get(indexInCache);
        Object id = getIdForPojo(pojo);
        idToIndex.put(id, new Integer(index));
        if (idToIndex.size() > ID_TO_INDEX_MAX_SIZE) {
            idToIndex.remove(idToIndex.keySet().iterator().next());
        }
        return id;
    }

    /**
     * Helper method to query new set of entity items to cache from given index.
     * 
     * @param index
     *            the index of first entity object ot be included in query
     */
    private void resetIndexRowBuffer(int index) {
        indexRowBufferFirstIndex = index;
        indexRowBuffer = getCriteria().setFirstResult(index).setMaxResults(ROW_BUF_SIZE).list();
    }

    public int indexOfId(Object itemId) {
        Integer index = idToIndex.get(itemId);
        return index;
    }

    public Collection<String> getSortableContainerPropertyIds() {
        String[] propertyNames = getClassMetadata().getPropertyNames();
        LinkedList<String> propertyIds = new LinkedList<String>();
        propertyIds.addAll(Arrays.asList(propertyNames));
        propertyIds.addAll(getEmbeddedKeyPropertyIds());
        return propertyIds;
    }

    public void sort(Object[] propertyId, boolean[] ascending) {
        clearInternalCache();
        orderPropertyIds = propertyId;
        orderAscendings = ascending;
    }

    /**
     * Helper method to clear all cache fields.
     */
    protected void clearInternalCache() {
        idToIndex.clear();
        indexRowBuffer = null;
        ascRowBuffer = null;
        descRowBuffer = null;
        firstId = null;
        lastId = null;
        size = null;
    }

    /**
     * Helper class to contain filtering data from Container.Filterable
     */
    class ContainerFilter {

        private final Object propertyId;

        private final String filterString;

        private final boolean onlyMatchPrefix;

        private final boolean ignoreCase;

        public ContainerFilter(Object propertyId, String filterString, boolean ignoreCase, boolean onlyMatchPrefix) {
            this.propertyId = propertyId;
            this.ignoreCase = ignoreCase;
            this.filterString = ignoreCase ? filterString.toLowerCase() : filterString;
            this.onlyMatchPrefix = onlyMatchPrefix;
        }
    }

    /**
     * Adds container filter for hibernate mapped property. For property not
     * mapped by Hibernate, {@link UnsupportedOperationException} is thrown.
     * 
     * @see Container.Filterable#addContainerFilter(Object, String, boolean,
     *      boolean)
     */
    public void addContainerFilter(Object propertyId, String filterString, boolean ignoreCase, boolean onlyMatchPrefix) {
        if (addedProperties.containsKey(propertyId)) {
            throw new UnsupportedOperationException("HbnContainer does not support filterig properties not mapped by Hibernate");
        } else {
            if (filters == null) {
                filters = new HashSet<ContainerFilter>();
            }
            ContainerFilter f = new ContainerFilter(propertyId, filterString, ignoreCase, onlyMatchPrefix);
            filters.add(f);
            clearInternalCache();
            fireItemSetChange();
        }
    }

    public void removeAllContainerFilters() {
        if (filters != null) {
            filters = null;
            clearInternalCache();
            fireItemSetChange();
        }
    }

    public void removeContainerFilters(Object propertyId) {
        if (filters != null) {
            for (Iterator<ContainerFilter> iterator = filters.iterator(); iterator.hasNext(); ) {
                ContainerFilter f = iterator.next();
                if (f.propertyId.equals(propertyId)) {
                    iterator.remove();
                }
            }
            clearInternalCache();
            fireItemSetChange();
        }
    }
}

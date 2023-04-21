package net.deytan.wofee.gae.persistence.action.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.deytan.wofee.exception.PersistenceException;
import net.deytan.wofee.exception.ReflectionException;
import net.deytan.wofee.exception.TranslatorException;
import net.deytan.wofee.gae.persistence.ActionsFactory;
import net.deytan.wofee.gae.persistence.CacheService;
import net.deytan.wofee.gae.persistence.DatastoreFactory;
import net.deytan.wofee.gae.persistence.transaction.DatastoreTransactionManager;
import net.deytan.wofee.persistence.PersistableDefinition;
import net.deytan.wofee.persistence.PersistableFactory;
import net.deytan.wofee.persistence.PersistableField;
import net.deytan.wofee.util.ReflectionUtils;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class AbstractStoreAction extends AbstractAction {

    public AbstractStoreAction(final DatastoreFactory datastoreFactory, final DatastoreTransactionManager transactionManager, final PersistableFactory persistableFactory, final ActionsFactory actionsFactory, final CacheService cacheService) throws PersistenceException {
        super(datastoreFactory, transactionManager, persistableFactory, actionsFactory, cacheService);
    }

    protected Object store(final Object instance, final Key parentKey) throws PersistenceException, ReflectionException {
        final Object ret;
        if ((instance instanceof Iterable<?>) || (instance instanceof Object[])) {
            final Iterable<?> iterable;
            if (instance instanceof Iterable<?>) {
                iterable = (Iterable<?>) instance;
            } else {
                iterable = Arrays.asList(((Object[]) instance));
            }
            final List<Key> keys = new ArrayList<Key>();
            for (Object object : iterable) {
                keys.add((Key) this.actionsFactory.getStoreAction(object.getClass()).store(object, parentKey));
            }
            ret = keys;
        } else {
            ret = this.storeSimple(instance, parentKey);
        }
        return ret;
    }

    private Key storeSimple(final Object instance, final Key parentKey) throws PersistenceException, ReflectionException {
        final Entity entity = this.instanceToEntity(instance, parentKey);
        boolean hadKey = false;
        Key key = entity.getKey();
        if (key.isComplete()) {
            this.storeChilds(instance, entity, key);
            hadKey = true;
        }
        key = this.datastore.put(this.transactionManager.getCurrentTransaction(), entity);
        this.setInstanceKey(key, instance);
        this.cacheService.put(key, instance);
        if (!hadKey) {
            this.storeChilds(instance, entity, key);
            this.datastore.put(this.transactionManager.getCurrentTransaction(), entity);
        }
        return key;
    }

    private void storeChilds(final Object instance, final Entity entity, final Key key) throws PersistenceException, ReflectionException {
        for (PersistableField persistableField : this.persistable.getFields().values()) {
            if (this.fieldIsChild(persistableField)) {
                final Object fieldInstance = ReflectionUtils.getFieldValue(instance, persistableField.getName());
                if (persistableField.isIterable()) {
                    if (fieldInstance != null) {
                        final Collection<?> collection;
                        if (Collection.class.isAssignableFrom(persistableField.getJavaType())) {
                            collection = (Collection<?>) fieldInstance;
                        } else {
                            collection = Arrays.asList(((Object[]) fieldInstance));
                        }
                        for (Object object : collection) {
                            Key childKey = this.getInstanceEncodedKey(this.persistableFactory.getPersistable(object), object);
                            if (childKey == null) {
                                this.actionsFactory.getStoreAction(object.getClass()).store(object, key);
                            }
                        }
                    }
                } else {
                    Object propertyValue = null;
                    if (fieldInstance != null) {
                        Key childKey = this.getInstanceEncodedKey(this.persistableFactory.getPersistable(fieldInstance), fieldInstance);
                        if (childKey == null) {
                            childKey = (Key) this.actionsFactory.getStoreAction(persistableField.getJavaType()).store(fieldInstance, key);
                        }
                        propertyValue = childKey;
                    }
                    if (persistableField.isIndexed()) {
                        entity.setProperty(persistableField.getName(), propertyValue);
                    } else {
                        entity.setUnindexedProperty(persistableField.getName(), propertyValue);
                    }
                }
            }
        }
    }

    /**
	 * Create an entity from an instance and fill it, without the "child" fields.
	 * 
	 * @param instance
	 * @param parentKey
	 * @return
	 * @throws PersistenceException
	 */
    protected Entity instanceToEntity(final Object instance, final Key parentKey) throws PersistenceException, ReflectionException {
        final Object instanceKey = this.getInstanceEncodedKey(instance);
        final Key key = this.createDatastoreKey(instanceKey, parentKey);
        final Entity entity;
        if (key == null) {
            entity = new Entity(this.persistable.getKind(), parentKey);
        } else {
            entity = new Entity(key);
        }
        this.instanceToEntity(instance, this.persistable, entity, "");
        return entity;
    }

    protected void instanceToEntity(final Object instance, final PersistableDefinition instancePersistable, final Entity entity, final String prefix) throws PersistenceException, ReflectionException {
        for (PersistableField persistableField : instancePersistable.getFields().values()) {
            if (!this.fieldIsChild(persistableField)) {
                Object javaValue = ReflectionUtils.getFieldValue(instance, persistableField.getName());
                if (javaValue != null) {
                    if (this.fieldIsEmbedded(persistableField)) {
                        this.instanceToEntity(javaValue, this.persistableFactory.getPersistable(persistableField.getJavaType()), entity, persistableField.getName() + "_");
                    } else {
                        Object datastoreValue = null;
                        try {
                            if (this.fieldIsUnowned(persistableField)) {
                                if (persistableField.isIterable()) {
                                    final Iterable<?> iterable;
                                    if (Collection.class.isAssignableFrom(persistableField.getJavaType())) {
                                        iterable = (Iterable<?>) javaValue;
                                    } else {
                                        iterable = Arrays.asList(((Object[]) javaValue));
                                    }
                                    final List<Key> keys = new ArrayList<Key>();
                                    for (Object object : iterable) {
                                        keys.add(this.getInstanceEncodedKey(this.persistableFactory.getPersistable(object), object));
                                    }
                                    datastoreValue = keys;
                                } else {
                                    javaValue = this.getInstanceEncodedKey(this.persistableFactory.getPersistable(javaValue), javaValue);
                                    datastoreValue = persistableField.getTranslator().toDatastore(javaValue);
                                }
                            } else if (persistableField.isIterable()) {
                                final Iterable<?> iterable;
                                if (Collection.class.isAssignableFrom(persistableField.getJavaType())) {
                                    iterable = (Iterable<?>) javaValue;
                                } else {
                                    iterable = Arrays.asList(((Object[]) javaValue));
                                }
                                final List<Object> values = new ArrayList<Object>();
                                for (Object object : iterable) {
                                    values.add(persistableField.getTranslator().toDatastore(object));
                                }
                                datastoreValue = values;
                            } else {
                                datastoreValue = persistableField.getTranslator().toDatastore(javaValue);
                            }
                        } catch (TranslatorException exception) {
                            throw new PersistenceException("Error translating field '" + persistableField.getName() + "' from instance '" + instance + "'", exception);
                        }
                        final String propertyName = prefix + persistableField.getName();
                        if (persistableField.isIndexed()) {
                            entity.setProperty(propertyName, datastoreValue);
                        } else {
                            entity.setUnindexedProperty(propertyName, datastoreValue);
                        }
                    }
                }
            }
        }
    }
}

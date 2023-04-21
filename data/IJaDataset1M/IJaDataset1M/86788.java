package ee.webAppToolkit.storage.twigPersist;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.vercer.engine.persist.FindCommand.RootFindCommand;
import com.vercer.engine.persist.util.generic.GenericTypeReflector;
import com.vercer.engine.persist.ObjectDatastore;
import ee.metadataUtils.PropertyMetadata;
import ee.metadataUtils.PropertyMetadataRegistry;
import ee.webAppToolkit.storage.Store;

public class TwigPersistStore implements Store {

    private static final Map<String, SortDirection> _sortOrderReference = _getSortOrderReference();

    private static Map<String, SortDirection> _getSortOrderReference() {
        Map<String, SortDirection> sortOrderReference = new HashMap<String, SortDirection>();
        sortOrderReference.put("asc", SortDirection.ASCENDING);
        sortOrderReference.put("desc", SortDirection.DESCENDING);
        return sortOrderReference;
    }

    private ObjectDatastore _objectDatastore;

    private PropertyMetadataRegistry _propertyMetadataRegistry;

    @Inject
    public TwigPersistStore(ObjectDatastore objectDatastore, PropertyMetadataRegistry propertyMetadataRegistry) {
        _objectDatastore = objectDatastore;
        _propertyMetadataRegistry = propertyMetadataRegistry;
    }

    @Override
    public Object getKey(Object entity) {
        return _objectDatastore.associatedKey(entity);
    }

    @Override
    public long getKeyAsLong(Object entity) {
        return _objectDatastore.associatedKey(entity).getId();
    }

    @Override
    public <T> T load(Class<T> entityClass, Object key) {
        return _objectDatastore.load(entityClass, key);
    }

    @Override
    public <T> T load(Class<T> entityClass, long key) {
        return load(entityClass, _getKey(entityClass, key));
    }

    @Override
    public void save(Object entity) {
        _objectDatastore.store(entity);
    }

    @Override
    public void remove(Object entity) {
        _objectDatastore.delete(entity);
    }

    @Override
    public void removeByKey(Class<?> entityClass, long key) {
        Object entity = load(entityClass, key);
        _objectDatastore.delete(entity);
    }

    @Override
    public int count(Class<?> entityClass) {
        return _objectDatastore.find().type(entityClass).countResultsNow();
    }

    @Override
    public <T> Iterable<T> list(Class<T> entityClass) {
        return list(entityClass, 0, 0, null);
    }

    @Override
    public <T> Iterable<T> list(Class<T> entityClass, String sortOrder) {
        return list(entityClass, 0, 0, sortOrder);
    }

    @Override
    public <T> Iterable<T> list(Class<T> entityClass, int offset, int maxResults) {
        return list(entityClass, offset, maxResults, null);
    }

    @Override
    public <T> Iterable<T> list(Class<T> entityClass, int offset, int maxResults, String sortOrder) {
        final RootFindCommand<T> findCommand = _objectDatastore.find().type(entityClass);
        if (offset > 0) {
            findCommand.startFrom(offset);
        }
        if (maxResults > 0) {
            findCommand.maximumResults(maxResults);
        }
        _addSort(sortOrder, findCommand);
        return new Iterable<T>() {

            @Override
            public Iterator<T> iterator() {
                return findCommand.returnResultsNow();
            }
        };
    }

    @Override
    public int count(Object exampleEntity) {
        @SuppressWarnings("unchecked") RootFindCommand<Object> findCommand = (RootFindCommand<Object>) _objectDatastore.find().type(exampleEntity.getClass());
        _addPropertyFilters(exampleEntity, findCommand);
        return findCommand.countResultsNow();
    }

    @Override
    public <T> Iterable<T> find(T exampleEntity) {
        return find(exampleEntity, 0, 0, null);
    }

    @Override
    public <T> Iterable<T> find(T exampleEntity, String sortOrder) {
        return find(exampleEntity, 0, 0, sortOrder);
    }

    @Override
    public <T> Iterable<T> find(T exampleEntity, int offset, int maxResults) {
        return find(exampleEntity, offset, maxResults, null);
    }

    @Override
    public <T> Iterable<T> find(T exampleEntity, int offset, int maxResults, String sortOrder) {
        @SuppressWarnings("unchecked") Class<T> entityClass = (Class<T>) exampleEntity.getClass();
        final RootFindCommand<T> findCommand = _objectDatastore.find().type(entityClass);
        _addPropertyFilters(exampleEntity, findCommand);
        if (offset > 0) {
            findCommand.startFrom(offset);
        }
        if (maxResults > 0) {
            findCommand.maximumResults(maxResults);
        }
        _addSort(sortOrder, findCommand);
        return new Iterable<T>() {

            @Override
            public Iterator<T> iterator() {
                return findCommand.returnResultsNow();
            }
        };
    }

    private <T> void _addSort(String sortOrder, RootFindCommand<T> findCommand) {
        String[] sortOrderArray = sortOrder.split(",");
        for (String sortOrderPart : sortOrderArray) {
            sortOrderPart = sortOrderPart.trim();
            String[] sortOrderPartArray = sortOrderPart.split(" +");
            if (sortOrderPartArray.length == 1) {
                findCommand.addSort(sortOrderPartArray[0]);
            } else {
                findCommand.addSort(sortOrderPartArray[0], _sortOrderReference.get(sortOrderPartArray[1].toLowerCase()));
            }
        }
    }

    private <T> void _addPropertyFilters(T exampleEntity, RootFindCommand<T> findCommand) {
        Map<String, PropertyMetadata> propertyMetadataMap = _propertyMetadataRegistry.getPropertyMetadataMap(exampleEntity.getClass());
        for (Entry<String, PropertyMetadata> entry : propertyMetadataMap.entrySet()) {
            Object value;
            try {
                value = entry.getValue().getValue(exampleEntity);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            if (value != null) {
                findCommand.addFilter(entry.getKey(), FilterOperator.EQUAL, value);
            }
        }
    }

    private Key _getKey(Class<?> entityClass, long id) {
        String type = GenericTypeReflector.erase(entityClass).getName();
        return KeyFactory.createKey(type, id);
    }
}

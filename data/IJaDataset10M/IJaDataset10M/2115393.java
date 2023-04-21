package kotan.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import kotan.datastore.api.Key;

public class DatastoreModel {

    private SortedMap<String, KindModel> kinds = Collections.synchronizedSortedMap(new TreeMap<String, KindModel>());

    final NavigableMap<Key, EntityModel> newEntities = new ConcurrentSkipListMap<Key, EntityModel>();

    final NavigableMap<Key, EntityModel> updateEntities = new ConcurrentSkipListMap<Key, EntityModel>();

    private List<Key> deleteEntities = Collections.synchronizedList(new ArrayList<Key>());

    public void add(KindModel model) {
        kinds.put(model.getName(), model);
    }

    /**
     * Return kind list of a copy.
     * @return
     */
    public List<String> getKindList() {
        return new ArrayList<String>(kinds.keySet());
    }

    public List<Key> getKeyList(String kind) {
        return new ArrayList<Key>(kinds.get(kind).keys);
    }

    public void clear() {
        newEntities.clear();
        updateEntities.clear();
        deleteEntities.clear();
    }

    public List<EntityModel> getAllUpdateEntities() {
        List<EntityModel> list = new ArrayList<EntityModel>(updateEntities.values());
        list.addAll(newEntities.values());
        return list;
    }

    public List<Key> getAllDeleteEntities() {
        return new ArrayList<Key>(deleteEntities);
    }

    public void update(EntityModel entityModel, int column, Object newValue) {
        if (column == 0) {
            throw new UnsupportedOperationException("Use replace method.");
        }
        KindModel kindModel = kinds.get(entityModel.getKind());
        kindModel.setValue(entityModel, column, newValue);
        if (!newEntities.containsKey(entityModel.getKey())) {
            updateEntities.put(entityModel.getKey(), entityModel);
        }
        if (deleteEntities.contains(entityModel.getKey())) {
            deleteEntities.remove(entityModel.getKey());
        }
    }

    public void add(EntityModel newModel) {
        KindModel kindModel = kinds.get(newModel.getKind());
        kindModel.add(newModel);
        newEntities.put(newModel.getKey(), newModel);
        if (deleteEntities.contains(newModel.getKey())) {
            deleteEntities.remove(newModel.getKey());
        }
    }

    public void delete(Key key) {
        KindModel kindModel = kinds.get(key.getKind());
        kindModel.remove(key);
        deleteEntities.add(key);
        if (newEntities.containsKey(key)) {
            newEntities.remove(key);
        }
        if (updateEntities.containsKey(key)) {
            updateEntities.remove(key);
        }
    }
}

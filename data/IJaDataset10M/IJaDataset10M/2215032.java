package com.tinywebgears.tuatara.framework.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.tinywebgears.tuatara.framework.common.CollectionHelper;
import com.tinywebgears.tuatara.framework.common.ListenerHelper;
import com.tinywebgears.tuatara.framework.model.AbstractDataModel;

public class MapSelectionModel<K, V> extends AbstractDataModel<MapSelectionListenerIF<K, V>> implements MapSelectionModelIF<K, V> {

    private List<KeyValuePair<K, V>> model = new ArrayList<KeyValuePair<K, V>>();

    private List<K> keys = new ArrayList<K>();

    private List<V> values = new ArrayList<V>();

    @Override
    protected void doDestroy() {
        super.doDestroy();
    }

    @Override
    public List<KeyValuePair<K, V>> getSelection() {
        return model;
    }

    @Override
    public List<V> getSelectedValues() {
        return values;
    }

    @Override
    public <T extends java.util.Collection<V>> T getSelectedValues(T result) {
        result.addAll(values);
        return result;
    }

    @Override
    public List<K> getSelectedKeys() {
        return keys;
    }

    @Override
    public <T extends java.util.Collection<K>> T getSelectedKeys(T result) {
        result.addAll(keys);
        return result;
    }

    @Override
    public int getSelectionCount() {
        return model.size();
    }

    @Override
    public boolean isSelected(K key) {
        for (KeyValuePair<K, V> entry : model) if (entry.getKey().equals(key)) return true;
        return false;
    }

    public V getSelectedValue(K key) {
        for (KeyValuePair<K, V> entry : model) if (entry.getKey().equals(key)) return entry.getValue();
        return null;
    }

    public void setSelection(List<KeyValuePair<K, V>> selection) {
        model = Collections.unmodifiableList(CollectionHelper.createNewList(selection));
        keys = Collections.unmodifiableList(MapModelHelper.getKeys(model));
        values = Collections.unmodifiableList(MapModelHelper.getValues(model));
        fireChangeEvent(model);
    }

    protected void fireChangeEvent(List<KeyValuePair<K, V>> entries) {
        tellListeners(new NotifyChange(entries));
    }

    private class NotifyChange implements ListenerHelper.NotifyTaskIF<MapSelectionListenerIF<K, V>> {

        private final List<KeyValuePair<K, V>> entries;

        public NotifyChange(List<KeyValuePair<K, V>> entries) {
            this.entries = entries;
        }

        @Override
        public void notify(MapSelectionListenerIF<K, V> listener) {
            listener.selectionChanged(entries);
        }
    }
}

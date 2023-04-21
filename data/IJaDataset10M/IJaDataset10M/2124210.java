package org.jpropeller.properties.map.impl;

import java.util.Map;
import org.jpropeller.bean.Bean;
import org.jpropeller.collection.ObservableMap;
import org.jpropeller.collection.impl.ObservableMapDefault;
import org.jpropeller.info.PropInfo;
import org.jpropeller.map.PropMap;
import org.jpropeller.name.PropName;
import org.jpropeller.properties.event.PropEvent;
import org.jpropeller.properties.event.PropInternalListener;
import org.jpropeller.properties.event.impl.PropEventDefault;
import org.jpropeller.properties.map.EditableMapProp;

/**
 * An implementation of an {@link EditableMapProp} that uses an {@link ObservableMap}
 * to back the data
 * @author mworcester
 * @param <K> 
 *		The type of key in the map/prop
 * @param <T>
 * 		The type of value in the map/prop
 */
public class EditableMapPropDefault<K, T> implements EditableMapProp<K, T>, PropInternalListener {

    PropMap propMap;

    private ObservableMap<K, T> map;

    PropName<EditableMapProp<K, T>, T> name;

    /**
	 * Create a prop
	 * Please note that you must NOT modify the data map after creating
	 * this prop. If you do, those changes will not be visible to any
	 * listeners watching the prop. 
	 * @param data
	 * 		The wrapped map
	 */
    private EditableMapPropDefault(PropName<EditableMapProp<K, T>, T> name, Map<K, T> data) {
        this.name = name;
        this.map = new ObservableMapDefault<K, T>(data);
        map.props().addInternalListener(this);
    }

    /**
	 * Create a new {@link EditableMapPropDefault}
	 * @param <K> 
	 * 		The type of key in the map/indexed property
	 * @param <T> 
	 * 		The type of data in the map/indexed property
	 * @param name 
	 * 		The string value of the property name
	 * @param clazz
	 * 		The class of data in the list/indexed property
	 * @param data
	 * 		The data to contain
	 * @return
	 * 		The new {@link EditableMapPropDefault}
	 */
    public static <K, T> EditableMapPropDefault<K, T> create(String name, Class<T> clazz, Map<K, T> data) {
        return new EditableMapPropDefault<K, T>(PropName.<K, T>editableMap(name, clazz), data);
    }

    /**
	 * Create a new {@link EditableMapPropDefault}
	 * @param <K> 
	 * 		The type of key in the map/indexed property
	 * @param <T> 
	 * 		The type of data in the map/indexed property
	 * @param name 
	 * 		The string value of the property name
	 * @param clazz
	 * 		The class of data in the list/indexed property
	 * @return
	 * 		The new {@link EditableMapPropDefault}
	 */
    public static <K, T> EditableMapPropDefault<K, T> create(String name, Class<T> clazz) {
        return new EditableMapPropDefault<K, T>(PropName.<K, T>editableMap(name, clazz), null);
    }

    @Override
    public T put(K key, T value) {
        return map.put(key, value);
    }

    @Override
    public <S> void propInternalChanged(PropEvent<S> event) {
        props().propChanged(new PropEventDefault<T>(this, event));
    }

    @Override
    public void setPropMap(PropMap map) {
        if (propMap != null) throw new IllegalArgumentException("Prop '" + this + "' already has its PropMap set to '" + propMap + "'");
        if (map == null) throw new IllegalArgumentException("PropMap must be non-null");
        this.propMap = map;
    }

    @Override
    public T get(K key) {
        return map.get(key);
    }

    @Override
    public Bean getBean() {
        return props().getBean();
    }

    public PropName<EditableMapProp<K, T>, T> getName() {
        return name;
    }

    @Override
    public PropMap props() {
        return propMap;
    }

    @Override
    public PropInfo getInfo() {
        return PropInfo.EDITABLE_MAP;
    }

    @Override
    public String toString() {
        return "Editable Map Prop '" + getName().getString() + "' = '" + get() + "'";
    }

    @Override
    public ObservableMap<K, T> get() {
        return map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }
}

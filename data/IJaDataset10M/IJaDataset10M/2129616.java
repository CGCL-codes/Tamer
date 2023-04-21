package com.tinywebgears.tuatara.framework.gui.table;

import java.io.Serializable;

public interface ItemValueGetterIF<K, V, VALUE_TYPE> extends Serializable {

    VALUE_TYPE getItemPropertyValue(K key, V value);
}

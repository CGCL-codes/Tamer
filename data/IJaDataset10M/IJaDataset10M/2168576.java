package com.unitt.commons.foundation.client.model.event;

public class ComplexMapModelItemChangeEvent<T, K, I> extends ModelItemChangeEvent<T, I> {

    protected K key;

    public ComplexMapModelItemChangeEvent(T aModel, K aKey, I aItem, EventType aType) {
        super(aModel, aItem, aType);
        key = aKey;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K aKey) {
        key = aKey;
    }
}

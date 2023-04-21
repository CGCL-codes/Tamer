package com.dyuproject.protostuff.model;

import java.util.Collection;
import java.util.Map;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractMessageLite.Builder;

/**
 * ModelMeta - metadata for the compiled protobuf classes
 * 
 * @author David Yu
 * @created Aug 25, 2009
 */
public class ModelMeta {

    private final Class<? extends AbstractMessageLite> _messageClass;

    private final Class<? extends Builder<?>> _builderClass;

    private final Map<String, ? extends PropertyMeta> _propertyMetaMap;

    private final int _minNumber, _maxNumber;

    private final AbstractMessageLite _prototype;

    public ModelMeta(Class<? extends AbstractMessageLite> messageClass, Class<? extends Builder<?>> builderClass, Map<String, ? extends PropertyMeta> propertyMetaMap, int minNumber, int maxNumber) {
        _messageClass = messageClass;
        _builderClass = builderClass;
        _propertyMetaMap = propertyMetaMap;
        _minNumber = minNumber;
        _maxNumber = maxNumber;
        try {
            _prototype = (AbstractMessageLite) _messageClass.getDeclaredMethod("getDefaultInstance").invoke(new Object[] {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Class<?> getMessageClass() {
        return _messageClass;
    }

    public Class<?> getBuilderClass() {
        return _builderClass;
    }

    public AbstractMessageLite getPrototype() {
        return _prototype;
    }

    public Collection<? extends PropertyMeta> getPropertyMetas() {
        return _propertyMetaMap.values();
    }

    public PropertyMeta getPropertyMeta(String normalizedName) {
        return _propertyMetaMap.get(normalizedName);
    }

    public int getPropertyCount() {
        return _propertyMetaMap.size();
    }

    public int getMinNumber() {
        return _minNumber;
    }

    public int getMaxNumber() {
        return _maxNumber;
    }

    public String toString() {
        return new StringBuilder().append(_messageClass.getSimpleName()).append(" properties=").append(getPropertyCount()).append(" minNum=").append(getMinNumber()).append(" maxNum=").append(getMaxNumber()).append('\n').append(_propertyMetaMap.values()).toString();
    }

    public interface Factory {

        public ModelMeta create(Class<? extends AbstractMessageLite> messageClass);
    }
}

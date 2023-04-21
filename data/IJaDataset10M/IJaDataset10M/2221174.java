package com.otom.bcel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import com.otom.bcel.annotations.CollectionHint;
import com.otom.bcel.annotations.CollectionInfo;
import com.otom.bcel.annotations.GetMethod;
import com.otom.bcel.annotations.Impls;
import com.otom.bcel.annotations.MapCollectionList;
import com.otom.bcel.annotations.MapTo;
import com.otom.bcel.annotations.SetMethod;

public class FieldInfo extends BaseObject {

    private Class<?> clazz;

    private Class<?> targetClass;

    private String name;

    private Class<?> fieldType;

    private String getMethod;

    private String setMethod;

    private String converterMethod;

    private FieldInfo childFieldInfo;

    private FieldInfo parentFieldInfo;

    private Class<?> concreteCollectionType;

    private Class<?> elementType;

    private String collectionObjectConverter;

    private String indexValue;

    private List<CollectionInfo> collectionTypeHintList;

    private String resolver;

    private Class<?>[] implementors;

    public FieldInfo() {
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public Class<?>[] getImplementors() {
        return implementors;
    }

    public void setImplementors(Class<?>[] implementors) {
        this.implementors = implementors;
    }

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }

    public List<CollectionInfo> getCollectionTypeHintList() {
        return collectionTypeHintList;
    }

    public void setCollectionTypeHintList(List<CollectionInfo> collectionTypeHintList) {
        this.collectionTypeHintList = collectionTypeHintList;
    }

    public String getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(String indexValue) {
        this.indexValue = indexValue;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getCollectionObjectConverter() {
        return collectionObjectConverter;
    }

    public void setCollectionObjectConverter(String collectionObjectConverter) {
        this.collectionObjectConverter = collectionObjectConverter;
    }

    public Class<?> getConcreteCollectionType() {
        return concreteCollectionType;
    }

    public void setConcreteCollectionType(Class<?> concreteCollectionType) {
        this.concreteCollectionType = concreteCollectionType;
    }

    public Class<?> getElementType() {
        return elementType;
    }

    public void setElementType(Class<?> elementType) {
        this.elementType = elementType;
    }

    public FieldInfo getParentFieldInfo() {
        return parentFieldInfo;
    }

    public void setParentFieldInfo(FieldInfo parentFieldInfo) {
        this.parentFieldInfo = parentFieldInfo;
    }

    public FieldInfo getChildFieldInfo() {
        return childFieldInfo;
    }

    public void setChildFieldInfo(FieldInfo childFieldInfo) {
        this.childFieldInfo = childFieldInfo;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(String getMethod) {
        this.getMethod = getMethod;
    }

    public String getSetMethod() {
        return setMethod;
    }

    public void setSetMethod(String setMethod) {
        this.setMethod = setMethod;
    }

    public String getConverterMethod() {
        return converterMethod;
    }

    public void setConverterMethod(String converterMethod) {
        this.converterMethod = converterMethod;
    }

    public void initFromAnnotations(String fieldName) {
        String[] fieldNames = fieldName.split("\\.");
        if (fieldNames[0].indexOf("[") != -1) {
            this.name = fieldNames[0].substring(0, fieldNames[0].indexOf("["));
            indexValue = fieldNames[0].substring(fieldNames[0].indexOf("[") + 1, fieldNames[0].indexOf("]"));
        } else {
            this.name = fieldNames[0];
        }
        Field field = validateFieldName();
        this.fieldType = field.getType();
        if (FieldHelper.isInterfaceOrAbstractClass(fieldType)) {
            Impls impls = field.getAnnotation(Impls.class);
            if (impls != null) {
                resolver = impls.resolver();
                implementors = impls.value();
            }
        }
        initConverterFromAnnotation(field);
        initGetMethodFromAnnotation(field);
        initSetMethodFromAnnotation(field);
        initCollectionTypeFromAnnotation(field);
        if (fieldNames.length > 1) {
            createChildInfo();
            childFieldInfo.initFromAnnotations(fieldName.substring(fieldName.indexOf(".") + 1));
        }
    }

    private void initConverterFromAnnotation(Field field) {
        MapTo fieldMapper = field.getAnnotation(MapTo.class);
        if (fieldMapper != null) {
            if (converterMethod == null || converterMethod.trim().equals("")) {
                if (fieldMapper.convertThisToTarget() != null && !fieldMapper.convertThisToTarget().equals("")) {
                    converterMethod = fieldMapper.convertThisToTarget();
                }
            }
        }
    }

    private void createChildInfo() {
        childFieldInfo = new FieldInfo();
        if (indexValue != null) {
            if (fieldType.isArray()) {
                childFieldInfo.setClazz(fieldType.getComponentType());
            } else {
                childFieldInfo.setClazz(elementType);
            }
        } else {
            childFieldInfo.setClazz(fieldType);
        }
        childFieldInfo.setConverterMethod(converterMethod);
        childFieldInfo.setCollectionObjectConverter(collectionObjectConverter);
        childFieldInfo.setConcreteCollectionType(concreteCollectionType);
        childFieldInfo.setElementType(elementType);
        childFieldInfo.setCollectionTypeHintList(collectionTypeHintList);
        childFieldInfo.setParentFieldInfo(this);
    }

    private void initCollectionTypeFromAnnotation(Field field) {
        if (Collection.class.isAssignableFrom(fieldType) || Map.class.isAssignableFrom(fieldType)) {
            if (indexValue != null) {
                if (collectionTypeHintList != null && collectionTypeHintList.size() > 0) {
                    CollectionInfo collectionType = collectionTypeHintList.remove(0);
                    if (collectionType.type() != void.class) {
                        elementType = collectionType.type();
                    }
                    if (collectionType.impl() != void.class) {
                        concreteCollectionType = collectionType.impl();
                    }
                }
            }
            CollectionHint collectionType = field.getAnnotation(CollectionHint.class);
            setCollectionInfo(collectionType);
            MapCollectionList mapCollectionList = field.getAnnotation(MapCollectionList.class);
            if (mapCollectionList != null) {
                outer: for (CollectionHint collectionHint : mapCollectionList.value()) {
                    if (collectionHint.source().forClass() == clazz) {
                        for (CollectionInfo collectionInfo : collectionHint.target()) {
                            if (collectionInfo.forClass() == targetClass) {
                                setCollectionInfo(collectionHint);
                                break outer;
                            }
                        }
                    }
                }
            }
        }
        if (Collection.class.isAssignableFrom(fieldType)) {
            if (fieldType.isInterface()) {
                if (concreteCollectionType == null || concreteCollectionType == void.class) {
                    HashMap<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
                    map.put(List.class, ArrayList.class);
                    map.put(Set.class, HashSet.class);
                    map.put(SortedSet.class, TreeSet.class);
                    if (map.containsKey(fieldType)) {
                        concreteCollectionType = map.get(fieldType);
                    }
                }
            } else {
                if (concreteCollectionType == null || concreteCollectionType == void.class) {
                    if (!Modifier.isAbstract(fieldType.getModifiers())) {
                        concreteCollectionType = fieldType;
                    }
                }
            }
            if (elementType == null || elementType == void.class) {
                if (field.getGenericType() instanceof ParameterizedType) {
                    elementType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                }
            }
        }
        if (Map.class.isAssignableFrom(fieldType)) {
            if (fieldType.isInterface()) {
                if (concreteCollectionType == null || concreteCollectionType == void.class) {
                    HashMap<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
                    map.put(Map.class, HashMap.class);
                    map.put(SortedMap.class, TreeMap.class);
                    if (map.containsKey(fieldType)) {
                        concreteCollectionType = map.get(fieldType);
                    }
                }
            } else {
                if (concreteCollectionType == null || concreteCollectionType == void.class) {
                    if (!Modifier.isAbstract(fieldType.getModifiers())) {
                        concreteCollectionType = fieldType;
                    }
                }
            }
            if (elementType == null || elementType == void.class) {
                if (field.getGenericType() instanceof ParameterizedType) {
                    elementType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1];
                }
            }
        }
    }

    private void setCollectionInfo(CollectionHint collectionType) {
        if (collectionType != null && collectionType.source() != null) {
            CollectionInfo collectionInfo = collectionType.source();
            if (concreteCollectionType == null) {
                concreteCollectionType = collectionInfo.impl();
            }
            if (elementType == null) {
                elementType = collectionInfo.type();
            }
            if (collectionObjectConverter == null && !collectionInfo.convert().equals("")) {
                collectionObjectConverter = collectionInfo.convert();
            }
        }
    }

    private void initSetMethodFromAnnotation(Field field) {
        if (setMethod == null || setMethod.equals("")) {
            String defaultSetMethod = null;
            for (Method method : FieldHelper.getMethods(clazz)) {
                String methodName = method.getName();
                SetMethod setMethodMapper = method.getAnnotation(SetMethod.class);
                if (setMethodMapper != null && setMethodMapper.forField().equals(this.name)) {
                    if (setMethod != null) {
                        throw new ValidationException("Setter method already defined for field:" + this.name);
                    }
                    this.setMethod = methodName;
                }
                String setmethodFromField = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                if ((methodName.equals(setmethodFromField)) && (method.getParameterTypes() != null && method.getParameterTypes().length == 1) && method.getParameterTypes()[0] == field.getType()) {
                    defaultSetMethod = method.getName();
                }
            }
            if (setMethod == null || setMethod.equals("")) {
                this.setMethod = defaultSetMethod;
            }
        }
    }

    private void initGetMethodFromAnnotation(Field field) {
        if (getMethod == null || getMethod.equals("")) {
            String defaultGetMethod = null;
            for (Method method : FieldHelper.getMethods(clazz)) {
                String methodName = method.getName();
                GetMethod getMethodMapper = method.getAnnotation(GetMethod.class);
                if (getMethodMapper != null && getMethodMapper.forField().equals(this.name)) {
                    if (this.getMethod != null && !getMethod.equals("")) {
                        throw new ValidationException("Getter method already defined for field:" + this.name);
                    }
                    this.getMethod = methodName;
                }
                String getMethodEnd = name.substring(0, 1).toUpperCase() + name.substring(1);
                if ((methodName.equals("get" + getMethodEnd) || (methodName.equals("is" + getMethodEnd) && method.getReturnType() == boolean.class)) && (method.getParameterTypes() == null || method.getParameterTypes().length == 0) && method.getReturnType() == field.getType()) {
                    defaultGetMethod = method.getName();
                }
            }
            if (getMethod == null || getMethod.equals("")) {
                this.getMethod = defaultGetMethod;
            }
        }
    }

    private Field validateFieldName() {
        try {
            return FieldHelper.getField(clazz, name);
        } catch (NoSuchFieldException e) {
            throw new ValidationException(e, e.getMessage() + ":" + clazz);
        }
    }

    public FieldInfo getFieldInfoForCollection() {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setName(elementType.getSimpleName() + "Element");
        fieldInfo.setFieldType(elementType);
        fieldInfo.setResolver(resolver);
        fieldInfo.setSetMethod("add");
        fieldInfo.setConverterMethod(collectionObjectConverter);
        fieldInfo.setParentFieldInfo(this);
        fieldInfo.setChildFieldInfo(childFieldInfo);
        fieldInfo.setIndexValue(indexValue);
        return fieldInfo;
    }

    public FieldInfo getFieldInfoForMap() {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setName(elementType.getSimpleName() + "Element");
        fieldInfo.setFieldType(elementType);
        fieldInfo.setSetMethod("put");
        fieldInfo.setResolver(resolver);
        fieldInfo.setIndexValue(indexValue);
        fieldInfo.setParentFieldInfo(this);
        fieldInfo.setConverterMethod(collectionObjectConverter);
        fieldInfo.setChildFieldInfo(childFieldInfo);
        return fieldInfo;
    }

    public FieldInfo getFieldInfoForArray(String arrayIndex) {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.setName(fieldType.getComponentType().getSimpleName() + "Element");
        fieldInfo.setFieldType(fieldType.getComponentType());
        fieldInfo.setArrayIndex(arrayIndex);
        fieldInfo.setResolver(resolver);
        fieldInfo.setParentFieldInfo(this);
        fieldInfo.setChildFieldInfo(childFieldInfo);
        return fieldInfo;
    }

    private String arrayIndex;

    public String getArrayIndex() {
        return arrayIndex;
    }

    public void setArrayIndex(String arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public boolean isPrimitiveWrapper() {
        return fieldType == Integer.class || fieldType == Character.class || fieldType == Double.class || fieldType == Boolean.class || fieldType == Float.class || fieldType == Long.class || fieldType == Short.class || fieldType == Byte.class;
    }

    public boolean isPrimitive() {
        return fieldType == int.class || fieldType == char.class || fieldType == double.class || fieldType == boolean.class || fieldType == float.class || fieldType == long.class || fieldType == short.class || fieldType == byte.class;
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom(fieldType);
    }

    public boolean isMap() {
        return Map.class.isAssignableFrom(fieldType);
    }

    public boolean isListable() {
        return isMap() || isCollection() || fieldType.isArray();
    }

    public void throwValidationException(String message) {
        throw new ValidationException(message);
    }
}

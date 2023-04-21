package org.semanticorm;

import java.lang.reflect.Field;
import java.util.Collection;

public class RelationDemux {

    private Class<?> _targetClass;

    private Field _targetField;

    private boolean _isCollection;

    public RelationDemux(String relation) throws DatasetCreationException {
        String[] split = relation.split("#");
        try {
            _targetClass = Class.forName(split[split.length - 2].replace('_', '.'));
            String fieldName = split[split.length - 1];
            if (!OrmMetadata.BLANK_NODE.equals(fieldName)) {
                _targetField = _targetClass.getDeclaredField(fieldName);
                _isCollection = Collection.class.isAssignableFrom(_targetField.getType());
            }
        } catch (ClassNotFoundException e) {
            throw new DatasetCreationException("Could not locate class in relation", e);
        } catch (SecurityException e) {
            throw new DatasetCreationException("Could not access class in relation", e);
        } catch (NoSuchFieldException e) {
            throw new DatasetCreationException("Could not find field in relation", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <A extends RdfResource> A newInstance() throws DatasetCreationException {
        try {
            return (A) _targetClass.newInstance();
        } catch (InstantiationException e) {
            throw new DatasetCreationException("Could not set create object due to exception thrown in constructor", e);
        } catch (IllegalAccessException e) {
            throw new DatasetCreationException("Could not set create object due to insufficient access", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object target, Object value) throws DatasetCreationException {
        _targetField.setAccessible(true);
        try {
            if (_isCollection) {
                Collection collection = (Collection) _targetField.get(target);
                collection.add(value);
            } else {
                _targetField.set(target, value);
            }
        } catch (IllegalArgumentException e) {
            throw new DatasetCreationException("Could not set field in relation, invalid value", e);
        } catch (IllegalAccessException e) {
            throw new DatasetCreationException("Could not access field in relation", e);
        }
    }
}

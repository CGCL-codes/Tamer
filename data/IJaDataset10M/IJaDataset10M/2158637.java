package org.springframework.binding.support;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.PropertyAccessor;
import org.springframework.binding.PropertyMetadata;
import org.springframework.binding.PropertyValueModel;
import org.springframework.binding.Undefined;
import org.springframework.binding.valuemodel.AbstractValueModel;
import org.springframework.binding.valuemodel.AbstractValueModelWrapper;
import org.springframework.binding.valuemodel.ValueModel;
import org.springframework.richclient.util.Assert;
import org.springframework.util.CachingMapDecorator;

public class NestedPropertyAccessStrategy extends AbstractPropertyAccessStrategy {

    private final ValueModel rootObjectHolder;

    private final SourceObjectProxyFactory sourceObjectProxyFactory = new DefaultSouceObjectProxyFactory();

    private final PropertyValueModelFactoryLocator propertyValueModelFactoryLocator;

    public NestedPropertyAccessStrategy(ValueModel rootObjectHolder) {
        this(rootObjectHolder, new DefaultPropertyValueModelFactoryLocator());
    }

    public NestedPropertyAccessStrategy(ValueModel rootObjectHolder, PropertyValueModelFactoryLocator propertyValueModelFactoryLocator) {
        Assert.required(rootObjectHolder, "rootObjectHolder");
        this.rootObjectHolder = sourceObjectProxyFactory.proxySourceObject(wrapAsPropertyValueModel(rootObjectHolder), tryToInferType(rootObjectHolder));
        this.propertyValueModelFactoryLocator = propertyValueModelFactoryLocator;
    }

    public Object getBackingObject() {
        return rootObjectHolder.getValue();
    }

    public ValueModel getBackingObjectHolder() {
        return rootObjectHolder;
    }

    public PropertyValueModel getFieldValueModel(String propertyPath) {
        Assert.hasText(propertyPath, "propertyPath must have text");
        String[] splitPath = splitPropertyPath(propertyPath);
        ValueModel parent = rootObjectHolder;
        for (int i = 0; i < splitPath.length; i++) {
            parent = getFieldPathValueModel(parent, splitPath[i]);
        }
        return (PropertyValueModel) parent;
    }

    /**
     * Splits the provided property path into it's sub-paths, ignoring dots in keys 
     * (like "map[my.key]").
     */
    private String[] splitPropertyPath(String propertyPath) {
        List splitPaths = new ArrayList();
        int lastPathStart = 0;
        boolean inKey = false;
        for (int i = 0; i < propertyPath.length(); i++) {
            switch(propertyPath.charAt(i)) {
                case PropertyAccessor.PROPERTY_KEY_SUFFIX_CHAR:
                    inKey = false;
                    if (lastPathStart < i) {
                        splitPaths.add(propertyPath.substring(lastPathStart, i));
                    }
                    lastPathStart = i + 1;
                    break;
                case PropertyAccessor.PROPERTY_KEY_PREFIX_CHAR:
                    inKey = true;
                    if (lastPathStart < i) {
                        splitPaths.add(propertyPath.substring(lastPathStart, i));
                    }
                    lastPathStart = i + 1;
                    break;
                case PropertyAccessor.NESTED_PROPERTY_SEPARATOR_CHAR:
                    if (!inKey) {
                        if (lastPathStart < i) {
                            splitPaths.add(propertyPath.substring(lastPathStart, i));
                        }
                        lastPathStart = i + 1;
                    }
                    break;
            }
        }
        if (lastPathStart < propertyPath.length() - 1) {
            splitPaths.add(propertyPath.substring(lastPathStart));
        }
        return (String[]) splitPaths.toArray(new String[splitPaths.size()]);
    }

    protected static Class tryToInferType(ValueModel objectHolder) {
        Object sourceObject = objectHolder.getValue();
        if (sourceObject != null && !(sourceObject instanceof Undefined)) {
            return sourceObject.getClass();
        } else if (objectHolder instanceof PropertyValueModel) {
            return ((PropertyValueModel) objectHolder).getMetadata().getType();
        } else {
            throw new UnsupportedOperationException("Unable to infer type for property with null parent.");
        }
    }

    private PropertyValueModel wrapAsPropertyValueModel(ValueModel rootObjectHolder) {
        if (rootObjectHolder instanceof PropertyValueModel) {
            return (PropertyValueModel) rootObjectHolder;
        } else {
            return new PropertyValueModelAdapter(rootObjectHolder);
        }
    }

    private final class DefaultSouceObjectProxyFactory implements SourceObjectProxyFactory {

        public PropertyValueModel proxySourceObject(PropertyValueModel sourceObjectHolder, Class sourceObjectType) {
            if ((sourceObjectType.isArray() || Collection.class.isAssignableFrom(sourceObjectType)) && sourceObjectType != CollectionValueModel.class) {
                return new CollectionValueModel(sourceObjectHolder, sourceObjectType);
            } else {
                return sourceObjectHolder;
            }
        }
    }

    private static class PropertyValueModelAdapter extends AbstractValueModelWrapper implements PropertyValueModel {

        private PropertyValueModelAdapter(ValueModel model) {
            super(model);
        }

        public PropertyMetadata getMetadata() {
            return new DefaultFieldMetadata2("", tryToInferType(getWrappedValueModel()), true, true);
        }
    }

    private class PropertyPathValueModel extends AbstractValueModel implements PropertyChangeListener, PropertyValueModel {

        private final ValueModel parentValueModel;

        private final String propertyName;

        private PropertyValueModel propertyValueModel;

        private Object savedPropertyValue;

        protected PropertyPathValueModel(ValueModel parentValueModel, String propertyName) {
            this.parentValueModel = parentValueModel;
            this.parentValueModel.addValueChangeListener(this);
            this.propertyName = propertyName;
            updatePropertyValueModel();
            savedPropertyValue = getValue();
        }

        public Object getValue() {
            return propertyValueModel.getValue();
        }

        public void setValue(Object newValue) {
            propertyValueModel.setValue(newValue);
        }

        public PropertyMetadata getMetadata() {
            return propertyValueModel.getMetadata();
        }

        private void updatePropertyValueModel() {
            PropertyValueModel newPropertyValueModel = getFieldValueModel(parentValueModel, propertyName);
            if (newPropertyValueModel != propertyValueModel) {
                if (propertyValueModel != null) {
                    propertyValueModel.removeValueChangeListener(this);
                }
                newPropertyValueModel.addValueChangeListener(this);
                propertyValueModel = newPropertyValueModel;
            }
        }

        private void parentChanged() {
            updatePropertyValueModel();
            Object newValue = getValue();
            Object oldSavedPropertyValue = savedPropertyValue;
            savedPropertyValue = newValue;
            fireValueChange(oldSavedPropertyValue, newValue);
        }

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getSource() == propertyValueModel) {
                fireValueChange(evt.getOldValue(), evt.getNewValue());
                savedPropertyValue = evt.getNewValue();
            } else {
                parentChanged();
            }
        }
    }

    protected ValueModel getFieldPathValueModel(ValueModel parentValueModel, String propertyName) {
        return (ValueModel) ((Map) propertyPathValueModels.get(parentValueModel)).get(propertyName);
    }

    private Map propertyPathValueModels = new CachingMapDecorator(true) {

        protected Object create(final Object parentValueModel) {
            return new CachingMapDecorator(true) {

                protected Object create(final Object property) {
                    return new PropertyPathValueModel((ValueModel) parentValueModel, (String) property);
                }
            };
        }
    };

    protected PropertyValueModel getFieldValueModel(ValueModel parentValueModel, String propertyName) {
        PropertyValueModel propertyValueModel = ((PropertyValueModelFactory) ((Map) propertyValueModelFactories.get(parentValueModel.getValue())).get(tryToInferType(parentValueModel))).getFieldValueModel(propertyName);
        return sourceObjectProxyFactory.proxySourceObject(propertyValueModel, propertyValueModel.getMetadata().getType());
    }

    private final Map propertyValueModelFactories = new CachingMapDecorator(true) {

        protected Object create(final Object sourceObject) {
            return new CachingMapDecorator(true) {

                protected Object create(Object sourceObjectType) {
                    return propertyValueModelFactoryLocator.locatePropertyValueModelFactory(sourceObject, (Class) sourceObjectType);
                }
            };
        }
    };
}

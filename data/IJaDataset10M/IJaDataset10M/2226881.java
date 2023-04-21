package org.jage.workplace.pico.config;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jage.config.ConfigurationException;
import org.jage.property.IPropertyContainer;
import org.picocontainer.PicoContainer;

public class ObjectDefinition implements IObjectDefinition {

    private final Class<?> _type;

    private final String _name;

    private final List<ConstructorArgument> _constructorArguments;

    private final Map<String, PropertyInitializer> _propertyInitializers;

    private final List<IObjectDefinition> _innerObjectDefinitions;

    private final boolean _isSingleton;

    public ObjectDefinition(String name, Class<?> type, boolean isSingleton) {
        _name = name;
        _type = type;
        _constructorArguments = new ArrayList<ConstructorArgument>();
        _propertyInitializers = new HashMap<String, PropertyInitializer>();
        _innerObjectDefinitions = new ArrayList<IObjectDefinition>();
        _isSingleton = isSingleton;
    }

    public Class<?> getType() {
        return _type;
    }

    public String getName() {
        return _name;
    }

    public boolean isSingleton() {
        return _isSingleton;
    }

    public void addConstructorArgument(ConstructorArgument argument) {
        _constructorArguments.add(argument);
    }

    public List<ConstructorArgument> getConstructorArguments() {
        return Collections.unmodifiableList(_constructorArguments);
    }

    public void addPropertyInitializer(PropertyInitializer initializer) {
        if (_propertyInitializers.containsKey(initializer.getPropertyName())) {
            throw new IllegalArgumentException("This object definition already contains initializer for property with the given name.");
        }
        _propertyInitializers.put(initializer.getPropertyName(), initializer);
    }

    public Map<String, PropertyInitializer> getPropertyInitializers() {
        return Collections.unmodifiableMap(_propertyInitializers);
    }

    public void addInnerObjectDefinition(IObjectDefinition innerDefinition) {
        _innerObjectDefinitions.add(innerDefinition);
    }

    public List<IObjectDefinition> getInnerObjectDefinitions() {
        return Collections.unmodifiableList(_innerObjectDefinitions);
    }

    @SuppressWarnings("unchecked")
    public Object createInstance(PicoContainer container) throws ConfigurationException {
        List<Object> constructionValues = new ArrayList<Object>();
        List<Class> constructionTypes = new ArrayList<Class>();
        for (ConstructorArgument argument : _constructorArguments) {
            Object value = argument.getValueProvider().getValue(container);
            if (value == null) {
                throw new ConfigurationException("Cannot pass null value to constructor.");
            }
            constructionValues.add(value);
            constructionTypes.add(value.getClass());
        }
        try {
            Constructor constructor = _type.getConstructor(constructionTypes.toArray(new Class[constructionTypes.size()]));
            IPropertyContainer result = (IPropertyContainer) constructor.newInstance(constructionValues.toArray());
            for (PropertyInitializer initializer : _propertyInitializers.values()) {
                initializer.initializeObjectProperty(result, container);
            }
            return result;
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage(), e);
        }
    }
}

package org.eclipse.epsilon.generics.bindings;

import java.util.HashMap;
import java.util.Map;

public class InMemoryModelBindings extends ModelBindings {

    private final Map<String, String> bindings = new HashMap<String, String>();

    private final Map<String, String> reverseBindings = new HashMap<String, String>();

    private final Map<String, Map<String, String>> propertyBindings = new HashMap<String, Map<String, String>>();

    public void add(String genericType, String concreteType) {
        bindings.put(genericType, concreteType);
        reverseBindings.put(concreteType, genericType);
    }

    public void add(String genericType, String genericProperty, String concreteProperty) {
        if (!propertyBindings.containsKey(genericType)) {
            propertyBindings.put(genericType, new HashMap<String, String>());
        }
        propertyBindings.get(genericType).put(genericProperty, concreteProperty);
    }

    @Override
    public String resolve(String genericType) {
        return bindings.get(genericType);
    }

    @Override
    public String resolve(String concreteType, String genericProperty) {
        String concreteProperty = null;
        String genericType = reverseBindings.get(concreteType);
        if (propertyBindings.containsKey(genericType)) {
            concreteProperty = propertyBindings.get(genericType).get(genericProperty);
        }
        return concreteProperty;
    }
}

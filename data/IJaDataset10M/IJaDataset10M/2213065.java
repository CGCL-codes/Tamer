package org.jage.workplace.pico.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jage.config.ConfigurationException;
import org.picocontainer.PicoContainer;

public class ListDefinition implements IObjectDefinition {

    private final String _name;

    private final boolean _isSingleton;

    private final List<IValueProvider> _items;

    private final List<IObjectDefinition> _innerDefinitions;

    public ListDefinition(String name, boolean isSingleton) {
        _name = name;
        _isSingleton = isSingleton;
        _items = new ArrayList<IValueProvider>();
        _innerDefinitions = new ArrayList<IObjectDefinition>();
    }

    public String getName() {
        return _name;
    }

    public Class<?> getType() {
        return List.class;
    }

    public boolean isSingleton() {
        return _isSingleton;
    }

    public void addItem(IValueProvider item) {
        _items.add(item);
    }

    public List<IValueProvider> getItems() {
        return Collections.unmodifiableList(_items);
    }

    public Object createInstance(PicoContainer container) throws ConfigurationException {
        List<Object> result = new ArrayList<Object>();
        for (IValueProvider valueProvider : _items) {
            result.add(valueProvider.getValue(container));
        }
        return result;
    }

    public List<IObjectDefinition> getInnerObjectDefinitions() {
        return Collections.unmodifiableList(_innerDefinitions);
    }

    public void addInnerObjectDefinition(IObjectDefinition definition) {
        _innerDefinitions.add(definition);
    }
}

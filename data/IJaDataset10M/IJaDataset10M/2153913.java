package org.beanutopia.htmap;

import org.beanutopia.slot.Slot;
import org.beanutopia.UpdatableProperty;
import org.beanutopia.exception.PropertyDefinitionException;
import org.beanutopia.function.Injection;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * @author: Yardena
 * @date: Oct 27, 2009 3:32:08 PM
 */
public class HTSlot<V> implements Slot<HasProperties, V> {

    private UpdatableProperty<?, V> property;

    public HTSlot(UpdatableProperty<?, V> property) {
        this.property = property;
    }

    public Function<HasProperties, V> getProjection() {
        return new Function<HasProperties, V>() {

            public V apply(HasProperties object) {
                return property.valueType().cast(object.bean().getProperty(property));
            }
        };
    }

    public Injection<HasProperties, V> getInjection() {
        return new Injection<HasProperties, V>() {

            public void apply(HasProperties object, V value) {
                object.bean().setProperty(property, value);
            }
        };
    }

    public Slot<HasProperties, V> initialize() throws PropertyDefinitionException {
        return this;
    }

    public Predicate<Object> supported() {
        return org.beanutopia.function.PropertyFunctions.isInstanceOf(HasProperties.class);
    }
}

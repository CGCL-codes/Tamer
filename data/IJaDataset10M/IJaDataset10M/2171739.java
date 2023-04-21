package org.ujoframework.implementation.factory;

import org.ujoframework.Ujo;
import org.ujoframework.UjoProperty;
import org.ujoframework.extensions.AbstractUjo;

/**
 * The Ujo Factory. A method called readValue() create new instance of the property always by a property type.
 * <br>Each the property type class (see getType() method) must have got at least one of constructor:
 * <ul>
 * <li>an two parameters constructor with types <pre>Ujo</code> and <code>UjoProperty</code> or</li>.
 * <li>a no parameter constructor</li>
 * </ul>
 * @author Pavel Ponec
 * @since 0.81 
 * @composed 1 - * FactoryProperty
  */
public abstract class FactoryUjo extends AbstractUjo {

    /** It is an unsupported function in this implementation. */
    public void writeValue(final UjoProperty property, final Object value) {
        throw new UnsupportedOperationException();
    }

    /** Method readValue() creates a new instance of the property always.
     * @see FactoryProperty#getValue(Ujo) FactoryProperty.getValue(Ujo)
     */
    @SuppressWarnings("unchecked")
    public Object readValue(final UjoProperty property) {
        Object result = ((FactoryProperty) property).readValue(this);
        return result != null ? result : property.getDefault();
    }

    /** A Property Factory, a default value is null. */
    protected static <UJO extends Ujo, VALUE> FactoryProperty<UJO, VALUE> newProperty(String name, Class<VALUE> type) {
        return new FactoryProperty<UJO, VALUE>(name, type);
    }
}

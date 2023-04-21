package org.databene.commons.accessor;

/**
 * Consecutively invokes a series of accessors 
 * using the result value of each invocation as input value for the next.<br/>
 * <br/>
 * Created: 21.07.2007 07:02:07
 * @author Volker Bergmann
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TypedAccessorChain implements TypedAccessor {

    private TypedAccessor[] subAccessors;

    public TypedAccessorChain(TypedAccessor[] realAccessors) {
        this.subAccessors = realAccessors;
    }

    public TypedAccessor[] getSubAccessors() {
        return subAccessors;
    }

    public Class<?> getValueType() {
        return subAccessors[subAccessors.length - 1].getValueType();
    }

    public Object getValue(Object target) {
        Object result = target;
        for (TypedAccessor accessor : subAccessors) result = accessor.getValue(result);
        return result;
    }
}

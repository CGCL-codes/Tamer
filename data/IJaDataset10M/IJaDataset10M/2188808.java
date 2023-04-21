package org.jmetis.reflection.property;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jmetis.kernel.metadata.IPropertyDescription;

/**
 * {@code MethodAccessor}
 * 
 * @author aerlach
 */
public class MethodAccessor extends PropertyAccessor {

    private Method readMethod;

    private Method writeMethod;

    /**
	 * Constructs a new {@code MethodAccessor} instance.
	 */
    public MethodAccessor(IPropertyDescription propertyDescriptor, Method readMethod, Method writeMethod) {
        super(propertyDescriptor);
        this.readMethod = this.mustNotBeNull("readMethod", readMethod);
        this.writeMethod = writeMethod;
    }

    public Object getValueFrom(Object object) {
        try {
            return readMethod.invoke(object);
        } catch (InvocationTargetException ex) {
            accessPropertyFailedBecauseOf(readMethod, object, ex.getCause());
        } catch (Exception ex) {
            accessPropertyFailedBecauseOf(readMethod, object, ex);
        }
        return null;
    }

    public boolean isReadOnlyFor(Object object) {
        return writeMethod == null;
    }

    public void setValueInto(Object value, Object object) {
        try {
            writeMethod.invoke(object, value);
        } catch (InvocationTargetException ex) {
            modifyPropertyFailedBecauseOf(writeMethod, object, value, ex.getCause());
        } catch (Exception ex) {
            modifyPropertyFailedBecauseOf(writeMethod, object, value, ex);
        }
    }
}

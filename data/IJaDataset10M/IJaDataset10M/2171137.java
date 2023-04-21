package net.sf.dozer.util.mapping.propertydescriptor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import net.sf.dozer.util.mapping.fieldmap.HintContainer;
import net.sf.dozer.util.mapping.util.MappingUtils;
import net.sf.dozer.util.mapping.util.ReflectionUtils;

/**
 * 
 * Internal class used to read and write values for fields that follow the java bean spec and have corresponding
 * getter/setter methods for the field that are name accordingly. If the field does not have the necessary
 * getter/setter, an exception will be thrown. Only intended for internal use.
 * 
 * @author garsombke.franz
 * @author tierney.matt
 */
public class JavaBeanPropertyDescriptor extends GetterSetterPropertyDescriptor {

    private PropertyDescriptor pd;

    public JavaBeanPropertyDescriptor(Class clazz, String fieldName, boolean isIndexed, int index, HintContainer srcDeepIndexHintContainer, HintContainer destDeepIndexHintContainer) {
        super(clazz, fieldName, isIndexed, index, srcDeepIndexHintContainer, destDeepIndexHintContainer);
    }

    public Method getWriteMethod() throws NoSuchMethodException {
        Method writeMethod = getPropertyDescriptor(destDeepIndexHintContainer).getWriteMethod();
        if (writeMethod == null) {
            throw new NoSuchMethodException("Unable to determine write method for Field: " + fieldName + " in Class: " + clazz);
        }
        return writeMethod;
    }

    protected String getSetMethodName() throws NoSuchMethodException {
        return getWriteMethod().getName();
    }

    protected Method getReadMethod() throws NoSuchMethodException {
        Method result = getPropertyDescriptor(srcDeepIndexHintContainer).getReadMethod();
        if (result == null) {
            throw new NoSuchMethodException("Unable to determine read method for Field: " + fieldName + " in Class: " + clazz);
        }
        return result;
    }

    private PropertyDescriptor getPropertyDescriptor(HintContainer deepIndexHintContainer) {
        if (pd == null) {
            pd = ReflectionUtils.findPropertyDescriptor(clazz, fieldName, deepIndexHintContainer);
            if (pd == null) {
                MappingUtils.throwMappingException("Property: " + fieldName + " not found in Class: " + clazz);
            }
        }
        return pd;
    }
}

package dnl.util.lang.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Accessors {

    public static void setPropertyValue(Object obj, String propertyName, Object value) throws PropertyAccessException {
        Method setter = Methods.getSetter(obj.getClass(), propertyName);
        try {
            setter.invoke(obj, new Object[] { value });
        } catch (IllegalAccessException e) {
            throw new PropertyAccessException(e);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof Exception) throw new PropertyAccessException(e.getTargetException());
            throw new PropertyAccessException(e);
        }
    }
}

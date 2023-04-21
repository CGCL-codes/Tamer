package oracle.toplink.essentials.internal.security;

import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;

public class PrivilegedGetValueFromField implements PrivilegedExceptionAction {

    private Field field;

    private Object object;

    public PrivilegedGetValueFromField(Field field, Object object) {
        this.field = field;
        this.object = object;
    }

    public Object run() throws IllegalAccessException {
        return PrivilegedAccessHelper.getValueFromField(field, object);
    }
}

package yaw.core.va;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ValueAccessorUtil {

    public static IValueAccessor[] getAll(Class<?> owner) {
        List<Object> list = new ArrayList<Object>();
        for (Field field : owner.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
                try {
                    Object elem = field.get(owner);
                    if (elem instanceof IValueAccessor) list.add(elem);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
        return list.toArray(new IValueAccessor[list.size()]);
    }
}

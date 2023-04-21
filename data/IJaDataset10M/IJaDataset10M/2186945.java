package xcordion.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import xcordion.api.TestElement;

public abstract class XmlUtils {

    public static <T extends TestElement<T>> Object elementToValue(T element, Class asClass) throws RuntimeException {
        Object value = element.getValue();
        if (value != null && asClass != null && !value.getClass().isAssignableFrom(asClass)) {
            if (asClass.isAssignableFrom(String.class)) {
                return value.toString();
            }
            Constructor bestOneArg = null;
            Constructor stringOneArg = null;
            for (Constructor c : asClass.getConstructors()) {
                Class[] paramClasses = c.getParameterTypes();
                if (paramClasses.length != 1) {
                    continue;
                }
                if (paramClasses[0].equals(value.getClass())) {
                    bestOneArg = c;
                    break;
                } else if (paramClasses[0].isAssignableFrom(value.getClass()) && (bestOneArg == null || !bestOneArg.getParameterTypes()[0].isAssignableFrom(paramClasses[0]))) {
                    bestOneArg = c;
                } else if (paramClasses[0].isAssignableFrom(String.class)) {
                    stringOneArg = c;
                }
            }
            try {
                if (bestOneArg != null) {
                    return bestOneArg.newInstance(value);
                } else if (stringOneArg != null) {
                    return stringOneArg.newInstance(value.toString());
                }
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return element.getValue();
    }

    public static <T extends TestElement<T>> String getFirstChildHref(T element) {
        String href = element.getAttribute("href");
        if (href != null) {
            return href;
        }
        for (T child : element.getChildren()) {
            href = getFirstChildHref(child);
            if (href != null) {
                return href;
            }
        }
        return null;
    }
}

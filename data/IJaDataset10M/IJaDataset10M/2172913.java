package wrm.saferJava.oval.guard;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.WeakHashMap;
import wrm.saferJava.oval.exception.ReflectionException;

/**
 * This implementation determines the names of constructor and method parameters by simply enumerating them based on there index:
 * arg0,arg1,arg2,..
 * @author Sebastian Thomschke
 */
public class ParameterNameResolverEnumerationImpl implements ParameterNameResolver {

    private final WeakHashMap<AccessibleObject, String[]> parameterNamesCache = new WeakHashMap<AccessibleObject, String[]>();

    /**
	 * {@inheritDoc}
	 */
    public String[] getParameterNames(final Constructor<?> constructor) throws ReflectionException {
        String[] parameterNames = parameterNamesCache.get(constructor);
        if (parameterNames == null) {
            final int parameterCount = constructor.getParameterTypes().length;
            parameterNames = new String[parameterCount];
            for (int i = 0; i < parameterCount; i++) {
                parameterNames[i] = "arg" + i;
            }
            parameterNamesCache.put(constructor, parameterNames);
        }
        return parameterNames;
    }

    /**
	 * {@inheritDoc}
	 */
    public String[] getParameterNames(final Method method) throws ReflectionException {
        String[] parameterNames = parameterNamesCache.get(method);
        if (parameterNames == null) {
            final int parameterCount = method.getParameterTypes().length;
            parameterNames = new String[parameterCount];
            for (int i = 0; i < parameterCount; i++) {
                parameterNames[i] = "arg" + i;
            }
            parameterNamesCache.put(method, parameterNames);
        }
        return parameterNames;
    }
}

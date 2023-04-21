package org.sourceforge.jemm.database.components.serviceproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.sourceforge.jemm.database.Identifiable;

public final class ServiceProxyFactory {

    private ServiceProxyFactory() {
    }

    @SuppressWarnings("unchecked")
    public static <IF, T extends Identifiable<K>, K> IF getProxyService(final Class<?> serviceClass, final Class<T> typeClass, Class<K> keyClass, final ObjectLocator<T, K> objLocator) {
        Class<?>[] ifClasses = { serviceClass };
        return (IF) Proxy.newProxyInstance(ServiceProxyFactory.class.getClassLoader(), ifClasses, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                K id = (K) args[0];
                T typeInstance = objLocator.get(id);
                Method m = null;
                try {
                    m = locateMethod(method, typeClass);
                    return m.invoke(typeInstance, removeFirst(args));
                } catch (InvocationTargetException ivte) {
                    throw ivte.getCause();
                } catch (Exception e) {
                    throw new RuntimeException("Internal error", e);
                } finally {
                    objLocator.release(typeInstance);
                }
            }

            private Object[] removeFirst(Object[] args) {
                Object[] newArgs = new Object[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                return newArgs;
            }

            private Class<?>[] removeFirstClassType(Class<?>[] args) {
                Class<?>[] newArgs = new Class<?>[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                return newArgs;
            }

            private Method locateMethod(Method serviceMethod, Class<T> typeClass) throws SecurityException, NoSuchMethodException {
                return typeClass.getMethod(serviceMethod.getName(), removeFirstClassType(serviceMethod.getParameterTypes()));
            }
        });
    }
}

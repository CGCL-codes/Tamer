package info.absu.snow.reflection;

import info.absu.snow.MoreThanOneResult;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;

/**
 * An implementation of an instantiator which uses a static method to create objects
 * of the given class.
 * @author Denys Rtveliashvili
 *
 */
class MethodBasedInstantiator<T> implements Instantiator<T> {

    private final Method method;

    /**
     * @param targetClass class of objects which should be created by this instantiator
     * @param methodName method name
     * @throws MoreThanOneResult if there is more than one method
     * @throws IllegalArgumentException if the method is not found or not suitable
     */
    public MethodBasedInstantiator(Class<? extends T> targetClass, String methodName) throws MoreThanOneResult, IllegalArgumentException {
        final Method method = ReflectionSupport.getInstanceForClass(targetClass).findMethod(methodName, null);
        if (method == null) throw new IllegalArgumentException("No suitable method was found with name " + methodName + " for class " + targetClass.getName());
        if (!Modifier.isStatic(method.getModifiers())) throw new IllegalArgumentException("The method '" + methodName + "' in class " + targetClass.getName() + " is not static, so there is no way to use it for instantiation.");
        if (!targetClass.equals(method.getReturnType())) throw new IllegalArgumentException("The return type of method '" + methodName + "' in class " + targetClass.getName() + " is not " + targetClass.getName() + ", it is " + method.getReturnType().getName());
        this.method = method;
    }

    @SuppressWarnings("unchecked")
    public T getInstance() throws CannotInstantiateException {
        try {
            return (T) method.invoke(null);
        } catch (Exception e) {
            throw new CannotInstantiateException(method.getDeclaringClass(), e);
        }
    }
}

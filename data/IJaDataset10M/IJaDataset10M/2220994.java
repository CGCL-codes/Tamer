package net.sourceforge.javabits.beans.accessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.sourceforge.javabits.beans.PropertyGetter;
import net.sourceforge.javabits.lang.IllegalParameterValueException;

/**
 * @author Jochen Kuhnle
 */
public class MethodPropertyGetter<T, P> extends MemberPropertyAccessor<T, P> implements PropertyGetter<T, P> {

    public MethodPropertyGetter(Method method) {
        super(method);
        if (method.getParameterTypes().length != 0) {
            throw new IllegalParameterValueException("method.parameterTypes.length", method.getParameterTypes().length);
        }
        if (method.getReturnType() == void.class) {
            throw new IllegalParameterValueException("method.returnType", method.getReturnType());
        }
        method.setAccessible(true);
    }

    public Method getMethod() {
        return (Method) getMember();
    }

    @SuppressWarnings("unchecked")
    public P get(T bean, Object... qualifiers) {
        try {
            return (P) getMethod().invoke(bean);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public Class<? extends P> getPropertyType() {
        return (Class<? extends P>) getMethod().getReturnType();
    }
}

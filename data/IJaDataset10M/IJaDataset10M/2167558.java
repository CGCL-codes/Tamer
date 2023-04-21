package nu.esox.gui.aspect;

import java.lang.reflect.*;

public class Aspect implements AspectIF {

    private final Method m_getAspectMethod;

    private final Method m_setAspectMethod;

    public Aspect(Class modelClass, String getAspectMethodName, String setAspectMethodName, Class aspectClass) {
        this(resolveGetAspectMethod(modelClass, getAspectMethodName), resolveSetAspectMethod(modelClass, setAspectMethodName, aspectClass));
    }

    public Aspect(Method getAspectMethod, Method setAspectMethod) {
        m_getAspectMethod = getAspectMethod;
        m_setAspectMethod = setAspectMethod;
    }

    public Object getAspectValue(Object model) {
        if (m_getAspectMethod == null) return model;
        boolean tmp = m_getAspectMethod.isAccessible();
        try {
            m_getAspectMethod.setAccessible(true);
            return invokeGetAspectMethod(m_getAspectMethod, model);
        } catch (IllegalAccessException ex) {
            throw new Error("Method not accessible: " + m_getAspectMethod);
        } catch (InvocationTargetException ex) {
            throw new Error("Invovation failure: " + m_getAspectMethod, ex);
        } finally {
            m_getAspectMethod.setAccessible(tmp);
        }
    }

    public void setAspectValue(Object model, Object aspectValue) {
        if (m_setAspectMethod == null) return;
        boolean tmp = m_setAspectMethod.isAccessible();
        try {
            m_setAspectMethod.setAccessible(true);
            invokeSetAspectMethod(m_setAspectMethod, model, aspectValue);
        } catch (IllegalAccessException ex) {
            throw new Error("Method not accessible: " + m_setAspectMethod);
        } catch (InvocationTargetException ex) {
            throw new Error("Invocation failure: " + m_setAspectMethod + ", " + aspectValue, ex);
        } catch (Throwable ex) {
            throw new Error("Failure: " + m_setAspectMethod + ", " + aspectValue, ex);
        } finally {
            m_setAspectMethod.setAccessible(tmp);
        }
    }

    protected Object invokeGetAspectMethod(Method getAspectMethod, Object model) throws IllegalAccessException, InvocationTargetException {
        return getAspectMethod.invoke(model);
    }

    protected void invokeSetAspectMethod(Method setAspectMethod, Object model, Object aspectValue) throws IllegalAccessException, InvocationTargetException {
        setAspectMethod.invoke(model, aspectValue);
    }

    protected static Method resolveGetAspectMethod(Class<?> modelClass, String getAspectMethodName) {
        if (getAspectMethodName == null) return null;
        try {
            return modelClass.getMethod(getAspectMethodName);
        } catch (NoSuchMethodException ex) {
            throw new Error("No such method: " + modelClass + "." + getAspectMethodName + "()");
        }
    }

    protected static Method resolveSetAspectMethod(Class<?> modelClass, String setAspectMethodName, Class aspectClass) {
        if (setAspectMethodName == null) return null;
        try {
            return modelClass.getMethod(setAspectMethodName, aspectClass);
        } catch (NoSuchMethodException ex) {
            throw new Error("No such method: " + modelClass + "." + setAspectMethodName + "( " + aspectClass + " )");
        }
    }
}

package org.nexopenframework.faces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.PropertyResolver;
import org.nexopenframework.context.framework.Contexts;
import org.springframework.util.ReflectionUtils;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class ContextsPropertyResolver extends PropertyResolver {

    private final PropertyResolver delegate;

    public ContextsPropertyResolver(final PropertyResolver delegate) {
        super();
        this.delegate = delegate;
    }

    public Class getType(Object base, int index) throws EvaluationException, PropertyNotFoundException {
        return delegate.getType(base, index);
    }

    public Class getType(Object base, Object property) throws EvaluationException, PropertyNotFoundException {
        return null;
    }

    public Object getValue(Object base, int index) throws EvaluationException, PropertyNotFoundException {
        return delegate.getValue(base, index);
    }

    public Object getValue(Object base, final Object property) throws EvaluationException, PropertyNotFoundException {
        try {
            return delegate.getValue(base, property);
        } catch (PropertyNotFoundException e) {
            return getInternalValue(base, property);
        }
    }

    public boolean isReadOnly(Object base, int index) throws EvaluationException, PropertyNotFoundException {
        return delegate.isReadOnly(base, index);
    }

    /**
	 * 
	 * @see javax.faces.el.PropertyResolver#isReadOnly(java.lang.Object, java.lang.Object)
	 */
    public boolean isReadOnly(Object base, Object property) throws EvaluationException, PropertyNotFoundException {
        return delegate.isReadOnly(base, property);
    }

    public void setValue(Object base, int index, Object value) throws EvaluationException, PropertyNotFoundException {
        delegate.setValue(base, index, value);
    }

    public void setValue(Object base, Object property, Object value) throws EvaluationException, PropertyNotFoundException {
    }

    /**
	 * <p></p>
	 * 
	 * @param base
	 * @param property
	 * @return
	 */
    protected Object getInternalValue(final Object base, final Object property) throws EvaluationException {
        try {
            MatchedMethodCallback callback = new MatchedMethodCallback(property);
            ReflectionUtils.doWithMethods(base.getClass(), callback);
            final Method matchedMethod = callback.getMatchedMethod();
            if (matchedMethod == null) {
                throw new EvaluationException("Method " + property + " not found related to object " + base);
            }
            AttributeResolverManager arm = AttributeResolverManager.getInstance();
            String[] names = arm.resolveAttributes(matchedMethod);
            Object[] args = new Object[names.length];
            for (int k = 0; k < names.length; k++) {
                String name = names[k];
                Object value = Contexts.getAttribute(name);
                args[k] = value;
            }
            Object value = matchedMethod.invoke(base, args);
            return value;
        } catch (IllegalAccessException ex) {
            throw new EvaluationException(ex);
        } catch (InvocationTargetException ex) {
            Throwable target = ex.getTargetException();
            throw new EvaluationException(target);
        }
    }

    /**
	 * <p>Method callback for matching the invoked method</p>
	 */
    private static class MatchedMethodCallback implements ReflectionUtils.MethodCallback {

        Method matchedMethod = null;

        Object property;

        public MatchedMethodCallback(Object property) {
            super();
            this.property = property;
        }

        public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
            if (method.getName().equals(property)) {
                matchedMethod = method;
            }
        }

        Method getMatchedMethod() {
            return matchedMethod;
        }
    }
}

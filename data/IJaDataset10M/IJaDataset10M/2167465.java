package net.sourceforge.javautil.interceptor.impl.standard;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.javautil.interceptor.IInterceptedContext;
import net.sourceforge.javautil.interceptor.IInterceptor;
import net.sourceforge.javautil.interceptor.IInterceptorClass;

/**
 * The standard implementation for class based interceptors.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class InterceptedContext implements IInterceptedContext {

    protected final Object target;

    protected final Method method;

    protected final Method targetMethod;

    protected final IInterceptorClass<?>[] interceptors;

    protected final int interceptorIndex;

    protected final Map<String, Object> contextData;

    protected Object[] parameters;

    public InterceptedContext(IInterceptorClass<?>[] interceptors, Object target, Method targetMethod, Method method, Object[] parameters) {
        this(interceptors, 0, new HashMap<String, Object>(), target, targetMethod, method, parameters);
    }

    private InterceptedContext(IInterceptorClass<?>[] interceptors, int index, Map<String, Object> contextData, Object target, Method targetMethod, Method method, Object[] parameters) {
        this.method = method;
        this.targetMethod = targetMethod;
        this.parameters = parameters;
        this.interceptors = interceptors;
        this.target = target;
        this.contextData = contextData;
        this.interceptorIndex = index;
    }

    public Map<String, Object> getContextData() {
        return this.contextData;
    }

    public Object[] getParameters() {
        return this.parameters;
    }

    public Object getTimer() {
        return null;
    }

    public void setParameters(Object[] params) {
        this.parameters = params;
    }

    /**
	 * @return The value returned by the {@link IInterceptorLink} for this invocation context
	 */
    public Object proceed() throws Exception {
        return this.proceedInternal(method, parameters);
    }

    /**
	 * NOTE: The arguments passed MUST be of the current count and types for this to work.
	 * 
	 * @param replacementParameters The replacement parameters to use
	 * @return The value returned from this modified invocation
	 */
    public Object proceed(Object... replacementParameters) throws Exception {
        return this.proceedInternal(method, replacementParameters);
    }

    public Object proceed(Method method, Object... replacementParameters) throws Exception {
        return this.proceedInternal(method, replacementParameters);
    }

    /**
	 * @return The target instance that this invocation may finally be invoked on
	 */
    public Object getTarget() {
        return target;
    }

    /**
	 * @return The target method that will possibly be the last invocation in the link
	 */
    public Method getMethod() {
        return method;
    }

    @Override
    public Method getRealTargetMethod() {
        return this.targetMethod;
    }

    protected Object proceedInternal(Method method, Object... parameters) throws Exception {
        return interceptors[interceptorIndex].invoke(new InterceptedContext(interceptors, interceptorIndex + 1, contextData, target, method == this.method ? targetMethod : method, method, parameters));
    }
}

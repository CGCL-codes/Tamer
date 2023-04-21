package nuts.ext.xwork2.interceptor;

import java.lang.reflect.Method;
import nuts.core.lang.StringUtils;
import nuts.ext.xwork2.exception.UnknownActionMethodException;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.UnknownHandlerManager;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * MethodExistsInterceptor
 */
public class UnknownMethodInterceptor extends AbstractInterceptor {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 8103709824799309483L;

    protected Container container;

    protected UnknownHandlerManager unknownHandlerManager;

    /**
	 * @param container the container to set
	 */
    @Inject
    public void setContainer(Container container) {
        this.container = container;
    }

    /**
	 * @param unknownHandlerManager the unknownHandlerManager to set
	 */
    @Inject
    public void setUnknownHandlerManager(UnknownHandlerManager unknownHandlerManager) {
        this.unknownHandlerManager = unknownHandlerManager;
    }

    /**
	 * Allows the Interceptor to do some processing on the request before and/or after the rest of the processing of the
	 * request by the {@link ActionInvocation} or to short-circuit the processing and just return a String return code.
	 *
	 * @param actionInvocation  the action invocation
	 * @return the return code, either returned from {@link ActionInvocation#invoke()}, or from the interceptor itself.
	 * @throws Exception any system-level error, as defined in {@link com.opensymphony.xwork2.Action#execute()}.
	 */
    @SuppressWarnings("unchecked")
    public String intercept(ActionInvocation actionInvocation) throws Exception {
        Class actionClass = actionInvocation.getAction().getClass();
        String methodName = actionInvocation.getProxy().getMethod();
        @SuppressWarnings("unused") Method method = null;
        try {
            method = actionClass.getMethod(methodName, new Class[0]);
        } catch (NoSuchMethodException e) {
            try {
                String altMethodName = "do" + StringUtils.capitalize(methodName);
                method = actionClass.getMethod(altMethodName, new Class[0]);
            } catch (NoSuchMethodException e1) {
                if (unknownHandlerManager.hasUnknownHandlers()) {
                    try {
                        Object result = unknownHandlerManager.handleUnknownMethod(actionInvocation.getAction(), methodName);
                        if (result instanceof Result) {
                            container.inject(result);
                            ((Result) result).execute(actionInvocation);
                            return null;
                        } else if (result instanceof String) {
                            return (String) result;
                        }
                    } catch (NoSuchMethodException e2) {
                    }
                }
                throw new UnknownActionMethodException(e);
            }
        }
        return actionInvocation.invoke();
    }
}

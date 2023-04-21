package com.aurorasoftworks.signal.runtime.ui.mvc;

import java.util.Hashtable;
import com.aurorasoftworks.signal.runtime.core.context.IBeanProcessor;
import com.aurorasoftworks.signal.runtime.core.context.IBeanReference;
import com.aurorasoftworks.signal.runtime.core.context.IContext;
import com.aurorasoftworks.signal.runtime.core.context.IContextAware;
import com.aurorasoftworks.signal.runtime.core.context.proxy.AbstractMethodInterceptor;
import com.aurorasoftworks.signal.runtime.core.context.proxy.IMethodHandler;
import com.aurorasoftworks.signal.runtime.core.context.proxy.IMethodInterceptor;
import com.aurorasoftworks.signal.runtime.core.context.proxy.IProxy;
import com.aurorasoftworks.signal.runtime.core.context.proxy.IProxyFactory;
import com.aurorasoftworks.signal.runtime.core.context.proxy.IProxyTarget;

public abstract class AbstractControllerMethodInterceptor implements IBeanProcessor, IContextAware {

    protected class ControllerMethodInterceptor extends AbstractMethodInterceptor {

        public Object invoke(IProxyTarget target, IMethodHandler handler, Object[] args) throws Exception {
            return onControllerMethodInvoked((IController) target, handler, args);
        }
    }

    protected class FlowCallerMethodInterceptor extends AbstractMethodInterceptor {

        public Object invoke(IProxyTarget target, IMethodHandler handler, Object[] args) throws Exception {
            return onFlowCallerMethodInvoked((IController) target, handler, args);
        }
    }

    private IMethodInterceptor[] handlers = new IMethodInterceptor[] { new ControllerMethodInterceptor() };

    private IMethodInterceptor[] flowCallerHandlers = new IMethodInterceptor[] { new FlowCallerMethodInterceptor() };

    private Hashtable controllerProxies = new Hashtable();

    private IContext context;

    protected IProxyFactory getProxyFactory() {
        return context.getProxyFactory();
    }

    protected abstract Object onControllerMethodInvoked(IController controller, IMethodHandler handler, Object[] args) throws Exception;

    protected abstract Object onFlowCallerMethodInvoked(IController controller, IMethodHandler handler, Object[] args) throws Exception;

    /**
	 * No-op. Actual processing is implemented in
	 * {@link #postProcessBean(String, Object)}
	 */
    public Object preProcessBean(IBeanReference ref) throws Exception {
        return ref;
    }

    public Object postProcessBean(IBeanReference ref) throws Exception {
        Object result;
        if (isController(ref)) {
            result = getProxyFactory().createProxy((IProxyTarget) ref.getBean(), handlers);
            controllerProxies.put(ref.getBean(), result);
        } else {
            result = ref;
        }
        return result;
    }

    protected boolean isController(IBeanReference ref) {
        return IController.class.isAssignableFrom(ref.getBeanClass());
    }

    protected IProxyTarget getProxyOrController(IController ctl) {
        IProxyTarget result = (IProxy) controllerProxies.get(ctl);
        if (result == null) {
            result = ctl;
        }
        return result;
    }

    protected IProxy getControllerProxy(IController ctl) {
        IProxy result = (IProxy) controllerProxies.get(ctl);
        if (result == null) {
            throw new RuntimeException("Proxy not found for cotroller: " + ctl);
        }
        return result;
    }

    protected IMethodInterceptor[] getFlowCallerHandlers() {
        return flowCallerHandlers;
    }

    public IContext getContext() {
        return context;
    }

    public void setContext(IContext context) {
        this.context = context;
    }
}

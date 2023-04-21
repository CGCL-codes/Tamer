package net.paoding.rose.web;

import java.lang.reflect.Method;
import net.paoding.rose.web.advancedinterceptor.ActionSelector;
import net.paoding.rose.web.advancedinterceptor.DispatcherSelector;
import net.paoding.rose.web.advancedinterceptor.Named;
import net.paoding.rose.web.advancedinterceptor.Ordered;
import net.paoding.rose.web.impl.thread.AfterCompletion;
import org.springframework.util.Assert;

/**
 * 
 * @author 王志亮 [qieqie.wang@gmail.com]
 * 
 */
public class InterceptorDelegate implements Ordered, Named, ControllerInterceptor, AfterCompletion, ActionSelector, DispatcherSelector, Comparable<InterceptorDelegate> {

    protected final ControllerInterceptor interceptor;

    private String name;

    private boolean isAfterCompletion;

    private boolean isDispatcherSelector;

    public static ControllerInterceptor getMostInnerInterceptor(ControllerInterceptor interceptor) {
        ControllerInterceptor temp = interceptor;
        while (temp instanceof InterceptorDelegate) {
            temp = ((InterceptorDelegate) temp).getInterceptor();
        }
        return temp;
    }

    public InterceptorDelegate(ControllerInterceptor interceptor) {
        Assert.notNull(interceptor);
        this.interceptor = interceptor;
        this.isAfterCompletion = interceptor instanceof AfterCompletion;
        this.isDispatcherSelector = interceptor instanceof DispatcherSelector;
    }

    public ControllerInterceptor getInterceptor() {
        return interceptor;
    }

    @Override
    public String getName() {
        if (interceptor instanceof Named) {
            name = ((Named) interceptor).getName();
        }
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        if (interceptor instanceof Named) {
            ((Named) interceptor).setName(name);
        }
    }

    @Override
    public int getPriority() {
        if (interceptor instanceof Ordered) {
            return ((Ordered) interceptor).getPriority();
        }
        return 0;
    }

    @Override
    public boolean isForAction(Class<?> controllerClazz, Method actionMethod) {
        if (interceptor instanceof Ordered) {
            return ((ActionSelector) interceptor).isForAction(controllerClazz, actionMethod);
        }
        return true;
    }

    @Override
    public boolean isForDispatcher(Dispatcher dispatcher) {
        if (isDispatcherSelector) {
            return ((DispatcherSelector) interceptor).isForDispatcher(dispatcher);
        }
        return true;
    }

    @Override
    public Object roundInvocation(Invocation inv, InvocationChain chain) throws Exception {
        return interceptor.roundInvocation(inv, chain);
    }

    @Override
    public void afterCompletion(Invocation inv, Throwable ex) throws Exception {
        if (isAfterCompletion) {
            ((AfterCompletion) interceptor).afterCompletion(inv, ex);
        }
    }

    @Override
    public int compareTo(InterceptorDelegate o) {
        if (o == this) {
            return 0;
        }
        if (this.getPriority() > o.getPriority()) {
            return -1;
        }
        if (this.getPriority() < o.getPriority()) {
            return 1;
        }
        return getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return getMostInnerInterceptor(this).getClass().getName();
    }
}

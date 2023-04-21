package com.objecteffects.clublist.web.stripes;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;

/**
 * @author Rusty Wright
 */
public abstract class AbstractActionBeanResolutionInterceptor implements Interceptor {

    private final Injector injector = Guice.createInjector(this.createModules());

    /** */
    public AbstractActionBeanResolutionInterceptor() {
        this.injector.injectMembers(this);
    }

    protected abstract Module[] createModules();

    @Override
    public Resolution intercept(final ExecutionContext context) throws Exception {
        final Resolution resolution = context.proceed();
        final ActionBean actionBean = context.getActionBean();
        this.injectMembers(actionBean);
        return (resolution);
    }

    protected void injectMembers(final Object obj) {
        this.injector.injectMembers(obj);
    }
}

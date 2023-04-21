package org.jrest4guice.plugin.struts2;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.jrest4guice.guice.GuiceContext;
import com.google.inject.Module;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.entities.InterceptorConfig;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
public class JRest4GuiceStruts2ObjectFactory extends ObjectFactory {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1045130626636512882L;

    static final Logger logger = Logger.getLogger(JRest4GuiceStruts2ObjectFactory.class.getName());

    Module module;

    boolean developmentMode = false;

    @Override
    public boolean isNoArgConstructorRequired() {
        return false;
    }

    @Inject(value = "guice.module", required = false)
    void setModule(String moduleClassName) {
        try {
            @SuppressWarnings({ "unchecked" }) Class<? extends Module> moduleClass = (Class<? extends Module>) Class.forName(moduleClassName);
            this.module = moduleClass.newInstance();
            GuiceContext.getInstance().addUserModule(this.module);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Inject(value = "struts.devMode", required = false)
    void setDevelopmentMode(String developmentMode) {
        this.developmentMode = developmentMode.trim().equals("true");
    }

    Set<Class<?>> boundClasses = new HashSet<Class<?>>();

    public Class getClassInstance(String name) throws ClassNotFoundException {
        Class<?> clazz = super.getClassInstance(name);
        return clazz;
    }

    public Object buildBean(Class clazz, Map extraContext) {
        if (!GuiceContext.getInstance().isInitialized()) GuiceContext.getInstance().init();
        return GuiceContext.getInstance().getBean(clazz);
    }

    public Interceptor buildInterceptor(InterceptorConfig interceptorConfig, Map interceptorRefParams) throws ConfigurationException {
        try {
            getClassInstance(interceptorConfig.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new LazyLoadedInterceptor(interceptorConfig, interceptorRefParams);
    }

    Interceptor superBuildInterceptor(InterceptorConfig interceptorConfig, Map interceptorRefParams) throws ConfigurationException {
        return super.buildInterceptor(interceptorConfig, interceptorRefParams);
    }

    class LazyLoadedInterceptor implements Interceptor {

        final InterceptorConfig config;

        final Map params;

        LazyLoadedInterceptor(InterceptorConfig config, Map params) {
            this.config = config;
            this.params = params;
        }

        Interceptor delegate = null;

        synchronized Interceptor getDelegate() {
            if (delegate == null) {
                delegate = superBuildInterceptor(config, params);
                delegate.init();
            }
            return delegate;
        }

        public void destroy() {
            getDelegate().destroy();
        }

        public void init() {
            throw new AssertionError();
        }

        public String intercept(ActionInvocation invocation) throws Exception {
            return getDelegate().intercept(invocation);
        }
    }
}

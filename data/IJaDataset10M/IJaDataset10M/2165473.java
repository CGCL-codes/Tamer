package org.impalaframework.web.spring.module;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.ServletContext;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.spring.resource.ResourceLoader;
import org.impalaframework.web.resource.ServletContextResourceLoader;

/**
 * Extension of {@link WebModuleLoader} which loads Spring context resources as {@link ServletContext}
 * resources rather than on the classpath. This is probably more standard use in typical Spring applications 
 * although in Impala the standard usage is to place Spring context resources on the classpath.
 * 
 * @author Phil Zoio
 */
public class WebRootModuleLoader extends WebModuleLoader {

    public WebRootModuleLoader() {
        super();
    }

    public WebRootModuleLoader(ModuleLocationResolver moduleLocationResolver, ServletContext servletContext) {
        super(servletContext);
    }

    @Override
    protected Collection<ResourceLoader> getSpringLocationResourceLoaders() {
        Collection<ResourceLoader> injectedLocationResourceLoaders = getInjectedSpringLocationResourceLoaders();
        if (injectedLocationResourceLoaders != null) {
            return injectedLocationResourceLoaders;
        }
        Collection<ResourceLoader> resourceLoaders = new ArrayList<ResourceLoader>();
        ServletContextResourceLoader servletContextResourceLoader = new ServletContextResourceLoader();
        servletContextResourceLoader.setServletContext(getServletContext());
        resourceLoaders.add(servletContextResourceLoader);
        return resourceLoaders;
    }
}

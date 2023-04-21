package org.impalaframework.module.holder.graph;

import org.impalaframework.bootstrap.CoreBootstrapProperties;
import org.impalaframework.classloader.graph.ClassLoaderOptions;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * {@link FactoryBean} implementation returns instanceof {@link WeavingGraphClassLoaderFactory} or {@link GraphClassLoaderFactory},
 * depending on whether {@link #aspectjAwareClassLoader} is set to true or not
 */
public class GraphClassLoaderFactoryBean implements FactoryBean, InitializingBean {

    private ModuleLocationResolver moduleLocationResolver;

    private boolean aspectjAwareClassLoader;

    /**
     * @see CoreBootstrapProperties#PARENT_CLASS_LOADER_FIRST
     */
    private boolean parentClassLoaderFirst;

    /**
     * @see CoreBootstrapProperties#SUPPORTS_MODULE_LIBRARIES
     */
    private boolean supportsModuleLibraries;

    /**
     * @see CoreBootstrapProperties#EXPORTS_MODULE_LIBRARIES
     */
    private boolean exportsModuleLibraries;

    /**
     * @see CoreBootstrapProperties#LOADS_MODULE_LIBRARY_RESOURCES
     */
    private boolean loadsModuleLibraryResources;

    private GraphClassLoaderFactory classLoaderFactory;

    public void afterPropertiesSet() throws Exception {
        if (aspectjAwareClassLoader) {
            this.classLoaderFactory = new WeavingGraphClassLoaderFactory();
        } else {
            this.classLoaderFactory = new GraphClassLoaderFactory();
        }
        this.classLoaderFactory.setModuleLocationResolver(moduleLocationResolver);
        this.classLoaderFactory.setOptions(new ClassLoaderOptions(parentClassLoaderFirst, supportsModuleLibraries, exportsModuleLibraries, loadsModuleLibraryResources));
        this.classLoaderFactory.init();
    }

    public Object getObject() throws Exception {
        return classLoaderFactory;
    }

    public Class<?> getObjectType() {
        return GraphClassLoaderFactory.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setAspectjAwareClassLoader(boolean aspectjAwareClassLoader) {
        this.aspectjAwareClassLoader = aspectjAwareClassLoader;
    }

    public void setModuleLocationResolver(ModuleLocationResolver moduleLocationResolver) {
        this.moduleLocationResolver = moduleLocationResolver;
    }

    public void setParentClassLoaderFirst(boolean parentClassLoaderFirst) {
        this.parentClassLoaderFirst = parentClassLoaderFirst;
    }

    public void setSupportsModuleLibraries(boolean supportsModuleLibraries) {
        this.supportsModuleLibraries = supportsModuleLibraries;
    }

    public void setExportsModuleLibraries(boolean exportsModuleLibraries) {
        this.exportsModuleLibraries = exportsModuleLibraries;
    }

    public void setLoadsModuleLibraryResources(boolean loadsModuleLibraryResources) {
        this.loadsModuleLibraryResources = loadsModuleLibraryResources;
    }
}

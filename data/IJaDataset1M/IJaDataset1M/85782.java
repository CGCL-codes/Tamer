package org.impalaframework.spring.module.loader;

import static org.easymock.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.spi.ModuleLoader;
import org.impalaframework.spring.module.SpringModuleLoader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import junit.framework.TestCase;

public class BaseApplicationContextLoaderTest extends TestCase {

    private BaseApplicationContextLoader loader;

    private ModuleLoaderRegistry moduleLoaderRegistry;

    private SimpleModuleDefinition definition;

    private ConfigurableApplicationContext applicationContext;

    private ModuleLoader moduleLoader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        loader = new BaseApplicationContextLoader();
        moduleLoaderRegistry = new ModuleLoaderRegistry();
        loader.setModuleLoaderRegistry(moduleLoaderRegistry);
        definition = new SimpleModuleDefinition("mymod");
        moduleLoader = createMock(SpringModuleLoader.class);
        applicationContext = createMock(ConfigurableApplicationContext.class);
    }

    public void testCloseContextNoModuleLoader() {
        replay(applicationContext, moduleLoader);
        loader.closeContext("id", definition, new GenericApplicationContext());
        verify(applicationContext, moduleLoader);
    }

    public void testCloseContextWithModuleLoader() throws Exception {
        moduleLoader = createMock(ModuleLoader.class);
        moduleLoaderRegistry.addItem("spring-APPLICATION", moduleLoader);
        applicationContext.close();
        replay(applicationContext, moduleLoader);
        loader.closeContext("id", definition, applicationContext);
        verify(applicationContext, moduleLoader);
    }

    public void testCloseContextWithSpringModuleLoader() throws Exception {
        SpringModuleLoader sm = (SpringModuleLoader) moduleLoader;
        moduleLoaderRegistry.addItem("spring-APPLICATION", moduleLoader);
        sm.beforeClose("id", applicationContext, definition);
        applicationContext.close();
        replay(applicationContext, moduleLoader);
        loader.closeContext("id", definition, applicationContext);
        verify(applicationContext, moduleLoader);
    }
}

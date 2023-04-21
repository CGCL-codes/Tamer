package com.googlecode.beauti4j.core.service;

import java.util.Map;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import com.googlecode.beauti4j.core.common.dao.AbstractDaoTestCase;
import com.googlecode.beauti4j.core.common.model.ModuleRegistration;
import com.googlecode.beauti4j.core.common.model.Unified;
import com.googlecode.beauti4j.core.service.impl.ModuleManagerImpl;

@RunWith(JUnit4ClassRunner.class)
public class TestModuleManager extends AbstractDaoTestCase {

    private ModuleManagerImpl moduleManager;

    public void setModuleManager(ModuleManagerImpl moduleManager) {
        this.moduleManager = moduleManager;
    }

    public void setModuleManager(ModuleManager moduleManager) {
        this.moduleManager = (ModuleManagerImpl) moduleManager;
    }

    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] { "/applicationContext-resources.xml", "/META-INF/spring/applicationContext-dao.xml" };
    }

    @Test
    public void testOnApplicationEvent() throws Exception {
        Map<ModuleRegistration, Module> moduleMap = moduleManager.getAvailableModules();
        assertNotNull(moduleMap);
        assertEquals(1, moduleMap.size());
        Module test = new ExampleModule();
        assertTrue(moduleMap.containsKey(test.getModuleRegistration()));
    }

    @Test
    public void testRegisterModule() throws Exception {
        Module test = new ExampleModule();
        ModuleRegistration moduleRegistration = test.getModuleRegistration();
        moduleManager.registerModule(moduleRegistration);
        assertNotNull(moduleRegistration.getId());
        assertNotNull(moduleRegistration.getStatus());
        assertEquals(Unified.SC_NORMAL, moduleRegistration.getStatus());
        Map<ModuleRegistration, Module> moduleMap = moduleManager.getAvailableModules();
        ExampleModule exampleModule = (ExampleModule) moduleMap.get(moduleRegistration);
        assertTrue(exampleModule.onRegisteredCalled);
        assertFalse(exampleModule.onUnregisteredCalled);
        moduleManager.unregisterModule(moduleRegistration);
        assertTrue(exampleModule.onUnregisteredCalled);
    }
}

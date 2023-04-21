package org.impalaframework.module.runtime;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.springframework.util.Assert;

public class SimpleRuntimeModule implements RuntimeModule {

    private final ClassLoader classLoader;

    private final ModuleDefinition moduleDefinition;

    public SimpleRuntimeModule(ClassLoader classLoader, ModuleDefinition moduleDefinition) {
        super();
        Assert.notNull(classLoader);
        Assert.notNull(moduleDefinition);
        this.classLoader = classLoader;
        this.moduleDefinition = moduleDefinition;
    }

    public Object getBean(String beanName) {
        return null;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public ModuleDefinition getModuleDefinition() {
        return moduleDefinition;
    }
}

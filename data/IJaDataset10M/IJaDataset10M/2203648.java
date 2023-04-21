package org.impalaframework.spring.module.impl;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.beans.factory.FactoryBean;

public class ModuleDefinitionFactoryBean implements FactoryBean {

    public Object getObject() throws Exception {
        return new SimpleModuleDefinition("mybean");
    }

    public Class<?> getObjectType() {
        return ModuleDefinition.class;
    }

    public boolean isSingleton() {
        return true;
    }
}

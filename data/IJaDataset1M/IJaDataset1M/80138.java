package org.streets.context.definition;

import java.util.Collection;
import java.util.List;

/**
 * 
 * @author dzb
 */
public interface ContextDefinition {

    public List<ModuleDefinition> getModules();

    public ModuleDefinition getModule(String moduleId);

    @SuppressWarnings("unchecked")
    public void _init_(Collection defs);
}

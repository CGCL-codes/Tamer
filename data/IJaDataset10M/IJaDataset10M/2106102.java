package org.impalaframework.module.operation;

import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.spi.Application;

/**
 * Implementation of {@link ModuleOperation} which encapsulates mechanism for updating the root module,
 * and potentially all modules in the graph/hierarchy. Unlike {@link ReloadModuleNamedLikeOperation} and 
 * {@link ReloadNamedModuleOperation}, this operation will only reflect changes to the module graph/
 * hierarchy if the module definitions themselves have changed. It won't for example, pick up changes to
 * module classes in the absence of module definition changes.
 * 
 * @see ReloadModuleNamedLikeOperation
 * @see ReloadNamedModuleOperation
 * @author Phil Zoio
 */
public class ReloadRootModuleOperation extends UpdateRootModuleOperation {

    protected ReloadRootModuleOperation() {
        super();
    }

    @Override
    protected RootModuleDefinition getExistingModuleDefinitionSource(Application application) {
        return application.getModuleStateHolder().cloneRootModuleDefinition();
    }
}

package org.impalaframework.module.operation;

import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.spi.TransitionManager;

/**
 * Shared base class of {@link ModuleOperation} implementation. Simply exposes getter and setter
 * for {@link ModificationExtractorRegistry}.
 * 
 * @author Phil Zoio
 */
public abstract class BaseModuleOperation extends LockingModuleOperation {

    private ModificationExtractorRegistry modificationExtractorRegistry;

    private TransitionManager transitionManager;

    protected BaseModuleOperation() {
        super();
    }

    protected ModificationExtractorRegistry getModificationExtractorRegistry() {
        return modificationExtractorRegistry;
    }

    protected TransitionManager getTransitionManager() {
        return transitionManager;
    }

    public void setModificationExtractorRegistry(ModificationExtractorRegistry modificationExtractorRegistry) {
        this.modificationExtractorRegistry = modificationExtractorRegistry;
    }

    public void setTransitionManager(TransitionManager transitionManager) {
        this.transitionManager = transitionManager;
    }
}

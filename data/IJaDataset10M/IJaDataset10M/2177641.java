package net.sf.csutils.core.registry;

import net.sf.csutils.core.model.ROMetaModel;

/**
 * Extension of {@link RegistryFacade} with support for model
 * driven operations.
 */
public interface ModelDrivenRegistryFacade extends RegistryFacade {

    /**
     * Returns the facades meta model.
     * @return The registry meta model, which this facade uses.
     */
    ROMetaModel getMetaModel();

    /**
     * Returns the facades model accessor.
     * @return The model accessor used by this facade.
     */
    ROModelAccessor getModelAccessor();

    /**
     * Returns the facades meta model accessor.
     * @return The meta model accessor used by this facade.
     */
    ROMetaModelAccessor getMetaModelAccessor();
}

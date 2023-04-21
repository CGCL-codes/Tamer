package org.npsnet.v.kernel;

/**
 * An event concerning a module container.
 *
 * @author Andrzej Kapolka
 */
public abstract class ModuleContainerEvent extends ModuleEvent {

    /**
     * The container in which the event took place.
     */
    protected ModuleContainer sourceContainer;

    /**
     * Constructor.
     *
     * @param pSourceContainer the container that generated the event
     */
    public ModuleContainerEvent(ModuleContainer pSourceContainer) {
        super(pSourceContainer);
        sourceContainer = pSourceContainer;
    }

    /**
     * Returns the container in which the event took place.
     *
     * @return the container in which the event took place
     */
    public ModuleContainer getSourceContainer() {
        return sourceContainer;
    }
}

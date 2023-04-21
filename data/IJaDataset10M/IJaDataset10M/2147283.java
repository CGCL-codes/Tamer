package es.eucm.eadventure.engine.core.control.functionaldata;

import es.eucm.eadventure.common.data.chapter.resources.Resources;

/**
 * Any element that can be drawed and updated
 */
public interface Renderable {

    /**
     * Draws the element.
     */
    public void draw();

    /**
     * Updates the element
     * 
     * @param elapsedTime
     *            The elapsed time from the last update
     */
    public void update(long elapsedTime);

    /**
     * Create a resources block for this renderable element
     */
    Resources createResourcesBlock();
}

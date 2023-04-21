package org.alcibiade.sculpt.render;

/**
 * This interface is implemented by classes that should get setting update
 * informations from the renderer.
 * 
 * @author Yannick Kirschhoffer
 * 
 */
public interface RendererSettingsObserver {

    /**
	 * Called when renderer is updated.
	 * 
	 * @param source
	 *            The source renderer.
	 */
    public void renderSettingsUpdated(Object source);
}

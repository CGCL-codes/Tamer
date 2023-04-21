package com.heavylead.wrappers.interfaces;

import com.jme.image.Image;
import com.jme.renderer.Renderer;

/**
 * The Interface IDisplaySystem.
 */
public interface IDisplaySystem {

    /**
     * Checks if is created.
     * 
     * @return true, if is created
     */
    boolean isCreated();

    /**
     * Checks if is closing.
     * 
     * @return true, if is closing
     */
    boolean isClosing();

    /**
     * Gets the renderer.
     * 
     * @return the renderer
     */
    Renderer getRenderer();

    /**
     * Reset.
     */
    void reset();

    /**
     * Close.
     */
    void close();

    /**
     * Creates the window.
     * 
     * @param width the width
     * @param height the height
     * @param depth the depth
     * @param frequency the frequency
     * @param fullscreen the fullscreen
     */
    void createWindow(int width, int height, int depth, int frequency, boolean fullscreen);

    /**
     * Gets the height.
     * 
     * @return the height
     */
    int getHeight();

    /**
     * Gets the width.
     * 
     * @return the width
     */
    int getWidth();

    /**
     * Recreate window.
     * 
     * @param width the width
     * @param height the height
     * @param depth the depth
     * @param frequency the frequency
     * @param fullscreen the fullscreen
     */
    void recreateWindow(int width, int height, int depth, int frequency, boolean fullscreen);

    /**
     * Sets the icon.
     * 
     * @param icons the new icon
     */
    void setIcon(Image[] icons);

    /**
     * Sets the min depth bits.
     * 
     * @param depthBits the new min depth bits
     */
    void setMinDepthBits(int depthBits);

    /**
     * Sets the min stencil bits.
     * 
     * @param stencilBits the new min stencil bits
     */
    void setMinStencilBits(int stencilBits);

    /**
     * Sets the min alpha bits.
     * 
     * @param alphaBits the new min alpha bits
     */
    void setMinAlphaBits(int alphaBits);

    /**
     * Sets the min samples.
     * 
     * @param samples the new min samples
     */
    void setMinSamples(int samples);

    /**
     * Sets the title.
     * 
     * @param title the new title
     */
    void setTitle(String title);

    /**
     * Sets the v sync enabled.
     * 
     * @param verticalSync the new v sync enabled
     */
    void setVSyncEnabled(boolean verticalSync);

    /**
     * Checks if is valid display mode.
     * 
     * @param width the width
     * @param height the height
     * @param depth the depth
     * @param frequency the frequency
     * 
     * @return true, if is valid display mode
     */
    boolean isValidDisplayMode(int width, int height, int depth, int frequency);
}

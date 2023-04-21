package org.xith3d.render.lwjgl;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.xith3d.render.config.DisplayMode;
import org.xith3d.render.config.DisplayModeSelector;
import org.xith3d.render.config.OpenGLLayer;

/**
 * DisplayModeSelector implementation for LWJGL.<br>
 * If you want to know, which DisplayModes are awailable on your System and
 * for a specific OpenGLLayer, make use of it.<br>
 * <br>
 * Instantiate it by invoking the static getImplementation() method.
 * 
 * @see DisplayModeSelector#getImplementation(org.xith3d.render.config.OpenGLLayer)
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class DisplayModeSelectorNativeImpl extends DisplayModeSelector {

    private static final OpenGLLayer OPENGL_LAYER = OpenGLLayer.LWJGL;

    private static DisplayMode[] cachedModes = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayMode[] getAvailableModes() {
        if (cachedModes != null) return (cachedModes);
        org.lwjgl.opengl.DisplayMode[] lwjglModes;
        try {
            lwjglModes = Display.getAvailableDisplayModes();
        } catch (LWJGLException e) {
            return (new DisplayMode[0]);
        }
        DisplayMode[] modes = new DisplayMode[lwjglModes.length];
        for (int i = 0; i < lwjglModes.length; i++) {
            modes[i] = new DisplayMode(OPENGL_LAYER, lwjglModes[i], lwjglModes[i].getWidth(), lwjglModes[i].getHeight(), lwjglModes[i].getBitsPerPixel(), lwjglModes[i].getFrequency());
        }
        sortModes(modes);
        cachedModes = modes;
        return (modes);
    }
}

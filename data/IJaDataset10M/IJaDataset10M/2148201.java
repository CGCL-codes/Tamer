package org.torbs.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import de.lessvoid.nifty.Nifty;

/**
 * Helper class shared by all the examples to initialize lwjgl and stuff.
 */
public class LWJGLHelper {

    /**
   * destroy all and quit.
   */
    public static void destroy() {
        Display.destroy();
    }

    public static DisplayMode[] getDisplayModes() {
        try {
            return (Display.getAvailableDisplayModes());
        } catch (LWJGLException e) {
            e.printStackTrace();
            return (null);
        }
    }

    /**
   * Init SubSystems.
   * @param pWindowTitle title pf window
   * @return true on success and false otherwise
   */
    public static boolean initSubSystems(final String pWindowTitle, int pScreenWidth, int pScreenHeight, int pNbBitsPerPixels, boolean pFullScreen) {
        if (!LWJGLHelper.initGraphics(pWindowTitle, pScreenWidth, pScreenHeight, pNbBitsPerPixels, pFullScreen)) {
            return false;
        }
        if (!LWJGLHelper.initInput()) {
            return false;
        }
        return true;
    }

    /**
   * @param nifty nifty instance
   * @param callback callback
   */
    public static void renderLoop(final Nifty nifty, final RenderLoopCallback callback) {
        boolean done = false;
        while (!Display.isCloseRequested() && !done) {
            if (callback != null) {
                callback.process();
            }
            Display.update();
            while (Keyboard.next()) {
                boolean eventKeyState = Keyboard.getEventKeyState();
                int eventKey = Keyboard.getEventKey();
                nifty.keyEvent(eventKey, Keyboard.getEventCharacter(), eventKeyState);
                if (eventKeyState && (eventKey == Keyboard.KEY_F2)) {
                    nifty.toggleDebugConsole(true);
                } else if (eventKeyState && (eventKey == Keyboard.KEY_F3)) {
                    nifty.toggleDebugConsole(false);
                }
            }
            int mouseX = Mouse.getX();
            int mouseY = Display.getDisplayMode().getHeight() - Mouse.getY();
            if (nifty.render(true, mouseX, mouseY, Mouse.isButtonDown(0))) {
                done = true;
            }
            int error = GL11.glGetError();
            if (error != GL11.GL_NO_ERROR) {
            }
        }
    }

    /**
   * Init lwjgl graphics.
   * @param title title of window
   * @return true on success and false otherwise
   */
    private static boolean initGraphics(final String title, int pScreenWidth, int pScreenHeight, int pNbBitsPerPixels, boolean pFullScreen) {
        try {
            DisplayMode currentMode = Display.getDisplayMode();
            DisplayMode[] modes = Display.getAvailableDisplayModes();
            List<DisplayMode> matching = new ArrayList<DisplayMode>();
            for (int i = 0; i < modes.length; i++) {
                DisplayMode mode = modes[i];
                if ((mode.getWidth() == pScreenWidth) && (mode.getHeight() == pScreenHeight) && (mode.getBitsPerPixel() == pNbBitsPerPixels)) {
                    matching.add(mode);
                }
            }
            DisplayMode[] matchingModes = matching.toArray(new DisplayMode[0]);
            boolean found = false;
            for (int i = 0; i < matchingModes.length; i++) {
                if (matchingModes[i].getFrequency() == currentMode.getFrequency()) {
                    Display.setDisplayMode(matchingModes[i]);
                    found = true;
                    break;
                }
            }
            if (!found) {
                Arrays.sort(matchingModes, new Comparator<DisplayMode>() {

                    public int compare(final DisplayMode o1, final DisplayMode o2) {
                        if (o1.getFrequency() > o2.getFrequency()) {
                            return 1;
                        } else if (o1.getFrequency() < o2.getFrequency()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                Display.setDisplayMode(matchingModes[0]);
            }
            int x = 0, y = 0;
            Display.setLocation(x, y);
            try {
                Display.setFullscreen(pFullScreen);
                Display.create();
                Display.setVSyncEnabled(true);
                Display.setTitle(title);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
            GL11.glViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), 0, -9999, 9999);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glAlphaFunc(GL11.GL_NOTEQUAL, 0);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            return true;
        } catch (LWJGLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
   * Init input system.
   * @return true on success and false otherwise
   */
    private static boolean initInput() {
        try {
            Keyboard.create();
            Keyboard.enableRepeatEvents(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
   * RenderLoopCallback.
   * @author void
   */
    public interface RenderLoopCallback {

        /**
     * process.
     */
        void process();
    }
}

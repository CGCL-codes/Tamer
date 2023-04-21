package rsm2b.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

/**
 * OpenGL manager based on GLDisplay.java given with the tutorial of http://nehe.gamedev.net
 */
public class MainDisplay {

    private static final int DEFAULT_HEIGHT = 768;

    private static final int DEFAULT_WIDTH = 1024;

    private static final int DONT_CARE = -1;

    public static MainDisplay createMainDisplay(String title) {
        return createMainDisplay(title, new GLCapabilities());
    }

    public static MainDisplay createMainDisplay(String title, GLCapabilities caps) {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        boolean fullscreen = false;
        return new MainDisplay(DEFAULT_WIDTH, DEFAULT_HEIGHT, fullscreen, caps);
    }

    Animator animator;

    private JFrame frame;

    boolean fullscreen = false;

    private GLCanvas glCanvas;

    private int height = 480;

    private GraphicsDevice usedDevice;

    private int width = 640;

    private MainDisplay(int width, int height, boolean fullscreen, GLCapabilities caps) {
        glCanvas = new GLCanvas(caps);
        glCanvas.setSize(width, height);
        glCanvas.setIgnoreRepaint(true);
        frame = new JFrame("rsm2b");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(glCanvas, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GuiTools.removeMouseCursor(frame);
        animator = new FPSAnimator(glCanvas, 60);
        animator.setRunAsFastAsPossible(false);
    }

    public void addGLEventListener(GLEventListener glEventListener) {
        glCanvas.addGLEventListener(glEventListener);
    }

    private DisplayMode findDisplayMode(DisplayMode[] displayModes, int requestedWidth, int requestedHeight, int requestedDepth, int requestedRefreshRate) {
        DisplayMode displayMode = findDisplayModeInternal(displayModes, requestedWidth, requestedHeight, requestedDepth, requestedRefreshRate);
        return displayMode;
    }

    private DisplayMode findDisplayModeInternal(DisplayMode[] displayModes, int requestedWidth, int requestedHeight, int requestedDepth, int requestedRefreshRate) {
        DisplayMode displayModeToUse = null;
        for (int i = 0; i < displayModes.length; i++) {
            DisplayMode displayMode = displayModes[i];
            if ((requestedWidth == DONT_CARE || displayMode.getWidth() == requestedWidth) && (requestedHeight == DONT_CARE || displayMode.getHeight() == requestedHeight) && (requestedHeight == DONT_CARE || displayMode.getRefreshRate() == requestedRefreshRate) && (requestedDepth == DONT_CARE || displayMode.getBitDepth() == requestedDepth)) displayModeToUse = displayMode;
        }
        return displayModeToUse;
    }

    public void start() {
        try {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setUndecorated(fullscreen);
            if (fullscreen) {
                usedDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                usedDevice.setFullScreenWindow(frame);
                usedDevice.setDisplayMode(findDisplayMode(usedDevice.getDisplayModes(), width, height, usedDevice.getDisplayMode().getBitDepth(), usedDevice.getDisplayMode().getRefreshRate()));
            } else {
                frame.setSize(frame.getContentPane().getPreferredSize());
                frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight()) / 2);
                frame.setVisible(true);
            }
            glCanvas.requestFocus();
            animator.start();
        } catch (Exception e) {
        }
    }
}

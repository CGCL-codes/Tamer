package genericirc;

import java.awt.*;
import javax.swing.*;

/**
 * Provides some commonly used utilities for use with Windows and JFrames
 * @author Steven West <sw349@kent.ac.uk>
 * @version 2009-10-27
 */
public class JFrameUtils {

    /**
     * Moves a JFrame into the center of the screen
     * @param window The JFrame to center
     */
    public static void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = window.getSize();
        int x = 0;
        int y = 0;
        double hWwindow = windowSize.getWidth() / 2;
        double hHwindow = windowSize.getHeight() / 2;
        double hWscreen = screenSize.getWidth() / 2;
        double hHscreen = screenSize.getHeight() / 2;
        x = (int) (hWscreen - hWwindow);
        y = (int) (hHscreen - hHwindow);
        window.setLocation(x, y);
    }

    /**
     * Centers window2 over window1
     * @param window1
     * @param window2
     */
    public static void centerWindow(Window window1, Window window2) {
        Dimension window1Size = window1.getSize();
        Dimension window2Size = window2.getSize();
        int x = 0;
        int y = 0;
        double hWwindow = window2Size.getWidth() / 2;
        double hHwindow = window2Size.getHeight() / 2;
        double hWscreen = window1Size.getWidth() / 2;
        double hHscreen = window1Size.getHeight() / 2;
        x = (int) (hWscreen - hWwindow);
        y = (int) (hHscreen - hHwindow);
        x += window1.getX();
        y += window1.getY();
        window2.setLocation(x, y);
    }

    /**
     * Attempts to maxamise the given JFrame
     * @param window
     */
    public static void maxamiseWindow(JFrame window) {
        if (java.awt.Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH)) {
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            System.out.println("Unable to maximise the window");
        }
    }

    /**
     * Attempts to load the system's look and feel
     */
    public static void loadDefaultLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Unable to load native look," + " using default swing");
            System.out.println(e.getMessage());
        }
    }
}

package zuul.core;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.IllegalComponentStateException;
import java.awt.Window;
import javax.swing.JFrame;

/**
 * Manager um den Bildschirm zu aender bzw. zu managen
 *
 * @author swe_0802
 */
public class ScreenManager {

    private GraphicsEnvironment environment;

    private GraphicsDevice device;

    /**
     * Gets the graphics environment and default graphics device
     */
    public ScreenManager() {
        environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = environment.getDefaultScreenDevice();
    }

    /**
     * Nimmt ein gegebenes Fenster und versucht es im Vollbild darzustellen.
     * Dabei wird versucht das Fenster zu entschm�cken (undecorate) und in der Groesse
     * unveraenderbar zu machen.
     * 
     * Hinweis:
     * !!!Wenn das Fenster bei der Initialisierung noch bestueckt ist (decorated)
     * kann das nicht rueckgaegngig gemacht werden !!!
     * 
     * @param window 
     *              Fenster zum darstellen im Vollbild Modus
     * @param display 
     *              Welches Darstellungsmodus soll das Fenster besitzten
     * @return  True --> Fenster ist nun im Vollbildmodus
     *          False --> Es hat leider nicht funktioniert.
     */
    public boolean setFullScreenWindow(Window window, DisplayMode display) {
        if (device.isFullScreenSupported()) {
            device.setFullScreenWindow(window);
            try {
                if (window instanceof JFrame) {
                    JFrame Jwindow = (JFrame) window;
                    Jwindow.setUndecorated(true);
                    Jwindow.setResizable(false);
                }
            } catch (IllegalComponentStateException er) {
            }
            if (device.isDisplayChangeSupported() && checkCompatibleDisplay(display)) {
                device.setDisplayMode(display);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gibt das Fenster im Vollbildmodus wieder
     * @return 
     *          Das vollbild Fenster
     */
    public Window getFullScreenWindow() {
        return device.getFullScreenWindow();
    }

    /**
     * Checks if the given DisplayMode is compatible with the current device
     * 
     * @param display The DisplayMode to check
     * @return True --> compatible ; False --> not compatible
     */
    public boolean checkCompatibleDisplay(DisplayMode display) {
        DisplayMode availableDisplayModes[] = device.getDisplayModes();
        for (DisplayMode mode : availableDisplayModes) {
            if (display.equals(mode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the Fullscreen Window
     */
    public void unFullScreenWindow() {
        Window window = getFullScreenWindow();
        if (window != null) {
            window.dispose();
        }
        device.setFullScreenWindow(null);
    }

    /**
     * gibt den aktuellen Bildschirmmodus wieder
     * 
     * @return DisplayMode
     */
    public DisplayMode getCurrentDisplayMode() {
        return device.getDisplayMode();
    }
}

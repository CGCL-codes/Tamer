package jfindit.GameLogic;

import jfindit.UI.ImageRaster;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import jfindit.UI.ImageRaster2;

/**
 *
 * @author Pepijn
 */
public class KeyboardListener implements KeyListener {

    private ImageRaster2 imageRaster;

    private Vector keysToListenTo = new Vector();

    public KeyboardListener(ImageRaster2 imgRaster) {
        imageRaster = imgRaster;
    }

    public void addKeyToListenTo(String key) {
        keysToListenTo.add(key);
    }

    public void keyTyped(KeyEvent e) {
        if (keysToListenTo.contains(String.valueOf(e.getKeyChar()))) {
            imageRaster.toggleSelectedBox(String.valueOf(e.getKeyChar()));
            imageRaster.checkValue(String.valueOf(e.getKeyChar()));
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            imageRaster.showRaster(true);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            imageRaster.showRaster(false);
        }
    }
}

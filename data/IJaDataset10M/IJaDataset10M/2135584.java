package client.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import client.communication.KeyHandlerThread;

/**
 * Unser angepasster Key-Listener der die 3 Tasten verarbeitet
 * @author aqualuk
 *
 */
public class OurKeyListener implements KeyListener {

    private KeyHandlerThread keyHandlerThread;

    private boolean[] booleanarray;

    /**
	 * Konstruktor. Erzeugt Booleanarray.
	 */
    public OurKeyListener() {
        booleanarray = new boolean[3];
    }

    /**
	 * Setzt Referenz zum KeyHandlerThread, der die Daten übermittelt.
	 * @param kh Referenz zum KeyHandlerThread
	 */
    public void setKeyHandler(KeyHandlerThread kh) {
        this.keyHandlerThread = kh;
    }

    /**
	 * Wenn eine Taste gedrückt ist, ist der Bool-Wert im Array true.
	 */
    public void keyPressed(KeyEvent e) {
        int pK = e.getKeyCode();
        switch(pK) {
            case KeyEvent.VK_LEFT:
                booleanarray[0] = true;
                break;
            case KeyEvent.VK_RIGHT:
                booleanarray[1] = true;
                break;
            case KeyEvent.VK_SPACE:
                booleanarray[2] = true;
                break;
        }
        byte keybyte = this.boolTobyte(booleanarray);
        if (keyHandlerThread != null && keyHandlerThread.isAlive()) {
            this.keyHandlerThread.setData(keybyte);
        }
    }

    /**
	 * Wenn eine Taste nicht gedrückt ist, ist der Bool-Wert im Array false.
	 */
    public void keyReleased(KeyEvent e) {
        int pK = e.getKeyCode();
        switch(pK) {
            case KeyEvent.VK_LEFT:
                booleanarray[0] = false;
                break;
            case KeyEvent.VK_RIGHT:
                booleanarray[1] = false;
                break;
            case KeyEvent.VK_SPACE:
                booleanarray[2] = false;
                break;
        }
        byte keybyte = this.boolTobyte(booleanarray);
        if (keyHandlerThread != null && keyHandlerThread.isAlive()) {
            this.keyHandlerThread.setData(keybyte);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
	 * Erzeugt das spezielle Byte, dass zum Server gesendet werden soll.
	 * @param booleanarray gedrückte Tasten
	 * @return Byte wird versandt
	 */
    public byte boolTobyte(boolean[] booleanarray) {
        byte keybyte = 0;
        for (int i = 0; i < booleanarray.length; i++) {
            if (booleanarray[i]) {
                keybyte += 1 << i;
            }
        }
        return keybyte;
    }

    /**
	 * Setzt Boolean-Array zurück. Wichtig bei neuer Wave, da KeyListener von Popup blockiert.
	 */
    public void resetBooleanarray() {
        booleanarray[0] = false;
        booleanarray[1] = false;
        booleanarray[2] = false;
        byte keybyte = this.boolTobyte(booleanarray);
        if (keyHandlerThread != null && keyHandlerThread.isAlive()) {
            this.keyHandlerThread.setData(keybyte);
        }
    }
}

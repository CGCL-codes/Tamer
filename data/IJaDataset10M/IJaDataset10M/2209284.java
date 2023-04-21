package jepe.util;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class InputHandler implements java.awt.event.KeyListener {

    private ArrayList<KeyEvent> pressedKeys = new ArrayList<KeyEvent>();

    public void keyPressed(KeyEvent e) {
        this.pressedKeys.add(e);
    }

    public void keyReleased(KeyEvent e) {
        ArrayList<KeyEvent> toRemove = new ArrayList<KeyEvent>();
        for (KeyEvent ke : this.pressedKeys) {
            if (e.getKeyCode() == ke.getKeyCode()) toRemove.add(ke);
        }
        this.pressedKeys.removeAll(toRemove);
    }

    public void keyTyped(KeyEvent e) {
    }

    public KeyEvent[] getPressedKeys() {
        KeyEvent[] arr = new KeyEvent[pressedKeys.size()];
        return pressedKeys.toArray(arr);
    }
}

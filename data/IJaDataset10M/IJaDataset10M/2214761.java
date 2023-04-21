package system;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManager implements KeyListener {

    boolean[] keyMap;

    public final int KEY_UP = 0;

    public final int KEY_DOWN = 1;

    public final int KEY_LEFT = 2;

    public final int KEY_RIGHT = 3;

    public final int KEY_CONFIRM = 4;

    public final int KEY_CANCEL = 5;

    public final int KEY_BACKSPACE = 6;

    public final int KEY_SPACE = 7;

    public final int KEY_CTRL = 8;

    private int lastKey = 0;

    private int typedKey = 0;

    public InputManager(Component g) {
        keyMap = new boolean[9];
        for (int i = 0; i < keyMap.length; i++) {
            keyMap[i] = false;
        }
        g.addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        lastKey = key;
        if (key == KeyEvent.VK_UP) {
            keyMap[KEY_UP] = true;
        }
        if (key == KeyEvent.VK_DOWN) {
            keyMap[KEY_DOWN] = true;
        }
        if (key == KeyEvent.VK_LEFT) {
            keyMap[KEY_LEFT] = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            keyMap[KEY_RIGHT] = true;
        }
        if (key == KeyEvent.VK_ENTER) {
            keyMap[KEY_CONFIRM] = true;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            keyMap[KEY_CANCEL] = true;
        }
        if (key == KeyEvent.VK_BACK_SPACE) {
            keyMap[KEY_BACKSPACE] = true;
        }
        if (key == KeyEvent.VK_SPACE) {
            keyMap[KEY_SPACE] = true;
        }
        if (key == KeyEvent.VK_CONTROL) {
            if (!keyMap[KEY_CTRL]) typedKey = 0;
            keyMap[KEY_CTRL] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            keyMap[KEY_UP] = false;
        }
        if (key == KeyEvent.VK_DOWN) {
            keyMap[KEY_DOWN] = false;
        }
        if (key == KeyEvent.VK_LEFT) {
            keyMap[KEY_LEFT] = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            keyMap[KEY_RIGHT] = false;
        }
        if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_CONTROL) {
            keyMap[KEY_CONFIRM] = false;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            keyMap[KEY_CANCEL] = false;
        }
        if (key == KeyEvent.VK_BACK_SPACE) {
            keyMap[KEY_BACKSPACE] = false;
        }
        if (key == KeyEvent.VK_SPACE) {
            keyMap[KEY_SPACE] = false;
        }
        if (key == KeyEvent.VK_CONTROL) {
            keyMap[KEY_CTRL] = false;
        }
    }

    public boolean isDirPressed() {
        return (isPressing(KEY_UP) || isPressing(KEY_DOWN) || isPressing(KEY_LEFT) || isPressing(KEY_RIGHT));
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_CONTROL) {
            typedKey = lastKey;
        }
    }

    public void handledKey(int keyCode) {
        keyMap[keyCode] = false;
    }

    public boolean isPressing(int keyCode) {
        return keyMap[keyCode];
    }

    public int getKeyTyped() {
        return typedKey;
    }

    public void resetKeyTyped() {
        typedKey = 0;
    }
}

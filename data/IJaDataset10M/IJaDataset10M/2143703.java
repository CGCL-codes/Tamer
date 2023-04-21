package creid.mythos.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {

    public static final char NUMPAD_0 = 0;

    public static final char NUMPAD_1 = 1;

    public static final char NUMPAD_2 = 2;

    public static final char NUMPAD_3 = 3;

    public static final char NUMPAD_4 = 4;

    public static final char NUMPAD_5 = 5;

    public static final char NUMPAD_6 = 6;

    public static final char NUMPAD_7 = 7;

    public static final char NUMPAD_8 = 8;

    public static final char NUMPAD_9 = 9;

    public static final char NO_COMMAND = 10;

    public static final char UP = 11;

    public static final char DOWN = 12;

    public static final char LEFT = 13;

    public static final char RIGHT = 14;

    public static final char ENTER = 15;

    public static final char PAGE_UP = 16;

    public static final char PAGE_DOWN = 17;

    public static final char TAB = 18;

    public static final char ESC = 19;

    private boolean shift;

    private boolean ctrl;

    private boolean alt;

    private CommandProcessor controller;

    public KeyInput(CommandProcessor controller) {
        setController(controller);
        setShift(false);
        setCtrl(false);
        setAlt(false);
    }

    public boolean getShift() {
        return shift;
    }

    public void setController(CommandProcessor controller) {
        this.controller = controller;
    }

    public CommandProcessor getController() {
        return controller;
    }

    public void setShift(boolean shift) {
        this.shift = shift;
    }

    public boolean isCtrl() {
        return ctrl;
    }

    public void setAlt(boolean alt) {
        this.alt = alt;
    }

    public boolean isAlt() {
        return alt;
    }

    public void setCtrl(boolean ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void keyPressed(KeyEvent k) {
        char command = k.getKeyChar();
        if (k.getKeyCode() == KeyEvent.VK_SHIFT) {
            setShift(true);
            return;
        } else if (k.getKeyCode() == KeyEvent.VK_ALT) {
            setAlt(true);
            return;
        } else if (k.getKeyCode() == KeyEvent.VK_CONTROL) {
            setCtrl(true);
            return;
        } else if (k.getKeyCode() == KeyEvent.VK_UP) {
            command = UP;
        } else if (k.getKeyCode() == KeyEvent.VK_DOWN) {
            command = DOWN;
        } else if (k.getKeyCode() == KeyEvent.VK_LEFT) {
            command = LEFT;
        } else if (k.getKeyCode() == KeyEvent.VK_RIGHT) {
            command = RIGHT;
        } else if (k.getKeyCode() == KeyEvent.VK_ENTER) {
            command = ENTER;
        } else if (k.getKeyCode() == KeyEvent.VK_ESCAPE) {
            command = ESC;
        } else if (k.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            command = PAGE_UP;
        } else if (k.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            command = PAGE_DOWN;
        } else if (k.getKeyCode() == KeyEvent.VK_NUMPAD0) {
            command = NUMPAD_0;
        } else if (k.getKeyCode() == KeyEvent.VK_NUMPAD1) {
            command = NUMPAD_1;
        } else if (k.getKeyCode() == KeyEvent.VK_NUMPAD2) {
            command = NUMPAD_2;
        } else if (k.getKeyCode() == KeyEvent.VK_NUMPAD3) {
            command = NUMPAD_3;
        } else if (k.getKeyCode() == KeyEvent.VK_NUMPAD4) {
            command = NUMPAD_4;
        } else if (k.getKeyCode() == KeyEvent.VK_NUMPAD5) {
            command = NUMPAD_5;
        } else if (k.getKeyCode() == KeyEvent.VK_NUMPAD6) {
            command = NUMPAD_6;
        } else if (k.getKeyCode() == KeyEvent.VK_NUMPAD7) {
            command = NUMPAD_7;
        } else if (k.getKeyCode() == KeyEvent.VK_NUMPAD8) {
            command = NUMPAD_8;
        } else if (k.getKeyCode() == KeyEvent.VK_NUMPAD9) {
            command = NUMPAD_9;
        }
        getController().playerInput(command, getShift(), isCtrl(), isAlt());
    }

    @Override
    public void keyReleased(KeyEvent k) {
        switch(k.getKeyCode()) {
            case KeyEvent.VK_SHIFT:
                setShift(false);
                break;
            case KeyEvent.VK_CONTROL:
                setCtrl(false);
                break;
            case KeyEvent.VK_ALT:
                setAlt(false);
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent k) {
    }
}

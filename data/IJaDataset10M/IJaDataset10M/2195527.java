package com.wizzer.m3g.lcdui;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.*;
import com.wizzer.m3g.midp.MIDPEmulator;

/**
 * This class emulates the J2ME <code>javax.microedition.lcdui.Canvas</code>
 * class.
 * 
 * @author Mark Millard
 */
public abstract class Canvas extends Displayable {

    public static final int UP = 1;

    public static final int DOWN = 6;

    public static final int LEFT = 2;

    public static final int RIGHT = 5;

    public static final int FIRE = 8;

    public static final int GAME_A = 9;

    public static final int GAME_B = 10;

    public static final int GAME_C = 11;

    public static final int GAME_D = 12;

    public static final int KEY_NUM0 = 48;

    public static final int KEY_NUM1 = 49;

    public static final int KEY_NUM2 = 50;

    public static final int KEY_NUM3 = 51;

    public static final int KEY_NUM4 = 52;

    public static final int KEY_NUM5 = 53;

    public static final int KEY_NUM6 = 54;

    public static final int KEY_NUM7 = 55;

    public static final int KEY_NUM8 = 56;

    public static final int KEY_NUM9 = 57;

    public static final int KEY_STAR = 42;

    public static final int KEY_POUND = 35;

    private GLCanvas glCanvas;

    private MIDPEmulator emulator = MIDPEmulator.getInstance();

    public Canvas() {
        super();
        glCanvas = new GLCanvas(new GLCapabilities());
        glCanvas.setSize(emulator.getDevice().getScreenWidth(), emulator.getDevice().getScreenHeight());
        glCanvas.setIgnoreRepaint(true);
        glCanvas.addKeyListener(new CanvasKeyAdapter());
        emulator.setGL(glCanvas.getGL());
        int contextStatus = glCanvas.getContext().makeCurrent();
        emulator.addInputListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                emulatorInputEvent(e);
            }
        });
    }

    public int getKeyCode(int gameAction) {
        switch(gameAction) {
            case UP:
                return 38;
            case DOWN:
                return 40;
            case LEFT:
                return 37;
            case RIGHT:
                return 39;
            case FIRE:
                return 32;
            case KEY_STAR:
                return 106;
            case KEY_NUM0:
                return 96;
            case KEY_NUM1:
                return 97;
            case KEY_NUM2:
                return 98;
            case KEY_NUM3:
                return 99;
            case KEY_NUM4:
                return 100;
            case KEY_NUM5:
                return 101;
            case KEY_NUM6:
                return 102;
            case KEY_NUM7:
                return 103;
            case KEY_NUM8:
                return 104;
            case KEY_NUM9:
                return 105;
            default:
                return 0;
        }
    }

    public String getKeyName(int keyCode) {
        switch(keyCode) {
            default:
                return "";
        }
    }

    public int getGameAction(int keyCode) {
        switch(keyCode) {
            case 38:
                return UP;
            case 40:
                return DOWN;
            case 37:
                return LEFT;
            case 39:
                return RIGHT;
            case 32:
                return FIRE;
            case 106:
                return KEY_STAR;
            case 96:
                return KEY_NUM0;
            case 97:
                return KEY_NUM1;
            case 98:
                return KEY_NUM2;
            case 99:
                return KEY_NUM3;
            case 100:
                return KEY_NUM4;
            case 101:
                return KEY_NUM5;
            case 102:
                return KEY_NUM6;
            case 103:
                return KEY_NUM7;
            case 104:
                return KEY_NUM8;
            case 105:
                return KEY_NUM9;
            default:
                return 0;
        }
    }

    public final void repaint() {
        callRepaint();
    }

    public final void repaint(int x, int y, int width, int height) {
        callRepaint();
    }

    protected void keyPressed(int keyCode) {
        Logger.global.logp(Level.INFO, "com.wizzer.m3g.lcdui.Canvas", "keyPressed(int keyCode)", "Default key action does nothing");
    }

    protected void keyRepeated(int keyCode) {
        Logger.global.logp(Level.INFO, "com.wizzer.m3g.lcdui.Canvas", "keyRepeated(int keyCode)", "Default key action does nothing");
    }

    protected void keyReleased(int keyCode) {
        Logger.global.logp(Level.INFO, "com.wizzer.m3g.lcdui.Canvas", "keyReleased(int keyCode)", "Default key action does nothing");
    }

    protected void pointerPressed(int x, int y) {
        Logger.global.logp(Level.INFO, "com.wizzer.m3g.lcdui.Canvas", "pointerPressed(int x, int y)", "Default pointer action does nothing");
    }

    protected void pointerReleased(int x, int y) {
        Logger.global.logp(Level.INFO, "com.wizzer.m3g.lcdui.Canvas", "pointerReleased(int x, int y)", "Default pointer action does nothing");
    }

    protected abstract void paint(Graphics g);

    private class CanvasKeyAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            Canvas.this.keyReleased(e.getKeyCode());
            processCommand(getCommand(e.getKeyCode()));
        }

        public void keyPressed(KeyEvent e) {
            Canvas.this.keyPressed(e.getKeyCode());
        }
    }

    private void emulatorInputEvent(ActionEvent e) {
        int key = Integer.parseInt(((Button) e.getSource()).getLabel());
        keyPressed(key + 96);
    }

    void callPaint(Graphics g) {
        paint(g);
    }

    GLCanvas getGLCanvas() {
        return this.glCanvas;
    }
}

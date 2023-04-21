package com.ivan.game.game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import com.ivan.game.unit.SimFonts;

public class GameCanvas extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2635338105799380475L;

    public GameCanvas() {
        controlhandler = new KeyHandler();
        setFocusable(true);
    }

    public void equipListener(boolean b) {
        if (b) {
            if (this.getKeyListeners().length == 0) addKeyListener(controlhandler);
        } else {
            if (this.getKeyListeners().length == 1) removeKeyListener(controlhandler);
        }
    }

    public char getInput(boolean resetInput) {
        char returnChar = input;
        if (resetInput) input = ' ';
        return returnChar;
    }

    public char checkInput() {
        return input;
    }

    private KeyHandler controlhandler;

    private char input = ' ';

    private int keyCount = 0;

    private Thread thread = Thread.currentThread();

    private class KeyHandler implements KeyListener {

        private char prekey = ' ';

        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() != prekey) {
                keyCount++;
            }
            input = e.getKeyChar();
            prekey = input;
        }

        public void keyReleased(KeyEvent e) {
            keyCount--;
            if (e.getKeyChar() == input) {
                prekey = input;
                input = prekey;
            } else {
                prekey = input;
            }
            if (keyCount <= 0) {
                keyCount = 0;
                input = ' ';
                prekey = ' ';
            }
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    public Graphics getGraphics() {
        Graphics g = super.getGraphics();
        g.setFont(SimFonts.getFont());
        return g;
    }
}

package eu.irreality.dai.ui.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

/**
 * A simple keyboard test. Displays keys pressed and released to the screen.
 * Useful for debugging key input, too.
 */
public class KeyTest implements KeyListener {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.addKeyListener(new KeyTest());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void keyPressed(KeyEvent e) {
        System.out.println(e);
        e.consume();
    }

    public void keyReleased(KeyEvent e) {
        e.consume();
    }

    public void keyTyped(KeyEvent e) {
        e.consume();
    }
}

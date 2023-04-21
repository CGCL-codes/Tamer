package spacedemo;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;

/**
 *
 * @author User
 */
public class Frame extends JFrame {

    private Panel panel;

    private Game game;

    private Timer timer;

    public Frame() {
        panel = new Panel();
        game = Game.getInstance();
        timer = new Timer();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Space Demo");
        Container c = this.getContentPane();
        c.setLayout(new GridLayout(1, 1));
        c.add(panel);
        this.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        game.right();
                        break;
                    case KeyEvent.VK_LEFT:
                        game.left();
                        break;
                    case KeyEvent.VK_SPACE:
                        game.shoot();
                        break;
                }
            }

            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        });
        this.pack();
        this.setVisible(true);
    }

    public void start() {
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                repaint();
            }
        }, 0, 50);
    }
}

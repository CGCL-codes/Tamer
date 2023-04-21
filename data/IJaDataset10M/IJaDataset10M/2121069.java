package ui;

import java.applet.Applet;
import java.awt.Graphics;

/**
 * For the future... ;)
 * @author ido
 *
 */
public class SuDoKuApplet extends Applet {

    private MainFrame frame;

    public void init() {
        frame = new MainFrame();
    }

    public void start() {
    }

    public void stop() {
    }

    public void destroy() {
    }

    public void paint(Graphics g) {
        frame.repaint();
    }
}
